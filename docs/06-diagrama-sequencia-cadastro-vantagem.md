# Diagrama de Sequência — Cadastro de Vantagem (HU-06)

**Caso de uso:** Como empresa parceira, cadastrar vantagens com descrição, foto e custo em moedas.

**Atores:** Empresa Parceira  
**Release:** 2 — Lab04S02

---

## Diagrama de Sequência

```mermaid
sequenceDiagram
    autonumber
    actor Empresa as Empresa Parceira
    participant View as VantagemFormView<br/>(Vue 3)
    participant API as vantagensApi<br/>(Axios)
    participant Filter as AuthFilter
    participant Ctrl as VantagemController
    participant Auth as AuthorizationService
    participant Svc as VantagemService
    participant EmpRepo as EmpresaParceiraRepository
    participant VantRepo as VantagemRepository
    participant DB as PostgreSQL

    Empresa->>View: Acessa /empresa/vantagens/nova
    Empresa->>View: Preenche nome, descrição, foto e custo em moedas
    Empresa->>View: Clica em "Salvar"

    View->>View: Converte foto para base64 (fotoUrl)
    View->>API: create({ nome, descricao, fotoUrl, custoEmMoedas })

    API->>Ctrl: POST /api/vantagens<br/>Authorization: Bearer JWT

    Ctrl->>Filter: Valida token JWT
    Filter-->>Ctrl: AuthenticatedUser (tipo=EMPRESA, id)

    Ctrl->>Auth: requireEmpresa(user)
    Auth-->>Ctrl: OK

    Ctrl->>Svc: criar(empresaId, VantagemCreateDTO)

    Svc->>EmpRepo: findById(empresaId)
    EmpRepo->>DB: SELECT empresa_parceira
    DB-->>EmpRepo: EmpresaParceira
    EmpRepo-->>Svc: EmpresaParceira

    Svc->>Svc: Monta entidade Vantagem<br/>(ativa=true, empresa vinculada)

    Svc->>VantRepo: save(vantagem)
    VantRepo->>DB: INSERT INTO vantagem
    DB-->>VantRepo: Vantagem persistida
    VantRepo-->>Svc: Vantagem

    Svc->>Svc: toResponseDTO(vantagem)
    Svc-->>Ctrl: VantagemResponseDTO

    Ctrl-->>API: HTTP 201 Created + JSON
    API-->>View: VantagemResponseDTO

    View->>View: Redireciona para /empresa/vantagens
    View-->>Empresa: Exibe lista com nova vantagem
```

---

## Descrição do fluxo

| Passo | Descrição |
|-------|-----------|
| 1–3 | A empresa autenticada acessa o formulário e informa os dados da vantagem. |
| 4–5 | A foto é enviada como URL/base64 no campo `fotoUrl` do JSON. |
| 6–9 | O backend valida o JWT e verifica se o usuário tem perfil `EMPRESA`. |
| 10–13 | O serviço carrega a empresa logada e cria a vantagem com `ativa = true`. |
| 14–18 | A vantagem é persistida e retornada como `VantagemResponseDTO`. |
| 19–21 | O frontend redireciona para a listagem de vantagens da empresa. |

---

## Mapeamento com o código (implementação)

| Camada | Artefato |
|--------|----------|
| Frontend — view | `frontend/sisttema-moeda-estudantil/src/views/empresa/VantagemFormView.vue` |
| Frontend — rota | `/empresa/vantagens/nova` (`empresa-vantagem-nova`) |
| Frontend — API | `frontend/sisttema-moeda-estudantil/src/api/vantagens.ts` → `create()` |
| Backend — controller | `VantagemController.criar()` → `POST /api/vantagens` |
| Backend — autorização | `AuthorizationService.requireEmpresa()` |
| Backend — serviço | `VantagemService.criar()` |
| Backend — persistência | `EmpresaParceiraRepository`, `VantagemRepository` |
| Backend — DTO entrada | `VantagemCreateDTO` (nome, descrição, fotoUrl, custoEmMoedas) |
| Backend — DTO saída | `VantagemResponseDTO` |
| Banco | Tabela `vantagem` (FK `empresa_id`) |

---

## Critérios de aceite atendidos

- CA implícito: empresa autenticada cadastra vantagem com descrição, foto e custo em moedas.
- A vantagem fica disponível para listagem pelos alunos (`ativa = true`).
