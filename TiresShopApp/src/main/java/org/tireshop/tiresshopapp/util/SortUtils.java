package org.tireshop.tiresshopapp.util;

import org.springframework.data.domain.Sort;

public class SortUtils {

  public static Sort parseSort(String sortParam) {
    String[] parts = sortParam.split(",");
    String property = parts[0];
    String direction = parts.length > 1 ? parts[1].toUpperCase() : "ASC";
    return Sort.by(new Sort.Order(Sort.Direction.valueOf(direction), property));
  }
}
