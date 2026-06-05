<script setup lang="ts">
import { onMounted, ref } from 'vue'
import { transacoesApi } from '../../api/transacoes'
import { extractErrorMessage } from '../../api/client'
import type { Extrato } from '../../types'

const extrato = ref<Extrato | null>(null)
const loading = ref(false)
const error = ref<string | null>(null)

const tipoLabel: Record<string, string> = {
  DISTRIBUICAO: 'Recebimento',
  RESGATE: 'Resgate',
  CREDITO_SEMESTRAL: 'Crédito semestral',
}

function formatDate(iso: string) {
  return new Date(iso).toLocaleString('pt-BR')
}

async function load() {
  loading.value = true
  error.value = null
  try {
    extrato.value = await transacoesApi.extratoAluno()
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
      <h1>Meu extrato</h1>
      <p>Histórico de recebimentos e resgates de moedas.</p>
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
        <th>Descrição</th>
        <th>Cupom</th>
      </tr>
    </thead>
    <tbody>
      <tr v-for="t in extrato.transacoes" :key="t.id">
        <td>{{ formatDate(t.dataHora) }}</td>
        <td>{{ tipoLabel[t.tipo] ?? t.tipo }}</td>
        <td>
          <span :class="t.tipo === 'RESGATE' ? 'text-debit' : 'text-credit'">
            {{ t.tipo === 'RESGATE' ? '-' : '+' }}{{ t.valor }}
          </span>
        </td>
        <td>
          {{ t.descricao }}
          <small v-if="t.professorNome"> · {{ t.professorNome }}</small>
          <small v-if="t.vantagemNome"> · {{ t.vantagemNome }}</small>
        </td>
        <td>{{ t.codigoCupom ?? '—' }}</td>
      </tr>
    </tbody>
  </table>
</template>
