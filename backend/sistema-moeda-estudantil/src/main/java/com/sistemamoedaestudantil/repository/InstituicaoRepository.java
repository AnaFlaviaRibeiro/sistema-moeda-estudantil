package com.sistemamoedaestudantil.repository;

import com.sistemamoedaestudantil.domain.Instituicao;
import io.micronaut.data.annotation.Repository;
import io.micronaut.data.repository.CrudRepository;

import java.util.Optional;

@Repository
public interface InstituicaoRepository extends CrudRepository<Instituicao, Long> {

    Optional<Instituicao> findByCnpj(String cnpj);
}
