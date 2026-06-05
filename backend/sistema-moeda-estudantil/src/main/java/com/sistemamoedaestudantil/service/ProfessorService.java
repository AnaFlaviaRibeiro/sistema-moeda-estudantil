package com.sistemamoedaestudantil.service;

import com.sistemamoedaestudantil.domain.Instituicao;
import com.sistemamoedaestudantil.domain.Professor;
import com.sistemamoedaestudantil.dto.InstituicaoDTO;
import com.sistemamoedaestudantil.dto.ProfessorResponseDTO;
import com.sistemamoedaestudantil.exception.NotFoundException;
import com.sistemamoedaestudantil.repository.ProfessorRepository;
import jakarta.inject.Singleton;

import javax.transaction.Transactional;

@Singleton
public class ProfessorService {

    private final ProfessorRepository professorRepository;

    public ProfessorService(ProfessorRepository professorRepository) {
        this.professorRepository = professorRepository;
    }

    @Transactional
    public ProfessorResponseDTO buscarPorId(Long id) {
        Professor professor = professorRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Professor não encontrado."));
        return toResponseDTO(professor);
    }

    private ProfessorResponseDTO toResponseDTO(Professor professor) {
        ProfessorResponseDTO dto = new ProfessorResponseDTO();
        dto.setId(professor.getId());
        dto.setNome(professor.getNome());
        dto.setEmail(professor.getEmail());
        dto.setCpf(professor.getCpf());
        dto.setDepartamento(professor.getDepartamento());
        dto.setSaldo(professor.getSaldo());
        if (professor.getInstituicao() != null) {
            Instituicao i = professor.getInstituicao();
            dto.setInstituicao(new InstituicaoDTO(i.getId(), i.getNome(), i.getCnpj()));
        }
        return dto;
    }
}
