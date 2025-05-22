package org.tireshop.tiresshopapp.entity;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.*;
import lombok.*;


@Entity
@Table(name = "tire")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Tire extends Product {

  @Column(nullable = false)
  @Schema(example = "SUMMER", description = "Tire season")
  private String season; // np. SUMMER, WINTER, ALL_SEASON

  @Column(nullable = false)
  @Schema(example = "205/55R16", description = "Tire size")
  private String size; // np. 205/55R16

}
