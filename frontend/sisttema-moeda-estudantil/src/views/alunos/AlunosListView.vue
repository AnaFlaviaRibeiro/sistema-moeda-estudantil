<script setup lang="ts">
import { onMounted, ref } from 'vue'
import { alunosApi } from '../../api/alunos'
import { extractErrorMessage } from '../../api/client'
import type { Aluno } from '../../types'

const alunos = ref<Aluno[]>([])
const loading = ref(false)
const error = ref<string | null>(null)

async function load() {
  loading.value = true
  error.value = null
  try {
    alunos.value = await alunosApi.list()
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
      <h1>Alunos</h1>
      <p>Consulte os alunos cadastrados para reconhecer o mérito com moedas.</p>
    </div>
  </div>

  <div v-if="error" class="alert alert-error">{{ error }}</div>
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
      </tr>
    </tbody>
  </table>
</template>
