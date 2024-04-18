        package com.shop.models;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal; // Using BigDecimal for consistent decimal representation
import java.util.List;

 @Entity(name = "order_details")
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Builder
public class OrderDetail {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @ManyToOne
    @JoinColumn(name = "order_id")
    @JsonBackReference
    private Order order;

    @ManyToOne
    @JoinColumn(name = "product_id")
    private Product product;

    @Column(name = "size")
    private String size;

    @Column(name = "color")
    private String color;

     @Column(name = "quantity", nullable = false)
     private int quantity;

    @Column(name = "price", nullable = false)
    private BigDecimal price;

    @Column(name = "total_money", nullable = false)
    private BigDecimal totalMoney;


}

