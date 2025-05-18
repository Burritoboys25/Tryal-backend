package com.backend.h2ak.user;

import com.backend.h2ak.user.dto.UserDTO;
import com.backend.h2ak.user.mapper.UserMapper;
import com.backend.h2ak.user.response.UserResponse;
import com.backend.h2ak.user.service.UserService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/users")
public class UserController {
    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    // get all Users
    @GetMapping()
    public ResponseEntity<List<UserDTO>> getAllUsers() {
        try {
            List<UserDTO> users = userService.getAllUsers()
                    .stream()
                    .map(UserMapper::mapUserDto)
                    .collect(Collectors.toList());

            if (users.isEmpty()) {
                return new ResponseEntity<>(HttpStatus.NO_CONTENT);
            }

            return new ResponseEntity<>(users, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    // get User by ID
    @GetMapping("/{userId}")
    public ResponseEntity<UserResponse> getUserById(@PathVariable UUID userId) {
        try {
            User user = userService.getUserById(userId);

            if (user == null) {
                return new ResponseEntity<>(new UserResponse(null, "User not found."),HttpStatus.NOT_FOUND);
            }

            UserDTO userDTO = UserMapper.mapUserDto(user);

            return new ResponseEntity<>(new UserResponse(userDTO, "User found."), HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    // Create User
    @PostMapping()
    public ResponseEntity<UserResponse> createUser(@RequestBody User user) {
        try {
            UserDTO newUser = UserMapper.mapUserDto(userService.createUser(user));
            return new ResponseEntity<>(new UserResponse(newUser, "User created successfully."), HttpStatus.CREATED);
        } catch (Exception e) {
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    // Patch User
    @PatchMapping("/{userId}")
    public ResponseEntity<UserResponse> updateUserById(@RequestBody User user, @PathVariable UUID userId) {
        try {
            User updatedUser = userService.updateUserById(userId, user);

            if (updatedUser == null) {
                return new ResponseEntity<>(new UserResponse(null, "User not found."), HttpStatus.NOT_FOUND);
            }

            UserDTO userDTO = UserMapper.mapUserDto(updatedUser);

            return new ResponseEntity<>(new UserResponse(userDTO, "User updated successfully."), HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    // Delete User
    @DeleteMapping("/{userId}")
    public ResponseEntity<String> deleteUserById(@PathVariable UUID userId) {
        try {
            if (userService.deleteUserById(userId)) {
                return new ResponseEntity<>("User deleted successfully.", HttpStatus.OK);
            }
            return new ResponseEntity<>("User not found.", HttpStatus.NOT_FOUND);
        } catch (Exception e) {
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}
