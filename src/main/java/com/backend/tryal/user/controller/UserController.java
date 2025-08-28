package com.backend.tryal.user.controller;

import com.backend.tryal.user.User;
import com.backend.tryal.user.dto.UserDTO;
import com.backend.tryal.user.mapper.UserMapper;
import com.backend.tryal.user.response.UserResponse;
import com.backend.tryal.user.service.UserService;
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
    public List<UserDTO> getAllUsers() {
      return userService.getAllUsers()
          .stream()
          .map(UserMapper::mapUserDTO)
          .collect(Collectors.toList());
    }

    // get User by ID
    @GetMapping("/{userId}")
    public User getUserById(@PathVariable UUID userId) {
        return userService.getUserById(userId);
    }

    // Patch User
    @PatchMapping("/{userId}")
    public ResponseEntity<UserResponse> updateUserById(@RequestBody User user, @PathVariable UUID userId) {
        try {
            User updatedUser = userService.updateUserById(userId, user);

            if (updatedUser == null) {
                return new ResponseEntity<>(new UserResponse(null, "User not found."), HttpStatus.NOT_FOUND);
            }

            UserDTO userDTO = UserMapper.mapUserDTO(updatedUser);

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
