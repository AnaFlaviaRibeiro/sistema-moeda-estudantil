# Diagrama de Sequência — Extrato do Aluno (HU-09)

**Caso de uso:** Como aluno, consultar meu saldo e histórico de recebimentos e resgates.

**Atores:** Aluno  
**Release:** 2

---

## Diagrama de Sequência

```mermaid
sequenceDiagram
    autonumber
    actor Aluno as Aluno
    participant View as ExtratoAlunoView<br/>(Vue 3)
    participant API as transacoesApi
    participant Filter as AuthFilter
    participant Ctrl as TransacaoController
    participant Auth as AuthorizationService
    participant Svc as TransacaoService
    participant AlunoRepo as AlunoRepository
    participant TransRepo as TransacaoRepository
    participant DB as PostgreSQL

    Aluno->>View: Acessa /extrato

    View->>API: extratoAluno()
    API->>Ctrl: GET /api/transacoes/extrato/aluno/me<br/>Authorization: Bearer JWT

    Ctrl->>Filter: Valida token JWT
    Ctrl->>Auth: requireAluno(user)
    Ctrl->>Svc: extratoAluno(alunoId)

    Svc->>AlunoRepo: findById(alunoId)
    AlunoRepo->>DB: SELECT aluno
    DB-->>Svc: Aluno (saldo)

    Svc->>TransRepo: findByAlunoIdOrderByDataHoraDesc(alunoId)
    TransRepo->>DB: SELECT transacao WHERE aluno_id
    DB-->>Svc: Lista de transações

    Svc->>Svc: Monta ExtratoResponseDTO
    Svc-->>Ctrl: ExtratoResponseDTO
    Ctrl-->>API: HTTP 200 + JSON
    API-->>View: Extrato (saldo + transações)

    View-->>Aluno: Exibe saldo e tabela de histórico
```

---

## Implementação

| Camada | Artefato |
|--------|----------|
| Frontend | `views/aluno/ExtratoAlunoView.vue`, rota `/extrato` |
| API | `transacoesApi.extratoAluno()` → `GET /api/transacoes/extrato/aluno/me` |
| Backend | `TransacaoService.extratoAluno()` |
