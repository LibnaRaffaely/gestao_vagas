package com.rocketseat.gestao_vagas.modules.company.entity;

import java.time.LocalDateTime;
import java.util.UUID;

import org.hibernate.annotations.CreationTimestamp;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Entity(name = "Job")
@Data
public class JobEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;
    private String description;

    @NotBlank(message = "Este campo é obrigatório")
    private String Level;

    private String benefits;

    // Many = tabela Job e One = Company
    @ManyToOne()
    @JoinColumn(name = "companyId", insertable = false, updatable = false) // qnd for inserir uma company aqui e ela n
                                                                           // tiver um id dela, dará erro
    private CompanyEntity companyEntity;

    private UUID companyId;

    @CreationTimestamp
    private LocalDateTime createAt;
}
