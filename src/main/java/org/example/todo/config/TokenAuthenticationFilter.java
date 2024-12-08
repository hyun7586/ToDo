package org.example.todo.config;


import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.example.todo.config.jwt.TokenProvider;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

@Component
@RequiredArgsConstructor
@Slf4j
public class TokenAuthenticationFilter extends OncePerRequestFilter {

  private final TokenProvider tokenProvider;
  private final static String HEADER_AUTHORIZATION = "Authorization";
  private final static String TOKEN_PREFIX = "Bearer ";


  @Override
  protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response,
      FilterChain filterChain) throws ServletException, IOException {

    String authorizationHeader = request.getHeader(HEADER_AUTHORIZATION);
    String token = getAccessToken(authorizationHeader);

    if (tokenProvider.validToken(token)) {
      Authentication authentication = tokenProvider.getAuthentication(token);
      SecurityContextHolder.getContext().setAuthentication(authentication);
      log.debug("Authentication in SecurityContext: {}",
          SecurityContextHolder.getContext().getAuthentication());
    }

    if (SecurityContextHolder.getContext().getAuthentication() == null) {
      log.warn("SecurityContext does not contain authentication information");
    }
    filterChain.doFilter(request, response);
  }

  private String getAccessToken(String authorizationHeader) {
    if (authorizationHeader != null && authorizationHeader.startsWith(TOKEN_PREFIX)) {
      return authorizationHeader.substring(TOKEN_PREFIX.length());
    }

    return null;
  }
}
