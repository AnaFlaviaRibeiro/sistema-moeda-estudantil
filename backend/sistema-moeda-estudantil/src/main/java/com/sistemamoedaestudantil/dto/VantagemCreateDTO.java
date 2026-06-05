package com.sistemamoedaestudantil.dto;

import io.micronaut.core.annotation.Introspected;
import io.micronaut.serde.annotation.Serdeable;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Serdeable
@Introspected
public class VantagemCreateDTO {

    @NotBlank
    private String nome;

    private String descricao;

    private String fotoUrl;

    @NotNull
    @Min(1)
    private Integer custoEmMoedas;

    public String getNome() { return nome; }
    public void setNome(String nome) { this.nome = nome; }

    public String getDescricao() { return descricao; }
    public void setDescricao(String descricao) { this.descricao = descricao; }

    public String getFotoUrl() { return fotoUrl; }
    public void setFotoUrl(String fotoUrl) { this.fotoUrl = fotoUrl; }

    public Integer getCustoEmMoedas() { return custoEmMoedas; }
    public void setCustoEmMoedas(Integer custoEmMoedas) { this.custoEmMoedas = custoEmMoedas; }
}
