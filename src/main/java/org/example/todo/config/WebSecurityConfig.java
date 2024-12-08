package org.example.todo.config;

import lombok.RequiredArgsConstructor;
import org.antlr.v4.runtime.Token;
import org.example.todo.service.user.UserDetailService;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.ProviderManager;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class WebSecurityConfig {

  private final TokenAuthenticationFilter tokenAuthenticationFilter;

  @Bean
  public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
    return http
        .authorizeHttpRequests(auth -> auth
            .requestMatchers(
                "/login",
                "/signup",
                "/user/api/modify_for_admin",
                "/error",
                "/swagger-ui/**",
                "/v3/api-docs/**",
                "/swagger-resources/**",
                "/webjars/**").permitAll()
            .requestMatchers("/mainPage").hasAuthority("ROLE_USER")
            .anyRequest().authenticated())
        .addFilterBefore(tokenAuthenticationFilter, UsernamePasswordAuthenticationFilter.class)
        .logout(logout -> logout
            .logoutSuccessUrl("/login")
            .invalidateHttpSession(true))
        .csrf(AbstractHttpConfigurer::disable)
        .build();
  }

  @Bean
  public AuthenticationManager authenticationManager(HttpSecurity http,
      // UserDetailsService(interface)가 아니라 UserDetailService!!
      // 위 interface를 구현한, 내가 커스텀한 UserDetailService 객체를 전달해야 함.
      BCryptPasswordEncoder bCryptPasswordEncoder, UserDetailService userService)
      throws Exception {
    DaoAuthenticationProvider authProvider = new DaoAuthenticationProvider();
    authProvider.setUserDetailsService(userService);
    authProvider.setPasswordEncoder(bCryptPasswordEncoder);

    return new ProviderManager(authProvider);
  }

  @Bean
  public BCryptPasswordEncoder bCryptPasswordEncoder() {
    return new BCryptPasswordEncoder();
  }
}
