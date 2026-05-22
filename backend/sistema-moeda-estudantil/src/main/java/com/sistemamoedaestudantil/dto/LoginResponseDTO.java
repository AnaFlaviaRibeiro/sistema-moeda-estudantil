package com.sistemamoedaestudantil.dto;

import com.sistemamoedaestudantil.domain.TipoUsuario;
import io.micronaut.core.annotation.Introspected;
import io.micronaut.serde.annotation.Serdeable;

@Serdeable
@Introspected
public class LoginResponseDTO {

    private String token;
    private String tipo;
    private Long id;
    private String nome;
    private String email;

    public LoginResponseDTO() {
    }

    public LoginResponseDTO(String token, TipoUsuario tipo, Long id, String nome, String email) {
        this.token = token;
        this.tipo = tipo.name();
        this.id = id;
        this.nome = nome;
        this.email = email;
    }

    public String getToken() { return token; }
    public void setToken(String token) { this.token = token; }

    public String getTipo() { return tipo; }
    public void setTipo(String tipo) { this.tipo = tipo; }

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getNome() { return nome; }
    public void setNome(String nome) { this.nome = nome; }

    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }
}
