package com.sistemamoedaestudantil.controller;

import com.sistemamoedaestudantil.dto.EmpresaParceiraCreateDTO;
import com.sistemamoedaestudantil.dto.EmpresaParceiraResponseDTO;
import com.sistemamoedaestudantil.dto.EmpresaParceiraUpdateDTO;
import com.sistemamoedaestudantil.security.AuthenticatedUser;
import com.sistemamoedaestudantil.security.AuthorizationService;
import com.sistemamoedaestudantil.service.EmpresaParceiraService;
import io.micronaut.http.HttpResponse;
import io.micronaut.http.MediaType;
import io.micronaut.http.annotation.Body;
import io.micronaut.http.annotation.Controller;
import io.micronaut.http.annotation.Delete;
import io.micronaut.http.annotation.Get;
import io.micronaut.http.annotation.PathVariable;
import io.micronaut.http.annotation.Post;
import io.micronaut.http.annotation.Put;
import io.micronaut.validation.Validated;

import javax.validation.Valid;
import java.net.URI;

@Validated
@Controller("/api/empresas")
public class EmpresaParceiraController {

    private final EmpresaParceiraService service;
    private final AuthorizationService authorization;

    public EmpresaParceiraController(EmpresaParceiraService service, AuthorizationService authorization) {
        this.service = service;
        this.authorization = authorization;
    }

    @Get(value = "/me", produces = MediaType.APPLICATION_JSON)
    public EmpresaParceiraResponseDTO minhaEmpresa(AuthenticatedUser user) {
        authorization.requireEmpresa(user);
        return service.buscarPorId(user.getId());
    }

    @Get(value = "/{id}", produces = MediaType.APPLICATION_JSON)
    public EmpresaParceiraResponseDTO buscar(@PathVariable Long id, AuthenticatedUser user) {
        authorization.requireLeituraEmpresa(user, id);
        return service.buscarPorId(id);
    }

    @Post(consumes = MediaType.APPLICATION_JSON, produces = MediaType.APPLICATION_JSON)
    public HttpResponse<EmpresaParceiraResponseDTO> criar(@Body @Valid EmpresaParceiraCreateDTO dto) {
        EmpresaParceiraResponseDTO criada = service.criar(dto);
        return HttpResponse.created(criada, URI.create("/api/empresas/" + criada.getId()));
    }

    @Put(value = "/{id}", consumes = MediaType.APPLICATION_JSON, produces = MediaType.APPLICATION_JSON)
    public EmpresaParceiraResponseDTO atualizar(@PathVariable Long id,
                                                @Body @Valid EmpresaParceiraUpdateDTO dto,
                                                AuthenticatedUser user) {
        authorization.requireEdicaoPropriaEmpresa(user, id);
        return service.atualizar(id, dto);
    }

    @Delete("/{id}")
    public HttpResponse<Void> remover(@PathVariable Long id, AuthenticatedUser user) {
        authorization.denyRemocao();
        service.remover(id);
        return HttpResponse.noContent();
    }
}
