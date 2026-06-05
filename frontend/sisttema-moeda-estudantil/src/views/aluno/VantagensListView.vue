<script setup lang="ts">
import { onMounted, ref } from 'vue'
import { alunosApi } from '../../api/alunos'
import { transacoesApi } from '../../api/transacoes'
import { vantagensApi } from '../../api/vantagens'
import { extractErrorMessage } from '../../api/client'
import type { Vantagem } from '../../types'

const vantagens = ref<Vantagem[]>([])
const saldo = ref(0)
const loading = ref(false)
const resgatando = ref<number | null>(null)
const error = ref<string | null>(null)
const success = ref<string | null>(null)

async function load() {
  loading.value = true
  error.value = null
  try {
    const [lista, aluno] = await Promise.all([vantagensApi.list(), alunosApi.me()])
    vantagens.value = lista
    saldo.value = aluno.saldo
  } catch (e) {
    error.value = extractErrorMessage(e)
  } finally {
    loading.value = false
  }
}

async function resgatar(vantagem: Vantagem) {
  if (!confirm(`Resgatar "${vantagem.nome}" por ${vantagem.custoEmMoedas} moedas?`)) return
  resgatando.value = vantagem.id
  error.value = null
  success.value = null
  try {
    const transacao = await transacoesApi.resgatar({ vantagemId: vantagem.id })
    success.value = `Resgate confirmado! Código do cupom: ${transacao.codigoCupom}`
    await load()
  } catch (e) {
    error.value = extractErrorMessage(e)
  } finally {
    resgatando.value = null
  }
}

onMounted(load)
</script>

<template>
  <div class="page-header">
    <div>
      <h1>Vantagens</h1>
      <p>Troque suas moedas por descontos e produtos das empresas parceiras.</p>
    </div>
    <span class="badge badge-lg">Saldo: {{ saldo }} moedas</span>
  </div>

  <div v-if="error" class="alert alert-error">{{ error }}</div>
  <div v-if="success" class="alert alert-success">{{ success }}</div>
  <div v-if="loading" class="alert alert-info">Carregando…</div>

  <div v-if="!loading && vantagens.length === 0" class="empty">
    Nenhuma vantagem disponível no momento.
  </div>

  <div v-else-if="vantagens.length > 0" class="card-grid">
    <article v-for="v in vantagens" :key="v.id" class="vantagem-card">
      <div v-if="v.fotoUrl" class="vantagem-card-img">
        <img :src="v.fotoUrl" :alt="v.nome" />
      </div>
      <div v-else class="vantagem-card-img vantagem-card-img-placeholder">🎁</div>
      <div class="vantagem-card-body">
        <span class="vantagem-empresa">{{ v.empresaNome }}</span>
        <h2>{{ v.nome }}</h2>
        <p>{{ v.descricao || 'Sem descrição.' }}</p>
        <div class="vantagem-card-footer">
          <strong>{{ v.custoEmMoedas }} moedas</strong>
          <button
            class="btn btn-primary"
            type="button"
            :disabled="saldo < v.custoEmMoedas || resgatando === v.id"
            @click="resgatar(v)"
          >
            {{ resgatando === v.id ? 'Resgatando…' : 'Resgatar' }}
          </button>
        </div>
      </div>
    </article>
  </div>
</template>
