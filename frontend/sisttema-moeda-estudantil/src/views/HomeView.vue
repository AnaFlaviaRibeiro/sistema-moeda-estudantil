<script setup lang="ts">
import { RouterLink } from 'vue-router'
</script>

<template>
  <section class="hero-card">
    <div class="hero-content">
      <span class="eyebrow">Programa de mérito</span>
      <h1>Reconhecimento estudantil com uma moeda virtual</h1>
      <p>
        Centralize cadastros, acompanhe instituições e prepare o caminho para distribuição
        de moedas, resgates de vantagens e extratos acadêmicos.
      </p>
      <div class="hero-actions">
        <RouterLink class="btn btn-primary" to="/alunos">Cadastrar aluno</RouterLink>
        <RouterLink class="btn btn-secondary" to="/empresas">Cadastrar empresa</RouterLink>
      </div>
    </div>
    <div class="hero-visual" aria-hidden="true">
      <div class="coin coin-lg">M</div>
      <div class="coin coin-sm">+</div>
      <div class="metric-card">
        <span>Saldo inicial</span>
        <strong>1.000</strong>
        <small>moedas por professor/semestre</small>
      </div>
    </div>
  </section>

  <section class="stats-grid">
    <article class="stat-card">
      <span>Instituições</span>
      <strong>Parceiras</strong>
      <p>Alunos e professores vinculados às instituições de ensino participantes.</p>
    </article>
    <article class="stat-card">
      <span>Alunos</span>
      <strong>Cadastro completo</strong>
      <p>Dados pessoais, instituição, curso e endereço em um único lugar.</p>
    </article>
    <article class="stat-card">
      <span>Empresas</span>
      <strong>Vantagens</strong>
      <p>Parceiros preparados para oferecer descontos e benefícios aos alunos.</p>
    </article>
  </section>

  <section class="card steps-card">
    <header>
      <span class="eyebrow">Como funciona</span>
      <h2>Três passos para reconhecer o mérito</h2>
    </header>
    <ol class="steps">
      <li>
        <span class="step-index">1</span>
        <div>
          <strong>Cadastre os participantes</strong>
          <p>Mantenha alunos e empresas parceiras atualizados em poucos cliques.</p>
        </div>
      </li>
      <li>
        <span class="step-index">2</span>
        <div>
          <strong>Distribua moedas</strong>
          <p>Professores reconhecem o mérito acadêmico enviando moedas com uma justificativa.</p>
        </div>
      </li>
      <li>
        <span class="step-index">3</span>
        <div>
          <strong>Resgate vantagens</strong>
          <p>Alunos trocam suas moedas por benefícios oferecidos pelas empresas parceiras.</p>
        </div>
      </li>
    </ol>
  </section>
</template>
