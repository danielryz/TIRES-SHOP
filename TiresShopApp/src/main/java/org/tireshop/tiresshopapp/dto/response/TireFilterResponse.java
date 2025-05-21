package org.tireshop.tiresshopapp.dto.response;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.util.List;

@Getter
@Setter
@AllArgsConstructor
public class TireFilterResponse {
  private List<FilterCountResponse> seasons;
  private List<FilterCountResponse> sizes;
  private BigDecimal minPrice;
  private BigDecimal maxPrice;
}
