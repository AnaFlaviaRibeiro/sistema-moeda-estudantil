package com.sistemamoedaestudantil.domain;

import io.micronaut.core.annotation.Introspected;

import javax.persistence.Column;
import javax.persistence.Embedded;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

@Entity
@Table(name = "aluno")
@Introspected
public class Aluno {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 150)
    private String nome;

    @Column(nullable = false, unique = true, length = 150)
    private String email;

    @Column(nullable = false, length = 200)
    private String senha;

    @Column(nullable = false, unique = true, length = 14)
    private String cpf;

    @Column(length = 20)
    private String rg;

    @Column(nullable = false, length = 100)
    private String curso;

    @Column(nullable = false)
    private Integer saldo = 0;

    @ManyToOne(fetch = FetchType.EAGER, optional = false)
    @JoinColumn(name = "instituicao_id", nullable = false)
    private Instituicao instituicao;

    @Embedded
    private Endereco endereco = new Endereco();

    public Aluno() {
    }

    public void creditar(int valor) {
        if (valor <= 0) throw new IllegalArgumentException("Valor de crédito deve ser positivo.");
        this.saldo = (this.saldo == null ? 0 : this.saldo) + valor;
    }

    public void debitar(int valor) {
        if (valor <= 0) throw new IllegalArgumentException("Valor de débito deve ser positivo.");
        if (this.saldo == null || this.saldo < valor) {
            throw new IllegalStateException("Saldo insuficiente.");
        }
        this.saldo -= valor;
    }

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getNome() { return nome; }
    public void setNome(String nome) { this.nome = nome; }

    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }

    public String getSenha() { return senha; }
    public void setSenha(String senha) { this.senha = senha; }

    public String getCpf() { return cpf; }
    public void setCpf(String cpf) { this.cpf = cpf; }

    public String getRg() { return rg; }
    public void setRg(String rg) { this.rg = rg; }

    public String getCurso() { return curso; }
    public void setCurso(String curso) { this.curso = curso; }

    public Integer getSaldo() { return saldo; }
    public void setSaldo(Integer saldo) { this.saldo = saldo; }

    public Instituicao getInstituicao() { return instituicao; }
    public void setInstituicao(Instituicao instituicao) { this.instituicao = instituicao; }

    public Endereco getEndereco() { return endereco; }
    public void setEndereco(Endereco endereco) { this.endereco = endereco; }
}
