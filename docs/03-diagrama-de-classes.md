# Diagrama de Classes

Visão de domínio do Sistema de Moeda Estudantil.

```mermaid
classDiagram
    class Usuario {
        <<abstract>>
        +Long id
        +String nome
        +String email
        +String senha
        +autenticar(String senha) boolean
    }

    class Endereco {
        +String logradouro
        +String numero
        +String complemento
        +String bairro
        +String cidade
        +String uf
        +String cep
    }

    class Instituicao {
        +Long id
        +String nome
        +String cnpj
    }

    class Aluno {
        +String cpf
        +String rg
        +String curso
        +int saldo
        +Endereco endereco
        +creditar(int valor) void
        +debitar(int valor) void
    }

    class Professor {
        +String cpf
        +String departamento
        +int saldo
        +distribuir(Aluno a, int valor, String motivo) Transacao
        +receberCreditoSemestral() void
    }

    class EmpresaParceira {
        +Long id
        +String razaoSocial
        +String cnpj
        +String email
        +String senha
        +String contato
        +Endereco endereco
    }

    class Vantagem {
        +Long id
        +String nome
        +String descricao
        +String fotoUrl
        +int custoEmMoedas
        +boolean ativa
    }

    class Transacao {
        +Long id
        +TipoTransacao tipo
        +int valor
        +String descricao
        +LocalDateTime dataHora
        +String codigoCupom
    }

    class TipoTransacao {
        <<enumeration>>
        DISTRIBUICAO
        RESGATE
        CREDITO_SEMESTRAL
    }

    class CupomResgate {
        +Long id
        +String codigo
        +LocalDateTime emitidoEm
        +boolean conferido
    }

    Usuario <|-- Aluno
    Usuario <|-- Professor
    Usuario <|-- EmpresaParceira

    Aluno "*" --> "1" Instituicao : pertence a
    Professor "*" --> "1" Instituicao : pertence a

    Aluno "1" *-- "1" Endereco
    EmpresaParceira "1" *-- "1" Endereco

    EmpresaParceira "1" --> "*" Vantagem : oferece
    Vantagem "*" --> "1" EmpresaParceira

    Professor "1" --> "*" Transacao : envia
    Aluno "1" --> "*" Transacao : participa
    Transacao --> TipoTransacao
    Transacao "0..1" --> "0..1" CupomResgate
    Transacao "0..1" --> "0..1" Vantagem
```

## Notas de modelagem

- `Usuario` é uma superclasse abstrata que generaliza atributos comuns (login/senha). Em JPA, será mapeada com `@MappedSuperclass`.
- `Aluno` e `Professor` mantêm um `saldo` em moedas (inteiro). Para o aluno o saldo é incrementado por `Transacao` do tipo DISTRIBUICAO e decrementado por RESGATE; para o professor, é incrementado por CREDITO_SEMESTRAL e decrementado por DISTRIBUICAO.
- `Endereco` é um Value Object embutível (`@Embeddable`).
- `Transacao` é a entidade central do extrato; serve tanto para distribuição (Professor → Aluno) quanto para resgate (Aluno → Vantagem da Empresa).
- `CupomResgate` poderia ser substituído por um `codigoCupom` simples na própria Transação; foi mantido como classe para evidenciar a regra "código gerado pelo sistema" em UC5.
