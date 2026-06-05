package com.sistemamoedaestudantil.service;

import com.sistemamoedaestudantil.domain.EmpresaParceira;
import com.sistemamoedaestudantil.domain.Vantagem;
import com.sistemamoedaestudantil.dto.VantagemCreateDTO;
import com.sistemamoedaestudantil.dto.VantagemResponseDTO;
import com.sistemamoedaestudantil.dto.VantagemUpdateDTO;
import com.sistemamoedaestudantil.exception.BusinessException;
import com.sistemamoedaestudantil.exception.NotFoundException;
import com.sistemamoedaestudantil.repository.EmpresaParceiraRepository;
import com.sistemamoedaestudantil.repository.VantagemRepository;
import jakarta.inject.Singleton;

import javax.transaction.Transactional;
import java.util.ArrayList;
import java.util.List;

@Singleton
public class VantagemService {

    private final VantagemRepository vantagemRepository;
    private final EmpresaParceiraRepository empresaParceiraRepository;

    public VantagemService(VantagemRepository vantagemRepository,
                           EmpresaParceiraRepository empresaParceiraRepository) {
        this.vantagemRepository = vantagemRepository;
        this.empresaParceiraRepository = empresaParceiraRepository;
    }

    @Transactional
    public VantagemResponseDTO criar(Long empresaId, VantagemCreateDTO dto) {
        EmpresaParceira empresa = empresaParceiraRepository.findById(empresaId)
                .orElseThrow(() -> new NotFoundException("Empresa parceira não encontrada."));

        Vantagem vantagem = new Vantagem();
        vantagem.setNome(dto.getNome().trim());
        vantagem.setDescricao(dto.getDescricao());
        vantagem.setFotoUrl(dto.getFotoUrl());
        vantagem.setCustoEmMoedas(dto.getCustoEmMoedas());
        vantagem.setAtiva(true);
        vantagem.setEmpresa(empresa);

        return toResponseDTO(vantagemRepository.save(vantagem));
    }

    @Transactional
    public VantagemResponseDTO atualizar(Long empresaId, Long vantagemId, VantagemUpdateDTO dto) {
        Vantagem vantagem = buscarVantagemDaEmpresa(empresaId, vantagemId);

        vantagem.setNome(dto.getNome().trim());
        vantagem.setDescricao(dto.getDescricao());
        vantagem.setFotoUrl(dto.getFotoUrl());
        vantagem.setCustoEmMoedas(dto.getCustoEmMoedas());
        vantagem.setAtiva(dto.getAtiva());

        return toResponseDTO(vantagemRepository.update(vantagem));
    }

    @Transactional
    public VantagemResponseDTO buscarPorId(Long id) {
        Vantagem vantagem = vantagemRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Vantagem não encontrada."));
        return toResponseDTO(vantagem);
    }

    @Transactional
    public VantagemResponseDTO buscarPorEmpresa(Long empresaId, Long vantagemId) {
        return toResponseDTO(buscarVantagemDaEmpresa(empresaId, vantagemId));
    }

    @Transactional
    public List<VantagemResponseDTO> listarAtivas() {
        List<VantagemResponseDTO> result = new ArrayList<>();
        for (Vantagem v : vantagemRepository.findByAtivaTrue()) {
            result.add(toResponseDTO(v));
        }
        return result;
    }

    @Transactional
    public List<VantagemResponseDTO> listarPorEmpresa(Long empresaId) {
        List<VantagemResponseDTO> result = new ArrayList<>();
        for (Vantagem v : vantagemRepository.findByEmpresaId(empresaId)) {
            result.add(toResponseDTO(v));
        }
        return result;
    }

    @Transactional
    public Vantagem buscarEntidadeAtiva(Long id) {
        Vantagem vantagem = vantagemRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Vantagem não encontrada."));
        if (!Boolean.TRUE.equals(vantagem.getAtiva())) {
            throw new BusinessException("Esta vantagem não está mais disponível.");
        }
        return vantagem;
    }

    private Vantagem buscarVantagemDaEmpresa(Long empresaId, Long vantagemId) {
        Vantagem vantagem = vantagemRepository.findById(vantagemId)
                .orElseThrow(() -> new NotFoundException("Vantagem não encontrada."));
        if (vantagem.getEmpresa() == null || !vantagem.getEmpresa().getId().equals(empresaId)) {
            throw new BusinessException("Esta vantagem não pertence à sua empresa.");
        }
        return vantagem;
    }

    private VantagemResponseDTO toResponseDTO(Vantagem vantagem) {
        VantagemResponseDTO dto = new VantagemResponseDTO();
        dto.setId(vantagem.getId());
        dto.setNome(vantagem.getNome());
        dto.setDescricao(vantagem.getDescricao());
        dto.setFotoUrl(vantagem.getFotoUrl());
        dto.setCustoEmMoedas(vantagem.getCustoEmMoedas());
        dto.setAtiva(vantagem.getAtiva());
        if (vantagem.getEmpresa() != null) {
            dto.setEmpresaId(vantagem.getEmpresa().getId());
            dto.setEmpresaNome(vantagem.getEmpresa().getRazaoSocial());
        }
        return dto;
    }
}
