package com.sistemamoedaestudantil.domain;

import io.micronaut.core.annotation.Introspected;

import javax.persistence.Embeddable;
import javax.persistence.Column;
import java.io.Serializable;

@Embeddable
@Introspected
public class Endereco implements Serializable {

    @Column(name = "end_logradouro", length = 200)
    private String logradouro;

    @Column(name = "end_numero", length = 20)
    private String numero;

    @Column(name = "end_complemento", length = 100)
    private String complemento;

    @Column(name = "end_bairro", length = 100)
    private String bairro;

    @Column(name = "end_cidade", length = 100)
    private String cidade;

    @Column(name = "end_uf", length = 2)
    private String uf;

    @Column(name = "end_cep", length = 9)
    private String cep;

    public Endereco() {
    }

    public String getLogradouro() { return logradouro; }
    public void setLogradouro(String logradouro) { this.logradouro = logradouro; }

    public String getNumero() { return numero; }
    public void setNumero(String numero) { this.numero = numero; }

    public String getComplemento() { return complemento; }
    public void setComplemento(String complemento) { this.complemento = complemento; }

    public String getBairro() { return bairro; }
    public void setBairro(String bairro) { this.bairro = bairro; }

    public String getCidade() { return cidade; }
    public void setCidade(String cidade) { this.cidade = cidade; }

    public String getUf() { return uf; }
    public void setUf(String uf) { this.uf = uf; }

    public String getCep() { return cep; }
    public void setCep(String cep) { this.cep = cep; }
}
