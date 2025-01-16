package com.rocketseat.gestao_vagas.modules.company.controller;

import javax.naming.AuthenticationException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.rocketseat.gestao_vagas.modules.company.dto.AuthCompanyDTO;
import com.rocketseat.gestao_vagas.modules.company.useCases.AuthCompanyUseCase;

@RestController
@RequestMapping("/company")
public class AuthCompanyController {

    @Autowired
    private AuthCompanyUseCase authCompanyUseCase;

    @PostMapping("/auth")
    public String create(@RequestBody AuthCompanyDTO authCompanyDTO) throws AuthenticationException {
        System.out.println("Sistema de verificação da conta iniciada");
        System.err.println(authCompanyDTO.getUsername());
        return this.authCompanyUseCase.execute(authCompanyDTO);

    }
}
