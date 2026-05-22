package com.sistemamoedaestudantil.security;

import com.sistemamoedaestudantil.domain.TipoUsuario;
import com.sistemamoedaestudantil.exception.ForbiddenException;
import jakarta.inject.Singleton;

import java.util.Arrays;

@Singleton
public class AuthorizationService {

    public void requireRole(AuthenticatedUser user, TipoUsuario... rolesPermitidos) {
        if (user == null) {
            throw new ForbiddenException("Acesso negado.");
        }
        boolean permitido = Arrays.stream(rolesPermitidos).anyMatch(r -> r == user.getTipo());
        if (!permitido) {
            throw new ForbiddenException("Seu perfil não tem permissão para esta operação.");
        }
    }

    public void requireProfessor(AuthenticatedUser user) {
        requireRole(user, TipoUsuario.PROFESSOR);
    }

    public void requireAluno(AuthenticatedUser user) {
        requireRole(user, TipoUsuario.ALUNO);
    }

    public void requireEmpresa(AuthenticatedUser user) {
        requireRole(user, TipoUsuario.EMPRESA);
    }

    public void requireLeituraAluno(AuthenticatedUser user, Long alunoId) {
        if (user.getTipo() == TipoUsuario.PROFESSOR) {
            return;
        }
        if (user.getTipo() == TipoUsuario.ALUNO && user.getId().equals(alunoId)) {
            return;
        }
        throw new ForbiddenException("Você só pode consultar o seu próprio cadastro de aluno.");
    }

    public void requireEdicaoProprioAluno(AuthenticatedUser user, Long alunoId) {
        requireAluno(user);
        if (!user.getId().equals(alunoId)) {
            throw new ForbiddenException("Você só pode editar o seu próprio cadastro.");
        }
    }

    public void requireLeituraEmpresa(AuthenticatedUser user, Long empresaId) {
        requireEmpresa(user);
        if (!user.getId().equals(empresaId)) {
            throw new ForbiddenException("Você só pode consultar os dados da sua própria empresa.");
        }
    }

    public void requireEdicaoPropriaEmpresa(AuthenticatedUser user, Long empresaId) {
        requireLeituraEmpresa(user, empresaId);
    }

    public void denyRemocao() {
        throw new ForbiddenException("Remoção de cadastro não permitida para o seu perfil.");
    }
}
