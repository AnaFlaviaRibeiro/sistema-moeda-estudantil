package com.sistemamoedaestudantil.service;

import com.sistemamoedaestudantil.domain.Aluno;
import com.sistemamoedaestudantil.domain.Endereco;
import com.sistemamoedaestudantil.domain.Instituicao;
import com.sistemamoedaestudantil.dto.AlunoCreateDTO;
import com.sistemamoedaestudantil.dto.AlunoResponseDTO;
import com.sistemamoedaestudantil.dto.AlunoUpdateDTO;
import com.sistemamoedaestudantil.dto.EnderecoDTO;
import com.sistemamoedaestudantil.dto.InstituicaoDTO;
import com.sistemamoedaestudantil.exception.BusinessException;
import com.sistemamoedaestudantil.exception.NotFoundException;
import com.sistemamoedaestudantil.repository.AlunoRepository;
import com.sistemamoedaestudantil.repository.InstituicaoRepository;
import jakarta.inject.Singleton;

import javax.transaction.Transactional;
import java.util.ArrayList;
import java.util.List;

@Singleton
public class AlunoService {

    private final AlunoRepository alunoRepository;
    private final InstituicaoRepository instituicaoRepository;

    public AlunoService(AlunoRepository alunoRepository,
                        InstituicaoRepository instituicaoRepository) {
        this.alunoRepository = alunoRepository;
        this.instituicaoRepository = instituicaoRepository;
    }

    @Transactional
    public AlunoResponseDTO criar(AlunoCreateDTO dto) {
        if (alunoRepository.existsByCpf(dto.getCpf())) {
            throw new BusinessException("Já existe um aluno cadastrado com este CPF.");
        }
        if (alunoRepository.existsByEmail(dto.getEmail())) {
            throw new BusinessException("Já existe um aluno cadastrado com este e-mail.");
        }

        Instituicao instituicao = instituicaoRepository.findById(dto.getInstituicaoId())
                .orElseThrow(() -> new NotFoundException("Instituição não encontrada."));

        Aluno aluno = new Aluno();
        aluno.setNome(dto.getNome());
        aluno.setEmail(dto.getEmail());
        aluno.setSenha(dto.getSenha());
        aluno.setCpf(dto.getCpf());
        aluno.setRg(dto.getRg());
        aluno.setCurso(dto.getCurso());
        aluno.setSaldo(0);
        aluno.setInstituicao(instituicao);
        aluno.setEndereco(toEndereco(dto.getEndereco()));

        return toResponseDTO(alunoRepository.save(aluno));
    }

    @Transactional
    public AlunoResponseDTO atualizar(Long id, AlunoUpdateDTO dto) {
        Aluno aluno = alunoRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Aluno não encontrado."));

        if (!aluno.getEmail().equalsIgnoreCase(dto.getEmail())
                && alunoRepository.existsByEmail(dto.getEmail())) {
            throw new BusinessException("Já existe um aluno cadastrado com este e-mail.");
        }

        Instituicao instituicao = instituicaoRepository.findById(dto.getInstituicaoId())
                .orElseThrow(() -> new NotFoundException("Instituição não encontrada."));

        aluno.setNome(dto.getNome());
        aluno.setEmail(dto.getEmail());
        aluno.setRg(dto.getRg());
        aluno.setCurso(dto.getCurso());
        aluno.setInstituicao(instituicao);
        aluno.setEndereco(toEndereco(dto.getEndereco()));

        return toResponseDTO(alunoRepository.update(aluno));
    }

    @Transactional
    public void remover(Long id) {
        if (!alunoRepository.existsById(id)) {
            throw new NotFoundException("Aluno não encontrado.");
        }
        alunoRepository.deleteById(id);
    }

    @Transactional
    public AlunoResponseDTO buscarPorId(Long id) {
        Aluno aluno = alunoRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Aluno não encontrado."));
        return toResponseDTO(aluno);
    }

    @Transactional
    public List<AlunoResponseDTO> listar() {
        List<AlunoResponseDTO> result = new ArrayList<>();
        for (Aluno aluno : alunoRepository.findAll()) {
            result.add(toResponseDTO(aluno));
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

    private AlunoResponseDTO toResponseDTO(Aluno aluno) {
        AlunoResponseDTO dto = new AlunoResponseDTO();
        dto.setId(aluno.getId());
        dto.setNome(aluno.getNome());
        dto.setEmail(aluno.getEmail());
        dto.setCpf(aluno.getCpf());
        dto.setRg(aluno.getRg());
        dto.setCurso(aluno.getCurso());
        dto.setSaldo(aluno.getSaldo());
        dto.setEndereco(toEnderecoDTO(aluno.getEndereco()));
        if (aluno.getInstituicao() != null) {
            Instituicao i = aluno.getInstituicao();
            dto.setInstituicao(new InstituicaoDTO(i.getId(), i.getNome(), i.getCnpj()));
        }
        return dto;
    }
}
