package com.backend.tryal.user.controller;

import com.backend.tryal.shared.response.ApiResponse;
import com.backend.tryal.user.User;
import com.backend.tryal.user.dto.UserDTO;
import com.backend.tryal.user.mapper.UserMapper;
import com.backend.tryal.user.service.UserService;
import jakarta.validation.Valid;
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
    public UserDTO getUserById(@PathVariable UUID userId) {
        User user = userService.getUserById(userId);
        return UserMapper.mapUserDTO(user);
    }

    // Patch User
    @PatchMapping("/{userId}")
    public UserDTO updateUserById(@Valid @RequestBody User user, @PathVariable UUID userId) {
      User updatedUser = userService.updateUserById(userId, user);

      return UserMapper.mapUserDTO(updatedUser);
    }

    // Delete User
    @DeleteMapping("/{userId}")
    public ApiResponse<String> deleteUserById(@PathVariable UUID userId) {
      userService.deleteUserById(userId);

      return new ApiResponse<>("success", "User deleted successfully.");
    }
}
