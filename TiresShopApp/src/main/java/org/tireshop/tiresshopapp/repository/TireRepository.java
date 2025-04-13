package org.tireshop.tiresshopapp.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.tireshop.tiresshopapp.dto.response.TireResponse;
import org.tireshop.tiresshopapp.entity.Tire;

import java.util.List;

public interface TireRepository extends JpaRepository<Tire, Long> {

    List<TireResponse> getTireBySeason(String seasonName);
    List<TireResponse> getTireBySize(String sizeName);
}
