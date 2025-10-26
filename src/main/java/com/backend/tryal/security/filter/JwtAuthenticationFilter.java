package com.backend.tryal.security.filter;

import com.backend.tryal.security.repository.TokenRepository;
import com.backend.tryal.security.service.JwtService;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Qualifier;
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
  private final TokenRepository tokenRepository;
  private final UserDetailsService userDetailsService;
  private final UserDetailsService businessDetailsService;

  public JwtAuthenticationFilter(
      JwtService jwtService,
      TokenRepository tokenRepository,
      @Qualifier("customUserDetailsService") UserDetailsService userDetailsService,
      @Qualifier("customBusinessDetailsService") UserDetailsService businessDetailsService
  ) {
    this.jwtService = jwtService;
    this.tokenRepository = tokenRepository;
    this.userDetailsService = userDetailsService;
    this.businessDetailsService = businessDetailsService;
  }

  @Override
  protected void doFilterInternal(HttpServletRequest request,
      HttpServletResponse response,
      FilterChain filterChain) throws ServletException, IOException {

    final String header = request.getHeader("Authorization");
    if (header == null || !header.startsWith("Bearer ")) {
      // No token → continue; later filters will cause 401 via entry point
      filterChain.doFilter(request, response);
      return;
    }

    final String jwt = header.substring(7);

    try {
      if (!jwtService.isValidToken(jwt)) {
        throw new org.springframework.security.authentication.BadCredentialsException(
            "Invalid token");
      }

      final String username = jwtService.extractUsernameFromToken(jwt);

      String stored = tokenRepository.getAccessToken(username);
      if (stored == null || !stored.equals(jwt)) {
        throw new org.springframework.security.authentication.BadCredentialsException(
            "Invalid token");
      }

      if (SecurityContextHolder.getContext().getAuthentication() == null) {
        UserDetails ud = jwtService.isBusinessUser(jwt)
            ? businessDetailsService.loadUserByUsername(username)
            : userDetailsService.loadUserByUsername(username);

        if (!jwtService.validateTokenForUsers(jwt, ud)) {
          throw new org.springframework.security.authentication.BadCredentialsException(
              "Invalid token");
        }

        UsernamePasswordAuthenticationToken auth =
            new UsernamePasswordAuthenticationToken(ud, null, ud.getAuthorities());
        auth.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
        SecurityContextHolder.getContext().setAuthentication(auth);
      }

    } catch (org.springframework.security.core.AuthenticationException ex) {
      // Let ExceptionTranslationFilter trigger your JsonAuthenticationEntryPoint
      throw ex;
    }

    filterChain.doFilter(request, response);
  }
}
