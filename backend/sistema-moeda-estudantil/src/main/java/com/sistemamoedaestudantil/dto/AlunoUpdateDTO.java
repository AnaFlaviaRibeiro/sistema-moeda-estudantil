package com.sistemamoedaestudantil.dto;

import io.micronaut.core.annotation.Introspected;
import io.micronaut.serde.annotation.Serdeable;

import javax.validation.Valid;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

@Serdeable
@Introspected
public class AlunoUpdateDTO {

    @NotBlank
    @Size(max = 150)
    private String nome;

    @NotBlank
    @Email
    @Size(max = 150)
    private String email;

    @Size(max = 20)
    private String rg;

    @NotBlank
    @Size(max = 100)
    private String curso;

    @NotNull
    private Long instituicaoId;

    @Valid
    private EnderecoDTO endereco;

    public AlunoUpdateDTO() {
    }

    public String getNome() { return nome; }
    public void setNome(String nome) { this.nome = nome; }

    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }

    public String getRg() { return rg; }
    public void setRg(String rg) { this.rg = rg; }

    public String getCurso() { return curso; }
    public void setCurso(String curso) { this.curso = curso; }

    public Long getInstituicaoId() { return instituicaoId; }
    public void setInstituicaoId(Long instituicaoId) { this.instituicaoId = instituicaoId; }

    public EnderecoDTO getEndereco() { return endereco; }
    public void setEndereco(EnderecoDTO endereco) { this.endereco = endereco; }
}
