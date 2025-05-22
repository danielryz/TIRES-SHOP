package org.tireshop.tiresshopapp;

import jakarta.transaction.Transactional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.jpa.domain.Specification;
import org.tireshop.tiresshopapp.entity.ProductType;
import org.tireshop.tiresshopapp.entity.Tire;
import org.tireshop.tiresshopapp.repository.CartItemRepository;
import org.tireshop.tiresshopapp.repository.OrderItemRepository;
import org.tireshop.tiresshopapp.repository.TireRepository;
import org.tireshop.tiresshopapp.specifications.TireSpecifications;

import java.math.BigDecimal;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@Transactional
class TireSpecificationIntegrationTest {

  @Autowired
  private TireRepository tireRepository;

  @Autowired
  private CartItemRepository cartItemRepository;

  @Autowired
  private OrderItemRepository orderItemRepository;

  private Tire tire1;
  private Tire tire2;
  private Tire tire3;

  @BeforeEach
  void setUp() {

    cartItemRepository.deleteAll();
    orderItemRepository.deleteAll();
    tireRepository.deleteAll();

    tire1 = new Tire();
    tire1.setName("Michelin Energy");
    tire1.setPrice(new BigDecimal("299.99"));
    tire1.setDescription("description1");
    tire1.setStock(10);
    tire1.setType(ProductType.TIRE);
    tire1.setSeason("summer");
    tire1.setSize("205/55 R16");

    tire2 = new Tire();
    tire2.setName("Goodyear UltraGrip");
    tire2.setPrice(new BigDecimal("349.99"));
    tire2.setDescription("description2");
    tire2.setStock(10);
    tire2.setType(ProductType.TIRE);
    tire2.setSeason("winter");
    tire2.setSize("195/65 R15");

    tire3 = new Tire();
    tire3.setName("Pirelli Cinturato");
    tire3.setPrice(new BigDecimal("399.99"));
    tire3.setDescription("description3");
    tire3.setStock(10);
    tire3.setType(ProductType.TIRE);
    tire3.setSeason("all-season");
    tire3.setSize("205/55 R16");

    tireRepository.saveAll(List.of(tire1, tire2, tire3));
  }

  @Test
  void testHasSeasonSummerAndWinter() {
    Specification<Tire> spec = TireSpecifications.hasSeasons(List.of("summer", "winter"));
    List<Tire> results = tireRepository.findAll(spec);

    assertThat(results).extracting(Tire::getSeason).containsExactlyInAnyOrder("summer", "winter");
  }

  @Test
  void testHasSizes205And195() {
    Specification<Tire> spec = TireSpecifications.hasSizes(List.of("205/55 R16", "195/65 R15"));
    List<Tire> results = tireRepository.findAll(spec);

    assertThat(results).hasSize(3);
  }

  @Test
  void testHasNameContainingPirelli() {
    Specification<Tire> spec = TireSpecifications.hasNameContaining("pirelli");
    List<Tire> results = tireRepository.findAll(spec);

    assertThat(results).hasSize(1);
    assertThat(results.get(0).getName()).containsIgnoringCase("pirelli");
  }

  @Test
  void testHasMinPrice350() {
    Specification<Tire> spec = TireSpecifications.hasMinPrice(new BigDecimal("350.00"));
    List<Tire> results = tireRepository.findAll(spec);

    assertThat(results).extracting(Tire::getPrice)
        .allMatch(price -> price.compareTo(new BigDecimal("350.00")) >= 0);
  }

  @Test
  void testHasMaxPrice300() {
    Specification<Tire> spec = TireSpecifications.hasMaxPrice(new BigDecimal("300.00"));
    List<Tire> results = tireRepository.findAll(spec);

    assertThat(results).hasSize(1);
    assertThat(results.get(0).getPrice()).isLessThanOrEqualTo(new BigDecimal("300.00"));
  }

  @Test
  void testCombinedSpecSeasonSummerAndMinPrice() {
    Specification<Tire> spec = Specification.where(TireSpecifications.hasSeasons(List.of("summer")))
        .and(TireSpecifications.hasMinPrice(new BigDecimal("200")));

    List<Tire> results = tireRepository.findAll(spec);

    assertThat(results).hasSize(1);
    assertThat(results.get(0).getSeason()).isEqualTo("summer");
  }
}
