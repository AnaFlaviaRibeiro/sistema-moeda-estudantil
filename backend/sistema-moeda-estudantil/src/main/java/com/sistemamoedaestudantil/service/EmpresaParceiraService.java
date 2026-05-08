package com.sistemamoedaestudantil.service;

import com.sistemamoedaestudantil.domain.Endereco;
import com.sistemamoedaestudantil.domain.EmpresaParceira;
import com.sistemamoedaestudantil.dto.EmpresaParceiraCreateDTO;
import com.sistemamoedaestudantil.dto.EmpresaParceiraResponseDTO;
import com.sistemamoedaestudantil.dto.EmpresaParceiraUpdateDTO;
import com.sistemamoedaestudantil.dto.EnderecoDTO;
import com.sistemamoedaestudantil.exception.BusinessException;
import com.sistemamoedaestudantil.exception.NotFoundException;
import com.sistemamoedaestudantil.repository.EmpresaParceiraRepository;
import jakarta.inject.Singleton;

import javax.transaction.Transactional;
import java.util.ArrayList;
import java.util.List;

@Singleton
public class EmpresaParceiraService {

    private final EmpresaParceiraRepository empresaRepository;

    public EmpresaParceiraService(EmpresaParceiraRepository empresaRepository) {
        this.empresaRepository = empresaRepository;
    }

    @Transactional
    public EmpresaParceiraResponseDTO criar(EmpresaParceiraCreateDTO dto) {
        if (empresaRepository.existsByCnpj(dto.getCnpj())) {
            throw new BusinessException("Já existe uma empresa cadastrada com este CNPJ.");
        }
        if (empresaRepository.existsByEmail(dto.getEmail())) {
            throw new BusinessException("Já existe uma empresa cadastrada com este e-mail.");
        }

        EmpresaParceira empresa = new EmpresaParceira();
        empresa.setRazaoSocial(dto.getRazaoSocial());
        empresa.setCnpj(dto.getCnpj());
        empresa.setEmail(dto.getEmail());
        empresa.setSenha(dto.getSenha());
        empresa.setContato(dto.getContato());
        empresa.setEndereco(toEndereco(dto.getEndereco()));

        return toResponseDTO(empresaRepository.save(empresa));
    }

    @Transactional
    public EmpresaParceiraResponseDTO atualizar(Long id, EmpresaParceiraUpdateDTO dto) {
        EmpresaParceira empresa = empresaRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Empresa não encontrada."));

        if (!empresa.getEmail().equalsIgnoreCase(dto.getEmail())
                && empresaRepository.existsByEmail(dto.getEmail())) {
            throw new BusinessException("Já existe uma empresa cadastrada com este e-mail.");
        }

        empresa.setRazaoSocial(dto.getRazaoSocial());
        empresa.setEmail(dto.getEmail());
        empresa.setContato(dto.getContato());
        empresa.setEndereco(toEndereco(dto.getEndereco()));

        return toResponseDTO(empresaRepository.update(empresa));
    }

    @Transactional
    public void remover(Long id) {
        if (!empresaRepository.existsById(id)) {
            throw new NotFoundException("Empresa não encontrada.");
        }
        empresaRepository.deleteById(id);
    }

    @Transactional
    public EmpresaParceiraResponseDTO buscarPorId(Long id) {
        EmpresaParceira empresa = empresaRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Empresa não encontrada."));
        return toResponseDTO(empresa);
    }

    @Transactional
    public List<EmpresaParceiraResponseDTO> listar() {
        List<EmpresaParceiraResponseDTO> result = new ArrayList<>();
        for (EmpresaParceira empresa : empresaRepository.findAll()) {
            result.add(toResponseDTO(empresa));
        }
        return result;
    }

    private Endereco toEndereco(EnderecoDTO dto) {
        Endereco e = new Endereco();
        if (dto != null) {
            e.setLogradouro(dto.getLogradouro());
            e.setNumero(dto.getNumero());
            e.setComplemento(dto.getComplemento());
            e.setBairro(dto.getBairro());
            e.setCidade(dto.getCidade());
            e.setUf(dto.getUf());
            e.setCep(dto.getCep());
        }
        return e;
    }

    private EnderecoDTO toEnderecoDTO(Endereco e) {
        if (e == null) return null;
        EnderecoDTO dto = new EnderecoDTO();
        dto.setLogradouro(e.getLogradouro());
        dto.setNumero(e.getNumero());
        dto.setComplemento(e.getComplemento());
        dto.setBairro(e.getBairro());
        dto.setCidade(e.getCidade());
        dto.setUf(e.getUf());
        dto.setCep(e.getCep());
        return dto;
    }

    private EmpresaParceiraResponseDTO toResponseDTO(EmpresaParceira e) {
        EmpresaParceiraResponseDTO dto = new EmpresaParceiraResponseDTO();
        dto.setId(e.getId());
        dto.setRazaoSocial(e.getRazaoSocial());
        dto.setCnpj(e.getCnpj());
        dto.setEmail(e.getEmail());
        dto.setContato(e.getContato());
        dto.setEndereco(toEnderecoDTO(e.getEndereco()));
        return dto;
    }
}
