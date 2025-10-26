package com.backend.tryal.security;

import com.backend.tryal.security.filter.JwtAuthenticationFilter;
import com.backend.tryal.security.handler.JsonAccessDeniedHandler;
import com.backend.tryal.security.handler.JsonAuthenticationEntryPoint;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.ProviderManager;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

  @Value("${spring.application.environment}")
  private String envVariable;

  @Autowired
  @Qualifier("customUserDetailsService") // Inject CustomUserDetailsService
  private UserDetailsService userDetailsService;

  @Autowired
  @Qualifier("customBusinessDetailsService") // Inject CustomBusinessDetailsService
  private UserDetailsService businessDetailsService;

  @Autowired
  private JwtAuthenticationFilter jwtAuthenticationFilter;

  @Bean
  public SecurityFilterChain securityFilterChain(HttpSecurity http,
      JsonAuthenticationEntryPoint authEntryPoint,
      JsonAccessDeniedHandler accessDeniedHandler,
      JwtAuthenticationFilter jwtAuthenticationFilter) throws Exception {

    if (envVariable.equals("dev")) {
      return http.csrf(AbstractHttpConfigurer::disable)
          .authorizeHttpRequests(request -> request
              .anyRequest().permitAll()
          )
          .build();
    }

    return http
        .csrf(AbstractHttpConfigurer::disable)
        .sessionManagement(sm -> sm.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
        .exceptionHandling(ex -> ex
            .authenticationEntryPoint(authEntryPoint)
            .accessDeniedHandler(accessDeniedHandler)
        )
        // IMPORTANT: disable these so they don't register their own entry points
        .httpBasic(AbstractHttpConfigurer::disable)
        .formLogin(AbstractHttpConfigurer::disable)
        .logout(AbstractHttpConfigurer::disable)

        .authorizeHttpRequests(auth -> auth
            .requestMatchers("/api/auth/**").permitAll()
            .anyRequest().authenticated()
        )
        .addFilterBefore(jwtAuthenticationFilter, UsernamePasswordAuthenticationFilter.class)
        .build();
  }

  @Bean
  public AuthenticationProvider userAuthenticationProvider() {
    DaoAuthenticationProvider provider = new DaoAuthenticationProvider();
    provider.setPasswordEncoder(new BCryptPasswordEncoder(12));
    provider.setUserDetailsService(userDetailsService);
    return provider;
  }

  @Bean
  public AuthenticationProvider businessAuthenticationProvider() {
    DaoAuthenticationProvider provider = new DaoAuthenticationProvider();
    provider.setPasswordEncoder(new BCryptPasswordEncoder(12));
    provider.setUserDetailsService(businessDetailsService);
    return provider;
  }

  @Bean
  public AuthenticationManager authenticationManager(
      AuthenticationProvider userAuthenticationProvider,
      AuthenticationProvider businessAuthenticationProvider) throws Exception {
    return new ProviderManager(userAuthenticationProvider, businessAuthenticationProvider);
  }
}
