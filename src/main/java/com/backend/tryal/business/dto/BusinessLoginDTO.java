package com.backend.tryal.business.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class BusinessLoginDTO {
    @Email(message = "Must be a valid email")
    @NotBlank(message = "Email is required")
    private String email;

    @NotBlank(message = "Password is required")
    private String password;

    public BusinessLoginDTO() {}

    public BusinessLoginDTO(String email, String password) {
        this.email = email;
        this.password = password;
    }
}
