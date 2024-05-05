package com.warmth.woven.by.mom.productservice.util.mapper;

import com.warmth.woven.by.mom.productservice.dto.ProductReviewRequest;
import com.warmth.woven.by.mom.productservice.dto.ProductReviewResponse;
import com.warmth.woven.by.mom.productservice.model.ProductReview;
import java.util.List;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

@Mapper
public interface ProductReviewMapper {

  ProductReviewMapper INSTANCE = Mappers.getMapper(ProductReviewMapper.class);

  @Mapping(target = "product.id", source = "productId")
  ProductReview mapProductReviewRequestToProductReview(ProductReviewRequest productReviewRequest);

  ProductReviewResponse mapProductReviewToProductReviewResponse(ProductReview productReview);

  List<ProductReviewResponse> mapProductReviewsToProductReviewsResponse(List<ProductReview> productReviews);
}
