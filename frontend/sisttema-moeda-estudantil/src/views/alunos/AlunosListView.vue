<script setup lang="ts">
import { onMounted, ref } from 'vue'
import { RouterLink, useRouter } from 'vue-router'
import { alunosApi } from '../../api/alunos'
import { extractErrorMessage } from '../../api/client'
import type { Aluno } from '../../types'

const alunos = ref<Aluno[]>([])
const loading = ref(false)
const error = ref<string | null>(null)
const router = useRouter()

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

async function remove(id: number, nome: string) {
  if (!confirm(`Remover o aluno "${nome}"?`)) return
  try {
    await alunosApi.remove(id)
    await load()
  } catch (e) {
    error.value = extractErrorMessage(e)
  }
}

function novo() {
  router.push({ name: 'alunos-novo' })
}

onMounted(load)
</script>

<template>
  <div class="page-header">
    <div>
      <h1>Alunos</h1>
      <p>Gerencie o cadastro dos alunos participantes do programa.</p>
    </div>
    <button class="btn btn-primary" @click="novo">+ Novo aluno</button>
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
        <td style="text-align: right;">
          <RouterLink class="btn-link" :to="{ name: 'alunos-editar', params: { id: a.id } }">Editar</RouterLink>
          <button class="btn-link" style="color: var(--danger)" @click="remove(a.id, a.nome)">Remover</button>
        </td>
      </tr>
    </tbody>
  </table>
</template>
