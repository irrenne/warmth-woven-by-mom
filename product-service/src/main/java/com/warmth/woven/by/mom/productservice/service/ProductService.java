package com.warmth.woven.by.mom.productservice.service;

import com.warmth.woven.by.mom.productservice.dto.ProductRequest;
import com.warmth.woven.by.mom.productservice.dto.ProductResponse;
import com.warmth.woven.by.mom.productservice.model.Product;
import com.warmth.woven.by.mom.productservice.repository.ProductRepository;
import com.warmth.woven.by.mom.productservice.util.mapper.ProductMapper;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class ProductService {

  private final ProductRepository productRepository;

  public ProductResponse createProduct(ProductRequest productRequest) {
    Product product = ProductMapper.INSTANCE.mapProductRequestToProduct(productRequest);
    Product savedProduct = productRepository.save(product);
    log.info("Product {} is saved", product.getId());
    return ProductMapper.INSTANCE.mapProductToProductResponse(savedProduct);
  }

  public ProductResponse getProductById(Long id) {
    var product = productRepository.findById(id)
        .orElse(null);
    return ProductMapper.INSTANCE.mapProductToProductResponse(product);
  }

  public Boolean getProductInStock(Long id, Integer quantity) {
    var product = productRepository.findById(id)
        .orElse(null);
    return product.getAmount() >= quantity;
  }

  public ProductResponse updateProduct(Long id, ProductRequest productRequest) {
    Product product = productRepository.findById(id).orElseThrow();
    ProductMapper.INSTANCE.mapProductRequestToProductUpdate(product, productRequest);
    Product savedProduct = productRepository.save(product);
    log.info("Product {} was updated: {}", savedProduct.getId(), savedProduct);
    return ProductMapper.INSTANCE.mapProductToProductResponse(savedProduct);
  }

  public ProductResponse restockProductAmount(Long id, Integer quantity) {
    Product product = productRepository.findById(id).orElseThrow();

    product.setAmount(product.getAmount() + quantity);

    Product savedProduct = productRepository.save(product);
    log.info("Product {} amount restocked to {}", savedProduct.getId(), savedProduct.getAmount());
    return ProductMapper.INSTANCE.mapProductToProductResponse(savedProduct);
  }

  public ProductResponse decreaseProductAmount(Long id, Integer quantity) {
    Product product = productRepository.findById(id).orElseThrow();
    if (product.getAmount() >= quantity) {
      product.setAmount(product.getAmount() - quantity);
    }
    Product savedProduct = productRepository.save(product);
    log.info("Product {} amount decreased to {}", savedProduct.getId(), savedProduct.getAmount());
    return ProductMapper.INSTANCE.mapProductToProductResponse(savedProduct);
  }

  public List<ProductResponse> getAllProducts() {
    List<Product> products = productRepository.findAll();
    return ProductMapper.INSTANCE.mapProductsToProductsResponse(products);
  }

  public List<ProductResponse> getRandomProducts(Integer amount) {
    List<Product> allProducts = productRepository.findAllByAmountGreaterThan(0);

    if (allProducts.isEmpty()) {
      return null;
    }

    List<Product> randomProducts = new ArrayList<>();

    Collections.shuffle(allProducts);

    int productsToSelect = Math.min(amount, allProducts.size());

    for (int i = 0; i < productsToSelect; i++) {
      randomProducts.add(allProducts.get(i));
    }

    return ProductMapper.INSTANCE.mapProductsToProductsResponse(randomProducts);
  }


  public Page<ProductResponse> findAllProducts(String name, Boolean inStock, int page, int size,
      String sortBy, String sortDirection) {
    Sort.Direction direction =
        sortDirection.equalsIgnoreCase("desc") ? Sort.Direction.DESC : Sort.Direction.ASC;
    Pageable pageable = PageRequest.of(page, size, Sort.by(direction, sortBy));

    Page<Product> productsPage;

    if (inStock != null) {
      if (name != null) {
        productsPage =
            inStock ? productRepository.findByNameContainingIgnoreCaseAndAmountGreaterThan(name, 0,
                pageable)
                : productRepository.findByNameContainingIgnoreCaseAndAmountEquals(name, 0,
                    pageable);
      } else {
        productsPage = inStock ? productRepository.findAllByAmountGreaterThan(0, pageable)
            : productRepository.findAllByAmountEquals(0, pageable);
      }
    } else {
      productsPage = name != null ? productRepository.findByNameContainingIgnoreCase(name, pageable)
          : productRepository.findAll(pageable);
    }

    return ProductMapper.INSTANCE.mapProductsToProductsResponse(productsPage);
  }

}
