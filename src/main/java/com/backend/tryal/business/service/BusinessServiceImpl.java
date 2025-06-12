package com.backend.tryal.business.service;

import com.backend.tryal.business.Business;
import com.backend.tryal.business.BusinessRepository;
import com.backend.tryal.business.dto.BusinessLoginDTO;
import com.backend.tryal.business.dto.BusinessSignupDTO;
import com.backend.tryal.business.mapper.BusinessMapper;
import com.backend.tryal.experience.Experience;
import com.backend.tryal.security.dto.RefreshTokenRequest;
import com.backend.tryal.security.dto.TokenPair;
import com.backend.tryal.security.service.JwtService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
public class BusinessServiceImpl implements BusinessService {
    private final BusinessRepository businessRepository;

    private final PasswordEncoder passwordEncoder;

    @Autowired
    private JwtService jwtService;

    @Autowired
    AuthenticationManager authenticationManager;

    @Autowired
    @Qualifier("customBusinessDetailsService")
    UserDetailsService userDetailsService;

    public BusinessServiceImpl(BusinessRepository businessRepository) {
        this.businessRepository = businessRepository;
        this.passwordEncoder = new BCryptPasswordEncoder();
    }

    @Override
    public List<Experience> getAllBusinessExperiences(UUID businessId) {
        Business business = getBusinessById(businessId);

        if (business == null) {
            return null;
        }

        return business.getExperiences();
    }

    @Override
    public List<Business> getAllBusinesses() {
        return businessRepository.findAll();
    }

    @Override
    public Business getBusinessById(UUID businessId) {
        return businessRepository.findById(businessId).orElse(null);
    }

    @Override
    public Business createBusiness(BusinessSignupDTO signupDTO) throws IllegalArgumentException {
        if (businessRepository.existsByEmail(signupDTO.getEmail())) {
            throw new IllegalArgumentException("Email is already taken.");
        }

        Business business = BusinessMapper.mapSignupDTOToBusiness(signupDTO);
        String encodedPassword = this.passwordEncoder.encode(signupDTO.getPassword());
        business.setPasswordHash(encodedPassword);

        return businessRepository.save(business);
    }

    @Override
    public TokenPair loginBusiness(BusinessLoginDTO loginDTO) {
        // Authenticate business
        Authentication authentication = authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(loginDTO.getEmail(), loginDTO.getPassword()));

        // Set authentication in security context
        SecurityContextHolder.getContext().setAuthentication(authentication);

        // Generate Token Pair
        return jwtService.generateTokenPair(authentication);
    }

    @Override
    public TokenPair refreshToken(@Valid RefreshTokenRequest refreshTokenRequest) {
        String refreshToken = refreshTokenRequest.getRefreshToken();

        // check if still valid refresh token
        if (!jwtService.isRefreshToken(refreshToken)) {
            throw new IllegalArgumentException("Invalid refresh token");
        }

        String business = jwtService.extractUsernameFromToken(refreshToken);
        boolean isBusiness = jwtService.isBusinessUser(refreshToken);

        if (!isBusiness) {
            throw new IllegalArgumentException("Invalid account type refresh token");
        }

        UserDetails userDetails = userDetailsService.loadUserByUsername(business);

        if (userDetails == null) {
            throw new IllegalArgumentException("Business not found");
        }

        UsernamePasswordAuthenticationToken authenticationToken =
                new UsernamePasswordAuthenticationToken(
                        userDetails,
                        null,
                        userDetails.getAuthorities()
                );

        String accessToken = jwtService.generateAccessToken(authenticationToken);
        return new TokenPair(accessToken, refreshToken);
    }

    @Override
    public Business updateBusinessById(UUID businessId, Business business) {
        if (getBusinessById(businessId) != null) {
            Business updatedBusiness = getBusinessById(businessId);

            if (business.getStripeAccountId() != null) {
                updatedBusiness.setStripeAccountId(business.getStripeAccountId());
            }

            if (business.getName() != null) {
                updatedBusiness.setName(business.getName());
            }

            if (business.getEmail() != null) {
                updatedBusiness.setEmail(business.getEmail());
            }

            if (business.getPasswordHash() != null) {
                updatedBusiness.setPasswordHash(business.getPasswordHash());
            }

            if (business.getWebsite() != null) {
                updatedBusiness.setWebsite(business.getWebsite());
            }

            if (business.getAddress() != null) {
                updatedBusiness.setAddress(business.getAddress());
            }

            if (business.getPhoneNumber() != null) {
                updatedBusiness.setPhoneNumber(business.getPhoneNumber());
            }

            businessRepository.save(updatedBusiness);
            return updatedBusiness;
        }
        return null;
    }

    @Override
    public boolean deleteBusinessById(UUID businessId) {
        if (getBusinessById(businessId) != null) {
            businessRepository.deleteById(businessId);
            return true;
        }

        return false;
    }
}
