export interface OpcaoSelect {
  value: string
  label: string
}

function normalizeUf(value?: string | null): string {
  return (value ?? '').replace(/[^a-zA-Z]/g, '').toUpperCase().slice(0, 2)
}

export const UF_OPCOES: OpcaoSelect[] = [
  { value: '', label: 'Selecione' },
  { value: 'AC', label: 'AC — Acre' },
  { value: 'AL', label: 'AL — Alagoas' },
  { value: 'AP', label: 'AP — Amapá' },
  { value: 'AM', label: 'AM — Amazonas' },
  { value: 'BA', label: 'BA — Bahia' },
  { value: 'CE', label: 'CE — Ceará' },
  { value: 'DF', label: 'DF — Distrito Federal' },
  { value: 'ES', label: 'ES — Espírito Santo' },
  { value: 'GO', label: 'GO — Goiás' },
  { value: 'MA', label: 'MA — Maranhão' },
  { value: 'MT', label: 'MT — Mato Grosso' },
  { value: 'MS', label: 'MS — Mato Grosso do Sul' },
  { value: 'MG', label: 'MG — Minas Gerais' },
  { value: 'PA', label: 'PA — Pará' },
  { value: 'PB', label: 'PB — Paraíba' },
  { value: 'PR', label: 'PR — Paraná' },
  { value: 'PE', label: 'PE — Pernambuco' },
  { value: 'PI', label: 'PI — Piauí' },
  { value: 'RJ', label: 'RJ — Rio de Janeiro' },
  { value: 'RN', label: 'RN — Rio Grande do Norte' },
  { value: 'RS', label: 'RS — Rio Grande do Sul' },
  { value: 'RO', label: 'RO — Rondônia' },
  { value: 'RR', label: 'RR — Roraima' },
  { value: 'SC', label: 'SC — Santa Catarina' },
  { value: 'SP', label: 'SP — São Paulo' },
  { value: 'SE', label: 'SE — Sergipe' },
  { value: 'TO', label: 'TO — Tocantins' },
]

export function ufOptionsFor(valorAtual?: string | null): OpcaoSelect[] {
  const valor = normalizeUf(valorAtual)
  if (!valor) return UF_OPCOES
  if (UF_OPCOES.some(o => o.value === valor)) return UF_OPCOES
  return [...UF_OPCOES, { value: valor, label: valor }]
}

export function parseUfFromApi(valorAtual?: string | null): string {
  return normalizeUf(valorAtual)
}

export const COMPLEMENTO_OPCOES: OpcaoSelect[] = [
  { value: '', label: 'Nenhum' },
  { value: 'Apartamento', label: 'Apartamento' },
  { value: 'Bloco', label: 'Bloco' },
  { value: 'Sala', label: 'Sala' },
  { value: 'Casa', label: 'Casa' },
  { value: 'Loja', label: 'Loja' },
  { value: 'Fundos', label: 'Fundos' },
  { value: 'Cobertura', label: 'Cobertura' },
  { value: 'Andar', label: 'Andar' },
  { value: 'Galpão', label: 'Galpão' },
  { value: 'Kitnet', label: 'Kitnet' },
  { value: 'Outro', label: 'Outro' },
]

/** Inclui valor já salvo no banco se não estiver na lista padrão. */
export function complementoOptionsFor(valorAtual?: string | null): OpcaoSelect[] {
  const valor = valorAtual?.trim() ?? ''
  if (!valor) return COMPLEMENTO_OPCOES
  if (COMPLEMENTO_OPCOES.some(o => o.value === valor)) return COMPLEMENTO_OPCOES
  return [...COMPLEMENTO_OPCOES, { value: valor, label: valor }]
}
