package org.tireshop.tiresshopapp;

import jakarta.transaction.Transactional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.jpa.domain.Specification;
import org.tireshop.tiresshopapp.entity.ProductType;
import org.tireshop.tiresshopapp.entity.Rim;
import org.tireshop.tiresshopapp.repository.CartItemRepository;
import org.tireshop.tiresshopapp.repository.OrderItemRepository;
import org.tireshop.tiresshopapp.repository.RimRepository;
import org.tireshop.tiresshopapp.specifications.RimSpecifications;

import java.math.BigDecimal;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@Transactional
class RimSpecificationIntegrationTest {

  @Autowired
  private RimRepository rimRepository;

  @Autowired
  private CartItemRepository cartItemRepository;

  @Autowired
  private OrderItemRepository orderItemRepository;

  private Rim rim1;
  private Rim rim2;
  private Rim rim3;

  @BeforeEach
  void setUp() {

    cartItemRepository.deleteAll();
    orderItemRepository.deleteAll();
    rimRepository.deleteAll();

    rim1 = new Rim();
    rim1.setName("Aluminium");
    rim1.setPrice(new BigDecimal("299.99"));
    rim1.setDescription("description1");
    rim1.setStock(10);
    rim1.setType(ProductType.TIRE);
    rim1.setMaterial("aluminium");
    rim1.setSize("R16");
    rim1.setBoltPattern("12x12");

    rim2 = new Rim();
    rim2.setName("Stall");
    rim2.setPrice(new BigDecimal("349.99"));
    rim2.setDescription("description2");
    rim2.setStock(10);
    rim2.setType(ProductType.TIRE);
    rim2.setMaterial("stall");
    rim2.setSize("R15");
    rim2.setBoltPattern("10x10");

    rim3 = new Rim();
    rim3.setName("Carbon");
    rim3.setPrice(new BigDecimal("399.99"));
    rim3.setDescription("description3");
    rim3.setStock(10);
    rim3.setType(ProductType.TIRE);
    rim3.setMaterial("carbon");
    rim3.setSize("R16");
    rim3.setBoltPattern("12x12");

    rimRepository.saveAll(List.of(rim1, rim2, rim3));
  }

  @Test
  void testHasMaterialAluminiumAndCarbon() {
    Specification<Rim> spec = RimSpecifications.hasMaterial(List.of("aluminium", "carbon"));
    List<Rim> results = rimRepository.findAll(spec);

    assertThat(results).extracting(Rim::getMaterial).containsExactlyInAnyOrder("aluminium",
        "carbon");
  }

  @Test
  void testHasSizes205And195() {
    Specification<Rim> spec = RimSpecifications.hasSize(List.of("R16", "R15"));
    List<Rim> results = rimRepository.findAll(spec);

    assertThat(results).hasSize(3);
  }

  @Test
  void testHasBoltPattern12x12And10x10() {
    Specification<Rim> spec = RimSpecifications.hasBoltPattern(List.of("12x12"));
    List<Rim> results = rimRepository.findAll(spec);

    assertThat(results).hasSize(2);
  }

  @Test
  void testHasNameContainingAluminium() {
    Specification<Rim> spec = RimSpecifications.hasNameContaining("aluminium");
    List<Rim> results = rimRepository.findAll(spec);

    assertThat(results).hasSize(1);
    assertThat(results.get(0).getName()).containsIgnoringCase("Aluminium");
  }

  @Test
  void testHasMinPrice350() {
    Specification<Rim> spec = RimSpecifications.hasMinPrice(new BigDecimal("350.00"));
    List<Rim> results = rimRepository.findAll(spec);

    assertThat(results).extracting(Rim::getPrice)
        .allMatch(price -> price.compareTo(new BigDecimal("350.00")) >= 0);
  }

  @Test
  void testHasMaxPrice300() {
    Specification<Rim> spec = RimSpecifications.hasMaxPrice(new BigDecimal("300.00"));
    List<Rim> results = rimRepository.findAll(spec);

    assertThat(results).hasSize(1);
    assertThat(results.get(0).getPrice()).isLessThanOrEqualTo(new BigDecimal("300.00"));
  }

  @Test
  void testCombinedSpecMaterialAluminiumAndSizeR16AndMinPrice() {
    Specification<Rim> spec =
        Specification.where(RimSpecifications.hasMaterial(List.of("aluminium")))
            .and(RimSpecifications.hasSize(List.of("R16")))
            .and(RimSpecifications.hasMinPrice(new BigDecimal("200")));

    List<Rim> results = rimRepository.findAll(spec);

    assertThat(results).hasSize(1);
    assertThat(results.get(0).getMaterial()).isEqualTo("aluminium");
  }
}
