package com.backend.tryal.user.controller;

import com.backend.tryal.shared.response.ApiResponse;
import com.backend.tryal.user.User;
import com.backend.tryal.user.dto.UserBookmarkRequestDTO;
import com.backend.tryal.user.dto.UserProfileBookmarkDTO;
import com.backend.tryal.user.service.UserService;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("api/user-bookmarks")
public class UserBookmarkController {
    @Autowired
    UserService userService;

    // get all Users bookmarks
    @GetMapping("/{userId}")
    public List<UserProfileBookmarkDTO> getAllUserBookmarks(@PathVariable UUID userId) {
        return userService.getAllUserBookmarksByUserId(userId);
    }

    // add user bookmark
    @PostMapping()
    public ApiResponse<String> addUserBookmark(@RequestBody UserBookmarkRequestDTO bookmarkRequestDTO) {
            userService.addUserBookmark(bookmarkRequestDTO);

//            if (user == null) {
//                throw new EntityNotFoundException("User ID or business ID does not exist.");
//            }

            return new ApiResponse<>("success", "User bookmark saved successfully.");
    }

    // Remove user bookmark
    @DeleteMapping("/{userId}/{businessId}")
    public ApiResponse<String> removeUserBookmark(@PathVariable UUID userId, @PathVariable UUID businessId) {
        userService.removeUserBookmark(userId, businessId);

        return new ApiResponse<>("success", "Remove user bookmark successfully.");
    }
}
