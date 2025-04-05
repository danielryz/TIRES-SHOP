package org.tireshop.tiresshopapp.entity;

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
    private String season; // np. SUMMER, WINTER, ALL_SEASON

    @Column(nullable = false)
    private String size;   // np. 205/55R16
}
