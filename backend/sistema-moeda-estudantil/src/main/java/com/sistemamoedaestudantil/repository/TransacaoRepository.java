package com.sistemamoedaestudantil.repository;

import com.sistemamoedaestudantil.domain.TipoTransacao;
import com.sistemamoedaestudantil.domain.Transacao;
import io.micronaut.data.annotation.Query;
import io.micronaut.data.annotation.Repository;
import io.micronaut.data.repository.CrudRepository;

import java.util.List;
import java.util.Optional;

@Repository
public interface TransacaoRepository extends CrudRepository<Transacao, Long> {

    List<Transacao> findByAlunoIdOrderByDataHoraDesc(Long alunoId);

    List<Transacao> findByProfessorIdOrderByDataHoraDesc(Long professorId);

    @Query("SELECT t FROM Transacao t WHERE t.vantagem.empresa.id = :empresaId AND t.tipo = :tipo ORDER BY t.dataHora DESC")
    List<Transacao> findResgatesByEmpresa(Long empresaId, TipoTransacao tipo);

    Optional<Transacao> findByCodigoCupom(String codigoCupom);
}
