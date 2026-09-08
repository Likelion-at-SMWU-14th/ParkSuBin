package com.likelion.seminar.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@Setter
@NoArgsConstructor
public class Product extends BaseEntity{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long number;

    private String name;
    private Integer price;
    private Integer stock;

    @OneToOne(mappedBy = "product")
    @ToString.Exclude
    private ProductDetail productDetail;

    public Product(String name, Integer price){
        this.name = name;
        this.price = price;
    }

    @ManyToOne
    @JoinColumn(name = "category_id")
    private Category category;
    @ManyToOne
    @JoinColumn(name = "provider_id")
    private Provider provider;

    @ManyToMany
    @ToString.Exclude
    private List<Producer> producers = new ArrayList<>();




}
