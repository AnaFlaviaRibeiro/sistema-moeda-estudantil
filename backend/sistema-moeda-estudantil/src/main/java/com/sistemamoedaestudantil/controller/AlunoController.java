package com.sistemamoedaestudantil.controller;

import com.sistemamoedaestudantil.dto.AlunoCreateDTO;
import com.sistemamoedaestudantil.dto.AlunoResponseDTO;
import com.sistemamoedaestudantil.dto.AlunoUpdateDTO;
import com.sistemamoedaestudantil.service.AlunoService;
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
@Controller("/api/alunos")
public class AlunoController {

    private final AlunoService alunoService;

    public AlunoController(AlunoService alunoService) {
        this.alunoService = alunoService;
    }

    @Get(produces = MediaType.APPLICATION_JSON)
    public List<AlunoResponseDTO> listar() {
        return alunoService.listar();
    }

    @Get(value = "/{id}", produces = MediaType.APPLICATION_JSON)
    public AlunoResponseDTO buscar(@PathVariable Long id) {
        return alunoService.buscarPorId(id);
    }

    @Post(consumes = MediaType.APPLICATION_JSON, produces = MediaType.APPLICATION_JSON)
    public HttpResponse<AlunoResponseDTO> criar(@Body @Valid AlunoCreateDTO dto) {
        AlunoResponseDTO criado = alunoService.criar(dto);
        return HttpResponse.created(criado, URI.create("/api/alunos/" + criado.getId()));
    }

    @Put(value = "/{id}", consumes = MediaType.APPLICATION_JSON, produces = MediaType.APPLICATION_JSON)
    public AlunoResponseDTO atualizar(@PathVariable Long id, @Body @Valid AlunoUpdateDTO dto) {
        return alunoService.atualizar(id, dto);
    }

    @Delete("/{id}")
    public HttpResponse<Void> remover(@PathVariable Long id) {
        alunoService.remover(id);
        return HttpResponse.noContent();
    }
}
