package com.backend.h2ak.repository;

import com.backend.h2ak.model.Business;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface BusinessRepository extends JpaRepository<Business, UUID> {
    void deleteByBusinessId(UUID id);

    Optional<Business> findByBusiness(UUID id);
}
