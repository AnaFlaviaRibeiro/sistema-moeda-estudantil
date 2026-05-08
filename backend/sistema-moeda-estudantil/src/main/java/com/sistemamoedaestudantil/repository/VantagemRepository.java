package com.sistemamoedaestudantil.repository;

import com.sistemamoedaestudantil.domain.Vantagem;
import io.micronaut.data.annotation.Repository;
import io.micronaut.data.repository.CrudRepository;

import java.util.List;

@Repository
public interface VantagemRepository extends CrudRepository<Vantagem, Long> {

    List<Vantagem> findByAtivaTrue();

    List<Vantagem> findByEmpresaId(Long empresaId);
}
