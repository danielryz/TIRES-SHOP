package org.tireshop.tiresshopapp.dto.response;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.util.List;

@Getter
@Setter
@AllArgsConstructor
public class RimFilterResponse {
  private List<FilterCountResponse> material;
  private List<FilterCountResponse> size;
  private List<FilterCountResponse> boltPattern;
  private BigDecimal minPrice;
  private BigDecimal maxPrice;
}
