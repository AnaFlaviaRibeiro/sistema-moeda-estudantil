export const CURSO_OUTROS = 'Outros'

export const CURSOS_GRADUACAO: string[] = [
  'Administração',
  'Agronomia',
  'Arquitetura e Urbanismo',
  'Artes Visuais',
  'Biomedicina',
  'Ciência da Computação',
  'Ciências Biológicas',
  'Ciências Contábeis',
  'Ciências Econômicas',
  'Design',
  'Design Gráfico',
  'Direito',
  'Educação Física',
  'Enfermagem',
  'Engenharia Ambiental',
  'Engenharia Civil',
  'Engenharia de Computação',
  'Engenharia de Software',
  'Engenharia Elétrica',
  'Engenharia Eletrônica',
  'Engenharia de Produção',
  'Engenharia Mecânica',
  'Engenharia Química',
  'Estética e Cosmética',
  'Farmácia',
  'Fisioterapia',
  'Fonoaudiologia',
  'Gastronomia',
  'Geografia',
  'História',
  'Jornalismo',
  'Letras',
  'Marketing',
  'Matemática',
  'Medicina',
  'Medicina Veterinária',
  'Música',
  'Nutrição',
  'Odontologia',
  'Pedagogia',
  'Psicologia',
  'Publicidade e Propaganda',
  'Química',
  'Relações Internacionais',
  'Serviço Social',
  'Sistemas de Informação',
  'Turismo',
  'Zootecnia',
  CURSO_OUTROS,
]

const CURSOS_PADRAO = new Set(CURSOS_GRADUACAO.filter((c) => c !== CURSO_OUTROS))

export function isCursoPadrao(curso: string): boolean {
  return CURSOS_PADRAO.has(curso)
}

export function parseCursoFromApi(curso?: string | null): { selecionado: string; outros: string } {
  const valor = curso?.trim() ?? ''
  if (!valor) {
    return { selecionado: '', outros: '' }
  }
  if (isCursoPadrao(valor)) {
    return { selecionado: valor, outros: '' }
  }
  return { selecionado: CURSO_OUTROS, outros: valor }
}

export function cursoParaEnvio(selecionado: string, outros: string): string {
  if (selecionado === CURSO_OUTROS) {
    return outros.trim()
  }
  return selecionado.trim()
}
