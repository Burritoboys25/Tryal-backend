package com.backend.h2ak.repository;

import com.backend.h2ak.model.Category;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface CategoryRepository extends JpaRepository<Category, UUID> {
    void deleteByCategoryId(UUID id);

//    Optional<Category> findByCategoryId(UUID id);
}
