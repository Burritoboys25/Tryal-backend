package com.backend.tryal.experience.response;

import com.backend.tryal.experience.dto.ExperienceDTO;

public class ExperienceResponse {
    private ExperienceDTO experienceDTO;
    private String message;

    public ExperienceResponse(ExperienceDTO experienceDTO, String message) {
        this.experienceDTO = experienceDTO;
        this.message = message;
    }

    public ExperienceDTO getExperienceDTO() {
        return experienceDTO;
    }

    public void setExperienceDTO(ExperienceDTO experienceDTO) {
        this.experienceDTO = experienceDTO;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
