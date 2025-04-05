package org.tireshop.tiresshopapp.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "rim")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Rim extends Product {

    @Column(nullable = false)
    private String material; // np. aluminum, steel

    @Column(nullable = false)
    private String size;     // np. 16", 17"

    @Column(name = "bolt_pattern", nullable = false)
    private String boltPattern; // np. 5x112
}
