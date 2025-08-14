package com.backend.tryal.user;

import com.backend.tryal.user.dto.UserBookmarkRequestDTO;
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
            ub.user_id AS userId,
            ub.business_id AS businessId
        FROM user_bookmarks ub
        WHERE ub.user_id = :userId
        """,
            nativeQuery = true
    )
    List<UserBookmarkRequestDTO> getAllUserBookmarksByUserId(@Param("userId") UUID userId);
}
