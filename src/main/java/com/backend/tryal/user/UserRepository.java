package com.backend.tryal.user;

import com.backend.tryal.user.dto.UserProfileBookmarkDTO;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface UserRepository extends JpaRepository<User, UUID> {
    Optional<User> findByEmail(String email);
    Boolean existsByEmail(String email);

    @Query(
            value = """
        SELECT
            u.user_id AS userId,
            bus.business_id AS businessId,
            bus.name AS businessName,
            bus.address
        FROM users u
        JOIN user_bookmarks ub ON u.user_id = ub.user_id
        JOIN businesses bus ON ub.business_id = bus.business_id
        WHERE u.user_id = :userId
        """,
            nativeQuery = true
    )
    List<UserProfileBookmarkDTO> getAllUserBookmarksByUserId(@Param("userId") UUID userId);
}
