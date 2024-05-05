package com.warmth.woven.by.mom.orderservice.client;

import java.math.BigDecimal;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestParam;

@FeignClient(name = "PRODUCT-SERVICE")
public interface ProductClient {

  @GetMapping("/api/product/{productId}/isInStock")
  Boolean checkInStock(@PathVariable Long productId, @RequestParam Integer quantity);

  @PutMapping("/api/product/update/{productId}")
  void decreaseProductAmount(@PathVariable Long productId, @RequestParam Integer quantity);

  @GetMapping("/api/product/price/{productId}")
  BigDecimal getProductPriceById(@PathVariable Long productId);
}
