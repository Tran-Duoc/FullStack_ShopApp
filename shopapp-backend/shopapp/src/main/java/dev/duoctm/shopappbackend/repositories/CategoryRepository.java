package dev.duoctm.shopappbackend.repositories;

import dev.duoctm.shopappbackend.models.Categogy;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CategoryRepository extends  JpaRepository<Categogy, Long> {}
