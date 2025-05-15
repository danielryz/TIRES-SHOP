package org.tireshop.tiresshopapp.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;
import org.tireshop.tiresshopapp.entity.Rim;

@Repository
public interface RimRepository extends JpaRepository<Rim, Long>, JpaSpecificationExecutor<Rim> {
}
