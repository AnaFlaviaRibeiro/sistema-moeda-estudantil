# Modelo Entidade-Relacionamento

Sistema de Moeda Estudantil — Release 1.

Este documento descreve o **modelo ER** do domínio: entidades, atributos, relacionamentos, cardinalidades e restrições. A representação é **textual e tabular**, conforme prática de modelagem de dados (não se trata de um diagrama de casos de uso nem de classes).

---

## 1. Notação

| Símbolo | Significado |
|---------|-------------|
| **PK** | Chave primária |
| **FK** | Chave estrangeira |
| **UK** | Valor único no conjunto da entidade/tabela |
| **O** | Atributo opcional (pode ser nulo) |
| **1:N** | Um para muitos |
| **N:1** | Muitos para um |
| **Total** | Participação obrigatória (todo registro da entidade fraca participa) |
| **Parcial** | Participação opcional |

---

## 2. Generalização (conceitual)

No nível **conceitual**, os participantes autenticáveis do sistema generalizam o tipo **USUÁRIO**:

| Supertipo | Subtipos |
|-----------|----------|
| **USUÁRIO** | **ALUNO**, **PROFESSOR**, **EMPRESA_PARCEIRA** |

**Atributos comuns de USUÁRIO:** `nome`, `email`, `senha`.

Na implementação relacional (Release 1), cada subtipo possui **tabela própria** com os atributos comuns repetidos (`nome`, `email`, `senha`), pois não há tabela única `usuario`. O mapeamento JPA usa `@MappedSuperclass` apenas no código, não como entidade persistida.

---

## 3. Entidades e atributos

### 3.1 INSTITUICAO

Instituição de ensino participante do programa (pré-cadastrada).

| Atributo | Tipo lógico | Restrição |
|----------|-------------|-----------|
| id | Inteiro longo | **PK** |
| nome | Texto (150) | Obrigatório |
| cnpj | Texto (18) | Obrigatório, **UK** |

---

### 3.2 ALUNO

Estudante que recebe moedas e resgata vantagens.

| Atributo | Tipo lógico | Restrição |
|----------|-------------|-----------|
| id | Inteiro longo | **PK** |
| nome | Texto (150) | Obrigatório (herdado de USUÁRIO) |
| email | Texto (150) | Obrigatório, **UK** |
| senha | Texto (200) | Obrigatório |
| cpf | Texto (14) | Obrigatório, **UK** |
| rg | Texto (20) | O |
| curso | Texto (100) | Obrigatório |
| saldo | Inteiro | Obrigatório, padrão **0** |
| instituicao_id | Inteiro longo | **FK** → INSTITUICAO.id, obrigatório |
| end_logradouro | Texto (200) | O (parte do endereço) |
| end_numero | Texto (20) | O |
| end_complemento | Texto (100) | O |
| end_bairro | Texto (100) | O |
| end_cidade | Texto (100) | O |
| end_uf | Texto (2) | O |
| end_cep | Texto (9) | O |

**Atributo composto:** `Endereço` = {logradouro, numero, complemento, bairro, cidade, uf, cep}, embutido na entidade ALUNO (não é entidade separada no modelo lógico).

---

### 3.3 PROFESSOR

Docente que distribui moedas aos alunos.

| Atributo | Tipo lógico | Restrição |
|----------|-------------|-----------|
| id | Inteiro longo | **PK** |
| nome | Texto (150) | Obrigatório |
| email | Texto (150) | Obrigatório, **UK** |
| senha | Texto (200) | Obrigatório |
| cpf | Texto (14) | Obrigatório, **UK** |
| departamento | Texto (100) | Obrigatório |
| saldo | Inteiro | Obrigatório, padrão **0** |
| instituicao_id | Inteiro longo | **FK** → INSTITUICAO.id, obrigatório |

---

### 3.4 EMPRESA_PARCEIRA

Empresa que oferece vantagens em troca de moedas.

| Atributo | Tipo lógico | Restrição |
|----------|-------------|-----------|
| id | Inteiro longo | **PK** |
| razao_social | Texto (200) | Obrigatório (equivale a `nome` do USUÁRIO) |
| cnpj | Texto (18) | Obrigatório, **UK** |
| email | Texto (150) | Obrigatório, **UK** |
| senha | Texto (200) | Obrigatório |
| contato | Texto (100) | O |
| end_logradouro … end_cep | Texto | O (mesmo composto **Endereço** de ALUNO) |

---

### 3.5 VANTAGEM

Benefício cadastrado por uma empresa (desconto, produto etc.).

| Atributo | Tipo lógico | Restrição |
|----------|-------------|-----------|
| id | Inteiro longo | **PK** |
| nome | Texto (150) | Obrigatório |
| descricao | Texto (1000) | O |
| foto_url | Texto (500) | O |
| custo_em_moedas | Inteiro | Obrigatório |
| ativa | Booleano | Obrigatório, padrão **verdadeiro** |
| empresa_id | Inteiro longo | **FK** → EMPRESA_PARCEIRA.id, obrigatório |

---

### 3.6 TRANSACAO

Registro de movimentação de moedas (extrato). Centraliza distribuição, resgate e crédito semestral.

| Atributo | Tipo lógico | Restrição |
|----------|-------------|-----------|
| id | Inteiro longo | **PK** |
| tipo | Enumeração | Obrigatório: `DISTRIBUICAO`, `RESGATE`, `CREDITO_SEMESTRAL` |
| valor | Inteiro | Obrigatório |
| descricao | Texto (500) | O (motivo da distribuição ou detalhe) |
| data_hora | Data/hora | Obrigatório |
| codigo_cupom | Texto (40) | O (preenchido em `RESGATE`) |
| cupom_conferido | Booleano | O, padrão **falso** |
| aluno_id | Inteiro longo | **FK** → ALUNO.id, O |
| professor_id | Inteiro longo | **FK** → PROFESSOR.id, O |
| vantagem_id | Inteiro longo | **FK** → VANTAGEM.id, O |

**Regra semântica por `tipo`:**

| tipo | professor_id | aluno_id | vantagem_id |
|------|--------------|----------|-------------|
| DISTRIBUICAO | Obrigatório | Obrigatório | Nulo |
| RESGATE | Nulo | Obrigatório | Obrigatório |
| CREDITO_SEMESTRAL | Obrigatório | Nulo | Nulo |

---

## 4. Relacionamentos

| # | Nome | Entidade 1 | Entidade 2 | Cardinalidade | Participação | Descrição |
|---|------|------------|------------|---------------|--------------|-----------|
| R1 | **pertence** | ALUNO | INSTITUICAO | N : 1 | ALUNO **Total**, INSTITUICAO Parcial | Cada aluno está vinculado a exatamente uma instituição; uma instituição possui vários alunos. |
| R2 | **leciona_em** | PROFESSOR | INSTITUICAO | N : 1 | PROFESSOR **Total**, INSTITUICAO Parcial | Cada professor pertence a uma instituição. |
| R3 | **oferece** | EMPRESA_PARCEIRA | VANTAGEM | 1 : N | EMPRESA **Total** (lado das vantagens), VANTAGEM **Total** | Uma empresa cadastra zero ou mais vantagens; cada vantagem pertence a uma única empresa. |
| R4 | **envia** | PROFESSOR | TRANSACAO | 1 : N | Parcial em PROFESSOR | Professor origina transações de distribuição ou crédito semestral. |
| R5 | **recebe_movimentacao** | ALUNO | TRANSACAO | 1 : N | Parcial em ALUNO | Aluno aparece em transações de distribuição ou resgate. |
| R6 | **objeto_de_resgate** | VANTAGEM | TRANSACAO | 1 : N | Parcial em VANTAGEM | Vantagem referenciada apenas em transações do tipo RESGATE. |

**Observação:** R4, R5 e R6 convergem na mesma entidade TRANSACAO, que funciona como registro polimórfico conforme o atributo `tipo`.

---

## 5. Restrições de integridade

1. **Unicidade:** `INSTITUICAO.cnpj`; `ALUNO.cpf`, `ALUNO.email`; `PROFESSOR.cpf`, `PROFESSOR.email`; `EMPRESA_PARCEIRA.cnpj`, `EMPRESA_PARCEIRA.email`.
2. **Saldo:** `ALUNO.saldo` e `PROFESSOR.saldo` ≥ 0; operações de débito devem respeitar saldo suficiente (regra de negócio na aplicação).
3. **Transação:** conforme tabela da seção 3.6; `codigo_cupom` único quando gerado em resgates.
4. **Vantagem:** `custo_em_moedas` > 0 para permitir resgate com valor positivo.
5. **Autenticação:** `email` + `senha` identificam o participante em cada subtipo de USUÁRIO (sem supertabela física).

---

## 6. Mapeamento lógico (tabelas)

| Tabela | Corresponde a |
|--------|----------------|
| `instituicao` | INSTITUICAO |
| `aluno` | ALUNO (+ colunas de Endereço embutidas) |
| `professor` | PROFESSOR |
| `empresa_parceira` | EMPRESA_PARCEIRA (+ Endereço embutido) |
| `vantagem` | VANTAGEM |
| `transacao` | TRANSACAO |

Relacionamentos R1–R6 são implementados por colunas **FK** nas tabelas filhas, conforme seção 3.

---

## 7. Estratégia de acesso a dados (implementação)

| Aspecto | Escolha |
|---------|---------|
| Persistência | JPA / Hibernate |
| Padrão de acesso | Repository (DAO) via **Micronaut Data** (`CrudRepository`) |
| Consultas derivadas | Por convenção de nome (`findByEmail`, `existsByCpf`, etc.) |
| Desenvolvimento | H2 em memória (`jdbc:h2:mem:moedaestudantil`), seed em `DataSeeder` |
| Produção (previsto) | PostgreSQL via configuração externa |
| Transações de negócio | `@Transactional` nos services (atomicidade ao movimentar moedas) |

---

## 8. Correspondência com outros artefatos

| Artefato | Arquivo |
|----------|---------|
| Diagrama de classes (domínio OO) | [03-diagrama-de-classes.md](./03-diagrama-de-classes.md) |
| Diagrama de componentes | [04-diagrama-de-componentes.md](./04-diagrama-de-componentes.md) |
| Código das entidades JPA | `backend/.../domain/` |

O diagrama de **classes** descreve comportamento e herança em objetos; este **modelo ER** descreve a estrutura **relacional** persistida no banco.
