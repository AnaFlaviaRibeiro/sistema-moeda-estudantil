package com.sistemamoedaestudantil.repository;

import com.sistemamoedaestudantil.domain.Aluno;
import io.micronaut.data.annotation.Repository;
import io.micronaut.data.repository.CrudRepository;

import java.util.Optional;

@Repository
public interface AlunoRepository extends CrudRepository<Aluno, Long> {

    Optional<Aluno> findByCpf(String cpf);

    Optional<Aluno> findByEmail(String email);

    boolean existsByCpf(String cpf);

    boolean existsByEmail(String email);
}
