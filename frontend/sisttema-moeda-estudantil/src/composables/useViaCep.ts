import { ref } from 'vue'
import { buscarEnderecoPorCep } from '../api/viacep'
import { maskCep, onlyDigits } from '../utils/masks'
import type { Endereco } from '../types'

export function useViaCep() {
  const cepLoading = ref(false)
  const cepError = ref<string | null>(null)

  let ultimoCepConsultado = ''

  async function preencherEndereco(endereco: Endereco, cep: string) {
    const digits = onlyDigits(cep)
    if (digits.length !== 8) {
      ultimoCepConsultado = ''
      cepError.value = null
      return
    }

    if (digits === ultimoCepConsultado) {
      return
    }

    cepLoading.value = true
    cepError.value = null

    try {
      const data = await buscarEnderecoPorCep(digits)
      ultimoCepConsultado = digits

      endereco.cep = maskCep(data.cep || digits)
      endereco.logradouro = data.logradouro ?? ''
      endereco.bairro = data.bairro ?? ''
      endereco.cidade = data.localidade ?? ''
      endereco.uf = data.uf ?? ''
    } catch (error) {
      ultimoCepConsultado = ''
      cepError.value = error instanceof Error ? error.message : 'Erro ao buscar CEP.'
    } finally {
      cepLoading.value = false
    }
  }

  function onCepChange(endereco: Endereco, cep: string) {
    endereco.cep = cep
    void preencherEndereco(endereco, cep)
  }

  return {
    cepLoading,
    cepError,
    preencherEndereco,
    onCepChange,
  }
}
