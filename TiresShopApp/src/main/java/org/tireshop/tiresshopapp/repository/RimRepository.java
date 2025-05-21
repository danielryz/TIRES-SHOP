package org.tireshop.tiresshopapp.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.tireshop.tiresshopapp.dto.response.FilterCountResponse;
import org.tireshop.tiresshopapp.entity.Rim;

import java.math.BigDecimal;
import java.util.List;

@Repository
public interface RimRepository extends JpaRepository<Rim, Long>, JpaSpecificationExecutor<Rim> {
  @Query("SELECT new org.tireshop.tiresshopapp.dto.response.FilterCountResponse(UPPER(r.material), COUNT(r)) "
      + "FROM Rim r GROUP BY UPPER(r.material)")
  List<FilterCountResponse> countRimByMaterial();

  @Query("SELECT new org.tireshop.tiresshopapp.dto.response.FilterCountResponse(UPPER(r.size), COUNT(r)) "
      + "FROM Rim r GROUP BY UPPER(r.size)")
  List<FilterCountResponse> countRimBySize();

  @Query("SELECT new org.tireshop.tiresshopapp.dto.response.FilterCountResponse(UPPER(r.boltPattern), COUNT(r)) "
      + "FROM Rim r GROUP BY UPPER(r.boltPattern)")
  List<FilterCountResponse> countRimByBoltPattern();

  @Query("SELECT MIN(r.price) FROM Rim r")
  BigDecimal findMinPrice();

  @Query("SELECT MAX(r.price) FROM Rim r")
  BigDecimal findMaxPrice();
}
