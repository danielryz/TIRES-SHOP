package org.tireshop.tiresshopapp.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.tireshop.tiresshopapp.dto.response.AccessoryFilterCountResponse;
import org.tireshop.tiresshopapp.entity.Accessory;

import java.math.BigDecimal;
import java.util.List;

@Repository
public interface AccessoryRepository
    extends JpaRepository<Accessory, Long>, JpaSpecificationExecutor<Accessory> {

  @Query("SELECT new org.tireshop.tiresshopapp.dto.response.AccessoryFilterCountResponse(a.accessoryType, COUNT(a)) "
      + "FROM Accessory a GROUP BY a.accessoryType")
  List<AccessoryFilterCountResponse> countAccessoryByType();

  @Query("SELECT MIN(a.price) FROM Accessory a")
  BigDecimal findMinPrice();

  @Query("SELECT MAX(a.price) FROM Accessory a")
  BigDecimal findMaxPrice();
}
