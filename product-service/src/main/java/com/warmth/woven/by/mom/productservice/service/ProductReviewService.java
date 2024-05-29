package com.warmth.woven.by.mom.productservice.service;

import com.warmth.woven.by.mom.productservice.dto.ProductReviewRequest;
import com.warmth.woven.by.mom.productservice.dto.ProductReviewResponse;
import com.warmth.woven.by.mom.productservice.model.ProductReview;
import com.warmth.woven.by.mom.productservice.repository.ProductReviewRepository;
import com.warmth.woven.by.mom.productservice.util.mapper.ProductReviewMapper;
import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class ProductReviewService {

  private final ProductReviewRepository productReviewRepository;

  public ProductReviewResponse createProductReview(ProductReviewRequest productReviewRequest) {
    ProductReview productReview = ProductReviewMapper.INSTANCE.mapProductReviewRequestToProductReview(
        productReviewRequest);
    ProductReview savedProductReview = productReviewRepository.save(productReview);
    log.info("Product review {} is saved", productReview.getId());
    return ProductReviewMapper.INSTANCE.mapProductReviewToProductReviewResponse(savedProductReview);
  }

  public List<ProductReviewResponse> getProductReviewsByProductId(Long productId) {
    var productReview = productReviewRepository.findAllByProductId(productId);
    return ProductReviewMapper.INSTANCE.mapProductReviewsToProductReviewsResponse(productReview);
  }

}
