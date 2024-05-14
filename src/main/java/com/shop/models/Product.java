package com.shop.models;

import jakarta.persistence.*;
import lombok.*;
import org.springframework.context.event.EventListener;

import java.math.BigDecimal;
import java.util.List;
import java.util.Set;

@Entity(name = "products")
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Builder
@EntityListeners(ProductListener.class)
public class Product {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Column(name = "name", nullable = false)
    private String name;

    @Column(name = "thumbnail")
    private String thumbnail;

    @Column(name = "price", nullable = false)
    private BigDecimal price;

    @Column(name = "description", nullable = false)
    private String description;

    @ManyToOne
    @JoinColumn(name = "cate_id")
    private Category category;
    @OneToMany(mappedBy = "product", cascade = CascadeType.ALL,fetch = FetchType.LAZY)
    private List<WarehouseProduct> warehouseProducts;
}