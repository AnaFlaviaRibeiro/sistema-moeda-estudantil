<script setup lang="ts">
import { onMounted, ref } from 'vue'
import { RouterLink, useRouter } from 'vue-router'
import { empresasApi } from '../../api/empresas'
import { extractErrorMessage } from '../../api/client'
import type { EmpresaParceira } from '../../types'

const empresas = ref<EmpresaParceira[]>([])
const loading = ref(false)
const error = ref<string | null>(null)
const router = useRouter()

async function load() {
  loading.value = true
  error.value = null
  try {
    empresas.value = await empresasApi.list()
  } catch (e) {
    error.value = extractErrorMessage(e)
  } finally {
    loading.value = false
  }
}

async function remove(id: number, nome: string) {
  if (!confirm(`Remover a empresa "${nome}"?`)) return
  try {
    await empresasApi.remove(id)
    await load()
  } catch (e) {
    error.value = extractErrorMessage(e)
  }
}

function nova() {
  router.push({ name: 'empresas-nova' })
}

onMounted(load)
</script>

<template>
  <div class="page-header">
    <div>
      <h1>Empresas Parceiras</h1>
      <p>Gerencie as empresas que oferecem vantagens aos alunos.</p>
    </div>
    <button class="btn btn-primary" @click="nova">+ Nova empresa</button>
  </div>

  <div v-if="error" class="alert alert-error">{{ error }}</div>
  <div v-if="loading" class="alert alert-info">Carregando…</div>

  <div v-if="!loading && empresas.length === 0" class="empty">
    Nenhuma empresa parceira cadastrada ainda.
  </div>

  <table v-else-if="empresas.length > 0">
    <thead>
      <tr>
        <th>Razão social</th>
        <th>CNPJ</th>
        <th>E-mail</th>
        <th>Contato</th>
        <th>Cidade/UF</th>
        <th></th>
      </tr>
    </thead>
    <tbody>
      <tr v-for="e in empresas" :key="e.id">
        <td>{{ e.razaoSocial }}</td>
        <td>{{ e.cnpj }}</td>
        <td>{{ e.email }}</td>
        <td>{{ e.contato }}</td>
        <td>{{ e.endereco?.cidade }}{{ e.endereco?.uf ? '/' + e.endereco.uf : '' }}</td>
        <td style="text-align: right;">
          <RouterLink class="btn-link" :to="{ name: 'empresas-editar', params: { id: e.id } }">Editar</RouterLink>
          <button class="btn-link" style="color: var(--danger)" @click="remove(e.id, e.razaoSocial)">Remover</button>
        </td>
      </tr>
    </tbody>
  </table>
</template>
