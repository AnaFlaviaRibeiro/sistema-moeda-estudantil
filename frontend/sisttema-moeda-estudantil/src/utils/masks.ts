/** Remove tudo que não for dígito. */
export function onlyDigits(value: string): string {
  return value.replace(/\D/g, '')
}

/** CPF: 000.000.000-00 (máx. 11 dígitos) */
export function maskCpf(value: string): string {
  const d = onlyDigits(value).slice(0, 11)
  if (d.length <= 3) return d
  if (d.length <= 6) return `${d.slice(0, 3)}.${d.slice(3)}`
  if (d.length <= 9) return `${d.slice(0, 3)}.${d.slice(3, 6)}.${d.slice(6)}`
  return `${d.slice(0, 3)}.${d.slice(3, 6)}.${d.slice(6, 9)}-${d.slice(9)}`
}

/** CNPJ: 00.000.000/0000-00 (máx. 14 dígitos) */
export function maskCnpj(value: string): string {
  const d = onlyDigits(value).slice(0, 14)
  if (d.length <= 2) return d
  if (d.length <= 5) return `${d.slice(0, 2)}.${d.slice(2)}`
  if (d.length <= 8) return `${d.slice(0, 2)}.${d.slice(2, 5)}.${d.slice(5)}`
  if (d.length <= 12) return `${d.slice(0, 2)}.${d.slice(2, 5)}.${d.slice(5, 8)}/${d.slice(8)}`
  return `${d.slice(0, 2)}.${d.slice(2, 5)}.${d.slice(5, 8)}/${d.slice(8, 12)}-${d.slice(12)}`
}

export function onCpfInput(event: Event, setter: (value: string) => void) {
  const el = event.target as HTMLInputElement
  setter(maskCpf(el.value))
}

export function onCnpjInput(event: Event, setter: (value: string) => void) {
  const el = event.target as HTMLInputElement
  setter(maskCnpj(el.value))
}

/** Telefone BR: (00) 0000-0000 ou (00) 00000-0000 */
export function maskTelefone(value: string): string {
  const d = onlyDigits(value).slice(0, 11)
  if (d.length === 0) return ''
  if (d.length <= 2) return `(${d}`
  if (d.length <= 6) return `(${d.slice(0, 2)}) ${d.slice(2)}`
  if (d.length <= 10) {
    return `(${d.slice(0, 2)}) ${d.slice(2, 6)}-${d.slice(6)}`
  }
  return `(${d.slice(0, 2)}) ${d.slice(2, 7)}-${d.slice(7)}`
}

export function onTelefoneInput(event: Event, setter: (value: string) => void) {
  const el = event.target as HTMLInputElement
  setter(maskTelefone(el.value))
}

/** CEP: 00000-000 (máx. 8 dígitos) */
export function maskCep(value: string): string {
  const d = onlyDigits(value).slice(0, 8)
  if (d.length <= 5) return d
  return `${d.slice(0, 5)}-${d.slice(5)}`
}

export function onCepInput(event: Event, setter: (value: string) => void) {
  const el = event.target as HTMLInputElement
  setter(maskCep(el.value))
}

/** RG: UF-00.000.000 (ex.: MG-52.345.678) */
export function maskRg(value: string): string {
  const raw = value.toUpperCase().replace(/[^A-Z0-9]/g, '')

  let uf = ''
  let digits = ''

  for (const ch of raw) {
    if (uf.length < 2 && /[A-Z]/.test(ch)) {
      uf += ch
      continue
    }
    if (/[0-9]/.test(ch)) {
      digits += ch
    }
  }

  digits = digits.slice(0, 9)

  if (!uf && digits.length === 0) {
    return ''
  }

  if (!uf) {
    if (digits.length <= 2) return digits
    if (digits.length <= 5) return `${digits.slice(0, 2)}.${digits.slice(2)}`
    if (digits.length <= 8) return `${digits.slice(0, 2)}.${digits.slice(2, 5)}.${digits.slice(5)}`
    return `${digits.slice(0, 2)}.${digits.slice(2, 5)}.${digits.slice(5, 8)}-${digits.slice(8)}`
  }

  let formatted = uf
  if (digits.length === 0) {
    return formatted
  }

  formatted += '-'
  if (digits.length <= 2) return formatted + digits
  if (digits.length <= 5) return `${formatted}${digits.slice(0, 2)}.${digits.slice(2)}`
  return `${formatted}${digits.slice(0, 2)}.${digits.slice(2, 5)}.${digits.slice(5)}`
}

export function onRgInput(event: Event, setter: (value: string) => void) {
  const el = event.target as HTMLInputElement
  setter(maskRg(el.value))
}
