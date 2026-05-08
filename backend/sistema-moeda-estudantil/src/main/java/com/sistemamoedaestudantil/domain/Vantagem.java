package com.sistemamoedaestudantil.domain;

import io.micronaut.core.annotation.Introspected;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

@Entity
@Table(name = "vantagem")
@Introspected
public class Vantagem {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 150)
    private String nome;

    @Column(length = 1000)
    private String descricao;

    @Column(name = "foto_url", length = 500)
    private String fotoUrl;

    @Column(name = "custo_em_moedas", nullable = false)
    private Integer custoEmMoedas;

    @Column(nullable = false)
    private Boolean ativa = Boolean.TRUE;

    @ManyToOne(fetch = FetchType.EAGER, optional = false)
    @JoinColumn(name = "empresa_id", nullable = false)
    private EmpresaParceira empresa;

    public Vantagem() {
    }

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getNome() { return nome; }
    public void setNome(String nome) { this.nome = nome; }

    public String getDescricao() { return descricao; }
    public void setDescricao(String descricao) { this.descricao = descricao; }

    public String getFotoUrl() { return fotoUrl; }
    public void setFotoUrl(String fotoUrl) { this.fotoUrl = fotoUrl; }

    public Integer getCustoEmMoedas() { return custoEmMoedas; }
    public void setCustoEmMoedas(Integer custoEmMoedas) { this.custoEmMoedas = custoEmMoedas; }

    public Boolean getAtiva() { return ativa; }
    public void setAtiva(Boolean ativa) { this.ativa = ativa; }

    public EmpresaParceira getEmpresa() { return empresa; }
    public void setEmpresa(EmpresaParceira empresa) { this.empresa = empresa; }
}
