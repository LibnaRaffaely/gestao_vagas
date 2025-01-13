package com.rocketseat.gestao_vagas.modules.candidate.useCase;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.rocketseat.gestao_vagas.exceptions.UserFoundException;
import com.rocketseat.gestao_vagas.modules.candidate.CandidateEntity;
import com.rocketseat.gestao_vagas.modules.candidate.CandidateRepository;

@Service
public class CreateCandidateUseCase {
    // classe responsável pela regra de negócio de criação e conferir credenciais do
    // usuário
    @Autowired
    private CandidateRepository candidateRepository;

    public CandidateEntity execute(CandidateEntity candidateEntity) {
        System.out.println("Candidato: ");
        System.out.println(candidateEntity.getEmail());
        this.candidateRepository
                .findByUsernameOrEmail(candidateEntity.getUsername(), candidateEntity.getEmail())
                .ifPresent((user) -> {
                    throw new UserFoundException();
                });

        return this.candidateRepository.save(candidateEntity);
    }
}
