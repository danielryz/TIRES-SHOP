package org.tireshop.tiresshopapp.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.tireshop.tiresshopapp.dto.response.RimResponse;
import org.tireshop.tiresshopapp.entity.Rim;

import java.util.List;
@Repository
public interface RimRepository extends JpaRepository<Rim, Long> {

    List<RimResponse> findRimBySize(String size);

    List<RimResponse> findRimByMaterial(String material);
}
