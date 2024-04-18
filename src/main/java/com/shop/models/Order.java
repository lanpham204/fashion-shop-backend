        package com.shop.models;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDateTime; // Using LocalDateTime for timestamp
import java.util.Date;
import java.util.List;

@Entity(name = "orders")
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Builder
public class Order {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

    @Column(name = "phone_number")
    private String phoneNumber;

    @Column(name = "fullname")
    private String fullName;

    @Column(name = "address")
    private String address;

    @Column(name = "note")
    private String note;

    @Column(name = "total_money", nullable = false)
    private BigDecimal totalMoney;

    @Column(name = "order_date")
    private LocalDateTime orderDate = LocalDateTime.now();
    @Enumerated(EnumType.STRING)
    @Column(name = "status")
    private OrderStatus status;
    @Column(name = "payment_method")
    private String paymentMethod;
    @Column(name = "active")
    private boolean active = true;
    @OneToMany(mappedBy = "order",cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @JsonManagedReference
    private List<OrderDetail> orderDetails;
}


