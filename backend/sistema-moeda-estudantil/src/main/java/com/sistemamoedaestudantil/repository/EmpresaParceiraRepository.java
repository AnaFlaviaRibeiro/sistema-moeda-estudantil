package com.sistemamoedaestudantil.repository;

import com.sistemamoedaestudantil.domain.EmpresaParceira;
import io.micronaut.data.annotation.Repository;
import io.micronaut.data.repository.CrudRepository;

import java.util.Optional;

@Repository
public interface EmpresaParceiraRepository extends CrudRepository<EmpresaParceira, Long> {

    Optional<EmpresaParceira> findByCnpj(String cnpj);

    Optional<EmpresaParceira> findByEmail(String email);

    boolean existsByCnpj(String cnpj);

    boolean existsByEmail(String email);
}
