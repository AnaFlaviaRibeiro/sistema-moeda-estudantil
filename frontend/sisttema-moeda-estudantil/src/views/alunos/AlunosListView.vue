<script setup lang="ts">
import { onMounted, reactive, ref } from 'vue'
import { alunosApi } from '../../api/alunos'
import { professoresApi } from '../../api/professores'
import { transacoesApi } from '../../api/transacoes'
import { extractErrorMessage } from '../../api/client'
import type { Aluno } from '../../types'

const alunos = ref<Aluno[]>([])
const saldoProfessor = ref<number | null>(null)
const loading = ref(false)
const error = ref<string | null>(null)
const success = ref<string | null>(null)

const modalAberto = ref(false)
const alunoSelecionado = ref<Aluno | null>(null)
const enviando = ref(false)

const form = reactive({
  valor: 10,
  motivo: '',
})

async function load() {
  loading.value = true
  error.value = null
  try {
    const [lista, professor] = await Promise.all([alunosApi.list(), professoresApi.me()])
    alunos.value = lista
    saldoProfessor.value = professor.saldo
  } catch (e) {
    error.value = extractErrorMessage(e)
  } finally {
    loading.value = false
  }
}

function abrirDistribuir(aluno: Aluno) {
  alunoSelecionado.value = aluno
  form.valor = 10
  form.motivo = ''
  modalAberto.value = true
}

function fecharModal() {
  modalAberto.value = false
  alunoSelecionado.value = null
}

async function distribuir() {
  if (!alunoSelecionado.value) return
  enviando.value = true
  error.value = null
  success.value = null
  try {
    await transacoesApi.distribuir({
      alunoId: alunoSelecionado.value.id,
      valor: form.valor,
      motivo: form.motivo.trim(),
    })
    success.value = `Moedas enviadas para ${alunoSelecionado.value.nome}.`
    fecharModal()
    await load()
  } catch (e) {
    error.value = extractErrorMessage(e)
  } finally {
    enviando.value = false
  }
}

onMounted(load)
</script>

<template>
  <div class="page-header">
    <div>
      <h1>Alunos</h1>
      <p>Consulte os alunos cadastrados e distribua moedas de mérito.</p>
    </div>
    <span v-if="saldoProfessor != null" class="badge badge-lg">
      Seu saldo: {{ saldoProfessor }} moedas
    </span>
  </div>

  <div v-if="error" class="alert alert-error">{{ error }}</div>
  <div v-if="success" class="alert alert-success">{{ success }}</div>
  <div v-if="loading" class="alert alert-info">Carregando…</div>

  <div v-if="!loading && alunos.length === 0" class="empty">
    Nenhum aluno cadastrado ainda.
  </div>

  <table v-else-if="alunos.length > 0">
    <thead>
      <tr>
        <th>Nome</th>
        <th>E-mail</th>
        <th>CPF</th>
        <th>Curso</th>
        <th>Instituição</th>
        <th>Saldo</th>
        <th></th>
      </tr>
    </thead>
    <tbody>
      <tr v-for="a in alunos" :key="a.id">
        <td>{{ a.nome }}</td>
        <td>{{ a.email }}</td>
        <td>{{ a.cpf }}</td>
        <td>{{ a.curso }}</td>
        <td>{{ a.instituicao?.nome }}</td>
        <td><span class="badge">{{ a.saldo }} moedas</span></td>
        <td>
          <button class="btn-link" type="button" @click="abrirDistribuir(a)">
            Enviar moedas
          </button>
        </td>
      </tr>
    </tbody>
  </table>

  <div v-if="modalAberto && alunoSelecionado" class="modal-overlay" @click.self="fecharModal">
    <div class="modal-card">
      <h2>Enviar moedas</h2>
      <p>Aluno: <strong>{{ alunoSelecionado.nome }}</strong></p>
      <form @submit.prevent="distribuir">
        <label>
          Quantidade de moedas
          <input v-model.number="form.valor" type="number" min="1" required />
        </label>
        <label>
          Motivo (obrigatório)
          <textarea v-model="form.motivo" rows="3" required maxlength="500" />
        </label>
        <div class="form-actions">
          <button class="btn" type="button" @click="fecharModal">Cancelar</button>
          <button class="btn btn-primary" type="submit" :disabled="enviando || !form.motivo.trim()">
            {{ enviando ? 'Enviando…' : 'Confirmar envio' }}
          </button>
        </div>
      </form>
    </div>
  </div>
</template>
