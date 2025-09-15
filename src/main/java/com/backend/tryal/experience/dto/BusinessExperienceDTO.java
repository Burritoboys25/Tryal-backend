package com.backend.tryal.experience.dto;

import com.backend.tryal.experience.Experience;
import com.backend.tryal.timeslot.dto.TimeslotDTO;
import java.util.List;
import java.util.UUID;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class BusinessExperienceDTO {
  private UUID experienceId;
  private UUID businessId;
  private String experienceName;
  private String description;
  private Experience.SkillLevel skillLevel;
  private Integer maxCapacity;
  private Integer remainingCapacity;
  private Integer duration;
  private Integer creditPrice;
  private Boolean isActive;
  private List<TimeslotDTO> timeslots;
}
