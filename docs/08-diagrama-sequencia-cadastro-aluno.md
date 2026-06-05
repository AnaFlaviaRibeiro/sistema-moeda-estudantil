# Diagrama de Sequência — Cadastro de Aluno (HU-01)

**Caso de uso:** Como aluno, cadastrar meus dados para participar do programa de mérito.

**Atores:** Aluno (visitante)  
**Release:** 1

---

## Diagrama de Sequência

```mermaid
sequenceDiagram
    autonumber
    actor Aluno as Aluno
    participant View as AlunoFormView<br/>(Vue 3)
    participant InstAPI as instituicoesApi
    participant AlunoAPI as alunosApi
    participant Ctrl as AlunoController
    participant Svc as AlunoService
    participant InstRepo as InstituicaoRepository
    participant AlunoRepo as AlunoRepository
    participant DB as PostgreSQL

    Aluno->>View: Acessa /alunos/novo
    View->>InstAPI: list()
    InstAPI->>Ctrl: GET /api/instituicoes (público)
    Ctrl-->>View: Lista de instituições

    Aluno->>View: Preenche dados e seleciona instituição
    Aluno->>View: Submete formulário

    View->>AlunoAPI: create(AlunoCreate)
    AlunoAPI->>Ctrl: POST /api/alunos (público)

    Ctrl->>Svc: criar(AlunoCreateDTO)

    Svc->>AlunoRepo: existsByCpf / existsByEmail
    AlunoRepo->>DB: SELECT aluno
    DB-->>Svc: Verificação de unicidade

    Svc->>InstRepo: findById(instituicaoId)
    InstRepo->>DB: SELECT instituicao
    DB-->>Svc: Instituicao

    Svc->>Svc: Cria Aluno (saldo=0, senha hash)
    Svc->>AlunoRepo: save(aluno)
    AlunoRepo->>DB: INSERT INTO aluno
    DB-->>Svc: Aluno persistido

    Svc-->>Ctrl: AlunoResponseDTO
    Ctrl-->>AlunoAPI: HTTP 201 Created
    AlunoAPI-->>View: Aluno criado
    View-->>Aluno: Confirmação de cadastro
```

---

## Implementação

| Camada | Artefato |
|--------|----------|
| Frontend | `views/alunos/AlunoFormView.vue`, rota `/alunos/novo` |
| API | `alunosApi.create()` → `POST /api/alunos` |
| Backend | `AlunoController.criar()`, `AlunoService.criar()` |
