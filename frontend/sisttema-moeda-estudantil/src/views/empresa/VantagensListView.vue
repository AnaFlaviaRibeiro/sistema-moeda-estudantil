<script setup lang="ts">
import { onMounted, ref } from 'vue'
import { RouterLink } from 'vue-router'
import { vantagensApi } from '../../api/vantagens'
import { extractErrorMessage } from '../../api/client'
import type { Vantagem } from '../../types'

const vantagens = ref<Vantagem[]>([])
const loading = ref(false)
const error = ref<string | null>(null)

async function load() {
  loading.value = true
  error.value = null
  try {
    vantagens.value = await vantagensApi.listMinhas()
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
      <h1>Minhas vantagens</h1>
      <p>Cadastre e gerencie as vantagens oferecidas aos alunos.</p>
    </div>
    <RouterLink class="btn btn-primary" to="/empresa/vantagens/nova">Nova vantagem</RouterLink>
  </div>

  <div v-if="error" class="alert alert-error">{{ error }}</div>
  <div v-if="loading" class="alert alert-info">Carregando…</div>

  <div v-if="!loading && vantagens.length === 0" class="empty">
    Nenhuma vantagem cadastrada. <RouterLink to="/empresa/vantagens/nova">Cadastrar primeira vantagem</RouterLink>
  </div>

  <table v-else-if="vantagens.length > 0">
    <thead>
      <tr>
        <th>Nome</th>
        <th>Custo</th>
        <th>Status</th>
        <th></th>
      </tr>
    </thead>
    <tbody>
      <tr v-for="v in vantagens" :key="v.id">
        <td>{{ v.nome }}</td>
        <td>{{ v.custoEmMoedas }} moedas</td>
        <td>
          <span :class="v.ativa ? 'badge badge-success' : 'badge'">
            {{ v.ativa ? 'Ativa' : 'Inativa' }}
          </span>
        </td>
        <td>
          <RouterLink class="btn-link" :to="`/empresa/vantagens/${v.id}/editar`">Editar</RouterLink>
        </td>
      </tr>
    </tbody>
  </table>
</template>
