package com.shop.response;

import lombok.*;

import java.util.List;

@AllArgsConstructor
@Data
@Builder
@NoArgsConstructor
@Getter
@Setter
public class ProductListResponse {
    private List<ProductResponse> products;
    private int totalPages;
}

