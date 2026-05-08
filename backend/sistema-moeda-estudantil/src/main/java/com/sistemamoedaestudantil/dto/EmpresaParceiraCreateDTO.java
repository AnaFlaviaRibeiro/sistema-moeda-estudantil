package com.sistemamoedaestudantil.dto;

import io.micronaut.core.annotation.Introspected;
import io.micronaut.serde.annotation.Serdeable;

import javax.validation.Valid;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

@Serdeable
@Introspected
public class EmpresaParceiraCreateDTO {

    @NotBlank
    @Size(max = 200)
    private String razaoSocial;

    @NotBlank
    @Size(min = 14, max = 18)
    private String cnpj;

    @NotBlank
    @Email
    @Size(max = 150)
    private String email;

    @NotBlank
    @Size(min = 6, max = 200)
    private String senha;

    @Size(max = 100)
    private String contato;

    @Valid
    private EnderecoDTO endereco;

    public EmpresaParceiraCreateDTO() {
    }

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

    public EnderecoDTO getEndereco() { return endereco; }
    public void setEndereco(EnderecoDTO endereco) { this.endereco = endereco; }
}
