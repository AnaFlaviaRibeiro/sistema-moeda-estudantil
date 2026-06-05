package com.sistemamoedaestudantil.dto;

import io.micronaut.core.annotation.Introspected;
import io.micronaut.serde.annotation.Serdeable;

import java.util.ArrayList;
import java.util.List;

@Serdeable
@Introspected
public class ExtratoResponseDTO {

    private Integer saldo;
    private List<TransacaoResponseDTO> transacoes = new ArrayList<>();

    public Integer getSaldo() { return saldo; }
    public void setSaldo(Integer saldo) { this.saldo = saldo; }

    public List<TransacaoResponseDTO> getTransacoes() { return transacoes; }
    public void setTransacoes(List<TransacaoResponseDTO> transacoes) { this.transacoes = transacoes; }
}
