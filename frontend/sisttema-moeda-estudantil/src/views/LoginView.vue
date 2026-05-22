<script setup lang="ts">
import { reactive, ref } from 'vue'
import { RouterLink, useRoute, useRouter } from 'vue-router'
import { extractErrorMessage } from '../api/client'
import { homeRouteForRole } from '../permissions'
import { useAuth } from '../composables/useAuth'

const router = useRouter()
const route = useRoute()
const auth = useAuth()

const form = reactive({
  email: '',
  senha: '',
})

const loading = ref(false)
const error = ref('')

async function onSubmit() {
  loading.value = true
  error.value = ''
  try {
    await auth.login({
      email: form.email.trim(),
      senha: form.senha,
    })
    const tipo = auth.user.value!.tipo
    const fallback = homeRouteForRole(tipo)
    const redirect = typeof route.query.redirect === 'string' ? route.query.redirect : fallback
    await router.push(redirect)
  } catch (err) {
    error.value = extractErrorMessage(err)
  } finally {
    loading.value = false
  }
}
</script>

<template>
  <div class="login-page">
    <section class="login-card card">
      <div class="login-brand">
        <div class="brand-mark">ME</div>
        <div>
          <span class="eyebrow">Programa de mérito</span>
          <h1>Entrar no sistema</h1>
          <p>Use o e-mail e a senha cadastrados para alunos, professores ou empresas parceiras.</p>
        </div>
      </div>

      <form class="login-form" @submit.prevent="onSubmit">
        <label>
          E-mail
          <input
            v-model="form.email"
            type="email"
            required
            autocomplete="username"
            placeholder="seu.email@exemplo.com"
          />
        </label>

        <label>
          Senha
          <input
            v-model="form.senha"
            type="password"
            required
            minlength="6"
            autocomplete="current-password"
            placeholder="Mínimo 6 caracteres"
          />
        </label>

        <p v-if="error" class="form-error" role="alert">{{ error }}</p>

        <button class="btn btn-primary btn-block" type="submit" :disabled="loading">
          {{ loading ? 'Entrando…' : 'Entrar' }}
        </button>
      </form>

      <div class="login-links">
        <RouterLink to="/alunos/novo">Cadastrar como aluno</RouterLink>
        <RouterLink to="/empresas/nova">Cadastrar empresa parceira</RouterLink>
      </div>

      <!-- <aside class="login-hint">
        <strong>Contas de demonstração</strong>
        <p>Senha padrão: <code>changeit</code></p>
        <ul>
          <li><span>Aluno</span> ana.lima@aluno.puc.example</li>
          <li><span>Professor</span> joao.silva@puc.example</li>
          <li><span>Empresa</span> contato@saborecia.example</li>
        </ul>
      </aside> -->
    </section>
  </div>
</template>
