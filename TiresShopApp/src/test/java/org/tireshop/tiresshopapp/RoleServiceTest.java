package org.tireshop.tiresshopapp;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.tireshop.tiresshopapp.entity.Role;
import org.tireshop.tiresshopapp.exception.RoleNotFoundException;
import org.tireshop.tiresshopapp.repository.RoleRepository;
import org.tireshop.tiresshopapp.service.RoleService;

class RoleServiceTest {

  @Mock
  private RoleRepository roleRepository;

  @InjectMocks
  private RoleService roleService;

  @BeforeEach
  void setUp() {
    MockitoAnnotations.openMocks(this);
  }

  @Test
  void shouldReturnAllRoles_WhenRolesExist() {
    // Arrange
    Role role1 = new Role();
    role1.setId(1L);
    role1.setName("ADMIN");

    Role role2 = new Role();
    role2.setId(2L);
    role2.setName("USER");

    when(roleRepository.findAll()).thenReturn(Arrays.asList(role1, role2));

    // Act
    List<Role> result = roleService.getAllRoles();

    // Assert
    assertEquals(2, result.size());
    assertEquals("ADMIN", result.get(0).getName());
    assertEquals("USER", result.get(1).getName());
  }

  @Test
    void shouldReturnEmptyList_WhenNoRolesExist() {
        // Arrange
        when(roleRepository.findAll()).thenReturn(Collections.emptyList());

        // Act
        List<Role> result = roleService.getAllRoles();

        // Assert
        assertNotNull(result);
        assertTrue(result.isEmpty());
    }

  @Test
  void shouldReturnRoleById_WhenExists() {
    // Arrange
    Role role = new Role();
    role.setId(1L);
    role.setName("ADMIN");

    when(roleRepository.findById(1L)).thenReturn(Optional.of(role));

    // Act
    Role result = roleService.getRoleById(1L);

    // Assert
    assertNotNull(result);
    assertEquals("ADMIN", result.getName());
  }

  @Test
  void shouldThrowRoleNotFoundException_WhenRoleDoesNotExist() {
    // Arrange
    Long id = 99L;
    when(roleRepository.findById(id)).thenReturn(Optional.empty());

    // Act & Assert
    assertThrows(RoleNotFoundException.class, () -> roleService.getRoleById(id));
  }
}
