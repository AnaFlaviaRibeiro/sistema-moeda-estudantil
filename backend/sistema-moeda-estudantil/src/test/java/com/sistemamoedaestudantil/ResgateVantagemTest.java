package com.sistemamoedaestudantil;

import com.sistemamoedaestudantil.domain.TipoTransacao;
import com.sistemamoedaestudantil.dto.ExtratoResponseDTO;
import com.sistemamoedaestudantil.dto.LoginRequestDTO;
import com.sistemamoedaestudantil.dto.ResgateVantagemDTO;
import com.sistemamoedaestudantil.dto.TransacaoResponseDTO;
import com.sistemamoedaestudantil.dto.VantagemResponseDTO;
import io.micronaut.core.type.Argument;
import io.micronaut.http.HttpRequest;
import io.micronaut.http.HttpResponse;
import io.micronaut.http.HttpStatus;
import io.micronaut.http.client.HttpClient;
import io.micronaut.http.client.annotation.Client;
import io.micronaut.http.client.exceptions.HttpClientResponseException;
import io.micronaut.test.extensions.junit5.annotation.MicronautTest;
import jakarta.inject.Inject;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

/**
 * Lab04S03 — troca de vantagens pelo aluno (HU-08).
 */
@MicronautTest(environments = "test")
class ResgateVantagemTest {

    @Inject
    @Client("/")
    HttpClient client;

    private String loginToken(String email) {
        LoginRequestDTO dto = new LoginRequestDTO();
        dto.setEmail(email);
        dto.setSenha("changeit");
        HttpResponse<java.util.Map> response = client.toBlocking().exchange(
                HttpRequest.POST("/api/auth/login", dto), java.util.Map.class);
        return (String) response.body().get("token");
    }

    @Test
    void alunoResgataVantagemDebitaSaldoEGeraCupom() {
        String token = loginToken("ana.lima@aluno.puc.example");

        ExtratoResponseDTO extratoAntes = client.toBlocking().retrieve(
                HttpRequest.GET("/api/transacoes/extrato/aluno/me")
                        .header("Authorization", "Bearer " + token),
                ExtratoResponseDTO.class);
        int saldoAntes = extratoAntes.getSaldo();

        List<VantagemResponseDTO> vantagens = client.toBlocking().retrieve(
                HttpRequest.GET("/api/vantagens").header("Authorization", "Bearer " + token),
                Argument.listOf(VantagemResponseDTO.class));
        assertFalse(vantagens.isEmpty());

        VantagemResponseDTO vantagem = vantagens.get(0);
        assertTrue(saldoAntes >= vantagem.getCustoEmMoedas());

        ResgateVantagemDTO resgate = new ResgateVantagemDTO();
        resgate.setVantagemId(vantagem.getId());

        HttpResponse<TransacaoResponseDTO> response = client.toBlocking().exchange(
                HttpRequest.POST("/api/transacoes/resgatar", resgate)
                        .header("Authorization", "Bearer " + token),
                TransacaoResponseDTO.class);

        assertEquals(HttpStatus.CREATED, response.getStatus());
        TransacaoResponseDTO transacao = response.body();
        assertNotNull(transacao);
        assertEquals(TipoTransacao.RESGATE, transacao.getTipo());
        assertEquals(vantagem.getCustoEmMoedas(), transacao.getValor());
        assertEquals(vantagem.getId(), transacao.getVantagemId());
        assertNotNull(transacao.getCodigoCupom());
        assertEquals(12, transacao.getCodigoCupom().length());
        assertFalse(Boolean.TRUE.equals(transacao.getCupomConferido()));

        ExtratoResponseDTO extratoDepois = client.toBlocking().retrieve(
                HttpRequest.GET("/api/transacoes/extrato/aluno/me")
                        .header("Authorization", "Bearer " + token),
                ExtratoResponseDTO.class);
        assertEquals(saldoAntes - vantagem.getCustoEmMoedas(), extratoDepois.getSaldo());
        assertTrue(extratoDepois.getTransacoes().stream()
                .anyMatch(t -> transacao.getCodigoCupom().equals(t.getCodigoCupom())));
    }

    @Test
    void resgateComSaldoInsuficienteRetorna422() {
        String token = loginToken("diego.martins@aluno.ufmg.example");

        List<VantagemResponseDTO> vantagens = client.toBlocking().retrieve(
                HttpRequest.GET("/api/vantagens").header("Authorization", "Bearer " + token),
                Argument.listOf(VantagemResponseDTO.class));
        assertFalse(vantagens.isEmpty());

        ResgateVantagemDTO resgate = new ResgateVantagemDTO();
        resgate.setVantagemId(vantagens.get(0).getId());

        HttpClientResponseException ex = assertThrows(HttpClientResponseException.class, () ->
                client.toBlocking().exchange(
                        HttpRequest.POST("/api/transacoes/resgatar", resgate)
                                .header("Authorization", "Bearer " + token),
                        TransacaoResponseDTO.class));

        assertEquals(HttpStatus.UNPROCESSABLE_ENTITY, ex.getStatus());
    }

    @Test
    void professorNaoPodeResgatarVantagem() {
        String token = loginToken("joao.silva@puc.example");

        ResgateVantagemDTO resgate = new ResgateVantagemDTO();
        resgate.setVantagemId(1L);

        HttpClientResponseException ex = assertThrows(HttpClientResponseException.class, () ->
                client.toBlocking().exchange(
                        HttpRequest.POST("/api/transacoes/resgatar", resgate)
                                .header("Authorization", "Bearer " + token),
                        TransacaoResponseDTO.class));

        assertEquals(HttpStatus.FORBIDDEN, ex.getStatus());
    }

    @Test
    void empresaConferirCupomAposResgate() {
        String tokenAluno = loginToken("bruno.costa@aluno.puc.example");
        String tokenEmpresa = loginToken("contato@saborecia.example");

        List<VantagemResponseDTO> vantagens = client.toBlocking().retrieve(
                HttpRequest.GET("/api/vantagens").header("Authorization", "Bearer " + tokenAluno),
                Argument.listOf(VantagemResponseDTO.class));

        VantagemResponseDTO vantagem = vantagens.stream()
                .filter(v -> "Restaurante Universitário Sabor & Cia Ltda".equals(v.getEmpresaNome()))
                .findFirst()
                .orElse(vantagens.get(0));

        ResgateVantagemDTO resgate = new ResgateVantagemDTO();
        resgate.setVantagemId(vantagem.getId());

        TransacaoResponseDTO criada = client.toBlocking().retrieve(
                HttpRequest.POST("/api/transacoes/resgatar", resgate)
                        .header("Authorization", "Bearer " + tokenAluno),
                TransacaoResponseDTO.class);

        List<TransacaoResponseDTO> resgatesEmpresa = client.toBlocking().retrieve(
                HttpRequest.GET("/api/transacoes/resgates/empresa")
                        .header("Authorization", "Bearer " + tokenEmpresa),
                Argument.listOf(TransacaoResponseDTO.class));

        assertTrue(resgatesEmpresa.stream()
                .anyMatch(r -> criada.getCodigoCupom().equals(r.getCodigoCupom())));

        TransacaoResponseDTO conferida = client.toBlocking().retrieve(
                HttpRequest.PUT("/api/transacoes/cupons/" + criada.getCodigoCupom() + "/conferir", null)
                        .header("Authorization", "Bearer " + tokenEmpresa),
                TransacaoResponseDTO.class);

        assertTrue(Boolean.TRUE.equals(conferida.getCupomConferido()));
    }
}
