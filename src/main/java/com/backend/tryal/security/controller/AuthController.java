package com.backend.tryal.security.controller;

import com.backend.tryal.business.dto.BusinessDTO;
import com.backend.tryal.business.dto.BusinessLoginDTO;
import com.backend.tryal.business.dto.BusinessSignupDTO;
import com.backend.tryal.business.mapper.BusinessMapper;
import com.backend.tryal.business.response.BusinessResponse;
import com.backend.tryal.business.service.BusinessService;
import com.backend.tryal.security.dto.RefreshTokenRequest;
import com.backend.tryal.security.dto.TokenPair;
import com.backend.tryal.user.dto.UserDTO;
import com.backend.tryal.user.dto.UserLoginDTO;
import com.backend.tryal.user.dto.UserSignupDTO;
import com.backend.tryal.user.mapper.UserMapper;
import com.backend.tryal.user.response.UserResponse;
import com.backend.tryal.user.service.UserService;
import jakarta.validation.Valid;
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

    private final BusinessService businessService;

    public AuthController(UserService userService, BusinessService businessService) {
        this.userService = userService;
        this.businessService = businessService;
    }

    // Signup User
    @PostMapping("/user/signup")
    public ResponseEntity<UserResponse> signupUser(@Valid @RequestBody UserSignupDTO signupDTO) {
        try {
            UserDTO newUser = UserMapper.mapUserDTO(userService.createUser(signupDTO));
            return new ResponseEntity<>(new UserResponse(newUser, "User created successfully."), HttpStatus.CREATED);
        } catch (IllegalArgumentException e) {
            return new ResponseEntity<>(new UserResponse(null, e.getMessage()), HttpStatus.BAD_REQUEST);
        } catch (Exception e) {
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    // Login User
    @PostMapping("/user/login")
    public ResponseEntity<?> loginUser(@Valid @RequestBody UserLoginDTO loginDTO) {
        TokenPair tokenPair = userService.loginUser(loginDTO);
        return ResponseEntity.ok(tokenPair);
    }

    // Refresh user token
    @PostMapping("/user/refresh-token")
    public ResponseEntity<?> refreshToken(@Valid @RequestBody RefreshTokenRequest refreshTokenRequest) {
        TokenPair tokenPair = userService.refreshToken(refreshTokenRequest);

        return ResponseEntity.ok(tokenPair);
    }

    // Signup business
    @PostMapping("/business/signup")
    public ResponseEntity<BusinessResponse> signupBusiness(@Valid @RequestBody BusinessSignupDTO signupDTO) {
        try {
            BusinessDTO newBusiness = BusinessMapper.mapBusinessDTO(businessService.createBusiness(signupDTO));
            return new ResponseEntity<>(new BusinessResponse(newBusiness, "Business created successfully."), HttpStatus.CREATED);
        } catch (IllegalArgumentException e) {
            return new ResponseEntity<>(new BusinessResponse(null, e.getMessage()), HttpStatus.BAD_REQUEST);
        } catch (Exception e) {
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    // Login business
    @PostMapping("/business/login")
    public ResponseEntity<?> loginBusiness(@Valid @RequestBody BusinessLoginDTO loginDTO) {
        TokenPair tokenPair = businessService.loginBusiness(loginDTO);
        return ResponseEntity.ok(tokenPair);
    }

    // Refresh business token
    @PostMapping("/business/refresh-token")
    public ResponseEntity<?> refreshBusinessToken(@Valid @RequestBody RefreshTokenRequest refreshTokenRequest) {
        TokenPair tokenPair = businessService.refreshToken(refreshTokenRequest);

        return ResponseEntity.ok(tokenPair);
    }
}
