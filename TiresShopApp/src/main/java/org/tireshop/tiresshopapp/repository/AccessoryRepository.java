package org.tireshop.tiresshopapp.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.tireshop.tiresshopapp.dto.response.AccessoryResponse;
import org.tireshop.tiresshopapp.entity.Accessory;

import java.util.List;
@Repository
public interface AccessoryRepository extends JpaRepository<Accessory, Long> {
    List<AccessoryResponse> findAccessoryByAccessoryType(String accessoryType);
}
