package com.warmth.woven.by.mom.productservice.util.mapper;

import com.warmth.woven.by.mom.productservice.dto.ProductRequest;
import com.warmth.woven.by.mom.productservice.dto.ProductResponse;
import com.warmth.woven.by.mom.productservice.model.Product;
import java.util.List;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;
import org.springframework.data.domain.Page;

@Mapper
public interface ProductMapper {

  ProductMapper INSTANCE = Mappers.getMapper(ProductMapper.class);

  Product mapProductRequestToProduct(ProductRequest productRequest);

  @Mapping(target = "inStock", expression = "java(mapAmountToInStock(product.getAmount()))")
  ProductResponse mapProductToProductResponse(Product product);

  List<ProductResponse> mapProductsToProductsResponse(List<Product> products);

  default Page<ProductResponse> mapProductsToProductsResponse(Page<Product> products) {
    return products.map(this::mapProductToProductResponse);
  }

  default boolean mapAmountToInStock(Integer amount) {
    return amount > 0;
  }
}
