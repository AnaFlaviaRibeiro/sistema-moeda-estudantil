# Diagrama de Sequência — Gerenciar Cadastro de Aluno (HU-11)

**Caso de uso:** Como professor ou o próprio aluno, consultar e atualizar registros de alunos.

**Atores:** Professor (listagem) / Aluno (edição do próprio perfil)  
**Release:** 1

> Este diagrama representa a **consulta da lista de alunos pelo professor**. O cadastro inicial é HU-01; a edição do próprio perfil usa `PUT /api/alunos/{id}`.

---

## Diagrama de Sequência

```mermaid
sequenceDiagram
    autonumber
    actor Prof as Professor
    participant View as AlunosListView<br/>(Vue 3)
    participant API as alunosApi
    participant Filter as AuthFilter
    participant Ctrl as AlunoController
    participant Auth as AuthorizationService
    participant Svc as AlunoService
    participant Repo as AlunoRepository
    participant DB as PostgreSQL

    Prof->>View: Acessa /alunos

    View->>API: list()
    API->>Ctrl: GET /api/alunos<br/>Authorization: Bearer JWT

    Ctrl->>Filter: Valida token JWT
    Ctrl->>Auth: requireProfessor(user)
    Auth-->>Ctrl: OK

    Ctrl->>Svc: listar()
    Svc->>Repo: findAll()
    Repo->>DB: SELECT aluno
    DB-->>Repo: Lista de alunos
    Repo-->>Svc: List<Aluno>

    Svc->>Svc: toResponseDTO (cada aluno)
    Svc-->>Ctrl: List<AlunoResponseDTO>
    Ctrl-->>API: HTTP 200 + JSON
    API-->>View: Aluno[]

    View-->>Prof: Tabela com nome, curso, instituição e saldo
```

---

## Implementação

| Camada | Artefato |
|--------|----------|
| Frontend — listagem | `views/alunos/AlunosListView.vue` |
| Frontend — edição | `views/alunos/AlunoFormView.vue` → `PUT /api/alunos/{id}` |
| API | `alunosApi.list()`, `alunosApi.update()` |
| Backend | `AlunoController`, `AlunoService`, `AuthorizationService` |
