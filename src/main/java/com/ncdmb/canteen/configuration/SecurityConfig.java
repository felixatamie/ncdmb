package com.ncdmb.canteen.configuration;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import java.util.List;

@Configuration
public class SecurityConfig {

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        return http.csrf(AbstractHttpConfigurer::disable)
                .cors()
                .and()
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers("/auth/**").permitAll()
                        .requestMatchers("/api/v1/canteen-owner/**").hasRole("USER")
                        .requestMatchers("/api/v1/ncdmb/**").hasRole("ADMIN")
                        .requestMatchers("/api/v1/tx/create-user").hasRole("USER")//all-staff-user/canteen/
                        .requestMatchers("/api/v1/tx/all-staff-user/canteen/**").hasAnyRole("USER", "ADMIN")
                        .requestMatchers("/api/v1/tx/details").hasAnyRole("USER","ADMIN")
                        .requestMatchers("/api/v1/tx/non-staff-user/canteen/**").hasRole("USER")
                        .requestMatchers("/api/v1/tx/total-user/canteen/**").hasRole("USER")
                        .requestMatchers("/api/v1/tx/verify-user/**").hasRole("USER")
                        .requestMatchers("/api/v1/tx/all-staff-ncdmb/**").hasRole("ADMIN")
                        .requestMatchers("/api/v1/tx/total-ncdmb/canteen/**").hasRole("ADMIN")
                        .requestMatchers("/api/v1/tx/ncdmb-summary").hasRole("ADMIN")
                        .requestMatchers("/api/v1/tx/ncdmb-summary/canteen/**").hasRole("ADMIN")// needed
                        .requestMatchers("/api/v1/tx/all-customers/canteen/*").hasRole("USER")
                        .anyRequest().authenticated()
                )
                .sessionManagement(sm -> sm.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .addFilterBefore(new JwtAuthFilter(), UsernamePasswordAuthenticationFilter.class)
                .build();
    }
    @Bean
    public CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration configuration = new CorsConfiguration();
        configuration.setAllowedOriginPatterns(List.of("*")); // allow all origins
        configuration.setAllowedMethods(List.of("GET", "POST", "PUT", "DELETE", "OPTIONS"));
        configuration.setAllowedHeaders(List.of("*"));
        configuration.setAllowCredentials(true); // needed if JWT is in cookies or Authorization header

        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", configuration);
        return source;
    }

}
