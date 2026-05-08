package com.sistemamoedaestudantil.repository;

import com.sistemamoedaestudantil.domain.Professor;
import io.micronaut.data.annotation.Repository;
import io.micronaut.data.repository.CrudRepository;

import java.util.Optional;

@Repository
public interface ProfessorRepository extends CrudRepository<Professor, Long> {

    Optional<Professor> findByCpf(String cpf);

    Optional<Professor> findByEmail(String email);
}
