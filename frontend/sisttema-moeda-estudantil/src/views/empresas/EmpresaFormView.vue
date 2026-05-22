<script setup lang="ts">
import { computed, onMounted, reactive, ref } from 'vue'
import { complementoOptionsFor, parseUfFromApi, ufOptionsFor } from '../../constants/endereco'
import { useRoute, useRouter } from 'vue-router'
import { empresasApi } from '../../api/empresas'
import { extractErrorMessage } from '../../api/client'
import { useAuth } from '../../composables/useAuth'
import { maskCep, maskCnpj, maskTelefone, onCepInput, onCnpjInput, onTelefoneInput } from '../../utils/masks'
import type { EmpresaParceiraCreate, EmpresaParceiraUpdate } from '../../types'

const props = defineProps<{ id?: string }>()

const router = useRouter()
const route = useRoute()
const auth = useAuth()

const isOwnProfile = computed(() => route.name === 'minha-empresa')
const isEdit = computed(() => isOwnProfile.value || Boolean(props.id))
const editId = computed(() =>
  isOwnProfile.value ? auth.user.value?.id : props.id ? Number(props.id) : undefined,
)

const complementoOpcoes = computed(() =>
  complementoOptionsFor(form.endereco?.complemento),
)

const ufOpcoes = computed(() => ufOptionsFor(form.endereco?.uf))

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
  if (!isEdit.value) return
  loading.value = true
  try {
    const e = isOwnProfile.value
      ? await empresasApi.me()
      : await empresasApi.get(Number(props.id))
    form.razaoSocial = e.razaoSocial
    form.cnpj = maskCnpj(e.cnpj)
    form.email = e.email
    form.contato = maskTelefone(e.contato ?? '')
    form.endereco = {
      logradouro: e.endereco?.logradouro ?? '',
      numero: e.endereco?.numero ?? '',
      complemento: e.endereco?.complemento ?? '',
      bairro: e.endereco?.bairro ?? '',
      cidade: e.endereco?.cidade ?? '',
      uf: parseUfFromApi(e.endereco?.uf),
      cep: maskCep(e.endereco?.cep ?? ''),
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
    if (isEdit.value && editId.value) {
      const dto: EmpresaParceiraUpdate = {
        razaoSocial: form.razaoSocial,
        email: form.email,
        contato: form.contato,
        endereco: form.endereco,
      }
      await empresasApi.update(editId.value, dto)
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
    if (isOwnProfile.value || isEdit.value) {
      router.push({ name: 'home' })
    } else {
      router.push({ name: 'login' })
    }
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
      <h1>{{ isOwnProfile ? 'Minha empresa' : isEdit ? 'Editar empresa' : 'Nova empresa parceira' }}</h1>
      <p>{{ isOwnProfile ? 'Atualize os dados da sua empresa parceira.' : 'Preencha os dados da empresa.' }}</p>
    </div>
    <button class="btn" @click="router.push({ name: isOwnProfile ? 'home' : 'login' })">← Voltar</button>
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
        <input
          :value="form.cnpj"
          required
          maxlength="18"
          inputmode="numeric"
          autocomplete="off"
          placeholder="00.000.000/0000-00"
          :disabled="isEdit"
          @input="onCnpjInput($event, v => (form.cnpj = v))"
        />
      </label>
    </div>

    <div class="form-row-3">
      <label>
        E-mail
        <input v-model="form.email" type="email" required maxlength="150" />
      </label>
      <label>
        Contato
        <input
          :value="form.contato"
          maxlength="15"
          inputmode="tel"
          autocomplete="tel"
          placeholder="(00) 00000-0000"
          @input="onTelefoneInput($event, v => (form.contato = v))"
        />
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
          <select v-model="form.endereco!.complemento">
            <option v-for="op in complementoOpcoes" :key="op.value" :value="op.value">
              {{ op.label }}
            </option>
          </select>
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
          <select v-model="form.endereco!.uf">
            <option v-for="op in ufOpcoes" :key="op.value" :value="op.value">
              {{ op.label }}
            </option>
          </select>
        </label>
      </div>
      <label style="margin-top: 12px; max-width: 200px;">
        CEP
        <input
          :value="form.endereco!.cep"
          maxlength="9"
          inputmode="numeric"
          autocomplete="postal-code"
          placeholder="00000-000"
          @input="onCepInput($event, v => (form.endereco!.cep = v))"
        />
      </label>
    </fieldset>

    <div class="form-actions">
      <button type="button" class="btn" @click="router.push({ name: isOwnProfile ? 'home' : 'login' })">Cancelar</button>
      <button type="submit" class="btn btn-primary" :disabled="saving">
        {{ saving ? 'Salvando…' : isEdit ? 'Salvar alterações' : 'Cadastrar' }}
      </button>
    </div>
  </form>
</template>
