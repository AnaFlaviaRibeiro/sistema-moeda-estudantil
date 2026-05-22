package com.sistemamoedaestudantil.controller;

import com.sistemamoedaestudantil.dto.AuthUserDTO;
import com.sistemamoedaestudantil.dto.LoginRequestDTO;
import com.sistemamoedaestudantil.dto.LoginResponseDTO;
import com.sistemamoedaestudantil.security.AuthenticatedUser;
import com.sistemamoedaestudantil.service.AuthService;
import io.micronaut.http.MediaType;
import io.micronaut.http.annotation.Body;
import io.micronaut.http.annotation.Controller;
import io.micronaut.http.annotation.Get;
import io.micronaut.http.annotation.Post;
import io.micronaut.validation.Validated;

import javax.validation.Valid;

@Validated
@Controller("/api/auth")
public class AuthController {

    private final AuthService authService;

    public AuthController(AuthService authService) {
        this.authService = authService;
    }

    @Post(value = "/login", consumes = MediaType.APPLICATION_JSON, produces = MediaType.APPLICATION_JSON)
    public LoginResponseDTO login(@Body @Valid LoginRequestDTO dto) {
        return authService.login(dto);
    }

    @Get(value = "/me", produces = MediaType.APPLICATION_JSON)
    public AuthUserDTO me(AuthenticatedUser user) {
        return authService.usuarioAtual(user);
    }
}
