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

export interface ApiError {
  status: number
  error: string
  message: string
  details?: string[]
}
