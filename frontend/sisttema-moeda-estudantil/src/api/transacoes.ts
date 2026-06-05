import { http } from './client'
import type { DistribuicaoMoedas, Extrato, ResgateVantagem, Transacao } from '../types'

const base = '/api/transacoes'

export const transacoesApi = {
  distribuir: (data: DistribuicaoMoedas) =>
    http.post<Transacao>(`${base}/distribuir`, data).then((r) => r.data),

  resgatar: (data: ResgateVantagem) =>
    http.post<Transacao>(`${base}/resgatar`, data).then((r) => r.data),

  extratoAluno: () =>
    http.get<Extrato>(`${base}/extrato/aluno/me`).then((r) => r.data),

  extratoProfessor: () =>
    http.get<Extrato>(`${base}/extrato/professor/me`).then((r) => r.data),

  resgatesEmpresa: () =>
    http.get<Transacao[]>(`${base}/resgates/empresa`).then((r) => r.data),

  conferirCupom: (codigo: string) =>
    http.put<Transacao>(`${base}/cupons/${encodeURIComponent(codigo)}/conferir`).then((r) => r.data),
}
