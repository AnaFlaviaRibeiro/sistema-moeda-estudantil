<script setup lang="ts">
import { onMounted, ref } from 'vue'
import { transacoesApi } from '../../api/transacoes'
import { extractErrorMessage } from '../../api/client'
import type { Transacao } from '../../types'

const resgates = ref<Transacao[]>([])
const loading = ref(false)
const conferindo = ref<string | null>(null)
const error = ref<string | null>(null)
const success = ref<string | null>(null)
const codigoBusca = ref('')

function formatDate(iso: string) {
  return new Date(iso).toLocaleString('pt-BR')
}

async function load() {
  loading.value = true
  error.value = null
  try {
    resgates.value = await transacoesApi.resgatesEmpresa()
  } catch (e) {
    error.value = extractErrorMessage(e)
  } finally {
    loading.value = false
  }
}

async function conferir(codigo: string) {
  conferindo.value = codigo
  error.value = null
  success.value = null
  try {
    await transacoesApi.conferirCupom(codigo)
    success.value = `Cupom ${codigo} conferido com sucesso.`
    await load()
  } catch (e) {
    error.value = extractErrorMessage(e)
  } finally {
    conferindo.value = null
  }
}

async function conferirPorCodigo() {
  const codigo = codigoBusca.value.trim().toUpperCase()
  if (!codigo) return
  await conferir(codigo)
  codigoBusca.value = ''
}

onMounted(load)
</script>

<template>
  <div class="page-header">
    <div>
      <h1>Resgates</h1>
      <p>Conferir cupons apresentados pelos alunos na troca presencial.</p>
    </div>
  </div>

  <div class="conferir-cupom-bar">
    <input
      v-model="codigoBusca"
      placeholder="Digite o código do cupom"
      maxlength="12"
      @keyup.enter="conferirPorCodigo"
    />
    <button class="btn btn-primary" type="button" :disabled="!codigoBusca.trim()" @click="conferirPorCodigo">
      Conferir código
    </button>
  </div>

  <div v-if="error" class="alert alert-error">{{ error }}</div>
  <div v-if="success" class="alert alert-success">{{ success }}</div>
  <div v-if="loading" class="alert alert-info">Carregando…</div>

  <div v-if="!loading && resgates.length === 0" class="empty">
    Nenhum resgate registrado ainda.
  </div>

  <table v-else-if="resgates.length > 0">
    <thead>
      <tr>
        <th>Data</th>
        <th>Aluno</th>
        <th>Vantagem</th>
        <th>Valor</th>
        <th>Código</th>
        <th>Status</th>
        <th></th>
      </tr>
    </thead>
    <tbody>
      <tr v-for="r in resgates" :key="r.id">
        <td>{{ formatDate(r.dataHora) }}</td>
        <td>{{ r.alunoNome }}</td>
        <td>{{ r.vantagemNome }}</td>
        <td>{{ r.valor }} moedas</td>
        <td><code>{{ r.codigoCupom }}</code></td>
        <td>
          <span :class="r.cupomConferido ? 'badge badge-success' : 'badge'">
            {{ r.cupomConferido ? 'Conferido' : 'Pendente' }}
          </span>
        </td>
        <td>
          <button
            v-if="!r.cupomConferido && r.codigoCupom"
            class="btn-link"
            type="button"
            :disabled="conferindo === r.codigoCupom"
            @click="conferir(r.codigoCupom!)"
          >
            {{ conferindo === r.codigoCupom ? 'Conferindo…' : 'Conferir' }}
          </button>
        </td>
      </tr>
    </tbody>
  </table>
</template>
