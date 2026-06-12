package com.sistemamoedaestudantil.service;

import com.sistemamoedaestudantil.domain.Aluno;
import com.sistemamoedaestudantil.domain.Professor;
import com.sistemamoedaestudantil.domain.TipoTransacao;
import com.sistemamoedaestudantil.domain.Transacao;
import com.sistemamoedaestudantil.domain.Vantagem;
import com.sistemamoedaestudantil.dto.DistribuicaoMoedasDTO;
import com.sistemamoedaestudantil.dto.ExtratoResponseDTO;
import com.sistemamoedaestudantil.dto.ResgateVantagemDTO;
import com.sistemamoedaestudantil.dto.TransacaoResponseDTO;
import com.sistemamoedaestudantil.exception.BusinessException;
import com.sistemamoedaestudantil.exception.NotFoundException;
import com.sistemamoedaestudantil.repository.AlunoRepository;
import com.sistemamoedaestudantil.repository.ProfessorRepository;
import com.sistemamoedaestudantil.repository.TransacaoRepository;
import jakarta.inject.Singleton;

import javax.transaction.Transactional;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Singleton
public class TransacaoService {

    private final TransacaoRepository transacaoRepository;
    private final AlunoRepository alunoRepository;
    private final ProfessorRepository professorRepository;
    private final VantagemService vantagemService;
    private final EmailService emailService;

    public TransacaoService(TransacaoRepository transacaoRepository,
                            AlunoRepository alunoRepository,
                            ProfessorRepository professorRepository,
                            VantagemService vantagemService,
                            EmailService emailService) {
        this.transacaoRepository = transacaoRepository;
        this.alunoRepository = alunoRepository;
        this.professorRepository = professorRepository;
        this.vantagemService = vantagemService;
        this.emailService = emailService;
    }

    @Transactional
    public TransacaoResponseDTO distribuirMoedas(Long professorId, DistribuicaoMoedasDTO dto) {
        Professor professor = professorRepository.findById(professorId)
                .orElseThrow(() -> new NotFoundException("Professor não encontrado."));
        Aluno aluno = alunoRepository.findById(dto.getAlunoId())
                .orElseThrow(() -> new NotFoundException("Aluno não encontrado."));

        int valor = dto.getValor();
        String motivo = dto.getMotivo().trim();
        if (motivo.isEmpty()) {
            throw new BusinessException("O motivo do reconhecimento é obrigatório.");
        }

        int saldoProfessor = professor.getSaldo() == null ? 0 : professor.getSaldo();
        if (saldoProfessor < valor) {
            throw new BusinessException("Saldo insuficiente para distribuir " + valor + " moeda(s).");
        }

        professor.setSaldo(saldoProfessor - valor);
        aluno.creditar(valor);
        professorRepository.update(professor);
        alunoRepository.update(aluno);

        Transacao transacao = new Transacao();
        transacao.setTipo(TipoTransacao.DISTRIBUICAO);
        transacao.setValor(valor);
        transacao.setDescricao(motivo);
        transacao.setDataHora(LocalDateTime.now());
        transacao.setProfessor(professor);
        transacao.setAluno(aluno);
        Transacao salva = transacaoRepository.save(transacao);

        emailService.enviarMoedasParaAluno(aluno.getEmail(), aluno.getNome(),
                professor.getNome(), valor, motivo);
        emailService.enviarConfirmacaoDistribuicaoProfessor(professor.getEmail(),
                professor.getNome(), aluno.getNome(), valor, motivo);

        return toResponseDTO(salva);
    }

    @Transactional
    public TransacaoResponseDTO resgatarVantagem(Long alunoId, ResgateVantagemDTO dto) {
        Aluno aluno = alunoRepository.findById(alunoId)
                .orElseThrow(() -> new NotFoundException("Aluno não encontrado."));
        Vantagem vantagem = vantagemService.buscarEntidadeAtiva(dto.getVantagemId());

        int custo = vantagem.getCustoEmMoedas();
        int saldoAluno = aluno.getSaldo() == null ? 0 : aluno.getSaldo();
        if (saldoAluno < custo) {
            throw new BusinessException("Saldo insuficiente para resgatar esta vantagem.");
        }

        aluno.debitar(custo);
        alunoRepository.update(aluno);

        String codigoCupom = gerarCodigoCupom();

        Transacao transacao = new Transacao();
        transacao.setTipo(TipoTransacao.RESGATE);
        transacao.setValor(custo);
        transacao.setDescricao("Resgate: " + vantagem.getNome());
        transacao.setDataHora(LocalDateTime.now());
        transacao.setCodigoCupom(codigoCupom);
        transacao.setCupomConferido(false);
        transacao.setAluno(aluno);
        transacao.setVantagem(vantagem);
        Transacao salva = transacaoRepository.save(transacao);

        emailService.enviarCupomAluno(aluno.getEmail(), aluno.getNome(),
                vantagem.getNome(), vantagem.getEmpresa().getRazaoSocial(), custo, codigoCupom);
        emailService.enviarCupomEmpresa(vantagem.getEmpresa().getEmail(),
                vantagem.getEmpresa().getRazaoSocial(), aluno.getNome(),
                vantagem.getNome(), custo, codigoCupom);

        return toResponseDTO(salva);
    }

    @Transactional
    public ExtratoResponseDTO extratoAluno(Long alunoId) {
        Aluno aluno = alunoRepository.findById(alunoId)
                .orElseThrow(() -> new NotFoundException("Aluno não encontrado."));

        ExtratoResponseDTO extrato = new ExtratoResponseDTO();
        extrato.setSaldo(aluno.getSaldo());
        extrato.setTransacoes(toResponseList(transacaoRepository.findByAlunoIdOrderByDataHoraDesc(alunoId)));
        return extrato;
    }

    @Transactional
    public ExtratoResponseDTO extratoProfessor(Long professorId) {
        Professor professor = professorRepository.findById(professorId)
                .orElseThrow(() -> new NotFoundException("Professor não encontrado."));

        ExtratoResponseDTO extrato = new ExtratoResponseDTO();
        extrato.setSaldo(professor.getSaldo());
        extrato.setTransacoes(toResponseList(transacaoRepository.findByProfessorIdOrderByDataHoraDesc(professorId)));
        return extrato;
    }

    @Transactional
    public List<TransacaoResponseDTO> listarResgatesEmpresa(Long empresaId) {
        return toResponseList(transacaoRepository.findResgatesByEmpresa(empresaId, TipoTransacao.RESGATE));
    }

    @Transactional
    public TransacaoResponseDTO conferirCupom(Long empresaId, String codigoCupom) {
        Transacao transacao = transacaoRepository.findByCodigoCupom(codigoCupom)
                .orElseThrow(() -> new NotFoundException("Cupom não encontrado."));

        if (transacao.getTipo() != TipoTransacao.RESGATE) {
            throw new BusinessException("Este código não corresponde a um resgate de vantagem.");
        }
        if (transacao.getVantagem() == null
                || transacao.getVantagem().getEmpresa() == null
                || !transacao.getVantagem().getEmpresa().getId().equals(empresaId)) {
            throw new BusinessException("Este cupom não pertence à sua empresa.");
        }
        if (Boolean.TRUE.equals(transacao.getCupomConferido())) {
            throw new BusinessException("Este cupom já foi conferido.");
        }

        transacao.setCupomConferido(true);
        return toResponseDTO(transacaoRepository.update(transacao));
    }

    private String gerarCodigoCupom() {
        for (int tentativa = 0; tentativa < 10; tentativa++) {
            String codigo = UUID.randomUUID().toString().replace("-", "").substring(0, 12).toUpperCase();
            if (!transacaoRepository.findByCodigoCupom(codigo).isPresent()) {
                return codigo;
            }
        }
        throw new BusinessException("Não foi possível gerar um código de cupom único.");
    }

    private List<TransacaoResponseDTO> toResponseList(List<Transacao> transacoes) {
        List<TransacaoResponseDTO> result = new ArrayList<>();
        for (Transacao t : transacoes) {
            result.add(toResponseDTO(t));
        }
        return result;
    }

    private TransacaoResponseDTO toResponseDTO(Transacao transacao) {
        TransacaoResponseDTO dto = new TransacaoResponseDTO();
        dto.setId(transacao.getId());
        dto.setTipo(transacao.getTipo());
        dto.setValor(transacao.getValor());
        dto.setDescricao(transacao.getDescricao());
        dto.setDataHora(transacao.getDataHora());
        dto.setCodigoCupom(transacao.getCodigoCupom());
        dto.setCupomConferido(transacao.getCupomConferido());

        if (transacao.getAluno() != null) {
            dto.setAlunoId(transacao.getAluno().getId());
            dto.setAlunoNome(transacao.getAluno().getNome());
        }
        if (transacao.getProfessor() != null) {
            dto.setProfessorId(transacao.getProfessor().getId());
            dto.setProfessorNome(transacao.getProfessor().getNome());
        }
        if (transacao.getVantagem() != null) {
            dto.setVantagemId(transacao.getVantagem().getId());
            dto.setVantagemNome(transacao.getVantagem().getNome());
            if (transacao.getVantagem().getEmpresa() != null) {
                dto.setEmpresaNome(transacao.getVantagem().getEmpresa().getRazaoSocial());
            }
        }
        return dto;
    }
}
