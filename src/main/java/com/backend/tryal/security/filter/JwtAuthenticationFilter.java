package com.backend.tryal.security.filter;

import com.backend.tryal.security.service.JwtService;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

@Component
public class JwtAuthenticationFilter extends OncePerRequestFilter {

  private final JwtService jwtService;
  private final UserDetailsService userDetailsService;
  private final UserDetailsService businessDetailsService;

  public JwtAuthenticationFilter(
      JwtService jwtService,
      @Qualifier("customUserDetailsService") UserDetailsService userDetailsService,
      @Qualifier("customBusinessDetailsService") UserDetailsService businessDetailsService
  ) {
    this.jwtService = jwtService;
    this.userDetailsService = userDetailsService;
    this.businessDetailsService = businessDetailsService;
  }

  @Override
  protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response,
      FilterChain filterChain) throws ServletException, IOException {
    // Intercept the request
    final String authHeader = request.getHeader("Authorization");
    final String jwt;
    final String username;

    if (authHeader == null || !authHeader.startsWith("Bearer ")) {
      filterChain.doFilter(request, response);
      return;
    }

    jwt = getJwtFormRequest(request);

    // check if token is valid
    if (!jwtService.isValidToken(jwt)) {
      filterChain.doFilter(request, response);
      return;
    }

    username = jwtService.extractUsernameFromToken(jwt);

    if (username != null && SecurityContextHolder.getContext().getAuthentication() == null) {
      UserDetails userDetails;

      // Decide which service to use based on claim in the token
      if (jwtService.isBusinessUser(jwt)) {
        userDetails = businessDetailsService.loadUserByUsername(username);
      } else {
        userDetails = userDetailsService.loadUserByUsername(username);
      }

      if (jwtService.validateTokenForUsers(jwt, userDetails)) {
        UsernamePasswordAuthenticationToken authToken = new UsernamePasswordAuthenticationToken(
            userDetails, null, userDetails.getAuthorities());

        authToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));

        SecurityContextHolder.getContext().setAuthentication(authToken);
      }

      filterChain.doFilter(request, response);
    }

  }

  private String getJwtFormRequest(HttpServletRequest request) {
    final String authHeader = request.getHeader("Authorization");
    // Bearer <token>
    return authHeader.substring(7);
  }
}
