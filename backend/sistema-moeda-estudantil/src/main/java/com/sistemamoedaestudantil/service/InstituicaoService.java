package com.sistemamoedaestudantil.service;

import com.sistemamoedaestudantil.domain.Instituicao;
import com.sistemamoedaestudantil.dto.InstituicaoDTO;
import com.sistemamoedaestudantil.exception.NotFoundException;
import com.sistemamoedaestudantil.repository.InstituicaoRepository;
import jakarta.inject.Singleton;

import javax.transaction.Transactional;
import java.util.ArrayList;
import java.util.List;

@Singleton
public class InstituicaoService {

    private final InstituicaoRepository repository;

    public InstituicaoService(InstituicaoRepository repository) {
        this.repository = repository;
    }

    @Transactional
    public List<InstituicaoDTO> listar() {
        List<InstituicaoDTO> result = new ArrayList<>();
        for (Instituicao i : repository.findAll()) {
            result.add(new InstituicaoDTO(i.getId(), i.getNome(), i.getCnpj()));
        }
        return result;
    }

    @Transactional
    public InstituicaoDTO buscarPorId(Long id) {
        Instituicao i = repository.findById(id)
                .orElseThrow(() -> new NotFoundException("Instituição não encontrada."));
        return new InstituicaoDTO(i.getId(), i.getNome(), i.getCnpj());
    }
}
