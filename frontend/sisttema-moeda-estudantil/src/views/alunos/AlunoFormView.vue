<script setup lang="ts">
import { computed, onMounted, reactive, ref } from 'vue'
import { complementoOptionsFor, parseUfFromApi, ufOptionsFor } from '../../constants/endereco'
import { useRoute, useRouter } from 'vue-router'
import { alunosApi } from '../../api/alunos'
import { instituicoesApi } from '../../api/instituicoes'
import { extractErrorMessage } from '../../api/client'
import { useAuth } from '../../composables/useAuth'
import { maskCep, maskCpf, onCepInput, onCpfInput } from '../../utils/masks'
import type { AlunoCreate, AlunoUpdate, Instituicao } from '../../types'

const props = defineProps<{ id?: string }>()

const router = useRouter()
const route = useRoute()
const auth = useAuth()

const isOwnProfile = computed(() => route.name === 'meu-perfil')
const isEdit = computed(() => isOwnProfile.value || Boolean(props.id))
const editId = computed(() =>
  isOwnProfile.value ? auth.user.value?.id : props.id ? Number(props.id) : undefined,
)

const complementoOpcoes = computed(() =>
  complementoOptionsFor(form.endereco?.complemento),
)

const ufOpcoes = computed(() => ufOptionsFor(form.endereco?.uf))

const form = reactive<AlunoCreate & AlunoUpdate>({
  nome: '',
  email: '',
  senha: '',
  cpf: '',
  rg: '',
  curso: '',
  instituicaoId: 0,
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

const instituicoes = ref<Instituicao[]>([])
const error = ref<string | null>(null)
const loading = ref(false)
const saving = ref(false)

async function load() {
  loading.value = true
  try {
    instituicoes.value = await instituicoesApi.list()
    if (isOwnProfile.value) {
      const a = await alunosApi.me()
      form.nome = a.nome
      form.email = a.email
      form.cpf = maskCpf(a.cpf)
      form.rg = a.rg ?? ''
      form.curso = a.curso
      form.instituicaoId = a.instituicao?.id ?? 0
      form.endereco = {
        logradouro: a.endereco?.logradouro ?? '',
        numero: a.endereco?.numero ?? '',
        complemento: a.endereco?.complemento ?? '',
        bairro: a.endereco?.bairro ?? '',
        cidade: a.endereco?.cidade ?? '',
        uf: parseUfFromApi(a.endereco?.uf),
        cep: maskCep(a.endereco?.cep ?? ''),
      }
    } else if (isEdit.value && props.id) {
      const a = await alunosApi.get(Number(props.id))
      form.nome = a.nome
      form.email = a.email
      form.cpf = maskCpf(a.cpf)
      form.rg = a.rg ?? ''
      form.curso = a.curso
      form.instituicaoId = a.instituicao?.id ?? 0
      form.endereco = {
        logradouro: a.endereco?.logradouro ?? '',
        numero: a.endereco?.numero ?? '',
        complemento: a.endereco?.complemento ?? '',
        bairro: a.endereco?.bairro ?? '',
        cidade: a.endereco?.cidade ?? '',
        uf: parseUfFromApi(a.endereco?.uf),
        cep: maskCep(a.endereco?.cep ?? ''),
      }
    } else if (instituicoes.value.length > 0) {
      form.instituicaoId = instituicoes.value[0].id
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
      const dto: AlunoUpdate = {
        nome: form.nome,
        email: form.email,
        rg: form.rg || undefined,
        curso: form.curso,
        instituicaoId: form.instituicaoId,
        endereco: form.endereco,
      }
      await alunosApi.update(editId.value, dto)
    } else {
      const dto: AlunoCreate = {
        nome: form.nome,
        email: form.email,
        senha: form.senha,
        cpf: form.cpf,
        rg: form.rg || undefined,
        curso: form.curso,
        instituicaoId: form.instituicaoId,
        endereco: form.endereco,
      }
      await alunosApi.create(dto)
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
      <h1>{{ isOwnProfile ? 'Meu cadastro' : isEdit ? 'Editar aluno' : 'Novo aluno' }}</h1>
      <p>{{ isOwnProfile ? 'Atualize seus dados de participação no programa.' : 'Preencha os dados do aluno.' }}</p>
    </div>
    <button class="btn" @click="router.push({ name: isOwnProfile ? 'home' : 'login' })">← Voltar</button>
  </div>

  <div v-if="error" class="alert alert-error">{{ error }}</div>
  <div v-if="loading" class="alert alert-info">Carregando…</div>

  <form v-if="!loading" class="card" @submit.prevent="submit">
    <div class="form-row">
      <label>
        Nome
        <input v-model="form.nome" required maxlength="150" />
      </label>
      <label>
        E-mail
        <input v-model="form.email" type="email" required maxlength="150" />
      </label>
    </div>

    <div class="form-row-3">
      <label>
        CPF
        <input
          :value="form.cpf"
          required
          maxlength="14"
          inputmode="numeric"
          autocomplete="off"
          placeholder="000.000.000-00"
          :disabled="isEdit"
          @input="onCpfInput($event, v => (form.cpf = v))"
        />
      </label>
      <label>
        RG
        <input v-model="form.rg" maxlength="20" />
      </label>
      <label v-if="!isEdit">
        Senha
        <input v-model="form.senha" type="password" required minlength="6" />
      </label>
    </div>

    <div class="form-row">
      <label>
        Curso
        <input v-model="form.curso" required maxlength="100" />
      </label>
      <label>
        Instituição
        <select v-model.number="form.instituicaoId" required>
          <option v-for="i in instituicoes" :key="i.id" :value="i.id">{{ i.nome }}</option>
        </select>
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
