package com.backend.tryal.timeslot;

import com.backend.tryal.experience.dto.BusinessExperienceDTO;
import com.backend.tryal.timeslot.dto.TimeslotDTO;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface TimeslotRepository extends JpaRepository<Timeslot, UUID> {

  @Query(
      value = """
          SELECT
            t.*
          FROM experiences e
          INNER JOIN timeslots t
              ON e.experience_id = t.experience_id
          WHERE e.business_id = :businessId
        """,
      nativeQuery = true
  )
  List<Timeslot> findByBusinessId(@Param("businessId") UUID businessId);
}
