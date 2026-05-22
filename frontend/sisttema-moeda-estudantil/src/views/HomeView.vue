<script setup lang="ts">
import { computed, onMounted, ref } from 'vue'
import { RouterLink } from 'vue-router'
import { alunosApi } from '../api/alunos'
import { extractErrorMessage } from '../api/client'
import { useAuth } from '../composables/useAuth'
import type { Aluno } from '../types'

const auth = useAuth()
const tipo = computed(() => auth.user.value?.tipo)

const aluno = ref<Aluno | null>(null)
const loading = ref(false)
const error = ref<string | null>(null)

const saldoExibido = computed(() => {
  if (tipo.value === 'ALUNO' && aluno.value != null) {
    return String(aluno.value.saldo)
  }
  if (tipo.value === 'PROFESSOR') {
    return '1.000'
  }
  return '—'
})

const metricLabel = computed(() => {
  if (tipo.value === 'PROFESSOR') return 'Crédito semestral'
  return 'Saldo atual'
})

async function loadAluno() {
  if (tipo.value !== 'ALUNO') return
  loading.value = true
  error.value = null
  try {
    aluno.value = await alunosApi.me()
  } catch (e) {
    error.value = extractErrorMessage(e)
  } finally {
    loading.value = false
  }
}

onMounted(loadAluno)
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
        <!-- <div v-else-if="aluno" class="saldo-resumo">
          <span class="saldo-resumo-label">Seu saldo</span>
          <strong class="saldo-resumo-valor">{{ aluno.saldo }} moedas</strong>
          <small v-if="aluno.instituicao">
            {{ aluno.curso }} · {{ aluno.instituicao.nome }}
          </small>
        </div> -->
        <div v-else-if="loading" class="alert alert-info">Carregando saldo…</div>
        <div class="hero-actions">
          <RouterLink class="btn btn-primary" to="/meu-perfil">Meu cadastro</RouterLink>
        </div>
        <ul class="role-capabilities">
          <li>Consultar saldo de moedas</li>
          <li>Extrato de transações (em breve)</li>
          <li>Listar e resgatar vantagens (em breve)</li>
          <li>Editar seu cadastro de aluno</li>
        </ul>
      </template>

      <template v-else-if="tipo === 'PROFESSOR'">
        <h1>Área do professor</h1>
        <p>
          Visualize os alunos cadastrados e prepare-se para distribuir moedas de mérito
          com justificativa obrigatória.
        </p>
        <div class="hero-actions">
          <RouterLink class="btn btn-primary" to="/alunos">Ver alunos</RouterLink>
        </div>
        <ul class="role-capabilities">
          <li>Consultar lista de alunos e saldos</li>
          <li>Distribuir moedas aos alunos (em breve)</li>
          <li>Consultar extrato de envios (em breve)</li>
          <li>Receber 1.000 moedas por semestre (automático, em breve)</li>
        </ul>
      </template>

      <template v-else-if="tipo === 'EMPRESA'">
        <h1>Área da empresa parceira</h1>
        <p>
          Mantenha os dados da sua empresa atualizados e cadastre vantagens para os alunos
          resgatarem com moedas.
        </p>
        <div class="hero-actions">
          <RouterLink class="btn btn-primary" to="/minha-empresa">Minha empresa</RouterLink>
        </div>
        <ul class="role-capabilities">
          <li>Editar cadastro da empresa</li>
          <li>Cadastrar vantagens com foto e custo (em breve)</li>
          <li>Conferir resgates por código de cupom (em breve)</li>
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
        <strong>{{ loading && tipo === 'ALUNO' ? '…' : saldoExibido }}</strong>
        <small>moedas</small>
      </div>
    </div>
  </section>
</template>
