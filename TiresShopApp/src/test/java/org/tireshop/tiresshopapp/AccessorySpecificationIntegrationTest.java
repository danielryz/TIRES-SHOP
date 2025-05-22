package org.tireshop.tiresshopapp;

import jakarta.transaction.Transactional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.jpa.domain.Specification;
import org.tireshop.tiresshopapp.entity.AccessoryType;
import org.tireshop.tiresshopapp.entity.ProductType;
import org.tireshop.tiresshopapp.entity.Accessory;
import org.tireshop.tiresshopapp.repository.CartItemRepository;
import org.tireshop.tiresshopapp.repository.OrderItemRepository;
import org.tireshop.tiresshopapp.repository.AccessoryRepository;
import org.tireshop.tiresshopapp.specifications.AccessorySpecification;

import java.math.BigDecimal;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@Transactional
class AccessorySpecificationIntegrationTest {

  @Autowired
  private AccessoryRepository accessoryRepository;

  @Autowired
  private CartItemRepository cartItemRepository;

  @Autowired
  private OrderItemRepository orderItemRepository;

  private Accessory accessory1;
  private Accessory accessory2;
  private Accessory accessory3;

  @BeforeEach
  void setUp() {

    cartItemRepository.deleteAll();
    orderItemRepository.deleteAll();
    accessoryRepository.deleteAll();

    accessory1 = new Accessory();
    accessory1.setName("Aluminium");
    accessory1.setPrice(new BigDecimal("299.99"));
    accessory1.setDescription("description1");
    accessory1.setStock(10);
    accessory1.setType(ProductType.TIRE);
    accessory1.setAccessoryType(AccessoryType.CHAINS);

    accessory2 = new Accessory();
    accessory2.setName("Stall");
    accessory2.setPrice(new BigDecimal("349.99"));
    accessory2.setDescription("description2");
    accessory2.setStock(10);
    accessory2.setType(ProductType.TIRE);
    accessory2.setAccessoryType(AccessoryType.BOLT);

    accessory3 = new Accessory();
    accessory3.setName("Carbon");
    accessory3.setPrice(new BigDecimal("399.99"));
    accessory3.setDescription("description3");
    accessory3.setStock(10);
    accessory3.setType(ProductType.TIRE);
    accessory3.setAccessoryType(AccessoryType.CHAINS);

    accessoryRepository.saveAll(List.of(accessory1, accessory2, accessory3));
  }

  @Test
  void testHasAccessoryTypeChain() {
    Specification<Accessory> spec =
        AccessorySpecification.hasAccessoryType(List.of(AccessoryType.CHAINS));
    List<Accessory> results = accessoryRepository.findAll(spec);

    assertThat(results).extracting(Accessory::getAccessoryType)
        .containsExactlyInAnyOrder(AccessoryType.CHAINS, AccessoryType.CHAINS);
  }

  @Test
  void testHasNameContainingPirelli() {
    Specification<Accessory> spec = AccessorySpecification.hasNameContaining("aluminium");
    List<Accessory> results = accessoryRepository.findAll(spec);

    assertThat(results).hasSize(1);
    assertThat(results.get(0).getName()).containsIgnoringCase("Aluminium");
  }

  @Test
  void testHasMinPrice350() {
    Specification<Accessory> spec = AccessorySpecification.hasMinPrice(new BigDecimal("350.00"));
    List<Accessory> results = accessoryRepository.findAll(spec);

    assertThat(results).extracting(Accessory::getPrice)
        .allMatch(price -> price.compareTo(new BigDecimal("350.00")) >= 0);
  }

  @Test
  void testHasMaxPrice300() {
    Specification<Accessory> spec = AccessorySpecification.hasMaxPrice(new BigDecimal("300.00"));
    List<Accessory> results = accessoryRepository.findAll(spec);

    assertThat(results).hasSize(1);
    assertThat(results.get(0).getPrice()).isLessThanOrEqualTo(new BigDecimal("300.00"));
  }

  @Test
  void testCombinedSpecAccessoryTypeChainsAndMinPrice() {
    Specification<Accessory> spec =
        Specification.where(AccessorySpecification.hasAccessoryType(List.of(AccessoryType.CHAINS)))
            .and(AccessorySpecification.hasMaxPrice(new BigDecimal("320.00")));

    List<Accessory> results = accessoryRepository.findAll(spec);

    assertThat(results).hasSize(1);
    assertThat(results.get(0).getAccessoryType()).isEqualTo(AccessoryType.CHAINS);
  }
}
