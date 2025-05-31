package com.backend.tryal.experience;

import com.backend.tryal.experience.dto.ExperienceDTO;
import com.backend.tryal.experience.dto.ExperienceRequestDTO;
import com.backend.tryal.experience.response.ExperienceResponse;
import com.backend.tryal.experience.service.ExperienceService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

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
            List<Experience> experiences = experienceService.getAllExperiences();


            return null;
            //return new ResponseEntity<>(experiences, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    // get experience by ID
    @GetMapping("/{experienceId}")
    public ResponseEntity<ExperienceResponse> getExperienceById(@PathVariable UUID experienceId) {
        try {

            return new ResponseEntity<>(new ExperienceResponse(null, "Experience found."), HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    // create experience
    @PostMapping()
    public ResponseEntity<ExperienceResponse> createExperience(@RequestBody ExperienceRequestDTO experienceRequestDTO, @RequestParam UUID businessId) {
        try {


            return new ResponseEntity<>(new ExperienceResponse(null, "Experience created successfully."), HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    // patch experience
    @PatchMapping("/{experienceId}")
    public ResponseEntity<ExperienceResponse> updateExperienceById(@RequestBody ExperienceRequestDTO experienceRequestDTO, @PathVariable UUID experienceId) {
        try {


            return new ResponseEntity<>(new ExperienceResponse(null, "Experience updated successfully."), HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    // delete experience
    @DeleteMapping("/{experienceId}")
    public ResponseEntity<String> deleteExperienceById(@PathVariable UUID experienceId) {
        try {

            return new ResponseEntity<>("Experience not found.", HttpStatus.NOT_FOUND);
        } catch (Exception e) {
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

}
