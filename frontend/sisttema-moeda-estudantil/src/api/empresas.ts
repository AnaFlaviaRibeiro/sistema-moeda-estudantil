import { http } from './client'
import type {
  EmpresaParceira,
  EmpresaParceiraCreate,
  EmpresaParceiraUpdate,
} from '../types'

const base = '/api/empresas'

export const empresasApi = {
  list: () => http.get<EmpresaParceira[]>(base).then(r => r.data),
  get: (id: number) => http.get<EmpresaParceira>(`${base}/${id}`).then(r => r.data),
  create: (dto: EmpresaParceiraCreate) =>
    http.post<EmpresaParceira>(base, dto).then(r => r.data),
  update: (id: number, dto: EmpresaParceiraUpdate) =>
    http.put<EmpresaParceira>(`${base}/${id}`, dto).then(r => r.data),
  remove: (id: number) => http.delete<void>(`${base}/${id}`).then(r => r.data),
}
