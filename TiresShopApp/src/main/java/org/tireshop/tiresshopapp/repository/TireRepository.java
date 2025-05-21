package org.tireshop.tiresshopapp.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.tireshop.tiresshopapp.dto.response.FilterCountResponse;
import org.tireshop.tiresshopapp.entity.Tire;

import java.math.BigDecimal;
import java.util.List;

@Repository
public interface TireRepository extends JpaRepository<Tire, Long>, JpaSpecificationExecutor<Tire> {

  @Query("SELECT new org.tireshop.tiresshopapp.dto.response.FilterCountResponse(UPPER(t.season), COUNT(t)) "
      + "FROM Tire t GROUP BY UPPER(t.season)")
  List<FilterCountResponse> countTiresBySeason();

  @Query("SELECT new org.tireshop.tiresshopapp.dto.response.FilterCountResponse(UPPER(t.size), COUNT(t)) "
      + "FROM Tire t GROUP BY UPPER(t.size)")
  List<FilterCountResponse> countTiresBySize();

  @Query("SELECT MIN(t.price) FROM Tire t")
  BigDecimal findMinPrice();

  @Query("SELECT MAX(t.price) FROM Tire t")
  BigDecimal findMaxPrice();
}

