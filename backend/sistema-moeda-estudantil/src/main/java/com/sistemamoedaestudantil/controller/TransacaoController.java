package com.sistemamoedaestudantil.controller;

import com.sistemamoedaestudantil.dto.DistribuicaoMoedasDTO;
import com.sistemamoedaestudantil.dto.ExtratoResponseDTO;
import com.sistemamoedaestudantil.dto.ResgateVantagemDTO;
import com.sistemamoedaestudantil.dto.TransacaoResponseDTO;
import com.sistemamoedaestudantil.security.AuthenticatedUser;
import com.sistemamoedaestudantil.security.AuthorizationService;
import com.sistemamoedaestudantil.service.TransacaoService;
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
@Controller("/api/transacoes")
public class TransacaoController {

    private final TransacaoService transacaoService;
    private final AuthorizationService authorization;

    public TransacaoController(TransacaoService transacaoService, AuthorizationService authorization) {
        this.transacaoService = transacaoService;
        this.authorization = authorization;
    }

    @Post(value = "/distribuir", consumes = MediaType.APPLICATION_JSON, produces = MediaType.APPLICATION_JSON)
    public HttpResponse<TransacaoResponseDTO> distribuir(@Body @Valid DistribuicaoMoedasDTO dto,
                                                         AuthenticatedUser user) {
        authorization.requireProfessor(user);
        TransacaoResponseDTO criada = transacaoService.distribuirMoedas(user.getId(), dto);
        return HttpResponse.created(criada, URI.create("/api/transacoes/" + criada.getId()));
    }

    @Post(value = "/resgatar", consumes = MediaType.APPLICATION_JSON, produces = MediaType.APPLICATION_JSON)
    public HttpResponse<TransacaoResponseDTO> resgatar(@Body @Valid ResgateVantagemDTO dto,
                                                       AuthenticatedUser user) {
        authorization.requireAluno(user);
        TransacaoResponseDTO criada = transacaoService.resgatarVantagem(user.getId(), dto);
        return HttpResponse.created(criada, URI.create("/api/transacoes/" + criada.getId()));
    }

    @Get(value = "/extrato/aluno/me", produces = MediaType.APPLICATION_JSON)
    public ExtratoResponseDTO extratoAluno(AuthenticatedUser user) {
        authorization.requireAluno(user);
        return transacaoService.extratoAluno(user.getId());
    }

    @Get(value = "/extrato/professor/me", produces = MediaType.APPLICATION_JSON)
    public ExtratoResponseDTO extratoProfessor(AuthenticatedUser user) {
        authorization.requireProfessor(user);
        return transacaoService.extratoProfessor(user.getId());
    }

    @Get(value = "/resgates/empresa", produces = MediaType.APPLICATION_JSON)
    public List<TransacaoResponseDTO> resgatesEmpresa(AuthenticatedUser user) {
        authorization.requireEmpresa(user);
        return transacaoService.listarResgatesEmpresa(user.getId());
    }

    @Put(value = "/cupons/{codigo}/conferir", produces = MediaType.APPLICATION_JSON)
    public TransacaoResponseDTO conferirCupom(@PathVariable String codigo, AuthenticatedUser user) {
        authorization.requireEmpresa(user);
        return transacaoService.conferirCupom(user.getId(), codigo);
    }
}
