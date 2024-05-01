package com.shop.services;

import com.shop.configuration.VnpayConfig;
import com.shop.dtos.OrderDTO;
import com.shop.dtos.ProductDTO;
import com.shop.exceptions.DataNotFoundException;
import com.shop.models.*;
import com.shop.repositories.OrderDetailRepository;
import com.shop.repositories.OrderRepository;
import com.shop.repositories.ProductRepository;
import com.shop.repositories.UserRepository;
import com.shop.response.OrderDetailResponse;
import com.shop.response.OrderResponse;
import com.shop.response.UserResponse;
import com.shop.services.interfaces.IOrderService;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestParam;

import java.io.UnsupportedEncodingException;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.text.SimpleDateFormat;
import java.util.*;

import static com.shop.configuration.VnpayConfig.vnp_Command;
import static com.shop.configuration.VnpayConfig.vnp_Version;

@Service
@RequiredArgsConstructor
public class OrderService implements IOrderService {
    private final OrderRepository orderRepository;
    private final UserRepository userRepository;
    private final OrderDetailRepository orderDetailRepository;
    private final ModelMapper modelMapper;
    private final ProductRepository productRepository;

    @Override
    public OrderResponse create(OrderDTO orderDTO) throws DataNotFoundException {
        User existingUser = userRepository.findById(orderDTO.getUserId())
                .orElseThrow(() -> new DataNotFoundException("Cannot found user with id: " + orderDTO.getUserId()));
        modelMapper.typeMap(OrderDTO.class, Order.class)
                .addMappings(mapper -> mapper.skip(Order::setId));
        Order order = modelMapper.map(orderDTO, Order.class);
        order.setUser(existingUser);
        if(orderDTO.getPaymentMethod().equals("vnpay")) {
            order.setStatus(OrderStatus.UNPAID);
        } else {
            order.setStatus(OrderStatus.PENDING);
        }
        orderRepository.save(order);
        List<OrderDetail> orderDetails = new ArrayList<>();
        orderDTO.getCartItems().forEach(cartItem -> {
            try {
                Product product = productRepository.findById(cartItem.getProductId())
                        .orElseThrow(() -> new DataNotFoundException("(Cannot found product with id: " + cartItem.getProductId()));
                BigDecimal price = cartItem.getPrice();
                Integer quantity = cartItem.getQuantity();
                OrderDetail orderDetail = new OrderDetail();
                orderDetail.setOrder(order);
                orderDetail.setSize(cartItem.getSize());
                orderDetail.setColor(cartItem.getColor());
                orderDetail.setProduct(product);
                orderDetail.setPrice(cartItem.getPrice());
                orderDetail.setTotalMoney(price.multiply(BigDecimal.valueOf(cartItem.getQuantity())));
                orderDetail.setQuantity(cartItem.getQuantity());
                orderDetails.add(orderDetail);
            } catch (DataNotFoundException e) {
                throw new RuntimeException(e.getMessage());
            }
        });
        orderDetailRepository.saveAll(orderDetails);
        OrderResponse orderResponse = modelMapper.map(order, OrderResponse.class);
        orderResponse.setOrderDetails(orderDetails.stream().map(orderDetail -> modelMapper.map(orderDetail, OrderDetailResponse.class)).toList());
        return orderResponse;
    }

    @Override
    public Page<OrderResponse> searchOrders(String keyword, PageRequest pageRequest) {
        return orderRepository.searchOrderByKeyword(keyword, pageRequest).map(order -> modelMapper.map(order, OrderResponse.class));
    }

    @Override
    public OrderResponse update(OrderDTO orderDTO, int id) throws DataNotFoundException {
        User existingUser = userRepository.findById(orderDTO.getUserId())
                .orElseThrow(() -> new DataNotFoundException("Cannot found user with id: " + orderDTO.getUserId()));
        Order existingOrder = modelMapper.map(getById(id), Order.class);
        modelMapper.typeMap(OrderDTO.class, Order.class)
                .addMappings(modelMapper -> modelMapper.skip(Order::setId));
        modelMapper.map(orderDTO, existingOrder);
        orderRepository.save(existingOrder);
        return modelMapper.map(existingOrder, OrderResponse.class);
    }

    @Override
    public OrderResponse getById(int id) throws DataNotFoundException {
        Order order = orderRepository.findById(id).orElseThrow(
                () -> new DataNotFoundException("Cannot found order with id: " + id));
        return modelMapper.map(order, OrderResponse.class);
    }

    @Override
    public void delete(int id) throws DataNotFoundException {
        Order order = orderRepository.findById(id).orElseThrow(
                () -> new DataNotFoundException("Cannot found order with id: " + id));
        order.setActive(false);
        orderRepository.save(order);
    }

    @Override
    public List<OrderResponse> getAllByUserId(int userId) throws DataNotFoundException {
        User existingUser = userRepository.findById(userId)
                .orElseThrow(() -> new DataNotFoundException("Cannot found user with id: " + userId));
        List<Order> orders = orderRepository.findByUserId(userId);
        return orders.stream().map(order -> modelMapper.map(order, OrderResponse.class)).toList();
    }

    public String createPayment(BigDecimal amount,int orderId, HttpServletRequest request) throws UnsupportedEncodingException {

        String orderType = "other";

        String vnp_TxnRef = VnpayConfig.getRandomNumber(8);
        String vnp_IpAddr = VnpayConfig.getIpAddress(request);

        String vnp_TmnCode = VnpayConfig.vnp_TmnCode;

        Map<String, String> vnp_Params = new HashMap<>();
        vnp_Params.put("vnp_Version", vnp_Version);
        vnp_Params.put("vnp_Command", vnp_Command);
        vnp_Params.put("vnp_TmnCode", vnp_TmnCode);
        vnp_Params.put("vnp_Amount", String.valueOf(amount.multiply(new BigDecimal(100)).setScale(0, RoundingMode.HALF_UP)));
        vnp_Params.put("vnp_CurrCode", "VND");
        vnp_Params.put("vnp_BankCode", "NCB");
//        if (bankCode != null && !bankCode.isEmpty()) {
//        }
        vnp_Params.put("vnp_TxnRef", vnp_TxnRef);
        vnp_Params.put("vnp_OrderInfo", "Thanh toan don hang:" + vnp_TxnRef);
        vnp_Params.put("vnp_OrderType", orderType);
        vnp_Params.put("vnp_Locale", "vn");
        vnp_Params.put("vnp_ReturnUrl", VnpayConfig.vnp_ReturnUrl+"?orderId="+String.valueOf(orderId));
        vnp_Params.put("vnp_IpAddr", vnp_IpAddr);
        Calendar cld = Calendar.getInstance(TimeZone.getTimeZone("Etc/GMT+7"));
        SimpleDateFormat formatter = new SimpleDateFormat("yyyyMMddHHmmss");
        String vnp_CreateDate = formatter.format(cld.getTime());
        vnp_Params.put("vnp_CreateDate", vnp_CreateDate);

        cld.add(Calendar.MINUTE, 15);
        String vnp_ExpireDate = formatter.format(cld.getTime());
        vnp_Params.put("vnp_ExpireDate", vnp_ExpireDate);

        List fieldNames = new ArrayList(vnp_Params.keySet());
        Collections.sort(fieldNames);
        StringBuilder hashData = new StringBuilder();
        StringBuilder query = new StringBuilder();
        Iterator itr = fieldNames.iterator();
        while (itr.hasNext()) {
            String fieldName = (String) itr.next();
            String fieldValue = (String) vnp_Params.get(fieldName);
            if ((fieldValue != null) && (fieldValue.length() > 0)) {
                //Build hash data
                hashData.append(fieldName);
                hashData.append('=');
                hashData.append(URLEncoder.encode(fieldValue, StandardCharsets.US_ASCII.toString()));
                //Build query
                query.append(URLEncoder.encode(fieldName, StandardCharsets.US_ASCII.toString()));
                query.append('=');
                query.append(URLEncoder.encode(fieldValue, StandardCharsets.US_ASCII.toString()));
                if (itr.hasNext()) {
                    query.append('&');
                    hashData.append('&');
                }
            }
        }
        String queryUrl = query.toString();
        String vnp_SecureHash = VnpayConfig.hmacSHA512(VnpayConfig.secretKey, hashData.toString());
        queryUrl += "&vnp_SecureHash=" + vnp_SecureHash;
        String paymentUrl = VnpayConfig.vnp_PayUrl + "?" + queryUrl;
//        com.google.gson.JsonObject job = new JsonObject();
//        job.addProperty("code", "00");
//        job.addProperty("message", "success");
//        job.addProperty("data", paymentUrl);
//        Gson gson = new Gson();
//        resp.getWriter().write(gson.toJson(job));
        return paymentUrl;
    }
}
