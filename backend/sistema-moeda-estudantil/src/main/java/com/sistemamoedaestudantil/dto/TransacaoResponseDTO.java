package com.sistemamoedaestudantil.dto;

import com.sistemamoedaestudantil.domain.TipoTransacao;
import io.micronaut.core.annotation.Introspected;
import io.micronaut.serde.annotation.Serdeable;

import java.time.LocalDateTime;

@Serdeable
@Introspected
public class TransacaoResponseDTO {

    private Long id;
    private TipoTransacao tipo;
    private Integer valor;
    private String descricao;
    private LocalDateTime dataHora;
    private String codigoCupom;
    private Boolean cupomConferido;
    private Long alunoId;
    private String alunoNome;
    private Long professorId;
    private String professorNome;
    private Long vantagemId;
    private String vantagemNome;
    private String empresaNome;

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public TipoTransacao getTipo() { return tipo; }
    public void setTipo(TipoTransacao tipo) { this.tipo = tipo; }

    public Integer getValor() { return valor; }
    public void setValor(Integer valor) { this.valor = valor; }

    public String getDescricao() { return descricao; }
    public void setDescricao(String descricao) { this.descricao = descricao; }

    public LocalDateTime getDataHora() { return dataHora; }
    public void setDataHora(LocalDateTime dataHora) { this.dataHora = dataHora; }

    public String getCodigoCupom() { return codigoCupom; }
    public void setCodigoCupom(String codigoCupom) { this.codigoCupom = codigoCupom; }

    public Boolean getCupomConferido() { return cupomConferido; }
    public void setCupomConferido(Boolean cupomConferido) { this.cupomConferido = cupomConferido; }

    public Long getAlunoId() { return alunoId; }
    public void setAlunoId(Long alunoId) { this.alunoId = alunoId; }

    public String getAlunoNome() { return alunoNome; }
    public void setAlunoNome(String alunoNome) { this.alunoNome = alunoNome; }

    public Long getProfessorId() { return professorId; }
    public void setProfessorId(Long professorId) { this.professorId = professorId; }

    public String getProfessorNome() { return professorNome; }
    public void setProfessorNome(String professorNome) { this.professorNome = professorNome; }

    public Long getVantagemId() { return vantagemId; }
    public void setVantagemId(Long vantagemId) { this.vantagemId = vantagemId; }

    public String getVantagemNome() { return vantagemNome; }
    public void setVantagemNome(String vantagemNome) { this.vantagemNome = vantagemNome; }

    public String getEmpresaNome() { return empresaNome; }
    public void setEmpresaNome(String empresaNome) { this.empresaNome = empresaNome; }
}
