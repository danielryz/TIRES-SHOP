package org.tireshop.tiresshopapp.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.tireshop.tiresshopapp.entity.ShippingAddress;

@Repository
public interface ShippingAddressRepository extends JpaRepository<ShippingAddress, Long> {
}
