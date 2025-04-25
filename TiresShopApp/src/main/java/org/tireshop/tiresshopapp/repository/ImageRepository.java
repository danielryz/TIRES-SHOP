package org.tireshop.tiresshopapp.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.tireshop.tiresshopapp.entity.Image;
import org.tireshop.tiresshopapp.entity.Product;

import java.util.List;

@Repository
public interface ImageRepository extends JpaRepository<Image, Long> {

  List<Image> findByProduct(Product product);

  void deleteByProduct(Product product);
}
