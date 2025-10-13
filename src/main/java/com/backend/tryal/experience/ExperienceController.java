package com.backend.tryal.experience;

import com.backend.tryal.experience.dto.BusinessExperienceDTO;
import com.backend.tryal.experience.dto.ExperienceDTO;
import com.backend.tryal.experience.dto.ExperienceRequestDTO;
import com.backend.tryal.experience.mapper.ExperienceMapper;
import com.backend.tryal.experience.service.ExperienceService;
import com.backend.tryal.shared.response.ApiResponse;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/experiences")
public class ExperienceController {

  private final ExperienceService experienceService;

  public ExperienceController(ExperienceService experienceService) {
    this.experienceService = experienceService;
  }

  // get all experiences or by businessId
  @GetMapping()
  public List<ExperienceDTO> getExperiences(@RequestParam(required = false) UUID businessId) {
    List<ExperienceDTO> experiences;
    if (businessId != null) {
      experiences = experienceService.getExperiencesByBusinessId_TEST(businessId)
          .stream()
          .map(ExperienceMapper::mapExperienceDto)
          .collect(Collectors.toList());
    } else {
      experiences = experienceService.getAllExperiences()
          .stream()
          .map(ExperienceMapper::mapExperienceDto)
          .collect(Collectors.toList());
    }
    return experiences;
  }

  // get experience by ID
  @GetMapping("/{experienceId}")
  public ExperienceDTO getExperienceById(@PathVariable UUID experienceId) {
    Experience experience = experienceService.getExperienceById(experienceId);

    return ExperienceMapper.mapExperienceDto(experience);
  }

  // get experiences with timeslots by business ID
  @GetMapping("/business/{businessId}")
  public List<BusinessExperienceDTO> getExperiencesByBusinessId(@PathVariable UUID businessId) {

    return experienceService.getExperiencesByBusinessId(businessId);
  }

  // create experience
  @PostMapping()
  public ExperienceDTO createExperience(@RequestBody ExperienceRequestDTO experienceRequestDTO) {
    return ExperienceMapper.mapExperienceDto(
        experienceService.createExperience(experienceRequestDTO));
  }

  // patch experience
  @PatchMapping("/{experienceId}")
  public ExperienceDTO updateExperienceById(@RequestBody ExperienceRequestDTO experienceRequestDTO,
      @PathVariable UUID experienceId) {
    Experience updatedExperience = experienceService.updateExperienceById(experienceRequestDTO,
        experienceId);
    return ExperienceMapper.mapExperienceDto(updatedExperience);
  }

  // delete experience
  @DeleteMapping("/{experienceId}")
  public ApiResponse<String> deleteExperienceById(@PathVariable UUID experienceId) {
    experienceService.deleteExperienceById(experienceId);
    return new ApiResponse<>("Experience deleted successfully.");

  }

}
