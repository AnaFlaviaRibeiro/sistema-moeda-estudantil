package com.sistemamoedaestudantil.dto;

import io.micronaut.core.annotation.Introspected;
import io.micronaut.serde.annotation.Serdeable;

import javax.validation.Valid;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

@Serdeable
@Introspected
public class EmpresaParceiraUpdateDTO {

    @NotBlank
    @Size(max = 200)
    private String razaoSocial;

    @NotBlank
    @Email
    @Size(max = 150)
    private String email;

    @Size(max = 100)
    private String contato;

    @Valid
    private EnderecoDTO endereco;

    public EmpresaParceiraUpdateDTO() {
    }

    public String getRazaoSocial() { return razaoSocial; }
    public void setRazaoSocial(String razaoSocial) { this.razaoSocial = razaoSocial; }

    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }

    public String getContato() { return contato; }
    public void setContato(String contato) { this.contato = contato; }

    public EnderecoDTO getEndereco() { return endereco; }
    public void setEndereco(EnderecoDTO endereco) { this.endereco = endereco; }
}
