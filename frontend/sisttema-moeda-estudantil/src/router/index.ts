import { createRouter, createWebHistory } from 'vue-router'

export const router = createRouter({
  history: createWebHistory(),
  routes: [
    {
      path: '/',
      name: 'home',
      component: () => import('../views/HomeView.vue'),
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
    },
    {
      path: '/alunos/:id/editar',
      name: 'alunos-editar',
      component: () => import('../views/alunos/AlunoFormView.vue'),
      props: true,
    },
    {
      path: '/empresas',
      name: 'empresas-list',
      component: () => import('../views/empresas/EmpresasListView.vue'),
    },
    {
      path: '/empresas/nova',
      name: 'empresas-nova',
      component: () => import('../views/empresas/EmpresaFormView.vue'),
    },
    {
      path: '/empresas/:id/editar',
      name: 'empresas-editar',
      component: () => import('../views/empresas/EmpresaFormView.vue'),
      props: true,
    },
  ],
})
