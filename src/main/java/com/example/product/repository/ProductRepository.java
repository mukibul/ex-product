package com.example.product.repository;

import com.example.product.domain.Product;
import org.springframework.data.repository.PagingAndSortingRepository;

import java.util.Optional;

/**
 * @author mukibulØ
 * @since 23/08/19
 */

public interface ProductRepository extends PagingAndSortingRepository<Product,Long> {
    Optional<Product> findById(Long id);
}
