package com.barbersync.barbersync_api.infra.security;

import com.barbersync.barbersync_api.Usuarios.repository.AdminRepository;
import com.barbersync.barbersync_api.Usuarios.repository.BarbeiroRepository;
import com.barbersync.barbersync_api.Usuarios.repository.ClienteRepository;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Component
public class SecurityFilter extends OncePerRequestFilter {

    @Autowired
    private ClienteRepository clienteRepository;

    @Autowired
    private AdminRepository adminRepository;

    @Autowired
    private BarbeiroRepository barbeiroRepository;

    @Autowired
    private TokenService tokenService;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        var token = recuperarToken(request);

        if (token != null){
            var subject = tokenService.coletarSubject(token);

            var admin = adminRepository.findByUsuarioEmail(subject);
            if (admin != null ) {
                var authentication = new UsernamePasswordAuthenticationToken(admin, null, admin.getAuthorities());

                SecurityContextHolder.getContext().setAuthentication(authentication);
            }

            var cliente = clienteRepository.findByUsuarioEmail(subject);
            if (cliente != null){
                var authentication = new UsernamePasswordAuthenticationToken(admin, null, cliente.getAuthorities());

                SecurityContextHolder.getContext().setAuthentication(authentication);
            }

            var barbeiro = barbeiroRepository.findByUsuarioEmail(subject);
            if (barbeiro != null){
                var authentication = new UsernamePasswordAuthenticationToken(admin, null, barbeiro.getAuthorities());

                SecurityContextHolder.getContext().setAuthentication(authentication);
            }
        }

        filterChain.doFilter(request, response);
    }

    public String recuperarToken(HttpServletRequest request){
        var authenticationHeader = request.getHeader("Authorization");

        if(authenticationHeader != null){
            return authenticationHeader.replace("Bearer ", "");
        }

        return null;
    }
}
