package com.likelion.seminar.product.service;

import com.likelion.seminar.product.dto.ProductResponse;
import com.likelion.seminar.product.repository.ProductRepository;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional(readOnly = true)
public class ProductService {

    private final ProductRepository productRepository;

    public ProductService(ProductRepository productRepository) {
        this.productRepository = productRepository;
    }

    public List<ProductResponse> getExpensiveProducts() {
        return productRepository.findTop10ByOrderByPriceDesc()
                .stream()
                .map(ProductResponse::from)
                .toList();
    }

    public List<ProductResponse> getAffordableProducts() {
        return productRepository
                .findAffordableProducts(PageRequest.of(0, 5))
                .stream()
                .map(ProductResponse::from)
                .toList();
    }
}
