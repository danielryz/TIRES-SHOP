package org.tireshop.tiresshopapp;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.transaction.annotation.Transactional;
import org.tireshop.tiresshopapp.entity.Role;
import org.tireshop.tiresshopapp.entity.User;
import org.tireshop.tiresshopapp.repository.CartItemRepository;
import org.tireshop.tiresshopapp.repository.RoleRepository;
import org.tireshop.tiresshopapp.repository.UserRepository;
import org.tireshop.tiresshopapp.specifications.UserSpecification;

import java.util.List;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@Transactional
public class UserSpecificationIntegrationTest {

  @Autowired
  private UserRepository userRepository;

  @Autowired
  private RoleRepository roleRepository;

  @Autowired
  private CartItemRepository cartItemRepository;

  private User user1;
  private User user2;

  @BeforeEach
  void setUp() {
    userRepository.deleteAll();
    roleRepository.deleteAll();
    cartItemRepository.deleteAll();

    Role adminRole = new Role();
    adminRole.setName("ADMIN");
    roleRepository.save(adminRole);

    Role userRole = new Role();
    userRole.setName("USER");
    roleRepository.save(userRole);

    user1 = new User();
    user1.setEmail("integration@test.pl");
    user1.setPhoneNumber("123456789");
    user1.setPassword("Pa$$word1");
    user1.setUsername("integration");
    user1.setFirstName("John");
    user1.setLastName("Doe");
    user1.setEnabled(true);
    user1.setRoles(Set.of(adminRole));
    userRepository.save(user1);

    user2 = new User();
    user2.setEmail("integration@test2.pl");
    user2.setPhoneNumber("987654321");
    user2.setPassword("Pa$$word2");
    user2.setUsername("integration2");
    user2.setFirstName("Jane");
    user2.setLastName("Smith");
    user2.setEnabled(true);
    user2.setRoles(Set.of(userRole));
    userRepository.save(user2);
  }

  @Test
  void testHasEmailContaining() {
    Specification<User> spec = UserSpecification.hasEmailContaining("@test2");
    List<User> users = userRepository.findAll(spec);
    assertEquals(1, users.size());
    assertTrue(users.get(0).getEmail().contains("@test2"));

    spec = UserSpecification.hasEmailContaining(null);
    users = userRepository.findAll(spec);
    assertEquals(2, users.size());
  }

  @Test
  void testHasUsernameContaining() {
    Specification<User> spec = UserSpecification.hasUsernameContaining("integration2");
    List<User> users = userRepository.findAll(spec);
    assertEquals(1, users.size());
    assertTrue(users.get(0).getUsername().contains("integration2"));

    spec = UserSpecification.hasUsernameContaining(null);
    users = userRepository.findAll(spec);
    assertEquals(2, users.size());
  }

  @Test
  void testHasFirstNameContaining() {
    Specification<User> spec = UserSpecification.hasFirstNameContaining("john");
    List<User> users = userRepository.findAll(spec);
    assertEquals(1, users.size());
    assertEquals("John", users.get(0).getFirstName());

    spec = UserSpecification.hasFirstNameContaining(null);
    users = userRepository.findAll(spec);
    assertEquals(2, users.size());
  }

  @Test
  void testHasLastNameContaining() {
    Specification<User> spec = UserSpecification.hasLastNameContaining("smith");
    List<User> users = userRepository.findAll(spec);
    assertEquals(1, users.size());
    assertEquals("Smith", users.get(0).getLastName());

    spec = UserSpecification.hasLastNameContaining(null);
    users = userRepository.findAll(spec);
    assertEquals(2, users.size());
  }

  @Test
  void testHasPhoneNumberContaining() {
    Specification<User> spec = UserSpecification.hasPhoneNumberContaining("123");
    List<User> users = userRepository.findAll(spec);
    assertEquals(1, users.size());
    assertTrue(users.get(0).getPhoneNumber().contains("123"));

    spec = UserSpecification.hasPhoneNumberContaining(null);
    users = userRepository.findAll(spec);
    assertEquals(2, users.size());
  }

  @Test
  void testHasRole() {
    Specification<User> spec = UserSpecification.hasRole("admin");
    List<User> users = userRepository.findAll(spec);
    assertEquals(1, users.size());
    assertTrue(users.get(0).getRoles().stream().anyMatch(r -> r.getName().equals("ADMIN")));

    spec = UserSpecification.hasRole("user");
    users = userRepository.findAll(spec);
    assertEquals(1, users.size());
    assertTrue(users.get(0).getRoles().stream().anyMatch(r -> r.getName().equals("USER")));

    spec = UserSpecification.hasRole(null);
    users = userRepository.findAll(spec);
    assertEquals(2, users.size());
  }
}
