package org.tireshop.tiresshopapp;

import jakarta.transaction.Transactional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.jpa.domain.Specification;
import org.tireshop.tiresshopapp.entity.*;
import org.tireshop.tiresshopapp.repository.*;
import org.tireshop.tiresshopapp.specifications.ProductSpecifications;

import java.math.BigDecimal;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@Transactional
class ProductSpecificationIntegrationTest {

  @Autowired
  private ProductRepository productRepository;
  @Autowired
  private TireRepository tireRepository;
  @Autowired
  private RimRepository rimRepository;
  @Autowired
  private AccessoryRepository accessoryRepository;

  @Autowired
  private CartItemRepository cartItemRepository;

  @Autowired
  private OrderItemRepository orderItemRepository;

  private Product product;
  private Tire tire;
  private Rim rim;
  private Accessory accessory;



  @BeforeEach
  void setUp() {

    cartItemRepository.deleteAll();
    orderItemRepository.deleteAll();
    productRepository.deleteAll();


    product = new Product();
    product.setName("All item");
    product.setPrice(new BigDecimal("299.99"));
    product.setDescription("description1");
    product.setStock(10);
    product.setType(ProductType.ALL);
    product = productRepository.save(product);

    tire = new Tire();
    tire.setName("Goodyear UltraGrip");
    tire.setPrice(new BigDecimal("349.99"));
    tire.setDescription("carbon2");
    tire.setStock(10);
    tire.setType(ProductType.TIRE);
    tire.setSeason("winter");
    tire.setSize("195/65 R15");
    tire = tireRepository.save(tire);

    rim = new Rim();
    rim.setName("Carbon");
    rim.setPrice(new BigDecimal("399.99"));
    rim.setDescription("description3");
    rim.setStock(10);
    rim.setType(ProductType.RIM);
    rim.setMaterial("carbon");
    rim.setSize("R16");
    rim.setBoltPattern("12x12");
    rim = rimRepository.save(rim);

    accessory = new Accessory();
    accessory.setName("carbon bolt");
    accessory.setPrice(new BigDecimal("150.99"));
    accessory.setDescription("description4");
    accessory.setStock(10);
    accessory.setType(ProductType.ACCESSORY);
    accessory.setAccessoryType(AccessoryType.BOLT);
    accessory = accessoryRepository.save(accessory);

  }


  @Test
  void testHasNameContainingAllItem() {
    Specification<Product> spec = ProductSpecifications.hasNameContaining("all item");
    List<Product> results = productRepository.findAll(spec);

    assertThat(results).hasSize(1);
    assertThat(results.get(0).getName()).containsIgnoringCase("All Item");
  }

  @Test
  void testHasMinPrice350() {
    Specification<Product> spec = ProductSpecifications.hasMinPrice(new BigDecimal("350.00"));
    List<Product> results = productRepository.findAll(spec);

    assertThat(results).extracting(Product::getPrice)
        .allMatch(price -> price.compareTo(new BigDecimal("350.00")) >= 0);
  }

  @Test
  void testHasMaxPrice300() {
    Specification<Product> spec = ProductSpecifications.hasMaxPrice(new BigDecimal("300.00"));
    List<Product> results = productRepository.findAll(spec);

    assertThat(results).hasSize(2);
    assertThat(results.get(0).getPrice()).isLessThanOrEqualTo(new BigDecimal("300.00"));
  }

  @Test
  void testHasProductTypeTire() {
    Specification<Product> spec = ProductSpecifications.hasProductType(ProductType.TIRE);
    List<Product> results = productRepository.findAll(spec);
    assertThat(results).hasSize(1);
    assertThat(results.get(0).getType()).isEqualTo(ProductType.TIRE);
  }

  @Test
  void testCombinedSpecNameAllItemProductTypeAllAndMinPrice() {
    Specification<Product> spec =
        Specification.where(ProductSpecifications.hasNameContaining("all item"))
            .and(ProductSpecifications.hasMinPrice(new BigDecimal("200")))
            .and(ProductSpecifications.hasProductType(ProductType.ALL));

    List<Product> results = productRepository.findAll(spec);

    assertThat(results).hasSize(1);
    assertThat(results.get(0).getName()).isEqualTo("All item");
  }

  @Test
  void testHasKeywordContaining() {
    Specification<Product> spec = ProductSpecifications.hasKeywordContaining("carbon");
    List<Product> results = productRepository.findAll(spec);
    assertThat(results).hasSize(3);
  }
}
