package com.backend.tryal.security.filter;

import com.backend.tryal.security.repository.TokenRepository;
import com.backend.tryal.security.service.CustomBusinessDetailsService;
import com.backend.tryal.security.service.CustomUserDetailsService;
import com.backend.tryal.security.service.JwtService;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.UUID;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.HttpHeaders;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

@Slf4j
@Component
public class JwtAuthenticationFilter extends OncePerRequestFilter {

  private final JwtService jwtService;
//  private final TokenRepository tokenRepository;
//  private final UserDetailsService userDetailsService;
//  private final UserDetailsService businessDetailsService;
  private final CustomUserDetailsService customUserDetailsService;
  private final CustomBusinessDetailsService customBusinessDetailsService;

  public JwtAuthenticationFilter(
      JwtService jwtService,
//      TokenRepository tokenRepository,
//      @Qualifier("customUserDetailsService") UserDetailsService userDetailsService,
//      @Qualifier("customBusinessDetailsService") UserDetailsService businessDetailsService,
      CustomUserDetailsService customUserDetailsService,
      CustomBusinessDetailsService customBusinessDetailsService
  ) {
    this.jwtService = jwtService;
//    this.tokenRepository = tokenRepository;
//    this.userDetailsService = userDetailsService;
//    this.businessDetailsService = businessDetailsService;
    this.customUserDetailsService = customUserDetailsService;
    this.customBusinessDetailsService = customBusinessDetailsService;
  }

  @Override
  protected void doFilterInternal(HttpServletRequest request,
      HttpServletResponse response,
      FilterChain filterChain) throws ServletException, IOException {

    try {
      final String header = request.getHeader(HttpHeaders.AUTHORIZATION);

      if (header != null && header.startsWith("Bearer ")
          && SecurityContextHolder.getContext().getAuthentication() == null) {

        final String jwt = header.substring(7);

        if (jwtService.isValidToken(jwt)) {
          final UUID id = UUID.fromString(jwtService.extractSubjectFromToken(jwt));

          UserDetails userDetails = jwtService.isBusinessUser(jwt)
              ? customBusinessDetailsService.loadUserById(id)
              : customUserDetailsService.loadUserById(id);

          UsernamePasswordAuthenticationToken auth =
              new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());
          auth.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
          SecurityContextHolder.getContext().setAuthentication(auth);
        }
      }
      filterChain.doFilter(request, response);

    } catch (org.springframework.security.core.AuthenticationException ex) {
      // Let ExceptionTranslationFilter trigger your JsonAuthenticationEntryPoint
//      throw ex;
    }

    //filterChain.doFilter(request, response);
  }
}
