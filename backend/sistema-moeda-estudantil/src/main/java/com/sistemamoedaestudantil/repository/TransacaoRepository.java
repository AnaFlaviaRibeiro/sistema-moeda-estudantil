package com.sistemamoedaestudantil.repository;

import com.sistemamoedaestudantil.domain.Transacao;
import io.micronaut.data.annotation.Repository;
import io.micronaut.data.repository.CrudRepository;

import java.util.List;

@Repository
public interface TransacaoRepository extends CrudRepository<Transacao, Long> {

    List<Transacao> findByAlunoIdOrderByDataHoraDesc(Long alunoId);

    List<Transacao> findByProfessorIdOrderByDataHoraDesc(Long professorId);
}
