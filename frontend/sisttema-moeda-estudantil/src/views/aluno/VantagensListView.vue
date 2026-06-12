<script setup lang="ts">
import { onMounted, ref } from 'vue'
import { RouterLink } from 'vue-router'
import { alunosApi } from '../../api/alunos'
import { transacoesApi } from '../../api/transacoes'
import { vantagensApi } from '../../api/vantagens'
import { extractErrorMessage } from '../../api/client'
import type { Transacao, Vantagem } from '../../types'

const vantagens = ref<Vantagem[]>([])
const saldo = ref(0)
const loading = ref(false)
const resgatando = ref<number | null>(null)
const error = ref<string | null>(null)
const cupomResgatado = ref<Transacao | null>(null)

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
  try {
    const transacao = await transacoesApi.resgatar({ vantagemId: vantagem.id })
    cupomResgatado.value = transacao
    await load()
  } catch (e) {
    error.value = extractErrorMessage(e)
  } finally {
    resgatando.value = null
  }
}

function fecharModal() {
  cupomResgatado.value = null
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

  <div v-if="cupomResgatado" class="modal-overlay" @click.self="fecharModal">
    <div class="modal-card cupom-modal">
      <span class="eyebrow">Resgate confirmado</span>
      <h2>{{ cupomResgatado.vantagemNome }}</h2>
      <p>
        O valor de <strong>{{ cupomResgatado.valor }} moedas</strong> foi debitado do seu saldo.
        Um e-mail com este cupom também foi enviado para você e para a empresa parceira.
      </p>
      <div class="cupom-codigo">
        <span>Código do cupom</span>
        <code>{{ cupomResgatado.codigoCupom }}</code>
      </div>
      <p class="cupom-hint">Apresente este código na troca presencial.</p>
      <div class="form-actions">
        <RouterLink class="btn" to="/extrato" @click="fecharModal">Ver extrato</RouterLink>
        <button class="btn btn-primary" type="button" @click="fecharModal">Fechar</button>
      </div>
    </div>
  </div>
</template>
