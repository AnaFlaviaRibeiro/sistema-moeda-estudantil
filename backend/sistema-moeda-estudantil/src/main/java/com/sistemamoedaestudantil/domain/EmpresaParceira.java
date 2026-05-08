package com.sistemamoedaestudantil.domain;

import io.micronaut.core.annotation.Introspected;

import javax.persistence.Column;
import javax.persistence.Embedded;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "empresa_parceira")
@Introspected
public class EmpresaParceira {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "razao_social", nullable = false, length = 200)
    private String razaoSocial;

    @Column(nullable = false, unique = true, length = 18)
    private String cnpj;

    @Column(nullable = false, unique = true, length = 150)
    private String email;

    @Column(nullable = false, length = 200)
    private String senha;

    @Column(length = 100)
    private String contato;

    @Embedded
    private Endereco endereco = new Endereco();

    public EmpresaParceira() {
    }

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getRazaoSocial() { return razaoSocial; }
    public void setRazaoSocial(String razaoSocial) { this.razaoSocial = razaoSocial; }

    public String getCnpj() { return cnpj; }
    public void setCnpj(String cnpj) { this.cnpj = cnpj; }

    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }

    public String getSenha() { return senha; }
    public void setSenha(String senha) { this.senha = senha; }

    public String getContato() { return contato; }
    public void setContato(String contato) { this.contato = contato; }

    public Endereco getEndereco() { return endereco; }
    public void setEndereco(Endereco endereco) { this.endereco = endereco; }
}
