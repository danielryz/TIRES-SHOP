package org.tireshop.tiresshopapp.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.tireshop.tiresshopapp.dto.response.AddressResponse;
import org.tireshop.tiresshopapp.entity.Address;
import org.tireshop.tiresshopapp.entity.AddressType;
import org.tireshop.tiresshopapp.entity.User;

import java.util.List;
import java.util.Optional;

public interface AddressRepository extends JpaRepository<Address, Long> {

  List<Address> findByUser(User user);

  Optional<Address> findByIdAndUser(Long id, User user);

  List<Address> findAddressesByTypeAndUser(AddressType addressType, User user);

}
