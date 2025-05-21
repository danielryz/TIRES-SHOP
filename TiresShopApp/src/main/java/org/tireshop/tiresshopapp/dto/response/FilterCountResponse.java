package org.tireshop.tiresshopapp.dto.response;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class FilterCountResponse {
  private String value;
  private Long count;
}
