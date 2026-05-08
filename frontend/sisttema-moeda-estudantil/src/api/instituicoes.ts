import { http } from './client'
import type { Instituicao } from '../types'

const base = '/api/instituicoes'

export const instituicoesApi = {
  list: () => http.get<Instituicao[]>(base).then(r => r.data),
  get: (id: number) => http.get<Instituicao>(`${base}/${id}`).then(r => r.data),
}
