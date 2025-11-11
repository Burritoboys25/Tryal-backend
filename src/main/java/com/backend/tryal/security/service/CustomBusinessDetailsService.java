package com.backend.tryal.security.service;

import com.backend.tryal.business.Business;
import com.backend.tryal.business.BusinessRepository;
import com.backend.tryal.security.model.BusinessPrincipal;
import com.backend.tryal.security.model.UserPrincipal;
import com.backend.tryal.user.User;
import java.util.UUID;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
@Lazy
public class CustomBusinessDetailsService implements UserDetailsService {
    @Autowired
    private BusinessRepository businessRepository;

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        Business business = businessRepository.findByEmail(email)
                .orElseThrow(() -> new UsernameNotFoundException("Business not found with email: " + email));

        return new BusinessPrincipal(business);
    }

  public UserDetails loadUserById(UUID id) {
    Business business = businessRepository.findById(id)
        .orElseThrow(() -> new UsernameNotFoundException("Business not found with id: " + id));

    return new BusinessPrincipal(business);
  }
}
