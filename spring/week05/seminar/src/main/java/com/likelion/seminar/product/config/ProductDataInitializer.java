package com.likelion.seminar.product.config;

import com.likelion.seminar.product.domain.Product;
import com.likelion.seminar.product.repository.ProductRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;

@Configuration
@Profile("local")
public class ProductDataInitializer {

    @Bean
    CommandLineRunner initProducts(ProductRepository productRepository) {
        return args -> {
            if (productRepository.count() > 0) {
                return;
            }

            for (int i = 1; i <= 12; i++) {
                productRepository.save(
                        Product.create("볼펜" + i, i * 500, i * 10)
                );
            }

            productRepository.save(Product.create("지우개", 1000, 100));
            productRepository.save(Product.create("연필", 1500, 90));
            productRepository.save(Product.create("공책", 2000, 80));
            productRepository.save(Product.create("파일", 2001, 200));
        };
    }
}

