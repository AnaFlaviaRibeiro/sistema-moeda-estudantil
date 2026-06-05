package com.sistemamoedaestudantil.service;

import com.sistemamoedaestudantil.domain.Professor;
import com.sistemamoedaestudantil.domain.TipoTransacao;
import com.sistemamoedaestudantil.domain.Transacao;
import com.sistemamoedaestudantil.repository.ProfessorRepository;
import com.sistemamoedaestudantil.repository.TransacaoRepository;
import io.micronaut.context.event.ApplicationEventListener;
import io.micronaut.runtime.server.event.ServerStartupEvent;
import jakarta.inject.Singleton;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.transaction.Transactional;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Singleton
public class CreditoSemestralService implements ApplicationEventListener<ServerStartupEvent> {

    private static final Logger LOG = LoggerFactory.getLogger(CreditoSemestralService.class);
    private static final int CREDITO_SEMESTRAL = 1000;

    private final ProfessorRepository professorRepository;
    private final TransacaoRepository transacaoRepository;

    public CreditoSemestralService(ProfessorRepository professorRepository,
                                   TransacaoRepository transacaoRepository) {
        this.professorRepository = professorRepository;
        this.transacaoRepository = transacaoRepository;
    }

    @Override
    public void onApplicationEvent(ServerStartupEvent event) {
        if (deveAplicarCreditoSemestral()) {
            int creditados = aplicarCreditoSemestral();
            LOG.info("Crédito semestral automático: {} professor(es) creditado(s).", creditados);
        }
    }

    private boolean deveAplicarCreditoSemestral() {
        LocalDateTime agora = LocalDateTime.now();
        return agora.getDayOfMonth() == 1 && (agora.getMonthValue() == 1 || agora.getMonthValue() == 7);
    }

    @Transactional
    public int aplicarCreditoSemestral() {
        List<Professor> professores = new ArrayList<>();
        for (Professor p : professorRepository.findAll()) {
            professores.add(p);
        }

        int creditados = 0;
        for (Professor professor : professores) {
            if (jaCreditadoNoSemestreAtual(professor.getId())) {
                continue;
            }

            int saldoAtual = professor.getSaldo() == null ? 0 : professor.getSaldo();
            professor.setSaldo(saldoAtual + CREDITO_SEMESTRAL);
            professorRepository.update(professor);

            Transacao transacao = new Transacao();
            transacao.setTipo(TipoTransacao.CREDITO_SEMESTRAL);
            transacao.setValor(CREDITO_SEMESTRAL);
            transacao.setDescricao("Crédito semestral de " + CREDITO_SEMESTRAL + " moedas");
            transacao.setDataHora(LocalDateTime.now());
            transacao.setProfessor(professor);
            transacaoRepository.save(transacao);
            creditados++;
        }

        return creditados;
    }

    private boolean jaCreditadoNoSemestreAtual(Long professorId) {
        int mes = LocalDateTime.now().getMonthValue();
        int ano = LocalDateTime.now().getYear();
        LocalDateTime inicioSemestre = mes <= 6
                ? LocalDateTime.of(ano, 1, 1, 0, 0)
                : LocalDateTime.of(ano, 7, 1, 0, 0);

        List<Transacao> transacoes = transacaoRepository.findByProfessorIdOrderByDataHoraDesc(professorId);
        for (Transacao t : transacoes) {
            if (t.getTipo() == TipoTransacao.CREDITO_SEMESTRAL
                    && t.getDataHora() != null
                    && !t.getDataHora().isBefore(inicioSemestre)) {
                return true;
            }
        }
        return false;
    }
}
