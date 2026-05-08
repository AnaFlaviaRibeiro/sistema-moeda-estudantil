<script setup lang="ts">
import { computed, onMounted, reactive, ref } from 'vue'
import { useRouter } from 'vue-router'
import { empresasApi } from '../../api/empresas'
import { extractErrorMessage } from '../../api/client'
import type { EmpresaParceiraCreate, EmpresaParceiraUpdate } from '../../types'

const props = defineProps<{ id?: string }>()

const router = useRouter()
const isEdit = computed(() => Boolean(props.id))

const form = reactive<EmpresaParceiraCreate & EmpresaParceiraUpdate>({
  razaoSocial: '',
  cnpj: '',
  email: '',
  senha: '',
  contato: '',
  endereco: {
    logradouro: '',
    numero: '',
    complemento: '',
    bairro: '',
    cidade: '',
    uf: '',
    cep: '',
  },
})

const error = ref<string | null>(null)
const loading = ref(false)
const saving = ref(false)

async function load() {
  if (!isEdit.value || !props.id) return
  loading.value = true
  try {
    const e = await empresasApi.get(Number(props.id))
    form.razaoSocial = e.razaoSocial
    form.cnpj = e.cnpj
    form.email = e.email
    form.contato = e.contato ?? ''
    form.endereco = {
      logradouro: e.endereco?.logradouro ?? '',
      numero: e.endereco?.numero ?? '',
      complemento: e.endereco?.complemento ?? '',
      bairro: e.endereco?.bairro ?? '',
      cidade: e.endereco?.cidade ?? '',
      uf: e.endereco?.uf ?? '',
      cep: e.endereco?.cep ?? '',
    }
  } catch (e) {
    error.value = extractErrorMessage(e)
  } finally {
    loading.value = false
  }
}

async function submit() {
  saving.value = true
  error.value = null
  try {
    if (isEdit.value && props.id) {
      const dto: EmpresaParceiraUpdate = {
        razaoSocial: form.razaoSocial,
        email: form.email,
        contato: form.contato,
        endereco: form.endereco,
      }
      await empresasApi.update(Number(props.id), dto)
    } else {
      const dto: EmpresaParceiraCreate = {
        razaoSocial: form.razaoSocial,
        cnpj: form.cnpj,
        email: form.email,
        senha: form.senha,
        contato: form.contato,
        endereco: form.endereco,
      }
      await empresasApi.create(dto)
    }
    router.push({ name: 'empresas-list' })
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
      <h1>{{ isEdit ? 'Editar empresa' : 'Nova empresa parceira' }}</h1>
      <p>Preencha os dados da empresa.</p>
    </div>
    <button class="btn" @click="router.push({ name: 'empresas-list' })">← Voltar</button>
  </div>

  <div v-if="error" class="alert alert-error">{{ error }}</div>
  <div v-if="loading" class="alert alert-info">Carregando…</div>

  <form v-if="!loading" class="card" @submit.prevent="submit">
    <div class="form-row">
      <label>
        Razão social
        <input v-model="form.razaoSocial" required maxlength="200" />
      </label>
      <label>
        CNPJ
        <input v-model="form.cnpj" required maxlength="18" :disabled="isEdit" />
      </label>
    </div>

    <div class="form-row-3">
      <label>
        E-mail
        <input v-model="form.email" type="email" required maxlength="150" />
      </label>
      <label>
        Contato
        <input v-model="form.contato" maxlength="100" />
      </label>
      <label v-if="!isEdit">
        Senha
        <input v-model="form.senha" type="password" required minlength="6" />
      </label>
    </div>

    <fieldset>
      <legend>Endereço</legend>
      <div class="form-row-3">
        <label>
          Logradouro
          <input v-model="form.endereco!.logradouro" maxlength="200" />
        </label>
        <label>
          Número
          <input v-model="form.endereco!.numero" maxlength="20" />
        </label>
        <label>
          Complemento
          <input v-model="form.endereco!.complemento" maxlength="100" />
        </label>
      </div>
      <div class="form-row-3" style="margin-top: 12px;">
        <label>
          Bairro
          <input v-model="form.endereco!.bairro" maxlength="100" />
        </label>
        <label>
          Cidade
          <input v-model="form.endereco!.cidade" maxlength="100" />
        </label>
        <label>
          UF
          <input v-model="form.endereco!.uf" maxlength="2" />
        </label>
      </div>
      <label style="margin-top: 12px; max-width: 200px;">
        CEP
        <input v-model="form.endereco!.cep" maxlength="9" />
      </label>
    </fieldset>

    <div class="form-actions">
      <button type="button" class="btn" @click="router.push({ name: 'empresas-list' })">Cancelar</button>
      <button type="submit" class="btn btn-primary" :disabled="saving">
        {{ saving ? 'Salvando…' : isEdit ? 'Salvar alterações' : 'Cadastrar' }}
      </button>
    </div>
  </form>
</template>
