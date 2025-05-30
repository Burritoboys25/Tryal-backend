package com.backend.tryal.security.controller;

import com.backend.tryal.user.User;
import com.backend.tryal.user.UserRepository;
import com.backend.tryal.user.dto.UserDTO;
import com.backend.tryal.user.mapper.UserMapper;
import com.backend.tryal.user.response.UserResponse;
import com.backend.tryal.user.service.UserService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/auth")
public class AuthController {
    private final UserService userService;
    private final UserRepository userRepository;

    public AuthController(UserService userService, UserRepository userRepository) {
        this.userService = userService;
        this.userRepository = userRepository;
    }

    // Create User
    @PostMapping("/user")
    public ResponseEntity<UserResponse> createUser(@RequestBody User user) {
        try {
            if (userRepository.existsByEmail(user.getEmail())) {
                return new ResponseEntity<>(new UserResponse(null, "Email is already taken."), HttpStatus.BAD_REQUEST);
            }


            UserDTO newUser = UserMapper.mapUserDto(userService.createUser(user));
            return new ResponseEntity<>(new UserResponse(newUser, "User created successfully."), HttpStatus.CREATED);
        } catch (Exception e) {
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}
