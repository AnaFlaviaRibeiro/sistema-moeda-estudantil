package com.sistemamoedaestudantil.domain;

import io.micronaut.core.annotation.Introspected;

import javax.persistence.AttributeOverride;
import javax.persistence.Column;
import javax.persistence.Embedded;
import javax.persistence.Entity;
import javax.persistence.Table;

@Entity
@Table(name = "empresa_parceira")
@AttributeOverride(name = "nome", column = @Column(name = "razao_social", nullable = false, length = 200))
@Introspected
public class EmpresaParceira extends Usuario {

    @Column(nullable = false, unique = true, length = 18)
    private String cnpj;

    @Column(length = 100)
    private String contato;

    @Embedded
    private Endereco endereco = new Endereco();

    public EmpresaParceira() {
    }

    public String getRazaoSocial() {
        return getNome();
    }

    public void setRazaoSocial(String razaoSocial) {
        setNome(razaoSocial);
    }

    public String getCnpj() { return cnpj; }
    public void setCnpj(String cnpj) { this.cnpj = cnpj; }

    public String getContato() { return contato; }
    public void setContato(String contato) { this.contato = contato; }

    public Endereco getEndereco() { return endereco; }
    public void setEndereco(Endereco endereco) { this.endereco = endereco; }
}
