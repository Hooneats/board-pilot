package com.example.pilot2.config;

import com.example.pilot2.Service.UserService;
import com.example.pilot2.dto.constant.Authority;
import com.example.pilot2.dto.security.UserPrincipal;
import org.springframework.boot.autoconfigure.security.servlet.PathRequest;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

import static org.springframework.security.config.Customizer.withDefaults;

@Configuration
public class SecurityConfig {

    @Bean
    public SecurityFilterChain securityFilterChain(
            HttpSecurity http
    ) throws Exception {
        return http
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers(
                                PathRequest.toStaticResources().atCommonLocations()
                        ).permitAll() // 정적 리소스 열어준다.
                        .mvcMatchers( // mvcMatchers 는 기존의 antMatchers 보다 스프링의 여러가지 패턴 매칭 기능을 더 제공해준다. ex) /log -> /log and /log.html
                                HttpMethod.GET,
                                "/", // 루트 페이지
                                "/boards/**"
                        ).permitAll()
                        .mvcMatchers(
                                "admin/**"
                        )
                        .hasAuthority(Authority.ADMIN.getRole())
                        .anyRequest().authenticated()

                )
                .formLogin(withDefaults())
                .logout(logout -> logout.logoutSuccessUrl("/"))
                .build();
    }

    @Bean
    public UserDetailsService userDetailsService(UserService userService) {
        return username -> userService
                .findUser(username)
                .map(UserPrincipal::from)
                .orElseThrow(() -> new UsernameNotFoundException("유저를 찾을 수 없습니다 - username: " + username));
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}
