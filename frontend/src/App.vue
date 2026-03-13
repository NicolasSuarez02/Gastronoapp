<template>
  <div v-if="auth.isLoggedIn" class="app-layout">
    <Sidebar />
    <main class="app-main">
      <RouterView />
    </main>
  </div>
  <RouterView v-else />
</template>

<script setup>
import { auth } from './stores/auth'
import Sidebar from './components/Sidebar.vue'
</script>

<style>
.app-layout {
  display: flex;
  min-height: 100vh;
}
.app-main {
  margin-left: 240px;
  flex: 1;
  min-height: 100vh;
  background: var(--bg);
}
@media (max-width: 768px) {
  .app-main {
    margin-left: 0;
  }
}
</style>
