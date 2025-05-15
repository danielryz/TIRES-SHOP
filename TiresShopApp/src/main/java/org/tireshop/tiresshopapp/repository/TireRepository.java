package org.tireshop.tiresshopapp.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;
import org.tireshop.tiresshopapp.entity.Tire;

@Repository
public interface TireRepository extends JpaRepository<Tire, Long>, JpaSpecificationExecutor<Tire> {
}
