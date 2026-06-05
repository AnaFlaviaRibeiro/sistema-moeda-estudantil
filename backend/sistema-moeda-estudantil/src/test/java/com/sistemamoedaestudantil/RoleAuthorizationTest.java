package com.sistemamoedaestudantil;

import com.sistemamoedaestudantil.dto.LoginRequestDTO;
import io.micronaut.http.HttpRequest;
import io.micronaut.http.HttpStatus;
import io.micronaut.http.client.HttpClient;
import io.micronaut.http.client.annotation.Client;
import io.micronaut.http.client.exceptions.HttpClientResponseException;
import io.micronaut.test.extensions.junit5.annotation.MicronautTest;
import org.junit.jupiter.api.Test;

import jakarta.inject.Inject;

import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

@MicronautTest(environments = "test")
class RoleAuthorizationTest {

    @Inject
    @Client("/")
    HttpClient client;

    private String tokenAluno() {
        return loginToken("ana.lima@aluno.puc.example", "changeit");
    }

    private String tokenProfessor() {
        return loginToken("joao.silva@puc.example", "changeit");
    }

    private String tokenEmpresa() {
        return loginToken("contato@saborecia.example", "changeit");
    }

    private String loginToken(String email, String senha) {
        LoginRequestDTO dto = new LoginRequestDTO();
        dto.setEmail(email);
        dto.setSenha(senha);
        io.micronaut.http.HttpResponse<Map> response = client.toBlocking().exchange(
                HttpRequest.POST("/api/auth/login", dto), Map.class);
        return (String) response.body().get("token");
    }

    @Test
    void alunoNaoPodeListarTodosAlunos() {
        HttpClientResponseException ex = assertThrows(HttpClientResponseException.class, () ->
                client.toBlocking().retrieve(
                        HttpRequest.GET("/api/alunos").header("Authorization", "Bearer " + tokenAluno()),
                        String.class
                )
        );
        assertEquals(HttpStatus.FORBIDDEN, ex.getStatus());
    }

    @Test
    void professorPodeListarAlunos() {
        HttpStatus status = client.toBlocking().exchange(
                HttpRequest.GET("/api/alunos").header("Authorization", "Bearer " + tokenProfessor()),
                String.class
        ).getStatus();
        assertEquals(HttpStatus.OK, status);
    }

    @Test
    void empresaNaoPodeListarAlunos() {
        HttpClientResponseException ex = assertThrows(HttpClientResponseException.class, () ->
                client.toBlocking().retrieve(
                        HttpRequest.GET("/api/alunos").header("Authorization", "Bearer " + tokenEmpresa()),
                        String.class
                )
        );
        assertEquals(HttpStatus.FORBIDDEN, ex.getStatus());
    }

    @Test
    void alunoPodeConsultarProprioPerfil() {
        HttpStatus status = client.toBlocking().exchange(
                HttpRequest.GET("/api/alunos/me").header("Authorization", "Bearer " + tokenAluno()),
                String.class
        ).getStatus();
        assertEquals(HttpStatus.OK, status);
    }

    @Test
    void empresaPodeConsultarPropriaEmpresa() {
        HttpStatus status = client.toBlocking().exchange(
                HttpRequest.GET("/api/empresas/me").header("Authorization", "Bearer " + tokenEmpresa()),
                String.class
        ).getStatus();
        assertEquals(HttpStatus.OK, status);
    }
}
