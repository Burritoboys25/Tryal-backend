package com.backend.tryal.groupType;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface GroupTypeRepository extends JpaRepository<GroupType, Long> {
}
