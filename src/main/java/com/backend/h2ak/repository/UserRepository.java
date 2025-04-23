package com.backend.h2ak.repository;

import com.backend.h2ak.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface UserRepository extends JpaRepository<User, UUID> {
    void deleteByUserId(UUID id);

//    Optional<User> findByUserId(UUID id);
}
