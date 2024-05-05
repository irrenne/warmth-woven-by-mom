package com.warmth.woven.by.mom.productservice.controller;

import com.warmth.woven.by.mom.productservice.dto.ProductRequest;
import com.warmth.woven.by.mom.productservice.dto.ProductResponse;
import com.warmth.woven.by.mom.productservice.service.ProductService;
import java.math.BigDecimal;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/product")
@RequiredArgsConstructor
public class ProductController {

  private final ProductService productService;

  @PostMapping
  @ResponseStatus(HttpStatus.CREATED)
  public ProductResponse createProduct(@RequestBody ProductRequest productRequest) {
    return productService.createProduct(productRequest);
  }

  @GetMapping("/{id}")
  @ResponseStatus(HttpStatus.OK)
  public ProductResponse getProductById(@PathVariable Long id) {
    return productService.getProductById(id);
  }

  @GetMapping("/price/{id}")
  @ResponseStatus(HttpStatus.OK)
  public BigDecimal getProductPriceById(@PathVariable Long id) {
    ProductResponse productById = productService.getProductById(id);
    return productById.getPrice();
  }

  @GetMapping("/{productId}/isInStock")
  public Boolean checkStock(@PathVariable Long productId, @RequestParam Integer quantity) {
    return productService.getProductInStock(productId, quantity);
  }

  @PutMapping("/update/{id}")
  @ResponseStatus(HttpStatus.OK)
  public ProductResponse setProductNotInStock(@PathVariable Long id,
      @RequestParam Integer quantity) {
    return productService.decreaseProductAmount(id, quantity);
  }

  @GetMapping
  @ResponseStatus(HttpStatus.OK)
  public List<ProductResponse> getAllProducts() {
    return productService.getAllProducts();
  }

  @GetMapping("/random")
  public List<ProductResponse> getRandomProducts(@RequestParam Integer amount) {
    return productService.getRandomProducts(amount);
  }

  @GetMapping("/paged")
  @ResponseStatus(HttpStatus.OK)
  public Page<ProductResponse> getProductsPaged(
      @RequestParam(required = false) String name,
      @RequestParam(required = false) Boolean inStock,
      @RequestParam(defaultValue = "0") int page,
      @RequestParam(defaultValue = "9") int size,
      @RequestParam(defaultValue = "price") String sortBy,
      @RequestParam(defaultValue = "asc") String sortDirection) {
    return productService.findAllProducts(name, inStock, page, size, sortBy, sortDirection);
  }
}
