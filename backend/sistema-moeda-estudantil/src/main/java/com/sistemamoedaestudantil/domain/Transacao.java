package com.sistemamoedaestudantil.domain;

import io.micronaut.core.annotation.Introspected;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import java.time.LocalDateTime;

@Entity
@Table(name = "transacao")
@Introspected
public class Transacao {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 30)
    private TipoTransacao tipo;

    @Column(nullable = false)
    private Integer valor;

    @Column(length = 500)
    private String descricao;

    @Column(name = "data_hora", nullable = false)
    private LocalDateTime dataHora = LocalDateTime.now();

    @Column(name = "codigo_cupom", length = 40)
    private String codigoCupom;

    @Column(name = "cupom_conferido")
    private Boolean cupomConferido = Boolean.FALSE;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "aluno_id")
    private Aluno aluno;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "professor_id")
    private Professor professor;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "vantagem_id")
    private Vantagem vantagem;

    public Transacao() {
    }

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

    public Aluno getAluno() { return aluno; }
    public void setAluno(Aluno aluno) { this.aluno = aluno; }

    public Professor getProfessor() { return professor; }
    public void setProfessor(Professor professor) { this.professor = professor; }

    public Vantagem getVantagem() { return vantagem; }
    public void setVantagem(Vantagem vantagem) { this.vantagem = vantagem; }
}
