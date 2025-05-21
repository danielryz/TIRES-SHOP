package org.tireshop.tiresshopapp.dto.response;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import org.tireshop.tiresshopapp.entity.AccessoryType;

@Getter
@Setter
@AllArgsConstructor
public class AccessoryFilterCountResponse {
  private AccessoryType value;
  private Long count;
}
