package org.tireshop.tiresshopapp;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.*;
import org.springframework.data.domain.*;
import org.springframework.data.jpa.domain.Specification;
import org.tireshop.tiresshopapp.dto.request.create.CreateProductRequest;
import org.tireshop.tiresshopapp.dto.request.update.UpdateProductRequest;
import org.tireshop.tiresshopapp.dto.response.*;
import org.tireshop.tiresshopapp.entity.*;
import org.tireshop.tiresshopapp.exception.ProductNotFoundException;
import org.tireshop.tiresshopapp.repository.ProductRepository;
import org.tireshop.tiresshopapp.service.ImageService;
import org.tireshop.tiresshopapp.service.ProductService;

import java.math.BigDecimal;
import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class ProductServiceTest {

  @Mock
  private ProductRepository productRepository;

  @Mock
  private ImageService imageService;

  @InjectMocks
  private ProductService productService;

  private Product product;

  @BeforeEach
  void setUp() {
    MockitoAnnotations.openMocks(this);

    product = new Product();
    product.setId(1L);
    product.setName("Test Product");
    product.setPrice(BigDecimal.valueOf(100));
    product.setDescription("Desc");
    product.setStock(10);
    product.setType(ProductType.TIRE);
  }

  @Test
    void shouldReturnProductById() {
        when(productRepository.findById(1L)).thenReturn(Optional.of(product));

        BaseProductResponse response = productService.getProductById(1L);

        assertEquals(1L, response.id());
        assertEquals("Test Product", response.name());
    }

  @Test
    void shouldThrowProductNotFound_WhenGetById() {
        when(productRepository.findById(1L)).thenReturn(Optional.empty());

        assertThrows(ProductNotFoundException.class,
                () -> productService.getProductById(1L));
    }

  @Test
  void shouldGetProductsWithFiltering() {
    Page<Product> page = new PageImpl<>(List.of(product));
    when(productRepository.findAll(any(Specification.class), any(Pageable.class))).thenReturn(page);

    Page<BaseProductResponse> result = productService.getProducts("Test", BigDecimal.valueOf(50),
        BigDecimal.valueOf(200), ProductType.TIRE, 0, 10, "price,asc");

    assertEquals(1, result.getTotalElements());
    assertEquals("Test Product", result.getContent().get(0).name());
  }

  @Test
  void shouldSearchProductsByKeyword() {
    Page<Product> page = new PageImpl<>(List.of(product));
    when(productRepository.findAll(any(Specification.class), any(Pageable.class))).thenReturn(page);

    Page<BaseProductResponse> result = productService.searchProducts("key", 0, 10, "name,desc");

    assertEquals(1, result.getTotalElements());
    assertEquals("Test Product", result.getContent().get(0).name());
  }

  @Test
  void shouldCreateProductsFromRequests() {
    CreateProductRequest req =
        new CreateProductRequest("New", BigDecimal.valueOf(150), "desc", 5, ProductType.ACCESSORY);
    Product newProduct = new Product();
    newProduct.setId(2L);
    newProduct.setName("New");
    newProduct.setPrice(BigDecimal.valueOf(150));
    newProduct.setDescription("desc");
    newProduct.setStock(5);
    newProduct.setType(ProductType.ACCESSORY);

    when(productRepository.saveAll(anyList())).thenReturn(List.of(newProduct));

    List<BaseProductResponse> responses = productService.createProduct(List.of(req));

    assertEquals(1, responses.size());
    assertEquals("New", responses.get(0).name());
  }

  @Test
  void shouldUpdateProductFields() {
    UpdateProductRequest req = new UpdateProductRequest("Updated", BigDecimal.valueOf(200),
        "Updated Desc", 15, ProductType.RIM);

    when(productRepository.findById(1L)).thenReturn(Optional.of(product));

    productService.updateProduct(1L, req);

    assertEquals("Updated", product.getName());
    assertEquals(BigDecimal.valueOf(200), product.getPrice());
    assertEquals("Updated Desc", product.getDescription());
    assertEquals(15, product.getStock());
    assertEquals(ProductType.RIM, product.getType());

    verify(productRepository).save(product);
  }

  @Test
  void shouldThrowWhenUpdateProductNotFound() {
    UpdateProductRequest req =
        new UpdateProductRequest("A", BigDecimal.ONE, "B", 1, ProductType.TIRE);

    when(productRepository.findById(1L)).thenReturn(Optional.empty());

    assertThrows(ProductNotFoundException.class, () -> productService.updateProduct(1L, req));
  }

  @Test
    void shouldDeleteProduct() {
        when(productRepository.existsById(1L)).thenReturn(true);

        productService.deleteProduct(1L);

        verify(imageService).deleteImagesByProductId(1L);
        verify(productRepository).deleteById(1L);
    }

  @Test
    void shouldThrowWhenDeleteProductNotFound() {
        when(productRepository.existsById(1L)).thenReturn(false);

        assertThrows(ProductNotFoundException.class,
                () -> productService.deleteProduct(1L));
    }

  @Test
  void shouldMapTireToResponse() {
    Tire tire = new Tire();
    tire.setId(1L);
    tire.setName("Tire");
    tire.setPrice(BigDecimal.TEN);
    tire.setStock(2);
    tire.setDescription("desc");
    tire.setType(ProductType.TIRE);
    tire.setSeason("Winter");
    tire.setSize("225/55");

    BaseProductResponse res = callMapToResponse(tire);
    assertInstanceOf(TireResponse.class, res);
    assertEquals("Tire", res.name());
    assertEquals(BigDecimal.TEN, res.price());
    assertEquals(2, res.stock());
    assertEquals("desc", res.description());
    assertEquals(ProductType.TIRE, res.type());
    assertEquals("Winter", ((TireResponse) res).season());
    assertEquals("225/55", ((TireResponse) res).size());
  }

  @Test
  void shouldMapRimToResponse() {
    Rim rim = new Rim();
    rim.setId(2L);
    rim.setName("Rim");
    rim.setPrice(BigDecimal.TEN);
    rim.setStock(2);
    rim.setDescription("desc");
    rim.setType(ProductType.RIM);
    rim.setMaterial("Aluminium");
    rim.setSize("17");
    rim.setBoltPattern("5x112");

    BaseProductResponse res = callMapToResponse(rim);
    assertInstanceOf(RimResponse.class, res);
    assertEquals("Rim", res.name());
    assertEquals(BigDecimal.TEN, res.price());
    assertEquals(2, res.stock());
    assertEquals("desc", res.description());
    assertEquals(ProductType.RIM, res.type());
    assertEquals("Aluminium", ((RimResponse) res).material());
    assertEquals("17", ((RimResponse) res).size());
    assertEquals("5x112", ((RimResponse) res).boltPattern());
  }

  @Test
  void shouldMapAccessoryToResponse() {
    Accessory accessory = new Accessory();
    accessory.setId(3L);
    accessory.setName("Cap");
    accessory.setPrice(BigDecimal.TEN);
    accessory.setStock(2);
    accessory.setDescription("desc");
    accessory.setType(ProductType.ACCESSORY);
    accessory.setAccessoryType(AccessoryType.CHAINS);

    BaseProductResponse res = callMapToResponse(accessory);
    assertInstanceOf(AccessoryResponse.class, res);
    assertEquals("Cap", res.name());
    assertEquals(BigDecimal.TEN, res.price());
    assertEquals(2, res.stock());
    assertEquals("desc", res.description());
    assertEquals(ProductType.ACCESSORY, res.type());
    assertEquals(AccessoryType.CHAINS, ((AccessoryResponse) res).accessoryType());
  }

  @Test
  void shouldMapGenericProductToResponse() {
    BaseProductResponse res = callMapToResponse(product);
    assertInstanceOf(ProductResponse.class, res);
  }

  private BaseProductResponse callMapToResponse(Product product) {
    try {
      var method = ProductService.class.getDeclaredMethod("mapToResponse", Product.class);
      method.setAccessible(true);
      return (BaseProductResponse) method.invoke(productService, product);
    } catch (Exception e) {
      throw new RuntimeException(e);
    }
  }
}
