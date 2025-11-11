package com.backend.tryal.security.service;

import com.backend.tryal.security.model.UserPrincipal;
import com.backend.tryal.user.User;
import com.backend.tryal.user.UserRepository;
import java.util.UUID;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
@Lazy
public class CustomUserDetailsService implements UserDetailsService {

    @Autowired
    private UserRepository userRepository;

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new UsernameNotFoundException("User not found with email: " + email));

        return new UserPrincipal(user);
    }

    public UserDetails loadUserById(UUID id) {
      User user = userRepository.findById(id)
          .orElseThrow(() -> new UsernameNotFoundException("User not found with id: " + id));

      return new UserPrincipal(user);
    }
}
