package org.tireshop.tiresshopapp.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.tireshop.tiresshopapp.dto.response.TireResponse;
import org.tireshop.tiresshopapp.entity.Tire;

import java.util.List;
@Repository
public interface TireRepository extends JpaRepository<Tire, Long> {

    List<TireResponse> findTireBySeason(String seasonName);
    List<TireResponse> findTireBySize(String sizeName);
}
