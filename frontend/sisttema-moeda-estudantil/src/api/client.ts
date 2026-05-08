import axios, { AxiosError } from 'axios'

const baseURL = import.meta.env.VITE_API_URL ?? 'http://localhost:8080'

export const http = axios.create({
  baseURL,
  headers: { 'Content-Type': 'application/json' },
  timeout: 10000,
})

export interface ApiErrorPayload {
  message?: string
  details?: string[]
  _embedded?: { errors?: { message: string }[] }
}

export function extractErrorMessage(err: unknown): string {
  if (err instanceof AxiosError) {
    const data = err.response?.data as ApiErrorPayload | undefined
    if (data?.message) {
      const det = data.details ? ': ' + data.details.join('; ') : ''
      return data.message + det
    }
    const fromEmbedded = data?._embedded?.errors?.map(e => e.message).join('; ')
    if (fromEmbedded) return fromEmbedded
    return err.message
  }
  if (err instanceof Error) return err.message
  return 'Erro desconhecido'
}
