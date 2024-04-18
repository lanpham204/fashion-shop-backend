package com.shop.response;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.shop.models.OrderStatus;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class OrderVNPAYResponse {
    private String url;
}
