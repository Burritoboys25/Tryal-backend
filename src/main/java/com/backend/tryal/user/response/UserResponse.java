package com.backend.tryal.user.response;

import com.backend.tryal.user.dto.UserDTO;
import lombok.Data;

@Data
public class UserResponse {
    private UserDTO userDTO;
    private String message;

    public UserResponse(UserDTO userDTO, String message) {
        this.userDTO = userDTO;
        this.message = message;
    }
}
