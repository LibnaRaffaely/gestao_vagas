package com.rocketseat.gestao_vagas.modules.company.repositories;

import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;

import com.rocketseat.gestao_vagas.modules.company.entity.JobEntity;

public interface JobRepository extends JpaRepository<JobEntity, UUID> {

}
