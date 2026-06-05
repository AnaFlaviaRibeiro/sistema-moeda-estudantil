package com.sistemamoedaestudantil.controller;

import com.sistemamoedaestudantil.dto.ProfessorResponseDTO;
import com.sistemamoedaestudantil.security.AuthenticatedUser;
import com.sistemamoedaestudantil.security.AuthorizationService;
import com.sistemamoedaestudantil.service.ProfessorService;
import io.micronaut.http.MediaType;
import io.micronaut.http.annotation.Controller;
import io.micronaut.http.annotation.Get;

@Controller("/api/professores")
public class ProfessorController {

    private final ProfessorService professorService;
    private final AuthorizationService authorization;

    public ProfessorController(ProfessorService professorService, AuthorizationService authorization) {
        this.professorService = professorService;
        this.authorization = authorization;
    }

    @Get(value = "/me", produces = MediaType.APPLICATION_JSON)
    public ProfessorResponseDTO meuPerfil(AuthenticatedUser user) {
        authorization.requireProfessor(user);
        return professorService.buscarPorId(user.getId());
    }
}
