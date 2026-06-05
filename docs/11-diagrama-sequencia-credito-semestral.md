# Diagrama de Sequência — Crédito Semestral do Professor (HU-04)

**Caso de uso:** Como professor, receber automaticamente 1.000 moedas a cada novo semestre.

**Atores:** Sistema (automático)  
**Release:** 2

---

## Diagrama de Sequência

```mermaid
sequenceDiagram
    autonumber
    participant App as Application<br/>(startup)
    participant Credito as CreditoSemestralService
    participant ProfRepo as ProfessorRepository
    participant TransRepo as TransacaoRepository
    participant DB as PostgreSQL

    App->>Credito: onApplicationEvent(ServerStartupEvent)

    Credito->>Credito: deveAplicarCreditoSemestral()<br/>(dia 1 de jan ou jul)

    alt É início de semestre e ainda não creditado
        Credito->>ProfRepo: findAll()
        ProfRepo->>DB: SELECT professor
        DB-->>Credito: Lista de professores

        loop Para cada professor
            Credito->>Credito: jaCreditadoNoSemestreAtual(professorId)
            Credito->>ProfRepo: update(professor)<br/>saldo += 1000
            ProfRepo->>DB: UPDATE professor
            Credito->>TransRepo: save(Transacao CREDITO_SEMESTRAL)
            TransRepo->>DB: INSERT INTO transacao
        end
    else Fora do período ou já creditado
        Credito-->>App: Nenhuma ação
    end
```

---

## Implementação

| Camada | Artefato |
|--------|----------|
| Backend | `CreditoSemestralService` (listener de startup) |
| Persistência | `ProfessorRepository`, `TransacaoRepository` |
| Regra | +1.000 moedas acumuláveis; tipo `CREDITO_SEMESTRAL` |
