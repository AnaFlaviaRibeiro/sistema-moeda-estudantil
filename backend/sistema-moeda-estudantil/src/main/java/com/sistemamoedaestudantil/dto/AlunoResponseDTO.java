package com.sistemamoedaestudantil.dto;

import io.micronaut.core.annotation.Introspected;
import io.micronaut.serde.annotation.Serdeable;

@Serdeable
@Introspected
public class AlunoResponseDTO {

    private Long id;
    private String nome;
    private String email;
    private String cpf;
    private String rg;
    private String curso;
    private Integer saldo;
    private InstituicaoDTO instituicao;
    private EnderecoDTO endereco;

    public AlunoResponseDTO() {
    }

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getNome() { return nome; }
    public void setNome(String nome) { this.nome = nome; }

    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }

    public String getCpf() { return cpf; }
    public void setCpf(String cpf) { this.cpf = cpf; }

    public String getRg() { return rg; }
    public void setRg(String rg) { this.rg = rg; }

    public String getCurso() { return curso; }
    public void setCurso(String curso) { this.curso = curso; }

    public Integer getSaldo() { return saldo; }
    public void setSaldo(Integer saldo) { this.saldo = saldo; }

    public InstituicaoDTO getInstituicao() { return instituicao; }
    public void setInstituicao(InstituicaoDTO instituicao) { this.instituicao = instituicao; }

    public EnderecoDTO getEndereco() { return endereco; }
    public void setEndereco(EnderecoDTO endereco) { this.endereco = endereco; }
}
