import { computed, ref } from 'vue'
import { login as apiLogin, fetchMe, type AuthUser, type LoginRequest } from '../api/auth'

const TOKEN_KEY = 'moeda_auth_token'
const USER_KEY = 'moeda_auth_user'

const token = ref<string | null>(localStorage.getItem(TOKEN_KEY))
const user = ref<AuthUser | null>(readStoredUser())

function readStoredUser(): AuthUser | null {
  const raw = localStorage.getItem(USER_KEY)
  if (!raw) return null
  try {
    return JSON.parse(raw) as AuthUser
  } catch {
    return null
  }
}

function persist() {
  if (token.value) {
    localStorage.setItem(TOKEN_KEY, token.value)
  } else {
    localStorage.removeItem(TOKEN_KEY)
  }
  if (user.value) {
    localStorage.setItem(USER_KEY, JSON.stringify(user.value))
  } else {
    localStorage.removeItem(USER_KEY)
  }
}

export function useAuth() {
  const isAuthenticated = computed(() => Boolean(token.value))

  async function login(payload: LoginRequest) {
    const response = await apiLogin(payload)
    token.value = response.token
    user.value = {
      tipo: response.tipo,
      id: response.id,
      nome: response.nome,
      email: response.email,
    }
    persist()
    return response
  }

  async function restoreSession() {
    if (!token.value) return false
    try {
      user.value = await fetchMe()
      persist()
      return true
    } catch {
      logout()
      return false
    }
  }

  function logout() {
    token.value = null
    user.value = null
    persist()
  }

  function getToken() {
    return token.value
  }

  return {
    token,
    user,
    isAuthenticated,
    login,
    logout,
    restoreSession,
    getToken,
  }
}
