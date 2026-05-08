package com.sistemamoedaestudantil.controller;

import com.sistemamoedaestudantil.dto.EmpresaParceiraCreateDTO;
import com.sistemamoedaestudantil.dto.EmpresaParceiraResponseDTO;
import com.sistemamoedaestudantil.dto.EmpresaParceiraUpdateDTO;
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
import java.util.List;

@Validated
@Controller("/api/empresas")
public class EmpresaParceiraController {

    private final EmpresaParceiraService service;

    public EmpresaParceiraController(EmpresaParceiraService service) {
        this.service = service;
    }

    @Get(produces = MediaType.APPLICATION_JSON)
    public List<EmpresaParceiraResponseDTO> listar() {
        return service.listar();
    }

    @Get(value = "/{id}", produces = MediaType.APPLICATION_JSON)
    public EmpresaParceiraResponseDTO buscar(@PathVariable Long id) {
        return service.buscarPorId(id);
    }

    @Post(consumes = MediaType.APPLICATION_JSON, produces = MediaType.APPLICATION_JSON)
    public HttpResponse<EmpresaParceiraResponseDTO> criar(@Body @Valid EmpresaParceiraCreateDTO dto) {
        EmpresaParceiraResponseDTO criada = service.criar(dto);
        return HttpResponse.created(criada, URI.create("/api/empresas/" + criada.getId()));
    }

    @Put(value = "/{id}", consumes = MediaType.APPLICATION_JSON, produces = MediaType.APPLICATION_JSON)
    public EmpresaParceiraResponseDTO atualizar(@PathVariable Long id, @Body @Valid EmpresaParceiraUpdateDTO dto) {
        return service.atualizar(id, dto);
    }

    @Delete("/{id}")
    public HttpResponse<Void> remover(@PathVariable Long id) {
        service.remover(id);
        return HttpResponse.noContent();
    }
}
