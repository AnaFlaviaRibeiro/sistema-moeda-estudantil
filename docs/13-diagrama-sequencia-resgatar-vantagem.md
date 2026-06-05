# Diagrama de Sequência — Resgatar Vantagem (HU-08)

**Caso de uso:** Como aluno, trocar moedas por uma vantagem cadastrada.

**Atores:** Aluno  
**Release:** 2

---

## Diagrama de Sequência

```mermaid
sequenceDiagram
    autonumber
    actor Aluno as Aluno
    participant View as VantagensListView<br/>(Vue 3)
    participant API as transacoesApi
    participant Filter as AuthFilter
    participant Ctrl as TransacaoController
    participant Auth as AuthorizationService
    participant Svc as TransacaoService
    participant VantSvc as VantagemService
    participant Email as EmailService
    participant AlunoRepo as AlunoRepository
    participant TransRepo as TransacaoRepository
    participant DB as PostgreSQL

    Aluno->>View: Clica "Resgatar" em uma vantagem
    View->>API: resgatar({ vantagemId })

    API->>Ctrl: POST /api/transacoes/resgatar<br/>Authorization: Bearer JWT

    Ctrl->>Filter: Valida token JWT
    Ctrl->>Auth: requireAluno(user)
    Ctrl->>Svc: resgatarVantagem(alunoId, dto)

    Svc->>AlunoRepo: findById(alunoId)
    Svc->>VantSvc: buscarEntidadeAtiva(vantagemId)
    Svc->>Svc: Valida saldo suficiente

    Svc->>Svc: aluno.debitar(custo)
    Svc->>AlunoRepo: update(aluno)
    Svc->>Svc: Gera codigoCupom único

    Svc->>TransRepo: save(Transacao RESGATE)
    TransRepo->>DB: INSERT transacao + UPDATE saldo aluno

    Svc->>Email: enviarCupomAluno(...)
    Svc->>Email: enviarCupomEmpresa(...)

    Svc-->>Ctrl: TransacaoResponseDTO (com codigoCupom)
    Ctrl-->>API: HTTP 201 Created
    API-->>View: TransacaoResponseDTO
    View-->>Aluno: Exibe código do cupom
```

---

## Implementação

| Camada | Artefato |
|--------|----------|
| Frontend | `views/aluno/VantagensListView.vue` |
| API | `transacoesApi.resgatar()` → `POST /api/transacoes/resgatar` |
| Backend | `TransacaoService.resgatarVantagem()`, `EmailService` |
