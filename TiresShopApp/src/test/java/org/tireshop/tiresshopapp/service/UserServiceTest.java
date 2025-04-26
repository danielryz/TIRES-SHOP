// package org.tireshop.tiresshopapp.service;
//
// import static org.mockito.Mockito.*;
// import static org.junit.jupiter.api.Assertions.*;
//
// import java.util.*;
//
// import org.junit.jupiter.api.BeforeEach;
// import org.junit.jupiter.api.Test;
// import org.mockito.InjectMocks;
// import org.mockito.Mock;
// import org.mockito.MockitoAnnotations;
// import org.springframework.security.core.Authentication;
// import org.springframework.security.core.context.SecurityContext;
// import org.springframework.security.core.context.SecurityContextHolder;
// import org.springframework.security.crypto.password.PasswordEncoder;
// import org.tireshop.tiresshopapp.dto.request.update.UpdateUserRequest;
// import org.tireshop.tiresshopapp.entity.Role;
// import org.tireshop.tiresshopapp.entity.User;
// import org.tireshop.tiresshopapp.repository.RoleRepository;
// import org.tireshop.tiresshopapp.repository.UserRepository;
//
// class UserServiceTest {
//
// @Mock
// private UserRepository userRepository;
// @Mock
// private RoleRepository roleRepository;
// @Mock
// private PasswordEncoder passwordEncoder;
//
// @InjectMocks
// private UserService userService;
//
// @BeforeEach
// void setUp() {
// MockitoAnnotations.openMocks(this);
// }
//
// @Test
// void shouldReturnAllUsers() {
// User user = new User();
// user.setId(1L);
// when(userRepository.findAll()).thenReturn(List.of(user));
//
// var result = userService.getAllUsers();
//
// assertEquals(1, result.size());
// verify(userRepository, times(1)).findAll();
// }
//
// @Test
// void shouldGetUserById_WhenExists() {
// User user = new User();
// user.setId(1L);
// when(userRepository.findById(1L)).thenReturn(Optional.of(user));
//
// var result = userService.getUserById(1L);
//
// assertNotNull(result);
// }
//
// @Test
// void shouldThrowWhenUserNotFoundById() {
// when(userRepository.findById(1L)).thenReturn(Optional.empty());
//
// RuntimeException ex = assertThrows(RuntimeException.class, () -> userService.getUserById(1L));
// assertEquals("Użytkownik o ID: 1 nie istnieje", ex.getMessage());
// }
//
// @Test
// void shouldGetCurrentUser() {
// User user = new User();
// user.setEmail("test@example.com");
//
// org.springframework.security.core.userdetails.User userDetails =
// new org.springframework.security.core.userdetails.User("test@example.com", "password",
// Collections.emptyList());
//
// Authentication auth = mock(Authentication.class);
// when(auth.getPrincipal()).thenReturn(userDetails); // <-- najważniejsza linijka!
// when(auth.getName()).thenReturn("test@example.com");
//
// SecurityContext context = mock(SecurityContext.class);
// when(context.getAuthentication()).thenReturn(auth);
// SecurityContextHolder.setContext(context);
//
// when(userRepository.findByEmail("test@example.com")).thenReturn(Optional.of(user));
//
// var result = userService.getCurrentUser();
//
// assertEquals("test@example.com", result.getEmail());
// }
//
//
// @Test
// void shouldUpdateCurrentUser() {
// UpdateUserRequest req =
// new UpdateUserRequest("newUsername", "newPassword", "John", "Doe", "123456789");
// User user = new User();
// user.setEmail("user@example.com");
//
// Authentication auth = mock(Authentication.class);
// when(auth.getName()).thenReturn("user@example.com");
//
// SecurityContext context = mock(SecurityContext.class);
// when(context.getAuthentication()).thenReturn(auth);
// SecurityContextHolder.setContext(context);
//
// when(userRepository.findByEmail("user@example.com")).thenReturn(Optional.of(user));
// when(passwordEncoder.encode("newPassword")).thenReturn("encodedPassword");
// when(userRepository.save(any())).thenReturn(user);
//
// var updatedUser = userService.updateCurrentUser(req);
//
// assertEquals("newUsername", updatedUser.getUsername());
// }
//
// @Test
// void shouldAddRoleToUser() {
// User user = new User();
// Role role = new Role();
// role.setName("ADMIN");
//
// when(userRepository.findById(1L)).thenReturn(Optional.of(user));
// when(roleRepository.findById(2L)).thenReturn(Optional.of(role));
// when(userRepository.save(user)).thenReturn(user);
//
// var result = userService.addRoleToUser(1L, 2L);
//
// assertTrue(result.getRoles().contains(role));
// }
//
// @Test
// void shouldRemoveUserRole() {
// User user = new User();
// Role role = new Role();
// role.setName("USER");
// user.setRoles(new HashSet<>(Set.of(role)));
//
// when(userRepository.findById(1L)).thenReturn(Optional.of(user));
// when(roleRepository.findByName("USER")).thenReturn(Optional.of(role));
// when(userRepository.save(user)).thenReturn(user);
//
// userService.removeUserRole(1L, 2L);
//
// assertFalse(user.getRoles().contains(role));
// }
//
// @Test
// void shouldDeleteUserById() {
// when(userRepository.existsById(1L)).thenReturn(true);
//
// userService.deleteUserById(1L);
//
// verify(userRepository, times(1)).deleteById(1L);
// }
//
// @Test
// void shouldThrowWhenDeletingNonExistingUser() {
// when(userRepository.existsById(1L)).thenReturn(false);
//
// RuntimeException ex = assertThrows(RuntimeException.class, () -> userService.deleteUserById(1L));
// assertEquals("Użytkownik o ID: 1 nie istnieje", ex.getMessage());
// }
//
// @Test
// void shouldDeleteCurrentUser() {
// User user = new User();
// user.setEmail("user@example.com");
//
// Authentication auth = mock(Authentication.class);
// when(auth.getName()).thenReturn("user@example.com");
//
// SecurityContext context = mock(SecurityContext.class);
// when(context.getAuthentication()).thenReturn(auth);
// SecurityContextHolder.setContext(context);
//
// when(userRepository.findByEmail("user@example.com")).thenReturn(Optional.of(user));
//
// userService.deleteCurrentUser();
//
// verify(userRepository, times(1)).delete(user);
// }
//
// @Test
// void shouldMapUserToResponse() {
// User user = new User();
// user.setId(1L);
// user.setUsername("testuser");
// user.setEmail("test@example.com");
// user.setFirstName("John");
// user.setLastName("Doe");
// user.setPhoneNumber("123456789");
//
// Role role = new Role();
// role.setName("USER");
// user.setRoles(Set.of(role));
//
// var response = userService.toMapResponse(user);
//
// assertEquals("testuser", response.username());
// assertEquals("test@example.com", response.email());
// assertTrue(response.roles().contains("USER"));
// }
//
// @Test
// void shouldGetCurrentUser_WhenPrincipalIsString() {
// Authentication auth = mock(Authentication.class);
// when(auth.getPrincipal()).thenReturn("test@example.com"); // <-- STRING, nie UserDetails
//
// SecurityContext context = mock(SecurityContext.class);
// when(context.getAuthentication()).thenReturn(auth);
// SecurityContextHolder.setContext(context);
//
// User user = new User();
// user.setEmail("test@example.com");
// when(userRepository.findByEmail("test@example.com")).thenReturn(Optional.of(user));
//
// var result = userService.getCurrentUser();
//
// assertEquals("test@example.com", result.getEmail());
// }
//
// @Test
// void shouldNotUpdateUser_WhenRequestFieldsAreNull() {
// UpdateUserRequest req = new UpdateUserRequest(null, null, null, null, null);
// User user = new User();
// user.setEmail("user@example.com");
//
// Authentication auth = mock(Authentication.class);
// when(auth.getName()).thenReturn("user@example.com");
//
// SecurityContext context = mock(SecurityContext.class);
// when(context.getAuthentication()).thenReturn(auth);
// SecurityContextHolder.setContext(context);
//
// when(userRepository.findByEmail("user@example.com")).thenReturn(Optional.of(user));
// when(userRepository.save(any())).thenReturn(user);
//
// var result = userService.updateCurrentUser(req);
//
// assertEquals("user@example.com", result.getEmail());
// }
//
//
// }
