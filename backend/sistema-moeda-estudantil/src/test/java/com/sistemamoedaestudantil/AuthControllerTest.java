package com.sistemamoedaestudantil;

import com.sistemamoedaestudantil.dto.LoginRequestDTO;
import com.sistemamoedaestudantil.dto.LoginResponseDTO;
import io.micronaut.http.HttpRequest;
import io.micronaut.http.HttpStatus;
import io.micronaut.http.client.HttpClient;
import io.micronaut.http.client.annotation.Client;
import io.micronaut.http.client.exceptions.HttpClientResponseException;
import io.micronaut.test.extensions.junit5.annotation.MicronautTest;
import org.junit.jupiter.api.Test;

import jakarta.inject.Inject;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;

@MicronautTest
class AuthControllerTest {

    @Inject
    @Client("/")
    HttpClient client;

    @Test
    void loginComCredenciaisValidasRetornaToken() {
        LoginRequestDTO dto = new LoginRequestDTO();
        dto.setEmail("ana.lima@aluno.puc.example");
        dto.setSenha("changeit");

        LoginResponseDTO body = client.toBlocking().retrieve(
                HttpRequest.POST("/api/auth/login", dto),
                LoginResponseDTO.class
        );

        assertNotNull(body.getToken());
        assertEquals("ALUNO", body.getTipo());
        assertEquals("Ana Beatriz Lima", body.getNome());
    }

    @Test
    void loginComSenhaInvalidaRetorna401() {
        LoginRequestDTO dto = new LoginRequestDTO();
        dto.setEmail("ana.lima@aluno.puc.example");
        dto.setSenha("senhaerrada");

        HttpClientResponseException ex = assertThrows(HttpClientResponseException.class, () ->
                client.toBlocking().retrieve(HttpRequest.POST("/api/auth/login", dto), LoginResponseDTO.class)
        );

        assertEquals(HttpStatus.UNAUTHORIZED, ex.getStatus());
    }

    @Test
    void endpointProtegidoSemTokenRetorna401() {
        HttpClientResponseException ex = assertThrows(HttpClientResponseException.class, () ->
                client.toBlocking().retrieve(HttpRequest.GET("/api/alunos"), String.class)
        );

        assertEquals(HttpStatus.UNAUTHORIZED, ex.getStatus());
    }
}
