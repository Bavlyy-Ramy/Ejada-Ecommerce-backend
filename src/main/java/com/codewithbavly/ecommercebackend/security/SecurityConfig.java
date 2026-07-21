package com.codewithbavly.ecommercebackend.security;

import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@RequiredArgsConstructor
public class SecurityConfig {

    private final CustomUserDetailsService userDetailsService;
    private final PasswordEncoder passwordEncoder;
    private final JwtAuthenticationFilter jwtAuthenticationFilter;

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http)
            throws Exception {

        http
                .csrf(AbstractHttpConfigurer::disable)

                .authorizeHttpRequests(auth -> auth

                        // Public endpoints
                        .requestMatchers(
                                "/api/users/register",
                                "/api/users/login"
                        ).permitAll()

                        // Anyone can view products
                        .requestMatchers(HttpMethod.GET,
                                "/api/products",
                                "/api/products/*"
                        ).permitAll()

                        .requestMatchers(HttpMethod.POST, "/api/orders")
                        .authenticated()

                            .requestMatchers(HttpMethod.GET,
                                "/api/orders/my")
                        .authenticated()
                        // Only ADMIN can add products
                        .requestMatchers(HttpMethod.POST,
                                "/api/products/add"
                        ).hasAuthority("ADMIN")

                        // Only ADMIN can update products
                        .requestMatchers(HttpMethod.PUT,
                                "/api/products/update/*",
                                "/api/products/update/**"
                        ).hasAuthority("ADMIN")

                        // Only ADMIN can delete products
                        .requestMatchers(HttpMethod.DELETE,
                                "/api/products/delete/*",
                                "/api/products/delete/**"

                        ).hasAuthority("ADMIN")

                        .requestMatchers(HttpMethod.GET, "/api/orders")
                        .hasAuthority("ADMIN")

                        .requestMatchers(HttpMethod.PUT,
                                "/api/orders/*/status")
                        .hasAuthority("ADMIN")

                        // Everything else requires authentication
                        .anyRequest().authenticated()
                )

                .authenticationProvider(authenticationProvider())

                .addFilterBefore(
                        jwtAuthenticationFilter,
                        UsernamePasswordAuthenticationFilter.class
                );
        return http.build();
    }

    @Bean
    public AuthenticationProvider authenticationProvider() {

        DaoAuthenticationProvider provider =
                new DaoAuthenticationProvider(userDetailsService);

        provider.setPasswordEncoder(passwordEncoder);

        return provider;
    }

    @Bean
    public AuthenticationManager authenticationManager(
            AuthenticationConfiguration configuration)
            throws Exception {

        return configuration.getAuthenticationManager();
    }

}