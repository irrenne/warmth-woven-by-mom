package com.warmth.woven.by.mom.productservice.repository;

import com.warmth.woven.by.mom.productservice.model.Product;
import java.util.List;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface ProductRepository extends JpaRepository<Product, Long> {

  Page<Product> findByNameContainingIgnoreCase(String name, Pageable pageable);

  Page<Product> findAllByAmountGreaterThan(Integer amount, Pageable pageable);

  Page<Product> findAllByAmountEquals(Integer amount, Pageable pageable);

  Page<Product> findByNameContainingIgnoreCaseAndAmountGreaterThan(String name, Integer amount, Pageable pageable);

  Page<Product> findByNameContainingIgnoreCaseAndAmountEquals(String name, Integer amount, Pageable pageable);

  List<Product> findAllByAmountGreaterThan(Integer amount);
}
