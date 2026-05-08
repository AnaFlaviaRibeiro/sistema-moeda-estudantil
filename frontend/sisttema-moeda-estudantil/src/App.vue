<script setup lang="ts">
import { RouterLink, RouterView } from 'vue-router'
</script>

<template>
  <div class="app">
    <aside class="sidebar">
      <div class="brand">
        <div class="brand-mark">ME</div>
        <div>
          <strong>Moeda Estudantil</strong>
          <span>Programa de mérito</span>
        </div>
      </div>

      <nav class="sidebar-nav">
        <RouterLink to="/"><span>Visão geral</span></RouterLink>
        <RouterLink to="/alunos"><span>Alunos</span></RouterLink>
        <RouterLink to="/empresas"><span>Empresas Parceiras</span></RouterLink>
      </nav>
    </aside>
    <main class="content">
      <RouterView />
    </main>
  </div>
</template>
