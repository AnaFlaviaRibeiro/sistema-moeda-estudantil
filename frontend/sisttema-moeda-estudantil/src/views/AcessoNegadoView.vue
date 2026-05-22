<script setup lang="ts">
import { RouterLink } from 'vue-router'
import { homeRouteForRole } from '../permissions'
import { useAuth } from '../composables/useAuth'

const auth = useAuth()
const home = auth.user.value ? homeRouteForRole(auth.user.value.tipo) : '/login'
</script>

<template>
  <section class="card acesso-negado">
    <span class="eyebrow">Acesso restrito</span>
    <h1>Você não tem permissão para esta página</h1>
    <p>Esta funcionalidade não está disponível para o seu perfil no sistema.</p>
    <RouterLink class="btn btn-primary" :to="home">Voltar à área permitida</RouterLink>
  </section>
</template>
