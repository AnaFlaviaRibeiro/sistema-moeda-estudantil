# Diagrama de Sequência — Distribuir Moedas (HU-05)

**Caso de uso:** Como professor, enviar moedas a um aluno informando um motivo.

**Atores:** Professor  
**Release:** 2

---

## Diagrama de Sequência

```mermaid
sequenceDiagram
    autonumber
    actor Prof as Professor
    participant View as AlunosListView<br/>(Vue 3)
    participant API as transacoesApi
    participant Filter as AuthFilter
    participant Ctrl as TransacaoController
    participant Auth as AuthorizationService
    participant Svc as TransacaoService
    participant Email as EmailService
    participant ProfRepo as ProfessorRepository
    participant AlunoRepo as AlunoRepository
    participant TransRepo as TransacaoRepository
    participant DB as PostgreSQL

    Prof->>View: Clica "Enviar moedas" em um aluno
    Prof->>View: Informa valor e motivo (obrigatório)
    View->>API: distribuir({ alunoId, valor, motivo })

    API->>Ctrl: POST /api/transacoes/distribuir<br/>Authorization: Bearer JWT

    Ctrl->>Filter: Valida token JWT
    Ctrl->>Auth: requireProfessor(user)
    Ctrl->>Svc: distribuirMoedas(professorId, dto)

    Svc->>ProfRepo: findById(professorId)
    Svc->>AlunoRepo: findById(alunoId)
    Svc->>Svc: Valida saldo e motivo

    Svc->>Svc: Debita professor e credita aluno
    Svc->>ProfRepo: update(professor)
    Svc->>AlunoRepo: update(aluno)
    Svc->>TransRepo: save(Transacao DISTRIBUICAO)
    TransRepo->>DB: INSERT transacao + UPDATE saldos

    Svc->>Email: enviarMoedasParaAluno(...)
    Svc->>Email: enviarConfirmacaoDistribuicaoProfessor(...)

    Svc-->>Ctrl: TransacaoResponseDTO
    Ctrl-->>API: HTTP 201 Created
    API-->>View: Transação criada
    View-->>Prof: Confirmação de envio
```

---

## Implementação

| Camada | Artefato |
|--------|----------|
| Frontend | `views/alunos/AlunosListView.vue` (modal de distribuição) |
| API | `transacoesApi.distribuir()` → `POST /api/transacoes/distribuir` |
| Backend | `TransacaoService.distribuirMoedas()`, `EmailService` |
