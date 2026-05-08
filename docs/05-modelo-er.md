# Modelo Entidade-Relacionamento

```mermaid
erDiagram
    INSTITUICAO ||--o{ ALUNO       : "possui"
    INSTITUICAO ||--o{ PROFESSOR   : "possui"
    EMPRESA_PARCEIRA ||--o{ VANTAGEM : "oferece"
    PROFESSOR  ||--o{ TRANSACAO    : "envia"
    ALUNO      ||--o{ TRANSACAO    : "participa de"
    VANTAGEM   ||--o{ TRANSACAO    : "é objeto de"

    INSTITUICAO {
        BIGINT  id PK
        VARCHAR nome
        VARCHAR cnpj UK
    }

    ALUNO {
        BIGINT  id PK
        VARCHAR nome
        VARCHAR email UK
        VARCHAR senha
        VARCHAR cpf  UK
        VARCHAR rg
        VARCHAR curso
        INT     saldo
        BIGINT  instituicao_id FK
        VARCHAR end_logradouro
        VARCHAR end_numero
        VARCHAR end_complemento
        VARCHAR end_bairro
        VARCHAR end_cidade
        VARCHAR end_uf
        VARCHAR end_cep
    }

    PROFESSOR {
        BIGINT  id PK
        VARCHAR nome
        VARCHAR email UK
        VARCHAR senha
        VARCHAR cpf  UK
        VARCHAR departamento
        INT     saldo
        BIGINT  instituicao_id FK
    }

    EMPRESA_PARCEIRA {
        BIGINT  id PK
        VARCHAR razao_social
        VARCHAR cnpj UK
        VARCHAR email UK
        VARCHAR senha
        VARCHAR contato
        VARCHAR end_logradouro
        VARCHAR end_numero
        VARCHAR end_complemento
        VARCHAR end_bairro
        VARCHAR end_cidade
        VARCHAR end_uf
        VARCHAR end_cep
    }

    VANTAGEM {
        BIGINT  id PK
        VARCHAR nome
        VARCHAR descricao
        VARCHAR foto_url
        INT     custo_em_moedas
        BOOLEAN ativa
        BIGINT  empresa_id FK
    }

    TRANSACAO {
        BIGINT      id PK
        VARCHAR     tipo "DISTRIBUICAO|RESGATE|CREDITO_SEMESTRAL"
        INT         valor
        VARCHAR     descricao
        TIMESTAMP   data_hora
        VARCHAR     codigo_cupom
        BOOLEAN     cupom_conferido
        BIGINT      aluno_id     FK
        BIGINT      professor_id FK "(nullable)"
        BIGINT      vantagem_id  FK "(nullable)"
    }
```

## Regras de integridade

- `INSTITUICAO.cnpj`, `ALUNO.cpf`, `ALUNO.email`, `PROFESSOR.cpf`, `PROFESSOR.email`, `EMPRESA_PARCEIRA.cnpj`, `EMPRESA_PARCEIRA.email` são UNIQUE.
- `ALUNO.saldo` e `PROFESSOR.saldo` têm valor padrão `0`.
- Em `TRANSACAO`, `professor_id` e `vantagem_id` são nuláveis (transação só usa um deles, conforme o `tipo`).
- O `codigo_cupom` é gerado para `tipo = RESGATE`.

## Estratégia de acesso a dados

- **ORM**: JPA / Hibernate
- **Padrão**: Repository (DAO) via Micronaut Data
  - `interface AlunoRepository extends CrudRepository<Aluno, Long>` — Micronaut gera implementação em tempo de compilação.
  - Métodos custom (e.g., `findByCpf`, `findByEmail`) são derivados do nome.
- **Datasource em desenvolvimento**: H2 in-memory (`jdbc:h2:mem:moedaestudantil`) com `data.sql` para seed.
- **Datasource em produção**: PostgreSQL (configurável via variável de ambiente).
- **Transações**: anotação `@Transactional` (Jakarta Transactions API) nos services para garantir atomicidade nas operações de moeda.
