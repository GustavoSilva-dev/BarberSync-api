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

                .authorizeHttpRequests(req ->
                    req.requestMatchers(HttpMethod.POST, "/auth/**").permitAll()
                    .requestMatchers(HttpMethod.POST, "/clientes").permitAll()
                    .requestMatchers(HttpMethod.POST, "/barbeiros").permitAll()
                    .requestMatchers(HttpMethod.POST, "/admins").hasAuthority("ADMIN")
                    .requestMatchers("/v3/api-docs/**", "/swagger-ui/**", "/swagger-ui.html").permitAll()
                    .requestMatchers(HttpMethod.PUT, "/clientes").hasAnyAuthority("CLIENTE", "ADMIN")
                    .requestMatchers(HttpMethod.PUT, "/barbeiros").hasAnyAuthority("BARBEIRO", "ADMIN")
                    .requestMatchers(HttpMethod.PUT, "/admins").hasAuthority("ADMIN")
                    .requestMatchers(HttpMethod.GET, "/admins").hasAuthority("ADMIN")
                    .requestMatchers(HttpMethod.GET, "/clientes").hasAnyAuthority("CLIENTE", "ADMIN")
                    .requestMatchers(HttpMethod.GET, "/barbeiros").hasAnyAuthority("BARBEIRO", "ADMIN")
                    .requestMatchers(HttpMethod.DELETE, "/barbeiros/**").hasAnyAuthority("BARBEIRO", "ADMIN")
                    .requestMatchers(HttpMethod.DELETE, "/clientes/**").hasAnyAuthority("CLIENTE", "ADMIN")
                    .requestMatchers(HttpMethod.DELETE, "/admins/**").hasAuthority("ADMIN")
                    .anyRequest().permitAll())
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
