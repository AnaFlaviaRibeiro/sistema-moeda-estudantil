package com.sistemamoedaestudantil.dto;

import io.micronaut.core.annotation.Introspected;
import io.micronaut.serde.annotation.Serdeable;

import javax.validation.constraints.NotNull;

@Serdeable
@Introspected
public class ResgateVantagemDTO {

    @NotNull
    private Long vantagemId;

    public Long getVantagemId() { return vantagemId; }
    public void setVantagemId(Long vantagemId) { this.vantagemId = vantagemId; }
}
