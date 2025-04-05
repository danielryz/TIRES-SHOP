package org.tireshop.tiresshopapp.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "accessory")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Accessory extends Product {

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private AccessoryType type;
}
