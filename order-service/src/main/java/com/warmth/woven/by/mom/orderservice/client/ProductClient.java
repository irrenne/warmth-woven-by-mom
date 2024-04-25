package com.warmth.woven.by.mom.orderservice.client;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;

@FeignClient(name = "PRODUCT-SERVICE")
public interface ProductClient {

  @GetMapping("/api/product/{productId}/isInStock")
  Boolean checkInStock(@PathVariable Long productId);

  @PutMapping("/api/product/update/{productId}")
  void decreaseProductAmount(@PathVariable Long productId);
}
