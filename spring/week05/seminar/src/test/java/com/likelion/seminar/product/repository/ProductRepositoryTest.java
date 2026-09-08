package com.likelion.seminar.product.repository;

import com.likelion.seminar.product.domain.Product;
import com.likelion.seminar.product.domain.QProduct;
import com.querydsl.jpa.impl.JPAQueryFactory;
import jakarta.persistence.EntityManager;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.data.jpa.test.autoconfigure.DataJpaTest;

import java.util.List;
import java.util.stream.IntStream;

import static org.assertj.core.api.Assertions.assertThat;

import org.springframework.data.domain.PageRequest;

@DataJpaTest
class ProductRepositoryTest {

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private EntityManager entityManager;

    @Test
    @DisplayName("이름에 펜이 포함된 상품을 저렴한 순으로 최대 10개 조회한다")
    void findCheapPensTop10() {
        // 준비: 가격이 서로 다른 펜 12개 저장
        for (int i = 12; i >= 1; i--) {
            productRepository.save(
                    Product.create("볼펜" + i, i * 100, 20)
            );
        }

        // 더 저렴해도 이름에 '펜'이 없으면 제외되어야 한다
        productRepository.save(Product.create("지우개", 50, 30));

        entityManager.flush();
        entityManager.clear();

        // 실행
        JPAQueryFactory queryFactory = new JPAQueryFactory(entityManager);
        QProduct product = QProduct.product;

        List<Product> result = queryFactory
                .selectFrom(product)
                .where(product.name.contains("펜"))
                .orderBy(product.price.asc())
                .limit(10)
                .fetch();

        // 검증: 저렴한 펜 10개가 정확한 순서로 반환되는지 확인
        assertThat(result).hasSize(10);

        assertThat(result)
                .extracting(Product::getName)
                .containsExactlyElementsOf(
                        IntStream.rangeClosed(1, 10)
                                .mapToObj(i -> "볼펜" + i)
                                .toList()
                );

        assertThat(result)
                .extracting(Product::getPrice)
                .containsExactly(
                        100, 200, 300, 400, 500,
                        600, 700, 800, 900, 1000
                );
    }
    @Test
    @DisplayName("가장 비싼 상품을 가격 내림차순으로 최대 10개 조회한다")
    void findExpensiveProductsTop10() {
        for (int i = 1; i <= 12; i++) {
            productRepository.save(
                    Product.create("상품" + i, i * 1000, 10)
            );
        }

        entityManager.flush();
        entityManager.clear();

        List<Product> result =
                productRepository.findTop10ByOrderByPriceDesc();

        assertThat(result)
                .extracting(Product::getPrice)
                .containsExactly(
                        12000, 11000, 10000, 9000, 8000,
                        7000, 6000, 5000, 4000, 3000
                );
    }

    @Test
    @DisplayName("2000원 이하 상품 중 재고가 많은 순으로 최대 5개 조회한다")
    void findAffordableProductsTop5() {
        for (int i = 1; i <= 6; i++) {
            productRepository.save(
                    Product.create("상품" + i, 1000, i * 10)
            );
        }

        // 2000원은 포함되어야 한다
        productRepository.save(Product.create("경계 상품", 2000, 70));

        // 재고가 많아도 2000원을 넘으면 제외되어야 한다
        productRepository.save(Product.create("비싼 상품", 2001, 100));

        entityManager.flush();
        entityManager.clear();

        List<Product> result = productRepository
                .findAffordableProducts(PageRequest.of(0, 5));

        assertThat(result)
                .extracting(Product::getStock)
                .containsExactly(70, 60, 50, 40, 30);

        assertThat(result)
                .allSatisfy(product ->
                        assertThat(product.getPrice()).isLessThanOrEqualTo(2000)
                );
    }
}