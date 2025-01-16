package com.rocketseat.gestao_vagas.security;

import java.io.IOException;
import java.util.Collections;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import com.rocketseat.gestao_vagas.providers.JWTProvider;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@Component
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

        // teste para ver se ta ok
        System.out.println(header);
        SecurityContextHolder.getContext().setAuthentication(null);

        if (request.getRequestURI().startsWith("/company") || request.getRequestURI().startsWith("/job")) {
            if (header != null) {
                var subjectToken = this.jwtProvider.validateToken(header);
                if (subjectToken.isEmpty()) {
                    response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
                    return;
                }

                // estamos armazenando na requisição o subjectToken como um atributo com o nome
                // "company_id"
                request.setAttribute("company_id", subjectToken);

                // vamos precisar o usuário para o Spring
                UsernamePasswordAuthenticationToken auth = new UsernamePasswordAuthenticationToken(subjectToken, null,
                        Collections.emptyList());

                SecurityContextHolder.getContext().setAuthentication(auth);
            }
        }

        filterChain.doFilter(request, response);

    }

}
