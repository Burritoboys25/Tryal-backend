package com.backend.tryal.timeslot;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface TimeslotRepository extends JpaRepository<Timeslot, UUID> {
}
