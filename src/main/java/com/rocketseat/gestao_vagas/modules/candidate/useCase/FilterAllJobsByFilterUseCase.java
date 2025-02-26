package com.rocketseat.gestao_vagas.modules.candidate.useCase;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.rocketseat.gestao_vagas.modules.company.entity.JobEntity;
import com.rocketseat.gestao_vagas.modules.company.repositories.JobRepository;

@Service
public class FilterAllJobsByFilterUseCase {
    @Autowired
    private JobRepository jobRepository;

    public List<JobEntity> execute(String filter) {
        return this.jobRepository.findByDescriptionContainingIgnoreCase(filter);
    }
}
