package com.barbersync.barbersync_api.infra.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.ProviderManager;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableWebSecurity
public class SecurityConfigurations {

    @Autowired
    private AuthenticationService authenticationService;

    @Autowired
    private SecurityFilter securityFilter;

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        return http
                .csrf(csrf -> csrf.disable())
                .sessionManagement(sm -> sm.sessionCreationPolicy(SessionCreationPolicy.STATELESS))

                .authorizeHttpRequests(req -> {
                    req.requestMatchers(HttpMethod.POST, "/auth/**").permitAll();
                    req.requestMatchers(HttpMethod.POST, "/clientes").permitAll();
                    req.requestMatchers(HttpMethod.POST, "/barbeiros").permitAll();
                    req.requestMatchers(HttpMethod.POST, "/admins").hasAnyRole("ADMIN");
                    req.requestMatchers("/v3/api-docs/**", "/swagger-ui/**", "/swagger-ui.html").permitAll();
                    req.requestMatchers(HttpMethod.PUT, "/clientes").hasAnyRole("CLIENTE", "ADMIN");
                    req.requestMatchers(HttpMethod.PUT, "/barbeiros").hasAnyRole("BARBEIRO", "ADMIN");
                    req.requestMatchers(HttpMethod.PUT, "/admins").hasAnyRole("ADMIN");
                    req.requestMatchers(HttpMethod.GET, "/admins").hasAnyRole("ADMIN");
                    req.requestMatchers(HttpMethod.GET, "/clientes").hasAnyRole("CLIENTE", "ADMIN");
                    req.requestMatchers(HttpMethod.GET, "/barbeiros").hasAnyRole("BARBEIRO", "ADMIN");
                    req.requestMatchers(HttpMethod.DELETE, "/barbeiros/**").hasAnyRole("BARBEIRO", "ADMIN");
                    req.requestMatchers(HttpMethod.DELETE, "/clientes/**").hasAnyRole("CLIENTE", "ADMIN");
                    req.requestMatchers(HttpMethod.DELETE, "/admins/**").hasAnyRole("BARBEIRO", "ADMIN");
                    req.anyRequest().permitAll();
                })
                .addFilterBefore(securityFilter, UsernamePasswordAuthenticationFilter.class)
                .build();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    @Primary
    public AuthenticationManager authenticationManager(PasswordEncoder encoder) {
        var provider = new DaoAuthenticationProvider(authenticationService);
        provider.setPasswordEncoder(encoder);
        return new ProviderManager(provider);
    }

}
