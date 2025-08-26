package com.sinse.electroshop.domain;

import jakarta.persistence.*;
import lombok.Data;

@Data
@Entity
@Table(name="product")
public class Product {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int product_id;

    private String product_name;
    private int price;
    private String brand;

    @ManyToOne
    @JoinColumn(name="store_id", nullable=false)
    private Store store;

}
