package org.tireshop.tiresshopapp.service;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.tireshop.tiresshopapp.entity.Role;
import org.tireshop.tiresshopapp.exception.RoleNotFoundException;
import org.tireshop.tiresshopapp.repository.RoleRepository;

import java.util.List;

@Service
@RequiredArgsConstructor
public class RoleService {
  private final RoleRepository roleRepository;

  // GET
  public List<Role> getAllRoles() {
    return roleRepository.findAll();
  }

  public Role getRoleById(Long id) {
    return roleRepository.findById(id).orElseThrow(() -> new RoleNotFoundException(id));
  }

}
