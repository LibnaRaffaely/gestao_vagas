package com.rocketseat.gestao_vagas.security;

import java.io.IOException;
import java.util.Collections;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import com.rocketseat.gestao_vagas.providers.JWTProvider;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@Component
@EnableMethodSecurity
// OncePerRequestFilter do próprio spring que tem o metodo que precisamos
public class SecurityFilter extends OncePerRequestFilter {

    @Autowired
    private JWTProvider jwtProvider;

    /*
     * Recebe tudo o que vem da requisição, manda coisas para outras camadas pelo
     * response, e o filterChain verifica se corresponde às condições, se sim
     * permite o fluxo do usuário
     */
    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {

        String header = request.getHeader("Authorization");
        if (header != null && header.startsWith("Bearer ")) {
            header = header.replace("Bearer ", "");
        }

        // teste para ver se ta ok
        System.out.println(header);

        if (request.getRequestURI().startsWith("/company") || request.getRequestURI().startsWith("/job")) {
            if (header != null) {
                var token = this.jwtProvider.validateToken(header);
                if (token == null) {
                    response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
                    return;
                }

                // estamos armazenando na requisição o subjectToken como um atributo com o nome
                // "company_id"
                request.setAttribute("company_id", token.getSubject());

                var roles = token.getClaim("roles").asList(Object.class);
                var grants = roles.stream()
                        .map(
                                role -> new SimpleGrantedAuthority("ROLE_" + role.toString().toUpperCase()))
                        .toList();

                // vamos precisar o usuário para o Spring
                UsernamePasswordAuthenticationToken auth = new UsernamePasswordAuthenticationToken(token.getSubject(),
                        null,
                        grants);

                SecurityContextHolder.getContext().setAuthentication(auth);
                System.out.println("TESTEZIM");
            }
        }

        filterChain.doFilter(request, response);

    }

}
