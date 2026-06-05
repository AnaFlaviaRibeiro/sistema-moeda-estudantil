# Diagrama de Sequência — Listagem de Vantagens (HU-07)

**Caso de uso:** Como aluno, visualizar as vantagens disponíveis para decidir como gastar minhas moedas.

**Atores:** Aluno  
**Release:** 2 — Lab04S02

---

## Diagrama de Sequência

```mermaid
sequenceDiagram
    autonumber
    actor Aluno as Aluno
    participant View as VantagensListView<br/>(Vue 3)
    participant VantAPI as vantagensApi<br/>(Axios)
    participant AlunoAPI as alunosApi<br/>(Axios)
    participant Filter as AuthFilter
    participant VantCtrl as VantagemController
    participant AlunoCtrl as AlunoController
    participant Auth as AuthorizationService
    participant VantSvc as VantagemService
    participant AlunoSvc as AlunoService
    participant VantRepo as VantagemRepository
    participant AlunoRepo as AlunoRepository
    participant DB as PostgreSQL

    Aluno->>View: Acessa /vantagens

    View->>VantAPI: list()
    View->>AlunoAPI: me()

    par Consulta vantagens
        VantAPI->>VantCtrl: GET /api/vantagens<br/>Authorization: Bearer JWT
        VantCtrl->>Filter: Valida token JWT
        Filter-->>VantCtrl: AuthenticatedUser (tipo=ALUNO, id)
        VantCtrl->>Auth: requireAluno(user)
        Auth-->>VantCtrl: OK
        VantCtrl->>VantSvc: listarAtivas()
        VantSvc->>VantRepo: findByAtivaTrue()
        VantRepo->>DB: SELECT * FROM vantagem WHERE ativa = true
        DB-->>VantRepo: Lista de Vantagem
        VantRepo-->>VantSvc: List<Vantagem>
        VantSvc->>VantSvc: toResponseDTO (cada item)
        VantSvc-->>VantCtrl: List<VantagemResponseDTO>
        VantCtrl-->>VantAPI: HTTP 200 + JSON
        VantAPI-->>View: Vantagem[]
    and Consulta saldo do aluno
        AlunoAPI->>AlunoCtrl: GET /api/alunos/me<br/>Authorization: Bearer JWT
        AlunoCtrl->>Auth: requireAluno(user)
        Auth-->>AlunoCtrl: OK
        AlunoCtrl->>AlunoSvc: buscarPorId(alunoId)
        AlunoSvc->>AlunoRepo: findById(alunoId)
        AlunoRepo->>DB: SELECT aluno
        DB-->>AlunoRepo: Aluno
        AlunoRepo-->>AlunoSvc: Aluno
        AlunoSvc-->>AlunoCtrl: AlunoResponseDTO
        AlunoCtrl-->>AlunoAPI: HTTP 200 + JSON
        AlunoAPI-->>View: Aluno (saldo)
    end

    View->>View: Renderiza cards com nome, descrição,<br/>foto, custo e nome da empresa
    View-->>Aluno: Exibe vantagens ativas e saldo atual
```

---

## Descrição do fluxo

| Passo | Descrição |
|-------|-----------|
| 1 | O aluno autenticado navega até a tela de vantagens. |
| 2–3 | A view dispara duas requisições em paralelo: listagem de vantagens e perfil do aluno (saldo). |
| 4–12 | O backend valida JWT, exige perfil `ALUNO` e retorna apenas vantagens com `ativa = true`. |
| 13–20 | Em paralelo, consulta o saldo do aluno logado via `/api/alunos/me`. |
| 21–22 | O frontend exibe cards com foto, descrição, custo em moedas e empresa parceira. |

---

## Mapeamento com o código (implementação)

| Camada | Artefato |
|--------|----------|
| Frontend — view | `frontend/sisttema-moeda-estudantil/src/views/aluno/VantagensListView.vue` |
| Frontend — rota | `/vantagens` (`aluno-vantagens`) |
| Frontend — API vantagens | `vantagensApi.list()` → `GET /api/vantagens` |
| Frontend — API aluno | `alunosApi.me()` → `GET /api/alunos/me` |
| Backend — controller | `VantagemController.listarAtivas()` |
| Backend — autorização | `AuthorizationService.requireAluno()` |
| Backend — serviço | `VantagemService.listarAtivas()` |
| Backend — persistência | `VantagemRepository.findByAtivaTrue()` |
| Backend — DTO saída | `VantagemResponseDTO` (id, nome, descricao, fotoUrl, custoEmMoedas, empresaNome) |
| Banco | Tabela `vantagem` + join com `empresa_parceira` |

---

## Critérios de aceite atendidos

- O aluno visualiza todas as vantagens **ativas** cadastradas pelas empresas parceiras.
- Cada vantagem exibe descrição, foto (quando informada) e custo em moedas.
- O saldo atual do aluno é exibido para apoiar a decisão de resgate.
