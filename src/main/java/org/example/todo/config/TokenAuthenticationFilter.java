package org.example.todo.config;


import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.MalformedJwtException;
import io.jsonwebtoken.UnsupportedJwtException;
import io.jsonwebtoken.security.SignatureException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.example.todo.config.jwt.TokenProvider;
import org.example.todo.repository.RefreshTokenRepository;
import org.example.todo.service.token.TokenService;
import org.springframework.http.HttpStatus;
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
  protected boolean shouldNotFilter(HttpServletRequest request) throws ServletException {
    String requestUri = request.getRequestURI();
    log.debug("request.getRequestURI: {}", requestUri);
    log.debug("equals? {}", requestUri.equals("/login"));
    return requestUri.startsWith("/swagger-ui")
        || requestUri.startsWith("/swagger-resources")
        || requestUri.startsWith("/v3/api-docs")
        || requestUri.equals("/login")
        || requestUri.equals("/signup")
        || requestUri.equals("/favicon.ico");
  }

  @Override
  protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response,
      FilterChain filterChain) throws ServletException, IOException {

    String authorizationHeader = request.getHeader(HEADER_AUTHORIZATION);

    // Authorization 헤더가 없는 경우 바로 필터 체인 진행
    if (authorizationHeader == null || !authorizationHeader.startsWith(TOKEN_PREFIX)) {
      log.debug("Authorization header is empty or AuthorizationHeader doesn't start with TOKEN_PREFIX");
      filterChain.doFilter(request, response);
      return;
    }

    String accessToken = getAccessToken(authorizationHeader);

    try {
      if (tokenProvider.validToken(accessToken)) {

        Authentication authentication = tokenProvider.getAuthentication(accessToken);
        SecurityContextHolder.getContext().setAuthentication(authentication);
        log.debug("Authentication in SecurityContext: {}",
            SecurityContextHolder.getContext().getAuthentication());
      }
    } catch (ExpiredJwtException e) {
      // server filter 단에서는 accessToken의 만료여부만 client 측에 전달함
      // client 측에서는 accessToken 재발급 api에 요청을 해야 함: TokenApiController.java에 구현
      response.setStatus(HttpStatus.UNAUTHORIZED.value());
      response.setContentType("application/json");
      response.setCharacterEncoding("UTF-8");
      response.getWriter().write("AccessToken is expired: AccessToken 재발급이 필요합니다.");
      return;
    } catch (Exception e) {
      // ExpiredJwtException 외 다른 Exception들을 받아서 처리하는 로직 필요
      response.setStatus(HttpStatus.FORBIDDEN.value());
      response.setContentType("application/json");
      response.setCharacterEncoding("UTF-8");
      response.getWriter().write("Unexpected Exception: " + e.getMessage());
      return;
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
