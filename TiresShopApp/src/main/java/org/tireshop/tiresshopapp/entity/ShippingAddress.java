package org.tireshop.tiresshopapp.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "shipping_address")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ShippingAddress {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @Column(nullable = false)
  private String street;
  @Column(nullable = false)
  private String houseNumber;
  private String apartmentNumber;
  @Column(nullable = false)
  private String postalCode;
  @Column(nullable = false)
  private String city;

  @OneToOne(mappedBy = "shippingAddress", cascade = CascadeType.ALL)
  private Order order;
}
