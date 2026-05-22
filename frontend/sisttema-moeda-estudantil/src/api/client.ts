import axios, { AxiosError, type InternalAxiosRequestConfig } from 'axios'

const baseURL = import.meta.env.VITE_API_URL ?? 'http://localhost:8080'
const TOKEN_KEY = 'moeda_auth_token'

export const http = axios.create({
  baseURL,
  headers: { 'Content-Type': 'application/json' },
  timeout: 10000,
})

http.interceptors.request.use((config: InternalAxiosRequestConfig) => {
  const token = localStorage.getItem(TOKEN_KEY)
  if (token) {
    config.headers.Authorization = `Bearer ${token}`
  }
  return config
})

http.interceptors.response.use(
  response => response,
  error => {
    const status = error instanceof AxiosError ? error.response?.status : undefined
    const url = error instanceof AxiosError ? error.config?.url : undefined
    const isLogin = url?.includes('/api/auth/login')
    if (status === 401 && !isLogin) {
      localStorage.removeItem(TOKEN_KEY)
      localStorage.removeItem('moeda_auth_user')
      if (!window.location.pathname.startsWith('/login')) {
        const redirect = encodeURIComponent(window.location.pathname + window.location.search)
        window.location.href = `/login?redirect=${redirect}`
      }
    }
    if (status === 403 && !window.location.pathname.startsWith('/acesso-negado')) {
      window.location.href = '/acesso-negado'
    }
    return Promise.reject(error)
  },
)

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
