<script setup lang="ts">
import { computed, onMounted, ref } from 'vue'
import { RouterLink } from 'vue-router'
import { alunosApi } from '../api/alunos'
import { professoresApi } from '../api/professores'
import { extractErrorMessage } from '../api/client'
import { useAuth } from '../composables/useAuth'
import type { Aluno, Professor } from '../types'

const auth = useAuth()
const tipo = computed(() => auth.user.value?.tipo)

const aluno = ref<Aluno | null>(null)
const professor = ref<Professor | null>(null)
const loading = ref(false)
const error = ref<string | null>(null)

const saldoExibido = computed(() => {
  if (tipo.value === 'ALUNO' && aluno.value != null) {
    return String(aluno.value.saldo)
  }
  if (tipo.value === 'PROFESSOR' && professor.value != null) {
    return String(professor.value.saldo)
  }
  return '—'
})

const metricLabel = computed(() => {
  if (tipo.value === 'PROFESSOR') return 'Saldo disponível'
  return 'Saldo atual'
})

async function load() {
  loading.value = true
  error.value = null
  try {
    if (tipo.value === 'ALUNO') {
      aluno.value = await alunosApi.me()
    } else if (tipo.value === 'PROFESSOR') {
      professor.value = await professoresApi.me()
    }
  } catch (e) {
    error.value = extractErrorMessage(e)
  } finally {
    loading.value = false
  }
}

onMounted(load)
</script>

<template>
  <section class="hero-card">
    <div class="hero-content">
      <span class="eyebrow">Programa de mérito</span>

      <template v-if="tipo === 'ALUNO'">
        <h1>Olá, {{ auth.user.value?.nome }}</h1>
        <p>
          Acompanhe seu saldo de moedas, consulte vantagens das empresas parceiras e
          atualize seus dados de cadastro.
        </p>
        <div v-if="error" class="alert alert-error">{{ error }}</div>
        <div v-else-if="loading" class="alert alert-info">Carregando saldo…</div>
        <div class="hero-actions">
          <RouterLink class="btn btn-primary" to="/vantagens">Ver vantagens</RouterLink>
          <RouterLink class="btn" to="/extrato">Meu extrato</RouterLink>
          <RouterLink class="btn" to="/meu-perfil">Meu cadastro</RouterLink>
        </div>
        <ul class="role-capabilities">
          <li>Consultar saldo de moedas</li>
          <li>Extrato de transações</li>
          <li>Listar e resgatar vantagens</li>
          <li>Editar seu cadastro de aluno</li>
        </ul>
      </template>

      <template v-else-if="tipo === 'PROFESSOR'">
        <h1>Área do professor</h1>
        <p>
          Visualize os alunos cadastrados, distribua moedas de mérito com justificativa
          obrigatória e acompanhe seu extrato.
        </p>
        <div v-if="error" class="alert alert-error">{{ error }}</div>
        <div class="hero-actions">
          <RouterLink class="btn btn-primary" to="/alunos">Ver alunos</RouterLink>
          <RouterLink class="btn" to="/professor/extrato">Extrato</RouterLink>
        </div>
        <ul class="role-capabilities">
          <li>Consultar lista de alunos e saldos</li>
          <li>Distribuir moedas aos alunos</li>
          <li>Consultar extrato de envios</li>
          <li>Receber 1.000 moedas por semestre (automático)</li>
        </ul>
      </template>

      <template v-else-if="tipo === 'EMPRESA'">
        <h1>Área da empresa parceira</h1>
        <p>
          Mantenha os dados da sua empresa atualizados, cadastre vantagens e confira
          resgates por código de cupom.
        </p>
        <div class="hero-actions">
          <RouterLink class="btn btn-primary" to="/empresa/vantagens">Vantagens</RouterLink>
          <RouterLink class="btn" to="/empresa/resgates">Resgates</RouterLink>
          <RouterLink class="btn" to="/minha-empresa">Minha empresa</RouterLink>
        </div>
        <ul class="role-capabilities">
          <li>Editar cadastro da empresa</li>
          <li>Cadastrar vantagens com foto e custo</li>
          <li>Conferir resgates por código de cupom</li>
        </ul>
      </template>

      <template v-else>
        <h1>Reconhecimento estudantil com uma moeda virtual</h1>
        <p>Faça login para acessar sua área no sistema.</p>
      </template>
    </div>

    <div class="hero-visual" aria-hidden="true">
      <div class="coin coin-lg">M</div>
      <div class="coin coin-sm">+</div>
      <div class="metric-card">
        <span>{{ metricLabel }}</span>
        <strong>{{ loading && (tipo === 'ALUNO' || tipo === 'PROFESSOR') ? '…' : saldoExibido }}</strong>
        <small>moedas</small>
      </div>
    </div>
  </section>
</template>
