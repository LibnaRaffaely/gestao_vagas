package com.rocketseat.gestao_vagas.modules.candidate;

import java.util.Optional;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;

/*pelo Spring JPA já ter alguns métodos que facilitam a persistência
ao colocar como interface nós poderemos extender o o JPARepository
usar interfaces permite que o Spring cuide da implementação por trás das cenas
*/
public interface CandidateRepository extends JpaRepository<CandidateEntity, UUID> {
    Optional<CandidateEntity> findByUsernameOrEmail(String username, String email);

    Optional<CandidateEntity> findByUsername(String username);
}
