package com.sistemamoedaestudantil.dto;

import io.micronaut.core.annotation.Introspected;
import io.micronaut.serde.annotation.Serdeable;

@Serdeable
@Introspected
public class ProfessorResponseDTO {

    private Long id;
    private String nome;
    private String email;
    private String cpf;
    private String departamento;
    private Integer saldo;
    private InstituicaoDTO instituicao;

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getNome() { return nome; }
    public void setNome(String nome) { this.nome = nome; }

    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }

    public String getCpf() { return cpf; }
    public void setCpf(String cpf) { this.cpf = cpf; }

    public String getDepartamento() { return departamento; }
    public void setDepartamento(String departamento) { this.departamento = departamento; }

    public Integer getSaldo() { return saldo; }
    public void setSaldo(Integer saldo) { this.saldo = saldo; }

    public InstituicaoDTO getInstituicao() { return instituicao; }
    public void setInstituicao(InstituicaoDTO instituicao) { this.instituicao = instituicao; }
}
