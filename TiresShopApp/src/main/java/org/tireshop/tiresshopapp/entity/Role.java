package org.tireshop.tiresshopapp.entity;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.*;
import lombok.*;

import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "roles")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor

public class Role {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Schema(example = "1", description = "Unikalne id Roli")
  private Long id;

  @Column(nullable = false, unique = true)
  @Schema(example = "ROLE_ADMIN", description = "Role jakie są przypisane do użytkwonika")
  private String name;

  @ManyToMany(mappedBy = "roles")
  private Set<User> users = new HashSet<>();

}
