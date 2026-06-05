# Diagrama de Sequência — Extrato do Professor (HU-10)

**Caso de uso:** Como professor, consultar meu saldo e os envios de moedas realizados.

**Atores:** Professor  
**Release:** 2

---

## Diagrama de Sequência

```mermaid
sequenceDiagram
    autonumber
    actor Prof as Professor
    participant View as ExtratoProfessorView<br/>(Vue 3)
    participant API as transacoesApi
    participant Filter as AuthFilter
    participant Ctrl as TransacaoController
    participant Auth as AuthorizationService
    participant Svc as TransacaoService
    participant ProfRepo as ProfessorRepository
    participant TransRepo as TransacaoRepository
    participant DB as PostgreSQL

    Prof->>View: Acessa /professor/extrato

    View->>API: extratoProfessor()
    API->>Ctrl: GET /api/transacoes/extrato/professor/me<br/>Authorization: Bearer JWT

    Ctrl->>Filter: Valida token JWT
    Ctrl->>Auth: requireProfessor(user)
    Ctrl->>Svc: extratoProfessor(professorId)

    Svc->>ProfRepo: findById(professorId)
    ProfRepo->>DB: SELECT professor
    DB-->>Svc: Professor (saldo)

    Svc->>TransRepo: findByProfessorIdOrderByDataHoraDesc(professorId)
    TransRepo->>DB: SELECT transacao WHERE professor_id
    DB-->>Svc: Lista de transações

    Svc->>Svc: Monta ExtratoResponseDTO
    Svc-->>Ctrl: ExtratoResponseDTO
    Ctrl-->>API: HTTP 200 + JSON
    API-->>View: Extrato (saldo + transações)

    View-->>Prof: Exibe saldo e tabela de envios
```

---

## Implementação

| Camada | Artefato |
|--------|----------|
| Frontend | `views/professor/ExtratoProfessorView.vue`, rota `/professor/extrato` |
| API | `transacoesApi.extratoProfessor()` → `GET /api/transacoes/extrato/professor/me` |
| Backend | `TransacaoService.extratoProfessor()` |
