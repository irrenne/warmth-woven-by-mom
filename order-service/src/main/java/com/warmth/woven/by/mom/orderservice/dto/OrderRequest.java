package com.warmth.woven.by.mom.orderservice.dto;

import java.math.BigDecimal;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class OrderRequest {

  private Long productId;
  private BigDecimal price;
  private Boolean withShipping;
  private String userId;
}
