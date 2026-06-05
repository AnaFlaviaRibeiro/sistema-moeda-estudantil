# Diagrama de Sequência — Gerenciar Cadastro de Empresa (HU-12)

**Caso de uso:** Como empresa parceira, consultar e atualizar os dados da própria empresa.

**Atores:** Empresa Parceira  
**Release:** 1

> Este diagrama representa a **edição do próprio cadastro**. O cadastro inicial é HU-02.

---

## Diagrama de Sequência

```mermaid
sequenceDiagram
    autonumber
    actor Empresa as Empresa Parceira
    participant View as EmpresaFormView<br/>(Vue 3)
    participant API as empresasApi
    participant Filter as AuthFilter
    participant Ctrl as EmpresaParceiraController
    participant Auth as AuthorizationService
    participant Svc as EmpresaParceiraService
    participant Repo as EmpresaParceiraRepository
    participant DB as PostgreSQL

    Empresa->>View: Acessa /minha-empresa

    View->>API: me()
    API->>Ctrl: GET /api/empresas/me<br/>Authorization: Bearer JWT

    Ctrl->>Filter: Valida token JWT
    Ctrl->>Auth: requireEmpresa(user)
    Ctrl->>Svc: buscarPorId(empresaId)
    Svc->>Repo: findById(empresaId)
    Repo->>DB: SELECT empresa_parceira
    DB-->>View: Dados atuais exibidos no formulário

    Empresa->>View: Altera dados e salva
    View->>API: update(id, EmpresaParceiraUpdate)

    API->>Ctrl: PUT /api/empresas/{id}<br/>Authorization: Bearer JWT
    Ctrl->>Auth: requireEdicaoPropriaEmpresa(user, id)
    Ctrl->>Svc: atualizar(id, dto)

    Svc->>Repo: update(empresa)
    Repo->>DB: UPDATE empresa_parceira
    DB-->>Svc: Empresa atualizada

    Svc-->>Ctrl: EmpresaParceiraResponseDTO
    Ctrl-->>API: HTTP 200 + JSON
    API-->>View: Empresa atualizada
    View-->>Empresa: Confirmação de alteração
```

---

## Implementação

| Camada | Artefato |
|--------|----------|
| Frontend | `views/empresas/EmpresaFormView.vue`, rota `/minha-empresa` |
| API | `empresasApi.me()`, `empresasApi.update()` |
| Backend | `EmpresaParceiraController`, `EmpresaParceiraService` |
