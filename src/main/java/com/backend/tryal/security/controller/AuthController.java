package com.backend.tryal.security.controller;

import com.backend.tryal.business.Business;
import com.backend.tryal.business.dto.BusinessDTO;
import com.backend.tryal.business.dto.BusinessLoginDTO;
import com.backend.tryal.business.dto.BusinessSignupDTO;
import com.backend.tryal.business.mapper.BusinessMapper;
import com.backend.tryal.business.response.BusinessResponse;
import com.backend.tryal.business.service.BusinessService;
import com.backend.tryal.security.dto.RefreshTokenRequest;
import com.backend.tryal.security.dto.TokenPair;
import com.backend.tryal.user.User;
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
    public UserDTO signupUser(@Valid @RequestBody UserSignupDTO signupDTO) {
      User user = userService.createUser(signupDTO);
      return UserMapper.mapUserDTO(user);
    }

    // Login User
    @PostMapping("/user/login")
    public TokenPair loginUser(@Valid @RequestBody UserLoginDTO loginDTO) {
         return userService.loginUser(loginDTO);
    }

    // Refresh user token
    @PostMapping("/user/refresh-token")
    public TokenPair refreshToken(@Valid @RequestBody RefreshTokenRequest refreshTokenRequest) {
        return userService.refreshToken(refreshTokenRequest);
    }

    // Signup business
    @PostMapping("/business/signup")
    public BusinessDTO signupBusiness(@Valid @RequestBody BusinessSignupDTO signupDTO) {
      Business business = businessService.createBusiness(signupDTO);
      return BusinessMapper.mapBusinessDTO(business);
    }

    // Login business
    @PostMapping("/business/login")
    public TokenPair loginBusiness(@Valid @RequestBody BusinessLoginDTO loginDTO) {
        return businessService.loginBusiness(loginDTO);
    }

    // Refresh business token
    @PostMapping("/business/refresh-token")
    public TokenPair refreshBusinessToken(@Valid @RequestBody RefreshTokenRequest refreshTokenRequest) {
        return businessService.refreshToken(refreshTokenRequest);
    }
}
