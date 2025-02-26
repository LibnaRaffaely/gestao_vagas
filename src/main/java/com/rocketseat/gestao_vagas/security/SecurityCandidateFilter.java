package com.rocketseat.gestao_vagas.security;

import java.io.IOException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import com.rocketseat.gestao_vagas.providers.JWTCandidateProvider;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@Component
@EnableMethodSecurity
public class SecurityCandidateFilter extends OncePerRequestFilter {
    @Autowired
    private JWTCandidateProvider jwtCandidateProvider;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {

        String header = request.getHeader("Authorization");
        if (header != null && header.startsWith("Bearer ")) {
            header = header.replace("Bearer ", "");
        }

        if (request.getRequestURI().startsWith("/candidate")) {
            if (header != null) {
                var token = this.jwtCandidateProvider.validateToken(header);

                if (token == null) {
                    response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
                    return;
                }

                request.setAttribute("candidate_id", token.getSubject());
                var roles = token.getClaim("roles").asList(Object.class); // extrai a lista de papeis/roles que aquele
                                                                          // token tem; ou seja que aquele usuário está
                                                                          // permitido

                // será uma lista de objetos GrantedAuthority
                var grants = roles.stream()
                        .map(
                                role -> new SimpleGrantedAuthority("ROLE_" + role.toString().toUpperCase())) // aqui
                                                                                                             // cada
                                                                                                             // role
                                                                                                             // será
                        // convertida
                        // em um objeto do tipo
                        // SimpleGrantedAuthority
                        .toList();

                UsernamePasswordAuthenticationToken auth = new UsernamePasswordAuthenticationToken(token.getSubject(),
                        null, grants);

                SecurityContextHolder.getContext().setAuthentication(auth);

                System.out.println("----------TOKEN-----------");
                System.out.println(token);
            }
        }

        filterChain.doFilter(request, response);
    }

}
