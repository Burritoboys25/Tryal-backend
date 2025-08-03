package com.backend.tryal.user.controller;

import com.backend.tryal.business.service.BusinessService;
import com.backend.tryal.user.User;
import com.backend.tryal.user.dto.UserBookmarkRequestDTO;
import com.backend.tryal.user.dto.UserProfileBookmarkDTO;
import com.backend.tryal.user.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
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
    public ResponseEntity<List<UserProfileBookmarkDTO>> getAllUserBookmarks(@PathVariable UUID userId) {
        try {
            List<UserProfileBookmarkDTO> userBookmarks = userService.getAllUserBookmarksByUserId(userId);
            if (userBookmarks.isEmpty()) {
                return new ResponseEntity<>(HttpStatus.NO_CONTENT);
            }

            return new ResponseEntity<>(userBookmarks, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    // add user bookmark
    @PostMapping()
    public ResponseEntity<String> addUserBookmark(@RequestBody UserBookmarkRequestDTO bookmarkRequestDTO) {
        try {
            User user = userService.addUserBookmark(bookmarkRequestDTO);

            if (user == null) {
                return new ResponseEntity<>(HttpStatus.NO_CONTENT);
            }
            return new ResponseEntity<>("User bookmark saved successfully", HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    // Remove user bookmark
    @DeleteMapping("/{userId}/{businessId}")
    public ResponseEntity<String> removeUserBookmark(@PathVariable UUID userId, @PathVariable UUID businessId) {
        try {
            if (userService.removeUserBookmark(userId, businessId)) {
                return new ResponseEntity<>("Remove user bookmark successfully.", HttpStatus.OK);
            }

            return new ResponseEntity<>("User or business not found.", HttpStatus.NOT_FOUND);
        } catch (Exception e) {
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}
