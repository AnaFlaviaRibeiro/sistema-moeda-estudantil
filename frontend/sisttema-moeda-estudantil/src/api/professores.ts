import { http } from './client'
import type { Professor } from '../types'

const base = '/api/professores'

export const professoresApi = {
  me: () => http.get<Professor>(`${base}/me`).then((r) => r.data),
}
