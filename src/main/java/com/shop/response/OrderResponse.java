package com.shop.response;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.shop.models.OrderDetail;
import com.shop.models.OrderStatus;
import com.shop.models.User;
import jakarta.persistence.*;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class OrderResponse {
    @JsonProperty("id")
    private int id;
    @JsonProperty("user_id")
    private int userId;
    @JsonProperty("fullname")
    private String fullName;
    @JsonProperty("phone_number")
    private String phoneNumber;

    @JsonProperty("address")
    private String address;

    @JsonProperty("note")
    private String note;

    @JsonProperty("total_money")
    private BigDecimal totalMoney;
    @JsonProperty("order_date")
    private LocalDateTime orderDate;
    @JsonProperty("payment_method")
    private String paymentMethod;
    @JsonProperty("status")
    private OrderStatus status;
    @JsonProperty("active")
    private boolean active;
    @JsonProperty("order_details")
    private List<OrderDetailResponse> orderDetails;
}
