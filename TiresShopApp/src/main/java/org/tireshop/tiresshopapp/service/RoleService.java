package org.tireshop.tiresshopapp.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.tireshop.tiresshopapp.entity.Role;
import org.tireshop.tiresshopapp.repository.RoleRepository;

import java.util.List;

@Service
@RequiredArgsConstructor
public class RoleService {
    private final RoleRepository roleRepository;
//GET
    public List<Role> getAllRoles() {
        return roleRepository.findAll();
    }

    public Role getRoleById(Long id) {
        return roleRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Rola o id " + id + " nie istnieje"));
    }
//POST
    public Role createRole(String name) {
        if (roleRepository.findByName(name).isPresent()) {
            throw new RuntimeException("Rola " + name + " już istnieje");
        }
        Role role = new Role();
        role.setName(name);
        return roleRepository.save(role);
    }
//PATCH
    public Role updateRole(Long id, String newName) {
        Role role = roleRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Rola o id " + id + " nie istnieje"));

        if (roleRepository.findByName(newName).isPresent()) {
            throw new RuntimeException("Rola " +newName+ "już istnieje");
        }
        role.setName(newName);
        return roleRepository.save(role);
    }
//DELETE
    public void deleteRole(String name){
        Role role = roleRepository.findByName(name)
                .orElseThrow(() -> new RuntimeException("Rola " + name + " nie istnieje"));

        if (!role.getUsers().isEmpty()) {
            throw new RuntimeException("Nie można usunąć Roli przypisanej do użytkownika");
        }
        roleRepository.delete(role);
    }
}
