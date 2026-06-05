import { http } from './client'
import type { Vantagem, VantagemCreate, VantagemUpdate } from '../types'

const base = '/api/vantagens'

export const vantagensApi = {
  list: () => http.get<Vantagem[]>(base).then((r) => r.data),

  get: (id: number) => http.get<Vantagem>(`${base}/${id}`).then((r) => r.data),

  listMinhas: () => http.get<Vantagem[]>(`${base}/empresa/minhas`).then((r) => r.data),

  getMinha: (id: number) => http.get<Vantagem>(`${base}/empresa/${id}`).then((r) => r.data),

  create: (data: VantagemCreate) =>
    http.post<Vantagem>(base, data).then((r) => r.data),

  update: (id: number, data: VantagemUpdate) =>
    http.put<Vantagem>(`${base}/${id}`, data).then((r) => r.data),
}
