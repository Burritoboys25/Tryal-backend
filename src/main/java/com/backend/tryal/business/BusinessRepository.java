package com.backend.tryal.business;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface BusinessRepository extends JpaRepository<Business, UUID> {
    Optional<Business> findByEmail(String email);
    Boolean existsByEmail(String email);
}
