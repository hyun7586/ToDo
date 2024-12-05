package org.example.todo.config;

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

@Configuration
@EnableWebSecurity
public class WebSecurityConfig {


  @Bean
  public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
    return http
        .authorizeHttpRequests(auth -> auth
            .requestMatchers("/login", "/signup").permitAll()
            .requestMatchers("/user/api/modify_for_admin").permitAll()
            .anyRequest().authenticated())
//        .formLogin(formlogin -> formlogin
//            // html에 쓰인 input field의 name 값과 usernameParameter 명이 맞아야 loadUserByUsername()
//            // method에 올바르게 argument가 전달됨
//            .usernameParameter("email")
//            .loginProcessingUrl("/login")
//            .defaultSuccessUrl("/main")
//            .loginPage("/login"))
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
