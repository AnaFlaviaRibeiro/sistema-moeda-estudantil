<script setup lang="ts">
import { onMounted, ref } from 'vue'
import { transacoesApi } from '../../api/transacoes'
import { extractErrorMessage } from '../../api/client'
import type { Extrato } from '../../types'

const extrato = ref<Extrato | null>(null)
const loading = ref(false)
const error = ref<string | null>(null)

const tipoLabel: Record<string, string> = {
  DISTRIBUICAO: 'Envio',
  CREDITO_SEMESTRAL: 'Crédito semestral',
}

function formatDate(iso: string) {
  return new Date(iso).toLocaleString('pt-BR')
}

async function load() {
  loading.value = true
  error.value = null
  try {
    extrato.value = await transacoesApi.extratoProfessor()
  } catch (e) {
    error.value = extractErrorMessage(e)
  } finally {
    loading.value = false
  }
}

onMounted(load)
</script>

<template>
  <div class="page-header">
    <div>
      <h1>Extrato do professor</h1>
      <p>Histórico de envios de moedas e créditos semestrais.</p>
    </div>
    <span v-if="extrato" class="badge badge-lg">Saldo: {{ extrato.saldo }} moedas</span>
  </div>

  <div v-if="error" class="alert alert-error">{{ error }}</div>
  <div v-if="loading" class="alert alert-info">Carregando…</div>

  <div v-if="!loading && extrato && extrato.transacoes.length === 0" class="empty">
    Nenhuma transação registrada ainda.
  </div>

  <table v-else-if="extrato && extrato.transacoes.length > 0">
    <thead>
      <tr>
        <th>Data</th>
        <th>Tipo</th>
        <th>Valor</th>
        <th>Aluno</th>
        <th>Descrição</th>
      </tr>
    </thead>
    <tbody>
      <tr v-for="t in extrato.transacoes" :key="t.id">
        <td>{{ formatDate(t.dataHora) }}</td>
        <td>{{ tipoLabel[t.tipo] ?? t.tipo }}</td>
        <td>
          <span :class="t.tipo === 'DISTRIBUICAO' ? 'text-debit' : 'text-credit'">
            {{ t.tipo === 'DISTRIBUICAO' ? '-' : '+' }}{{ t.valor }}
          </span>
        </td>
        <td>{{ t.alunoNome ?? '—' }}</td>
        <td>{{ t.descricao }}</td>
      </tr>
    </tbody>
  </table>
</template>
