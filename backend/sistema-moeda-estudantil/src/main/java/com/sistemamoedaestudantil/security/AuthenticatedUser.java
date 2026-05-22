package com.sistemamoedaestudantil.security;

import com.sistemamoedaestudantil.domain.TipoUsuario;

public class AuthenticatedUser {

    private final Long id;
    private final TipoUsuario tipo;
    private final String email;
    private final String nome;

    public AuthenticatedUser(Long id, TipoUsuario tipo, String email, String nome) {
        this.id = id;
        this.tipo = tipo;
        this.email = email;
        this.nome = nome;
    }

    public Long getId() { return id; }
    public TipoUsuario getTipo() { return tipo; }
    public String getEmail() { return email; }
    public String getNome() { return nome; }
}
