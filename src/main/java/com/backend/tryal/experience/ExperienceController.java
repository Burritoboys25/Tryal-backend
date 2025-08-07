package com.backend.tryal.experience;

import com.backend.tryal.experience.dto.ExperienceDTO;
import com.backend.tryal.experience.dto.ExperienceRequestDTO;
import com.backend.tryal.experience.mapper.ExperienceMapper;
import com.backend.tryal.experience.response.ExperienceResponse;
import com.backend.tryal.experience.service.ExperienceService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/experiences")
public class ExperienceController {
    private final ExperienceService experienceService;

    public ExperienceController(ExperienceService experienceService) {
        this.experienceService = experienceService;
    }

    // get all experiences
    @GetMapping()
    public ResponseEntity<List<ExperienceDTO>> getAllExperiences() {
        try {
            List<ExperienceDTO> experiences = experienceService.getAllExperiences()
                    .stream()
                    .map(ExperienceMapper::mapExperienceDto)
                    .collect(Collectors.toList());

            if (experiences.isEmpty()) {
                return new ResponseEntity<>(HttpStatus.NO_CONTENT);
            }

            return new ResponseEntity<>(experiences, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    // get experiences by businessId
    @GetMapping("/by-business")
    public ResponseEntity<List<ExperienceDTO>> getExperiencesByBusinessId(@RequestParam UUID businessId) {
        try {
            List<ExperienceDTO> experiences = experienceService.getExperiencesByBusinessId(businessId)
                    .stream()
                    .map(ExperienceMapper::mapExperienceDto)
                    .collect(Collectors.toList());
            if (experiences.isEmpty()) {
                return new ResponseEntity<>(HttpStatus.NO_CONTENT);
            }
            return new ResponseEntity<>(experiences, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    // get experience by ID (uses /id/{experienceId} to avoid ambiguity)
    @GetMapping("/id/{experienceId}")
    public ResponseEntity<ExperienceResponse> getExperienceById(@PathVariable UUID experienceId) {
        try {
            Experience experience = experienceService.getExperienceById(experienceId);
            if (experience == null) {
                return new ResponseEntity<>(new ExperienceResponse(null, "Experience not found."),HttpStatus.NOT_FOUND);
            }
            ExperienceDTO experienceDTO = ExperienceMapper.mapExperienceDto(experience);
            return new ResponseEntity<>(new ExperienceResponse(experienceDTO, "Experience found."), HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    // create experience
    @PostMapping()
    public ResponseEntity<ExperienceResponse> createExperience(@RequestBody ExperienceRequestDTO experienceRequestDTO, @RequestParam UUID businessId) {
        try {
            ExperienceDTO experienceDTO = ExperienceMapper.mapExperienceDto(experienceService.createExperience(experienceRequestDTO, businessId));

            if(experienceDTO == null){
                return new ResponseEntity<>(new ExperienceResponse(null, "Business with id: " + businessId +  " not found."), HttpStatus.NOT_FOUND);
            }

            return new ResponseEntity<>(new ExperienceResponse(experienceDTO, "Experience created successfully."), HttpStatus.CREATED);
        } catch (Exception e) {
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    // patch experience
    @PatchMapping("/{experienceId}")
    public ResponseEntity<ExperienceResponse> updateExperienceById(@RequestBody ExperienceRequestDTO experienceRequestDTO, @PathVariable UUID experienceId) {
        try {
            Experience updatedExperience = experienceService.updateExperienceById(experienceRequestDTO, experienceId);

            if(updatedExperience == null){
                return new ResponseEntity<>(new ExperienceResponse(null, "Experience not found."), HttpStatus.NOT_FOUND);
            }

            ExperienceDTO experienceDTO = ExperienceMapper.mapExperienceDto(updatedExperience);

            return new ResponseEntity<>(new ExperienceResponse(experienceDTO, "Experience updated successfully."), HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    // delete experience
    @DeleteMapping("/{experienceId}")
    public ResponseEntity<String> deleteExperienceById(@PathVariable UUID experienceId) {
        try {

            if (experienceService.deleteExperienceById(experienceId)) {
                return new ResponseEntity<>("Experience deleted successfully.", HttpStatus.OK);
            }

            return new ResponseEntity<>("Experience not found.", HttpStatus.NOT_FOUND);
        } catch (Exception e) {
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

}
