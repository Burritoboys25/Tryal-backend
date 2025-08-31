package com.backend.tryal.business.service;

import com.backend.tryal.business.Business;
import com.backend.tryal.business.BusinessRepository;
import com.backend.tryal.business.dto.*;
import com.backend.tryal.business.mapper.BusinessMapper;
import com.backend.tryal.category.Category;
import com.backend.tryal.experience.Experience;
import com.backend.tryal.security.dto.RefreshTokenRequest;
import com.backend.tryal.security.dto.TokenPair;
import com.backend.tryal.security.service.JwtService;
import com.backend.tryal.user.UserRepository;
import jakarta.persistence.EntityNotFoundException;
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

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

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

    @Autowired
    UserRepository userRepository;

    public BusinessServiceImpl(BusinessRepository businessRepository) {
        this.businessRepository = businessRepository;
        this.passwordEncoder = new BCryptPasswordEncoder();
    }

    @Override
    public List<Experience> getAllBusinessExperiences(UUID businessId) {
        Business business = getBusinessById(businessId);

        if (business == null) {
          throw new EntityNotFoundException("Could not find business with id: " + businessId);
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
        if (businessRepository.existsByEmail(signupDTO.getEmail()) || userRepository.existsByEmail(signupDTO.getEmail())) {
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

            if (business.getLongitude() != null) {
                updatedBusiness.setLongitude(business.getLongitude());
            }

            if (business.getLatitude() != null) {
                updatedBusiness.setLatitude(business.getLongitude());
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

    @Override
    public List<BusinessFilteredResponseDTO> getFilteredBusinesses(BusinessFilteredRequestDTO filters) {
        Integer limit = filters.getLimit();

        // Normalize filters: treat empty lists as null
        List<Experience.SkillLevel> skillLevel = filters.getSkillLevel();
        if (skillLevel != null && skillLevel.isEmpty()) {
            skillLevel = null; // treat empty as null and return all;
        }

        List<UUID> categoryIds = filters.getCategoryIds();
        if (categoryIds != null && categoryIds.isEmpty()) {
            categoryIds = null; // treat empty as null and return all;;
        }

        List<Business> businesses = businessRepository.findFilteredBusinesses(
                categoryIds,
                filters.getGroupTypeIds(),
                skillLevel,
                filters.getDuration(),
                filters.getCreditsMin(),
                filters.getCreditsMax()
        );

        List<BusinessFilteredResponseDTO> response = new ArrayList<>();

        for (Business business : businesses) {
            List<Experience> filteredExperiences = new ArrayList<>();

            for (Experience experience : business.getExperiences()) {
                boolean matchesSkill = skillLevel == null || skillLevel.contains(experience.getSkillLevel());
                boolean matchesDuration = filters.getDuration() == null || experience.getDuration() <= filters.getDuration();

                boolean matchesCategory = false;
                if (categoryIds == null) {
                    matchesCategory = true; // no filter = allow all
                } else {
                    for (Category category : experience.getCategories()) {
                        if (categoryIds.contains(category.getCategoryId())) {
                            matchesCategory = true;
                            break;
                        }
                    }
                }


                if (matchesSkill && matchesDuration && matchesCategory) {
                    filteredExperiences.add(experience);
                }
            }
            // Only include businesses that still have at least one valid experience
            if (!filteredExperiences.isEmpty()) {
                BusinessFilteredResponseDTO dto = BusinessMapper.mapToBusinessFilteredResponseDTO(business, filteredExperiences);
                response.add(dto);
            }

        }

        if (limit != null && limit > 0 && response.size() > limit) {
            return response.subList(0, limit);
        }

        return response;
    }

    @Override
    public List<String> getBusinessCategories(UUID businessId) {
        List<Experience> experiences = getAllBusinessExperiences(businessId);
        if (experiences == null || experiences.isEmpty()) {
            return List.of(); // Return an empty list if no experiences are found
        }

        return experiences.stream()
                .flatMap(experience -> experience.getCategories().stream())
                .map(Category::getName)
                .distinct()
                .collect(Collectors.toList());
    }
    @Override
    public BusinessCreditRangeDTO getBusinessCreditRangeById(UUID businessId) {
        if (getBusinessById(businessId) != null) {
            return businessRepository.getBusinessCreditRangeById(businessId);
        }
        return null;
    }
}
