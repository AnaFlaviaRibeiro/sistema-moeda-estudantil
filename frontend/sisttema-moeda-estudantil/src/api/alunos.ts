import { http } from './client'
import type { Aluno, AlunoCreate, AlunoUpdate } from '../types'

const base = '/api/alunos'

export const alunosApi = {
  list: () => http.get<Aluno[]>(base).then(r => r.data),
  get: (id: number) => http.get<Aluno>(`${base}/${id}`).then(r => r.data),
  create: (dto: AlunoCreate) => http.post<Aluno>(base, dto).then(r => r.data),
  update: (id: number, dto: AlunoUpdate) =>
    http.put<Aluno>(`${base}/${id}`, dto).then(r => r.data),
  remove: (id: number) => http.delete<void>(`${base}/${id}`).then(r => r.data),
}
