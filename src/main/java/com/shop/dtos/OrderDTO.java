package com.shop.dtos;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.shop.models.OrderDetail;
import com.shop.models.OrderStatus;
import com.shop.models.User;
import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Builder
@Data
public class OrderDTO {
    @JsonProperty("user_id")
    @Min(value = 1, message = "User's ID must be > 0")
    private int userId;
    @JsonProperty("fullname")
    private String fullName;

    @JsonProperty("phone_number")
    @NotBlank(message = "Phone number is required")
    @Pattern(regexp = "(84|0[3|5|7|8|9])[0-9]{8}"
    ,message = "Invalid phone number")
    private String phoneNumber;

    @NotBlank(message = "address is required")
    @JsonProperty("address")
    private String address;

    @NotBlank(message = "Payment method is required")
    @JsonProperty("payment_method")
    private String paymentMethod;

    @JsonProperty("note")
    private String note;
    @JsonProperty("status")
    private OrderStatus status;
    @JsonProperty("total_money")
    @Min(value =  0, message = "total_money must be greater than or equal to 0")
    @Max(value =  100000000, message = "total_money must be less than or equal to 100000000")
    private BigDecimal totalMoney;

    @JsonProperty("cart_items")
    private List<CartItemDTO> cartItems;

}
