package com.likelion.seminar.product.controller;

import com.likelion.seminar.product.dto.ProductResponse;
import com.likelion.seminar.product.service.ProductService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/product")
public class ProductController {

    private final ProductService productService;

    public ProductController(ProductService productService) {
        this.productService = productService;
    }

    @GetMapping("/jpa")
    public List<ProductResponse> getExpensiveProducts() {
        return productService.getExpensiveProducts();
    }

    @GetMapping("/jpql")
    public List<ProductResponse> getAffordableProducts() {
        return productService.getAffordableProducts();
    }
}
