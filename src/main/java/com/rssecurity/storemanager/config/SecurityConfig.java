package com.rssecurity.storemanager.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.web.SecurityFilterChain;

import jakarta.servlet.http.HttpServletResponse;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    // private final UserDetailsService userDetailsService;
    // private final PasswordEncoder passwordEncoder;

    // public SecurityConfig(UserDetailsService userDetailsService, PasswordEncoder passwordEncoder) {
    //     this.userDetailsService = userDetailsService;
    //     this.passwordEncoder = passwordEncoder;
    // }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
            .csrf(csrf -> csrf.disable())
            .authorizeHttpRequests(auth -> auth
                .requestMatchers("/auth/**", "/css/**", "/js/**", "/images/**").permitAll()
                .requestMatchers("/importar/**", "/api/importar/**").hasRole("ADMIN")
                
                .requestMatchers(HttpMethod.GET, "/produtos/**", "/api/produto/**").hasAnyRole("ADMIN", "USER")
                .requestMatchers(HttpMethod.POST, "/api/produto/**").hasRole("ADMIN")
                .requestMatchers(HttpMethod.PUT, "/api/produto/**").hasRole("ADMIN")
                .requestMatchers(HttpMethod.DELETE, "/api/produto/**").hasRole("ADMIN")
                
                .requestMatchers(HttpMethod.GET, "/compras/**", "/api/compra/**").hasAnyRole("ADMIN", "USER")
                .requestMatchers(HttpMethod.POST, "/api/compra/**").hasRole("ADMIN")
                .requestMatchers(HttpMethod.PUT, "/api/compra/**").hasRole("ADMIN")
                .requestMatchers(HttpMethod.DELETE, "/api/compra/**").hasRole("ADMIN")

                .requestMatchers(HttpMethod.GET, "/fornecedores/**", "/api/fornecedor/**").hasAnyRole("ADMIN", "USER")
                .requestMatchers(HttpMethod.POST, "/api/fornecedor/**").hasRole("ADMIN")
                .requestMatchers(HttpMethod.PUT, "/api/fornecedor/**").hasRole("ADMIN")
                .requestMatchers(HttpMethod.DELETE, "/api/fornecedor/**").hasRole("ADMIN")

                .requestMatchers("/relatorios/**", "/api/relatorio/**").hasRole("ADMIN")

                .requestMatchers("/usuarios/**", "/api/usuario/**").hasRole("ADMIN")

                .anyRequest().authenticated()
            )
            .formLogin(form -> form
                .loginPage("/auth/login")
                .defaultSuccessUrl("/home", true)
                .permitAll()
            )
            .logout(logout -> logout
                .logoutUrl("/auth/logout")
                .logoutSuccessUrl("/auth/login?logout")
                .permitAll()
            )
            .httpBasic(Customizer.withDefaults())
            .exceptionHandling(ex -> ex
                .authenticationEntryPoint((request, response, authException) -> {
                        String hxRequest = request.getHeader("HX-Request");

                        if ("true".equals(hxRequest)) {
                            // HTMX request: trigger client-side redirect on session expiration
                            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
                            response.setHeader("HX-Redirect", "/auth/login");
                        } else if ("XMLHttpRequest".equals(request.getHeader("X-Requested-With")) ||
                                "application/json".equals(request.getHeader("Accept")) ||
                                request.getRequestURI().startsWith("/api")) {
                            // API or generic AJAX request: respond with HTTP 401 Unauthorized
                            response.sendError(HttpServletResponse.SC_UNAUTHORIZED);
                        } else {
                            // Standard browser request: perform redirect to login page
                            response.sendRedirect("/auth/login");
                        }
                    })
            );

        return http.build();
    }

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration config) throws Exception {
        return config.getAuthenticationManager();
    }
}

