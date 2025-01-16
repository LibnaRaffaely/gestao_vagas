package com.rocketseat.gestao_vagas.modules.candidate.useCase;

import java.time.Duration;
import java.time.Instant;
import java.util.Arrays;

import javax.naming.AuthenticationException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.rocketseat.gestao_vagas.modules.candidate.CandidateRepository;
import com.rocketseat.gestao_vagas.modules.candidate.dto.AuthCandidateRequestDTO;
import com.rocketseat.gestao_vagas.modules.candidate.dto.AuthCandidateResponseDTO;

@Service
public class AuthCandidateUseCase {
        // saber se temos um candidate pelo repositório
        @Autowired
        private CandidateRepository candidateRepository;

        @Autowired
        private PasswordEncoder passwordEncoder;

        @Value("${security.token.secret.candidate}")
        private String secretKey;

        public AuthCandidateResponseDTO execute(AuthCandidateRequestDTO authCandidateRequestDTO)
                        throws AuthenticationException {
                var candidate = this.candidateRepository.findByUsername(authCandidateRequestDTO.username())
                                .orElseThrow(() -> {
                                        throw new UsernameNotFoundException("Username/password Incorrect");
                                });

                var passwordsMatches = this.passwordEncoder
                                .matches(authCandidateRequestDTO.password(), candidate.getPassword());

                if (!passwordsMatches) {
                        throw new AuthenticationException();
                }

                Algorithm algorithm = Algorithm.HMAC256(secretKey);

                var expiresIn = Instant.now().plus(Duration.ofHours(2));

                var token = JWT.create()
                                .withIssuer("Javagas")
                                .withSubject(candidate.getId().toString())
                                .withExpiresAt(expiresIn)
                                .withClaim("roles", Arrays.asList("CANDIDATE"))
                                .sign(algorithm);

                var authCandidateResponseDTO = AuthCandidateResponseDTO.builder()
                                .acess_token(token)
                                .expires_in(expiresIn.toEpochMilli())
                                .build();

                return authCandidateResponseDTO;
        }
}
