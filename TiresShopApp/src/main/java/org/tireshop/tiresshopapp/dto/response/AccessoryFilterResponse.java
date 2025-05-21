package org.tireshop.tiresshopapp.dto.response;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.util.List;

@Getter
@Setter
@AllArgsConstructor
public class AccessoryFilterResponse {
  private List<AccessoryFilterCountResponse> accessoryType;
  private BigDecimal minPrice;
  private BigDecimal maxPrice;
}
