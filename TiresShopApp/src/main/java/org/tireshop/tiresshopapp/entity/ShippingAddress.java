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

  private String street;
  private String houseNumber;
  private String apartmentNumber;
  private String postalCode;
  private String city;

  @OneToOne(mappedBy = "shippingAddress", cascade = CascadeType.ALL)
  private Order order;
}
