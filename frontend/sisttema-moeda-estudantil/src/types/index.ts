export interface Endereco {
  logradouro?: string
  numero?: string
  complemento?: string
  bairro?: string
  cidade?: string
  uf?: string
  cep?: string
}

export interface Instituicao {
  id: number
  nome: string
  cnpj: string
}

export interface Aluno {
  id: number
  nome: string
  email: string
  cpf: string
  rg?: string
  curso: string
  saldo: number
  instituicao: Instituicao
  endereco?: Endereco
}

export interface AlunoCreate {
  nome: string
  email: string
  senha: string
  cpf: string
  rg?: string
  curso: string
  instituicaoId: number
  endereco?: Endereco
}

export interface AlunoUpdate {
  nome: string
  email: string
  rg?: string
  curso: string
  instituicaoId: number
  endereco?: Endereco
}

export interface EmpresaParceira {
  id: number
  razaoSocial: string
  cnpj: string
  email: string
  contato?: string
  endereco?: Endereco
}

export interface EmpresaParceiraCreate {
  razaoSocial: string
  cnpj: string
  email: string
  senha: string
  contato?: string
  endereco?: Endereco
}

export interface EmpresaParceiraUpdate {
  razaoSocial: string
  email: string
  contato?: string
  endereco?: Endereco
}

export type TipoTransacao = 'DISTRIBUICAO' | 'RESGATE' | 'CREDITO_SEMESTRAL'

export interface Professor {
  id: number
  nome: string
  email: string
  cpf: string
  departamento: string
  saldo: number
  instituicao: Instituicao
}

export interface Vantagem {
  id: number
  nome: string
  descricao?: string
  fotoUrl?: string
  custoEmMoedas: number
  ativa: boolean
  empresaId?: number
  empresaNome?: string
}

export interface VantagemCreate {
  nome: string
  descricao?: string
  fotoUrl?: string
  custoEmMoedas: number
}

export interface VantagemUpdate {
  nome: string
  descricao?: string
  fotoUrl?: string
  custoEmMoedas: number
  ativa: boolean
}

export interface Transacao {
  id: number
  tipo: TipoTransacao
  valor: number
  descricao?: string
  dataHora: string
  codigoCupom?: string
  cupomConferido?: boolean
  alunoId?: number
  alunoNome?: string
  professorId?: number
  professorNome?: string
  vantagemId?: number
  vantagemNome?: string
  empresaNome?: string
}

export interface Extrato {
  saldo: number
  transacoes: Transacao[]
}

export interface DistribuicaoMoedas {
  alunoId: number
  valor: number
  motivo: string
}

export interface ResgateVantagem {
  vantagemId: number
}

export interface ApiError {
  status: number
  error: string
  message: string
  details?: string[]
}
