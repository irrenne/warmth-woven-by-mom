package com.warmth.woven.by.mom.productservice.repository;

import com.warmth.woven.by.mom.productservice.model.ProductReview;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ProductReviewRepository extends JpaRepository<ProductReview, Long> {

  List<ProductReview> findAllByProductId(Long productId);
}
