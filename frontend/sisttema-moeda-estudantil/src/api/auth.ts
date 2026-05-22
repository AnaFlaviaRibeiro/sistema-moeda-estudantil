import { http } from './client'

export type TipoUsuario = 'ALUNO' | 'PROFESSOR' | 'EMPRESA'

export interface LoginRequest {
  email: string
  senha: string
}

export interface LoginResponse {
  token: string
  tipo: TipoUsuario
  id: number
  nome: string
  email: string
}

export interface AuthUser {
  tipo: TipoUsuario
  id: number
  nome: string
  email: string
}

export async function login(payload: LoginRequest): Promise<LoginResponse> {
  const { data } = await http.post<LoginResponse>('/api/auth/login', payload)
  return data
}

export async function fetchMe(): Promise<AuthUser> {
  const { data } = await http.get<AuthUser>('/api/auth/me')
  return data
}
