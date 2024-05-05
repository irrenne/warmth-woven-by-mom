package com.warmth.woven.by.mom.productservice.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "product_reviews")
@Getter
@Setter
@RequiredArgsConstructor
@AllArgsConstructor
public class ProductReview {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Column(columnDefinition = "BIGINT")
  private Long id;
  private String userId;
  private String comment;
  @ManyToOne
  @JoinColumn(name = "product_id")
  private Product product;
}
