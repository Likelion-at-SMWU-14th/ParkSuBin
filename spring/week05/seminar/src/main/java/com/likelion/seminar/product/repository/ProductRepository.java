package com.likelion.seminar.product.repository;

import com.likelion.seminar.product.domain.Product;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface ProductRepository extends JpaRepository<Product, Long> {

    List<Product> findTop10ByOrderByPriceDesc();

    @Query("""
            SELECT p
            FROM Product p
            WHERE p.price <= 2000
            ORDER BY p.stock DESC
            """)
    List<Product> findAffordableProducts(Pageable pageable);
}