<script setup lang="ts">
import { computed, onMounted, reactive, ref } from 'vue'
import { useRouter } from 'vue-router'
import { vantagensApi } from '../../api/vantagens'
import { extractErrorMessage } from '../../api/client'
import type { VantagemCreate, VantagemUpdate } from '../../types'

const props = defineProps<{ id?: string }>()

const router = useRouter()

const isEdit = computed(() => Boolean(props.id))
const editId = computed(() => (props.id ? Number(props.id) : undefined))

const form = reactive<VantagemCreate & VantagemUpdate>({
  nome: '',
  descricao: '',
  fotoUrl: '',
  custoEmMoedas: 10,
  ativa: true,
})

const error = ref<string | null>(null)
const loading = ref(false)
const saving = ref(false)
const fotoPreview = ref<string | null>(null)

async function load() {
  if (!isEdit.value || !editId.value) return
  loading.value = true
  try {
    const v = await vantagensApi.getMinha(editId.value)
    form.nome = v.nome
    form.descricao = v.descricao ?? ''
    form.fotoUrl = v.fotoUrl ?? ''
    form.custoEmMoedas = v.custoEmMoedas
    form.ativa = v.ativa
    fotoPreview.value = v.fotoUrl ?? null
  } catch (e) {
    error.value = extractErrorMessage(e)
  } finally {
    loading.value = false
  }
}

function onFotoChange(event: Event) {
  const input = event.target as HTMLInputElement
  const file = input.files?.[0]
  if (!file) return

  const reader = new FileReader()
  reader.onload = () => {
    const result = reader.result as string
    form.fotoUrl = result
    fotoPreview.value = result
  }
  reader.readAsDataURL(file)
}

async function submit() {
  saving.value = true
  error.value = null
  try {
    if (isEdit.value && editId.value) {
      await vantagensApi.update(editId.value, {
        nome: form.nome,
        descricao: form.descricao,
        fotoUrl: form.fotoUrl,
        custoEmMoedas: form.custoEmMoedas,
        ativa: form.ativa,
      })
    } else {
      await vantagensApi.create({
        nome: form.nome,
        descricao: form.descricao,
        fotoUrl: form.fotoUrl,
        custoEmMoedas: form.custoEmMoedas,
      })
    }
    router.push({ name: 'empresa-vantagens' })
  } catch (e) {
    error.value = extractErrorMessage(e)
  } finally {
    saving.value = false
  }
}

onMounted(load)
</script>

<template>
  <div class="page-header">
    <div>
      <h1>{{ isEdit ? 'Editar vantagem' : 'Nova vantagem' }}</h1>
      <p>Informe descrição, foto e custo em moedas.</p>
    </div>
  </div>

  <div v-if="error" class="alert alert-error">{{ error }}</div>
  <div v-if="loading" class="alert alert-info">Carregando…</div>

  <form v-else class="form-card" @submit.prevent="submit">
    <label>
      Nome
      <input v-model="form.nome" required maxlength="150" />
    </label>

    <label>
      Descrição
      <textarea v-model="form.descricao" rows="4" maxlength="1000" />
    </label>

    <label>
      Custo em moedas
      <input v-model.number="form.custoEmMoedas" type="number" min="1" required />
    </label>

    <label v-if="isEdit">
      Status
      <select v-model="form.ativa">
        <option :value="true">Ativa</option>
        <option :value="false">Inativa</option>
      </select>
    </label>

    <label>
      Foto do produto
      <input type="file" accept="image/*" @change="onFotoChange" />
    </label>

    <div v-if="fotoPreview" class="foto-preview">
      <img :src="fotoPreview" alt="Pré-visualização" />
    </div>

    <div class="form-actions">
      <button class="btn" type="button" @click="router.back()">Cancelar</button>
      <button class="btn btn-primary" type="submit" :disabled="saving">
        {{ saving ? 'Salvando…' : 'Salvar' }}
      </button>
    </div>
  </form>
</template>
