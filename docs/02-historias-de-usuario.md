# Histórias do Usuário

Convenção: `Como <papel>, eu quero <objetivo>, para <benefício>`.
Critérios de aceite (CA) listados em cada história.

## Cadastro e Autenticação

### HU-01 — Cadastro de Aluno
**Como** aluno interessado em participar do programa de mérito,
**eu quero** cadastrar meus dados (nome, e-mail, CPF, RG, endereço, instituição e curso),
**para** poder receber moedas de meus professores.

- CA1: O sistema só permite selecionar uma instituição já pré-cadastrada.
- CA2: CPF e e-mail são únicos no sistema.
- CA3: Ao concluir o cadastro, o aluno tem um saldo inicial de 0 moedas.
- CA4: Login (e-mail) e senha são obrigatórios.

### HU-02 — Cadastro de Empresa Parceira
**Como** empresa parceira,
**eu quero** cadastrar minha empresa no sistema,
**para** oferecer vantagens aos alunos.

- CA1: Devem ser informados razão social, CNPJ, endereço e contato.
- CA2: CNPJ é único no sistema.
- CA3: Login e senha são obrigatórios.

### HU-03 — Login
**Como** usuário (aluno, professor ou empresa),
**eu quero** fazer login com meu e-mail e senha,
**para** acessar minhas funcionalidades.

- CA1: O sistema valida credenciais antes de liberar acesso.
- CA2: Cada papel é direcionado para sua área específica.

## Distribuição de Moedas

### HU-04 — Receber 1.000 moedas por semestre (Professor)
**Como** professor,
**eu quero** receber automaticamente 1.000 moedas a cada novo semestre,
**para** poder reconhecer meus alunos.

- CA1: Saldo é acumulável (não zera no fim do semestre).

### HU-05 — Distribuir moedas a um aluno
**Como** professor,
**eu quero** enviar moedas a um aluno informando um motivo,
**para** reconhecer mérito acadêmico ou comportamental.

- CA1: O sistema só permite o envio se houver saldo suficiente.
- CA2: A mensagem (motivo) é obrigatória.
- CA3: O aluno recebe um e-mail com o valor e a mensagem.
- CA4: A transação é registrada no extrato de ambos.

## Vantagens e Resgates

### HU-06 — Cadastrar vantagem
**Como** empresa parceira,
**eu quero** cadastrar vantagens com descrição, foto e custo em moedas,
**para** oferecer benefícios aos alunos.

### HU-07 — Listar vantagens
**Como** aluno,
**eu quero** visualizar as vantagens disponíveis,
**para** decidir como gastar minhas moedas.

### HU-08 — Resgatar vantagem
**Como** aluno,
**eu quero** trocar moedas por uma vantagem,
**para** obter descontos ou produtos.

- CA1: O sistema só permite o resgate se houver saldo suficiente.
- CA2: O valor é debitado do saldo do aluno.
- CA3: É gerado um código de cupom único.
- CA4: O aluno recebe e-mail com o cupom; a empresa recebe e-mail com o mesmo código para conferência.

## Extratos

### HU-09 — Extrato do Aluno
**Como** aluno, **eu quero** consultar meu saldo e meu histórico (recebimentos e resgates), **para** acompanhar a evolução do meu mérito.

### HU-10 — Extrato do Professor
**Como** professor, **eu quero** consultar meu saldo e os envios que fiz, **para** acompanhar minha distribuição.

## CRUDs (foco da Release 1)

### HU-11 — Gerenciar cadastro de aluno (CRUD)
**Como** administrador (ou o próprio aluno),
**eu quero** criar, listar, editar e remover registros de alunos,
**para** manter os dados atualizados.

### HU-12 — Gerenciar cadastro de empresa parceira (CRUD)
**Como** administrador (ou a própria empresa),
**eu quero** criar, listar, editar e remover registros de empresas parceiras,
**para** manter o catálogo de parceiros atualizado.
