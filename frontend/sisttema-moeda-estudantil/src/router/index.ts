import { createRouter, createWebHistory } from 'vue-router'
import { useAuth } from '../composables/useAuth'
import { canAccessRoute } from '../permissions'

const PUBLIC_ROUTE_NAMES = new Set(['login', 'alunos-novo', 'empresas-nova'])

export const router = createRouter({
  history: createWebHistory(),
  routes: [
    {
      path: '/login',
      name: 'login',
      component: () => import('../views/LoginView.vue'),
      meta: { public: true, layout: 'auth' },
    },
    {
      path: '/',
      name: 'home',
      component: () => import('../views/HomeView.vue'),
    },
    {
      path: '/acesso-negado',
      name: 'acesso-negado',
      component: () => import('../views/AcessoNegadoView.vue'),
    },
    {
      path: '/alunos',
      name: 'alunos-list',
      component: () => import('../views/alunos/AlunosListView.vue'),
    },
    {
      path: '/alunos/novo',
      name: 'alunos-novo',
      component: () => import('../views/alunos/AlunoFormView.vue'),
      meta: { public: true },
    },
    {
      path: '/meu-perfil',
      name: 'meu-perfil',
      component: () => import('../views/alunos/AlunoFormView.vue'),
      meta: { ownProfile: true },
    },
    {
      path: '/alunos/:id/editar',
      name: 'alunos-editar',
      component: () => import('../views/alunos/AlunoFormView.vue'),
      props: true,
    },
    {
      path: '/empresas/nova',
      name: 'empresas-nova',
      component: () => import('../views/empresas/EmpresaFormView.vue'),
      meta: { public: true },
    },
    {
      path: '/minha-empresa',
      name: 'minha-empresa',
      component: () => import('../views/empresas/EmpresaFormView.vue'),
      meta: { ownProfile: true },
    },
    {
      path: '/empresas/:id/editar',
      name: 'empresas-editar',
      component: () => import('../views/empresas/EmpresaFormView.vue'),
      props: true,
    },
    {
      path: '/empresas',
      redirect: { name: 'acesso-negado' },
    },
  ],
})

router.beforeEach((to) => {
  const auth = useAuth()
  const isPublic =
    to.meta.public === true || (to.name && PUBLIC_ROUTE_NAMES.has(String(to.name)))

  if (to.name === 'login' && auth.isAuthenticated.value) {
    return { name: 'home' }
  }

  if (isPublic) {
    return true
  }

  if (!auth.isAuthenticated.value) {
    return { name: 'login', query: { redirect: to.fullPath } }
  }

  const tipo = auth.user.value?.tipo
  const routeName = to.name ? String(to.name) : undefined

  if (!canAccessRoute(routeName, tipo)) {
    return { name: 'acesso-negado' }
  }

  if (routeName === 'alunos-editar') {
    const alunoId = Number(to.params.id)
    if (tipo !== 'ALUNO' || auth.user.value?.id !== alunoId) {
      return { name: 'acesso-negado' }
    }
  }

  if (routeName === 'empresas-editar') {
    const empresaId = Number(to.params.id)
    if (tipo !== 'EMPRESA' || auth.user.value?.id !== empresaId) {
      return { name: 'acesso-negado' }
    }
  }

  return true
})
