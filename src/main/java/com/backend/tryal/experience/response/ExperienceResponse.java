package com.backend.tryal.experience.response;

import com.backend.tryal.experience.dto.ExperienceDTO;
import lombok.Data;

@Data
public class ExperienceResponse {
    private ExperienceDTO experienceDTO;
    private String message;

    public ExperienceResponse(ExperienceDTO experienceDTO, String message) {
        this.experienceDTO = experienceDTO;
        this.message = message;
    }
}
