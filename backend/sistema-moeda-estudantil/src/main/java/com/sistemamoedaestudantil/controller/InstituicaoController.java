package com.sistemamoedaestudantil.controller;

import com.sistemamoedaestudantil.dto.InstituicaoDTO;
import com.sistemamoedaestudantil.service.InstituicaoService;
import io.micronaut.http.MediaType;
import io.micronaut.http.annotation.Controller;
import io.micronaut.http.annotation.Get;
import io.micronaut.http.annotation.PathVariable;

import java.util.List;

@Controller("/api/instituicoes")
public class InstituicaoController {

    private final InstituicaoService service;

    public InstituicaoController(InstituicaoService service) {
        this.service = service;
    }

    @Get(produces = MediaType.APPLICATION_JSON)
    public List<InstituicaoDTO> listar() {
        return service.listar();
    }

    @Get(value = "/{id}", produces = MediaType.APPLICATION_JSON)
    public InstituicaoDTO buscar(@PathVariable Long id) {
        return service.buscarPorId(id);
    }
}
