<script setup lang="ts">
import { computed, onMounted, reactive, ref } from 'vue'
import { useRouter } from 'vue-router'
import { alunosApi } from '../../api/alunos'
import { instituicoesApi } from '../../api/instituicoes'
import { extractErrorMessage } from '../../api/client'
import type { AlunoCreate, AlunoUpdate, Instituicao } from '../../types'

const props = defineProps<{ id?: string }>()

const router = useRouter()
const isEdit = computed(() => Boolean(props.id))

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
    if (isEdit.value && props.id) {
      const a = await alunosApi.get(Number(props.id))
      form.nome = a.nome
      form.email = a.email
      form.cpf = a.cpf
      form.rg = a.rg ?? ''
      form.curso = a.curso
      form.instituicaoId = a.instituicao?.id ?? 0
      form.endereco = {
        logradouro: a.endereco?.logradouro ?? '',
        numero: a.endereco?.numero ?? '',
        complemento: a.endereco?.complemento ?? '',
        bairro: a.endereco?.bairro ?? '',
        cidade: a.endereco?.cidade ?? '',
        uf: a.endereco?.uf ?? '',
        cep: a.endereco?.cep ?? '',
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
    if (isEdit.value && props.id) {
      const dto: AlunoUpdate = {
        nome: form.nome,
        email: form.email,
        rg: form.rg || undefined,
        curso: form.curso,
        instituicaoId: form.instituicaoId,
        endereco: form.endereco,
      }
      await alunosApi.update(Number(props.id), dto)
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
    router.push({ name: 'alunos-list' })
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
      <h1>{{ isEdit ? 'Editar aluno' : 'Novo aluno' }}</h1>
      <p>Preencha os dados do aluno.</p>
    </div>
    <button class="btn" @click="router.push({ name: 'alunos-list' })">← Voltar</button>
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
        <input v-model="form.cpf" required maxlength="14" :disabled="isEdit" />
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
      <button type="button" class="btn" @click="router.push({ name: 'alunos-list' })">Cancelar</button>
      <button type="submit" class="btn btn-primary" :disabled="saving">
        {{ saving ? 'Salvando…' : isEdit ? 'Salvar alterações' : 'Cadastrar' }}
      </button>
    </div>
  </form>
</template>
