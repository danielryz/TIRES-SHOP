package org.tireshop.tiresshopapp.entity;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.*;
import lombok.*;

import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "users")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class User {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Schema(example = "1", description = "Unikalny identyfikator użytkownika")
  private Long id;

  @Column(nullable = false, unique = true, length = 100)
  @Schema(example = "tires_shop1", description = "Unikalna nazwa użytkownika")
  private String username;

  @Column(nullable = false, unique = true, length = 100)
  @Schema(example = "tires@tiresshop.pl", description = "Adres email użytkownika")
  private String email;

  @Column(length = 50)
  @Schema(example = "Jan", description = "Imię użytkownika")
  private String firstName;

  @Column(length = 80)
  @Schema(example = "Kowalski", description = "Nazwisko użytkownika")
  private String lastName;

  @Schema(example = "$2a$10$hashedpassword", description = "Zahasłowane hasło użytkownika",
      accessMode = Schema.AccessMode.WRITE_ONLY)
  @Column(nullable = false)
  private String password;

  @Schema(description = "Adresy użytkownika")
  @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true)
  private Set<Address> addresses = new HashSet<>();

  @Column(length = 20)
  @Schema(example = "+48 123456789", description = "Numer telefonu")
  private String phoneNumber;

  @Column(nullable = false)
  @Schema(example = "true", description = "Czy konto użytkownika jest aktywne")
  private boolean enabled = true;

  @ManyToMany(fetch = FetchType.EAGER)
  @JoinTable(name = "user_roles", joinColumns = @JoinColumn(name = "user_id"),
      inverseJoinColumns = @JoinColumn(name = "role_id"))
  @Schema(description = "Role przypisane do użytkownika")
  private Set<Role> roles = new HashSet<>();

  @Schema(description = "Zamównienia użytkownika")
  @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true,
      fetch = FetchType.LAZY)
  private Set<Order> orders = new HashSet<>();



}
