package com.sistemamoedaestudantil.controller;

import com.sistemamoedaestudantil.dto.VantagemCreateDTO;
import com.sistemamoedaestudantil.dto.VantagemResponseDTO;
import com.sistemamoedaestudantil.dto.VantagemUpdateDTO;
import com.sistemamoedaestudantil.security.AuthenticatedUser;
import com.sistemamoedaestudantil.security.AuthorizationService;
import com.sistemamoedaestudantil.service.VantagemService;
import io.micronaut.http.HttpResponse;
import io.micronaut.http.MediaType;
import io.micronaut.http.annotation.Body;
import io.micronaut.http.annotation.Controller;
import io.micronaut.http.annotation.Get;
import io.micronaut.http.annotation.PathVariable;
import io.micronaut.http.annotation.Post;
import io.micronaut.http.annotation.Put;
import io.micronaut.validation.Validated;

import javax.validation.Valid;
import java.net.URI;
import java.util.List;

@Validated
@Controller("/api/vantagens")
public class VantagemController {

    private final VantagemService vantagemService;
    private final AuthorizationService authorization;

    public VantagemController(VantagemService vantagemService, AuthorizationService authorization) {
        this.vantagemService = vantagemService;
        this.authorization = authorization;
    }

    @Get(produces = MediaType.APPLICATION_JSON)
    public List<VantagemResponseDTO> listarAtivas(AuthenticatedUser user) {
        authorization.requireAluno(user);
        return vantagemService.listarAtivas();
    }

    @Get(value = "/{id}", produces = MediaType.APPLICATION_JSON)
    public VantagemResponseDTO buscar(@PathVariable Long id, AuthenticatedUser user) {
        authorization.requireAluno(user);
        return vantagemService.buscarPorId(id);
    }

    @Get(value = "/empresa/minhas", produces = MediaType.APPLICATION_JSON)
    public List<VantagemResponseDTO> listarMinhas(AuthenticatedUser user) {
        authorization.requireEmpresa(user);
        return vantagemService.listarPorEmpresa(user.getId());
    }

    @Get(value = "/empresa/{id}", produces = MediaType.APPLICATION_JSON)
    public VantagemResponseDTO buscarMinha(@PathVariable Long id, AuthenticatedUser user) {
        authorization.requireEmpresa(user);
        return vantagemService.buscarPorEmpresa(user.getId(), id);
    }

    @Post(consumes = MediaType.APPLICATION_JSON, produces = MediaType.APPLICATION_JSON)
    public HttpResponse<VantagemResponseDTO> criar(@Body @Valid VantagemCreateDTO dto,
                                                   AuthenticatedUser user) {
        authorization.requireEmpresa(user);
        VantagemResponseDTO criada = vantagemService.criar(user.getId(), dto);
        return HttpResponse.created(criada, URI.create("/api/vantagens/" + criada.getId()));
    }

    @Put(value = "/{id}", consumes = MediaType.APPLICATION_JSON, produces = MediaType.APPLICATION_JSON)
    public VantagemResponseDTO atualizar(@PathVariable Long id,
                                         @Body @Valid VantagemUpdateDTO dto,
                                         AuthenticatedUser user) {
        authorization.requireEmpresa(user);
        return vantagemService.atualizar(user.getId(), id, dto);
    }
}
