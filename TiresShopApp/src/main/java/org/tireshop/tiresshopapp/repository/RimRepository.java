package org.tireshop.tiresshopapp.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.tireshop.tiresshopapp.dto.response.RimResponse;
import org.tireshop.tiresshopapp.entity.Rim;

import java.util.List;

public interface RimRepository extends JpaRepository<Rim, Long> {

    List<RimResponse> getRimBySize(String size);

    List<RimResponse> getRimByMaterial(String material);
}
