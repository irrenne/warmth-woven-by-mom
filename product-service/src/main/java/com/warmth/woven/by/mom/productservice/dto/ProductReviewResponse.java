package com.warmth.woven.by.mom.productservice.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ProductReviewResponse {

  private Long id;
  private String userId;
  private String comment;
}
