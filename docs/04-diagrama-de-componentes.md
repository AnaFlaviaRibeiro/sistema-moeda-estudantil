# Diagrama de Componentes

Visão de alto nível dos componentes do sistema.

```mermaid
flowchart TB
    subgraph Browser["Browser do Usuário"]
        UI[Frontend SPA<br/>Vue 3 + Vite + TypeScript]
    end

    subgraph Backend["Servidor de Aplicação (Micronaut 3)"]
        direction TB
        REST[Camada REST<br/>Controllers]
        SVC[Camada de Serviço<br/>Services]
        REPO[Camada de Persistência<br/>Repositories - DAO]
        EMAIL[Serviço de Email<br/>EmailService]
        AUTH[Autenticação<br/>AuthService]

        REST --> SVC
        SVC --> REPO
        SVC --> EMAIL
        REST --> AUTH
    end

    subgraph Infra["Infraestrutura"]
        DB[(Banco de Dados<br/>H2 / PostgreSQL)]
        SMTP[Servidor SMTP]
    end

    UI -- HTTP/JSON --> REST
    REPO -- JDBC/JPA --> DB
    EMAIL -- SMTP --> SMTP
```

## Decomposição

### Frontend (Vue 3 + Vite)
- `views/` — páginas (Home, CRUD Aluno, CRUD Empresa, etc.)
- `components/` — componentes reutilizáveis
- `api/` — clientes HTTP (axios) por recurso
- `router/` — roteamento SPA
- `stores/` — estado global (pinia)

### Backend (Micronaut 3)
- `controller/` — endpoints REST (`/api/alunos`, `/api/empresas`, ...)
- `service/` — regras de negócio (transferência atômica de moedas, geração de cupom)
- `repository/` — Micronaut Data JPA (Padrão DAO/Repository)
- `domain/` — entidades JPA do modelo
- `dto/` — objetos de transporte de entrada e saída
- `exception/` — handlers globais de erro

### Infraestrutura
- **Banco**: H2 em desenvolvimento (in-memory) e PostgreSQL em produção (configurável via `application.properties`).
- **SMTP**: integração futura para notificações de moedas e cupons.

## Comunicação

| De | Para | Protocolo | Formato |
|----|------|-----------|---------|
| Frontend | Backend | HTTP/REST | JSON |
| Backend | Banco | JDBC | SQL |
| Backend | SMTP | SMTP | MIME |
