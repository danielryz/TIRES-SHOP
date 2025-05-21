package org.tireshop.tiresshopapp.dto.response;

import org.tireshop.tiresshopapp.entity.ProductType;

import java.math.BigDecimal;

public interface BaseProductResponse {
  Long id();

  String name();

  BigDecimal price();

  String description();

  Integer stock();

  ProductType type();
}
