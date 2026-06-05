# Diagrama de Sequência — Login (HU-03)

**Caso de uso:** Como usuário, fazer login com e-mail e senha para acessar o sistema.

**Atores:** Aluno, Professor ou Empresa Parceira  
**Release:** 1

---

## Diagrama de Sequência

```mermaid
sequenceDiagram
    autonumber
    actor Usuario as Usuário
    participant View as LoginView<br/>(Vue 3)
    participant API as authApi
    participant Ctrl as AuthController
    participant Svc as AuthService
    participant AlunoRepo as AlunoRepository
    participant ProfRepo as ProfessorRepository
    participant EmpRepo as EmpresaParceiraRepository
    participant JWT as JwtService
    participant Router as Vue Router

    Usuario->>View: Informa e-mail e senha
    View->>API: login({ email, senha })
    API->>Ctrl: POST /api/auth/login (público)

    Ctrl->>Svc: login(LoginRequestDTO)

    Svc->>AlunoRepo: findByEmail(email)
    alt Credenciais de aluno válidas
        AlunoRepo-->>Svc: Aluno
        Svc->>Svc: autenticar(senha)
    else Não é aluno
        Svc->>ProfRepo: findByEmail(email)
        alt Credenciais de professor válidas
            ProfRepo-->>Svc: Professor
        else Não é professor
            Svc->>EmpRepo: findByEmail(email)
            EmpRepo-->>Svc: EmpresaParceira
        end
    end

    Svc->>JWT: generateToken(AuthenticatedUser)
    JWT-->>Svc: JWT
    Svc-->>Ctrl: LoginResponseDTO (token, tipo, id, nome)
    Ctrl-->>API: HTTP 200 + JSON
    API-->>View: LoginResponseDTO

    View->>View: Armazena token e usuário (localStorage)
    View->>Router: Redireciona conforme papel (ALUNO/PROFESSOR/EMPRESA)
    Router-->>Usuario: Área específica do perfil
```

---

## Implementação

| Camada | Artefato |
|--------|----------|
| Frontend | `views/LoginView.vue`, `composables/useAuth.ts` |
| API | `authApi.login()` → `POST /api/auth/login` |
| Backend | `AuthController.login()`, `AuthService.login()` |
| Redirecionamento | `permissions/homeRouteForRole()` |
