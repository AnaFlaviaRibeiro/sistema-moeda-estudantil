<script setup lang="ts">
import { computed } from 'vue'
import { RouterLink, RouterView, useRoute, useRouter } from 'vue-router'
import { navItemsForRole } from './permissions'
import { useAuth } from './composables/useAuth'

const route = useRoute()
const router = useRouter()
const auth = useAuth()

const isAuthLayout = computed(() => route.meta.layout === 'auth')

const navItems = computed(() => navItemsForRole(auth.user.value?.tipo))

const tipoLabel: Record<string, string> = {
  ALUNO: 'Aluno',
  PROFESSOR: 'Professor',
  EMPRESA: 'Empresa parceira',
}

function logout() {
  auth.logout()
  router.push({ name: 'login' })
}
</script>

<template>
  <div v-if="isAuthLayout" class="auth-shell">
    <RouterView />
  </div>

  <div v-else class="app">
    <aside class="sidebar">
      <div class="brand">
        <div class="brand-mark">ME</div>
        <div>
          <strong>Moeda Estudantil</strong>
          <span>Programa de mérito</span>
        </div>
      </div>

      <div v-if="auth.user.value" class="sidebar-user">
        <span class="sidebar-user-role">{{ tipoLabel[auth.user.value.tipo] ?? auth.user.value.tipo }}</span>
        <strong>{{ auth.user.value.nome }}</strong>
        <small>{{ auth.user.value.email }}</small>
      </div>

      <nav class="sidebar-nav">
        <RouterLink
          v-for="item in navItems"
          :key="item.to"
          :to="item.to"
        >
          <span>{{ item.label }}</span>
        </RouterLink>
      </nav>

      <button class="btn btn-secondary btn-logout" type="button" @click="logout">
        Sair
      </button>
    </aside>
    <main class="content">
      <RouterView />
    </main>
  </div>
</template>
