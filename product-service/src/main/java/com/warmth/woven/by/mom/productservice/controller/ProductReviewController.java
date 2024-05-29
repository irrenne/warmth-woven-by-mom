package com.warmth.woven.by.mom.productservice.controller;

import com.warmth.woven.by.mom.productservice.dto.ProductReviewRequest;
import com.warmth.woven.by.mom.productservice.dto.ProductReviewResponse;
import com.warmth.woven.by.mom.productservice.service.ProductReviewService;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/product/review")
@RequiredArgsConstructor
public class ProductReviewController {

  private final ProductReviewService productReviewService;

  @PostMapping
  @ResponseStatus(HttpStatus.CREATED)
  public ProductReviewResponse createProductReview(@RequestBody ProductReviewRequest productRequest) {
    return productReviewService.createProductReview(productRequest);
  }

  @GetMapping("/product/{productId}")
  @ResponseStatus(HttpStatus.OK)
  public List<ProductReviewResponse> getProductReviewsByProductId(@PathVariable Long productId){
    return productReviewService.getProductReviewsByProductId(productId);
  }
}
