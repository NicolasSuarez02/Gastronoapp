<template>
  <div class="page">
    <div class="page-header">
      <h1 class="page-title">Vista Cocina</h1>
      <div class="header-right">
        <span class="last-update">Actualizado: {{ lastUpdateTime }}</span>
        <button class="btn-secondary" @click="fetchCommands">↻ Actualizar</button>
      </div>
    </div>

    <div v-if="loading" class="spinner"></div>
    <div v-else>
      <div v-if="!activeCommands.length" class="empty-state card">
        No hay pedidos pendientes. ¡Todo al día! 🎉
      </div>
      <div v-else class="kitchen-grid">
        <div
          v-for="cmd in activeCommands"
          :key="cmd.id"
          :class="['kitchen-card', statusClass(cmd.status)]"
        >
          <div class="kcard-header">
            <span class="kcard-id">#{{ cmd.id }}</span>
            <span class="kcard-table">Mesa {{ cmd.tableNumber }}</span>
            <span :class="['badge', badgeClass(cmd.status)]">{{ cmd.status }}</span>
          </div>
          <div class="kcard-time">{{ formatTime(cmd.date) }}</div>

          <ul class="kcard-items">
            <li v-for="item in cmd.menuItems" :key="item.id">{{ item.name }}</li>
          </ul>

          <div v-if="cmd.observations" class="kcard-obs">
            📝 {{ cmd.observations }}
          </div>

          <div class="kcard-actions">
            <button
              v-if="cmd.status === 'RECIBIDO'"
              class="btn-primary"
              @click="setStatus(cmd, 'EN_CURSO')"
            >
              Iniciar
            </button>
            <button
              v-if="cmd.status === 'EN_CURSO'"
              class="btn-success"
              @click="setStatus(cmd, 'LISTO')"
            >
              Listo
            </button>
            <button
              v-if="cmd.status === 'LISTO'"
              class="btn-secondary"
              @click="setStatus(cmd, 'ENTREGADO')"
            >
              Entregado
            </button>
          </div>
        </div>
      </div>
    </div>
  </div>
</template>

<script setup>
import { ref, computed, onMounted, onUnmounted } from 'vue'
import { getCommands, updateCommandStatus } from '../api'

const commands = ref([])
const loading = ref(true)
const lastUpdateTime = ref('-')
let interval = null

const activeCommands = computed(() =>
  commands.value.filter(c => c.status !== 'ENTREGADO').sort((a, b) => a.id - b.id)
)

async function fetchCommands() {
  try {
    const res = await getCommands()
    commands.value = res.data
    lastUpdateTime.value = new Date().toLocaleTimeString('es-AR')
  } finally {
    loading.value = false
  }
}

onMounted(() => {
  fetchCommands()
  interval = setInterval(fetchCommands, 15000) // auto-refresh every 15s
})

onUnmounted(() => clearInterval(interval))

async function setStatus(cmd, status) {
  await updateCommandStatus(cmd.id, status)
  cmd.status = status
  if (status === 'ENTREGADO') {
    setTimeout(() => { commands.value = commands.value.filter(c => c.id !== cmd.id) }, 800)
  }
}

function formatTime(d) {
  if (!d) return ''
  return new Date(d).toLocaleTimeString('es-AR', { hour: '2-digit', minute: '2-digit' })
}

function statusClass(s) {
  return { RECIBIDO: 'kcard-recibido', EN_CURSO: 'kcard-en-curso', LISTO: 'kcard-listo' }[s] ?? ''
}

function badgeClass(status) {
  const map = { RECIBIDO: 'badge-recibido', EN_CURSO: 'badge-en-curso', LISTO: 'badge-listo', ENTREGADO: 'badge-entregado' }
  return map[status] ?? 'badge-recibido'
}
</script>

<style scoped>
.header-right { display: flex; align-items: center; gap: 12px; }
.last-update { font-size: 13px; color: var(--text-muted); }

.kitchen-grid {
  display: grid;
  grid-template-columns: repeat(auto-fill, minmax(280px, 1fr));
  gap: 16px;
}

.kitchen-card {
  background: var(--surface);
  border-radius: var(--radius);
  box-shadow: var(--shadow);
  padding: 20px;
  border-left: 5px solid var(--border);
  transition: border-color .2s;
}
.kitchen-card.kcard-recibido { border-left-color: var(--info); }
.kitchen-card.kcard-en-curso { border-left-color: var(--warning); }
.kitchen-card.kcard-listo    { border-left-color: var(--success); }

.kcard-header {
  display: flex;
  align-items: center;
  gap: 10px;
  margin-bottom: 6px;
}
.kcard-id    { font-size: 16px; font-weight: 700; }
.kcard-table { flex: 1; font-size: 14px; color: var(--text-muted); }
.kcard-time  { font-size: 13px; color: var(--text-muted); margin-bottom: 12px; }

.kcard-items {
  list-style: none;
  margin-bottom: 12px;
}
.kcard-items li {
  padding: 4px 0;
  border-bottom: 1px dashed var(--border);
  font-size: 14px;
  font-weight: 500;
}
.kcard-items li:last-child { border-bottom: none; }

.kcard-obs {
  font-size: 13px;
  color: var(--warning);
  background: rgba(245,158,11,.12);
  border: 1px solid rgba(245,158,11,.25);
  border-radius: 4px;
  padding: 6px 10px;
  margin-bottom: 12px;
}

.kcard-actions { display: flex; gap: 8px; }
.kcard-actions button { flex: 1; }
</style>
