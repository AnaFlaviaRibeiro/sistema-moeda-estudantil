# Diagrama de Sequência — Cadastro de Empresa Parceira (HU-02)

**Caso de uso:** Como empresa parceira, cadastrar minha empresa no sistema.

**Atores:** Empresa Parceira (visitante)  
**Release:** 1

---

## Diagrama de Sequência

```mermaid
sequenceDiagram
    autonumber
    actor Empresa as Empresa Parceira
    participant View as EmpresaFormView<br/>(Vue 3)
    participant API as empresasApi
    participant Ctrl as EmpresaParceiraController
    participant Svc as EmpresaParceiraService
    participant Repo as EmpresaParceiraRepository
    participant DB as PostgreSQL

    Empresa->>View: Acessa /empresas/nova
    Empresa->>View: Preenche razão social, CNPJ, endereço, contato e senha
    Empresa->>View: Submete formulário

    View->>API: create(EmpresaParceiraCreate)
    API->>Ctrl: POST /api/empresas (público)

    Ctrl->>Svc: criar(EmpresaParceiraCreateDTO)

    Svc->>Repo: existsByCnpj / existsByEmail
    Repo->>DB: SELECT empresa_parceira
    DB-->>Svc: Verificação de unicidade

    Svc->>Svc: Cria EmpresaParceira (senha hash)
    Svc->>Repo: save(empresa)
    Repo->>DB: INSERT INTO empresa_parceira
    DB-->>Svc: Empresa persistida

    Svc-->>Ctrl: EmpresaParceiraResponseDTO
    Ctrl-->>API: HTTP 201 Created
    API-->>View: Empresa criada
    View-->>Empresa: Confirmação de cadastro
```

---

## Implementação

| Camada | Artefato |
|--------|----------|
| Frontend | `views/empresas/EmpresaFormView.vue`, rota `/empresas/nova` |
| API | `empresasApi.create()` → `POST /api/empresas` |
| Backend | `EmpresaParceiraController.criar()`, `EmpresaParceiraService.criar()` |
