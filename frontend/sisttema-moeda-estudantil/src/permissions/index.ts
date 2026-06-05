import type { TipoUsuario } from '../api/auth'

export type RouteRole = TipoUsuario

export interface NavItem {
  to: string
  label: string
  name?: string
}

const ALL_ROLES: TipoUsuario[] = ['ALUNO', 'PROFESSOR', 'EMPRESA']

export const ROUTE_PERMISSIONS: Record<string, TipoUsuario[]> = {
  home: ALL_ROLES,
  'alunos-list': ['PROFESSOR'],
  'alunos-novo': [],
  'alunos-editar': ['ALUNO'],
  'meu-perfil': ['ALUNO'],
  'aluno-vantagens': ['ALUNO'],
  'aluno-extrato': ['ALUNO'],
  'professor-extrato': ['PROFESSOR'],
  'empresas-list': [],
  'empresas-nova': [],
  'empresas-editar': ['EMPRESA'],
  'minha-empresa': ['EMPRESA'],
  'empresa-vantagens': ['EMPRESA'],
  'empresa-vantagem-nova': ['EMPRESA'],
  'empresa-vantagem-editar': ['EMPRESA'],
  'empresa-resgates': ['EMPRESA'],
  'acesso-negado': ALL_ROLES,
  login: [],
}

export function canAccessRoute(routeName: string | undefined, tipo: TipoUsuario | undefined): boolean {
  if (!routeName) return true
  const allowed = ROUTE_PERMISSIONS[routeName]
  if (!allowed) return true
  if (allowed.length === 0) return true
  if (!tipo) return false
  return allowed.includes(tipo)
}

export function homeRouteForRole(tipo: TipoUsuario): string {
  switch (tipo) {
    case 'PROFESSOR':
      return '/alunos'
    case 'EMPRESA':
      return '/minha-empresa'
    default:
      return '/'
  }
}

export function navItemsForRole(tipo: TipoUsuario | undefined): NavItem[] {
  if (!tipo) return []

  const items: NavItem[] = [{ to: '/', label: 'Visão geral', name: 'home' }]

  if (tipo === 'ALUNO') {
    items.push({ to: '/vantagens', label: 'Vantagens', name: 'aluno-vantagens' })
    items.push({ to: '/extrato', label: 'Meu extrato', name: 'aluno-extrato' })
    items.push({ to: '/meu-perfil', label: 'Meu cadastro', name: 'meu-perfil' })
  }

  if (tipo === 'PROFESSOR') {
    items.push({ to: '/alunos', label: 'Alunos', name: 'alunos-list' })
    items.push({ to: '/professor/extrato', label: 'Extrato', name: 'professor-extrato' })
  }

  if (tipo === 'EMPRESA') {
    items.push({ to: '/empresa/vantagens', label: 'Vantagens', name: 'empresa-vantagens' })
    items.push({ to: '/empresa/resgates', label: 'Resgates', name: 'empresa-resgates' })
    items.push({ to: '/minha-empresa', label: 'Minha empresa', name: 'minha-empresa' })
  }

  return items
}

export function canEditAluno(tipo: TipoUsuario | undefined, alunoId: number, userId: number | undefined): boolean {
  return tipo === 'ALUNO' && userId === alunoId
}

export function canEditEmpresa(tipo: TipoUsuario | undefined, empresaId: number, userId: number | undefined): boolean {
  return tipo === 'EMPRESA' && userId === empresaId
}
