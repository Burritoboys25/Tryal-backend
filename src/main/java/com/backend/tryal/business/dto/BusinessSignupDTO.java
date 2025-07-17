package com.backend.tryal.business.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class BusinessSignupDTO {
    @NotBlank(message = "Business name is required")
    private String name;

    @Email(message = "Must be a valid email")
    @NotBlank(message = "Email is required")
    private String email;

    @NotBlank(message = "Password is required")
    private String password;

//    @NotBlank(message = "Business address is required")
//    private String address;
//
//    @NotBlank(message = "Business phone number is required")
//    private String phoneNumber;

    public BusinessSignupDTO(){}

    public BusinessSignupDTO(String name, String email, String password) {
        this.name = name;
        this.email = email;
        this.password = password;
    }
}
