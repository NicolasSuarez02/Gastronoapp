<template>
  <div class="page">
    <div class="page-header">
      <h1 class="page-title">Bienvenido, {{ auth.user?.name }}</h1>
      <span class="date-label">{{ today }}</span>
    </div>

    <div v-if="loading" class="spinner"></div>
    <div v-else class="stats-grid">
      <div class="stat-card">
        <div class="stat-icon">📋</div>
        <div class="stat-value">{{ commandCounts.total }}</div>
        <div class="stat-label">Pedidos totales</div>
      </div>
      <div class="stat-card accent-blue">
        <div class="stat-icon">🆕</div>
        <div class="stat-value">{{ commandCounts.recibido }}</div>
        <div class="stat-label">Recibidos</div>
      </div>
      <div class="stat-card accent-yellow">
        <div class="stat-icon">👨‍🍳</div>
        <div class="stat-value">{{ commandCounts.enCurso }}</div>
        <div class="stat-label">En curso</div>
      </div>
      <div class="stat-card accent-green">
        <div class="stat-icon">✅</div>
        <div class="stat-value">{{ commandCounts.listo }}</div>
        <div class="stat-label">Listos</div>
      </div>
      <div class="stat-card">
        <div class="stat-icon">🍕</div>
        <div class="stat-value">{{ menuItemsCount }}</div>
        <div class="stat-label">Platos en menú</div>
      </div>
    </div>

    <div class="actions-row">
      <RouterLink to="/commands/new" class="action-btn btn-primary">+ Nuevo pedido</RouterLink>
      <RouterLink to="/commands" class="action-btn btn-secondary">Ver todos los pedidos</RouterLink>
      <RouterLink to="/kitchen" class="action-btn btn-secondary">Vista cocina</RouterLink>
    </div>

    <div class="recent-section card" v-if="recentCommands.length">
      <h2 class="section-title">Pedidos recientes</h2>
      <table>
        <thead>
          <tr>
            <th>#</th>
            <th>Mesa</th>
            <th>Fecha</th>
            <th>Estado</th>
            <th>Ítems</th>
          </tr>
        </thead>
        <tbody>
          <tr v-for="cmd in recentCommands" :key="cmd.id">
            <td>{{ cmd.id }}</td>
            <td>Mesa {{ cmd.tableNumber }}</td>
            <td>{{ formatDate(cmd.date) }}</td>
            <td><span :class="['badge', badgeClass(cmd.status)]">{{ cmd.status }}</span></td>
            <td>{{ cmd.menuItems?.length ?? 0 }} ítem(s)</td>
          </tr>
        </tbody>
      </table>
    </div>
  </div>
</template>

<script setup>
import { ref, computed, onMounted } from 'vue'
import { getCommands, getMenuItems } from '../api'
import { auth } from '../stores/auth'

const commands = ref([])
const menuItemsCount = ref(0)
const loading = ref(true)

const today = new Date().toLocaleDateString('es-AR', { weekday: 'long', year: 'numeric', month: 'long', day: 'numeric' })

onMounted(async () => {
  try {
    const [cmdRes, itemRes] = await Promise.all([getCommands(), getMenuItems()])
    commands.value = cmdRes.data
    menuItemsCount.value = itemRes.data.length
  } finally {
    loading.value = false
  }
})

const commandCounts = computed(() => ({
  total: commands.value.length,
  recibido: commands.value.filter(c => c.status === 'RECIBIDO').length,
  enCurso: commands.value.filter(c => c.status === 'EN_CURSO').length,
  listo: commands.value.filter(c => c.status === 'LISTO').length,
}))

const recentCommands = computed(() => [...commands.value].reverse().slice(0, 5))

function formatDate(d) {
  if (!d) return '-'
  return new Date(d).toLocaleString('es-AR', { dateStyle: 'short', timeStyle: 'short' })
}

function badgeClass(status) {
  const map = { RECIBIDO: 'badge-recibido', EN_CURSO: 'badge-en-curso', LISTO: 'badge-listo', ENTREGADO: 'badge-entregado' }
  return map[status] ?? 'badge-recibido'
}
</script>

<style scoped>
.date-label { color: var(--text-muted); font-size: 14px; }

.stats-grid {
  display: grid;
  grid-template-columns: repeat(auto-fill, minmax(180px, 1fr));
  gap: 16px;
  margin-bottom: 24px;
}

.stat-card {
  background: var(--surface);
  border-radius: var(--radius);
  box-shadow: var(--shadow);
  padding: 20px;
  text-align: center;
  border-top: 3px solid var(--border);
}
.stat-card.accent-blue   { border-top-color: var(--info); }
.stat-card.accent-yellow { border-top-color: var(--warning); }
.stat-card.accent-green  { border-top-color: var(--success); }

.stat-icon  { font-size: 28px; margin-bottom: 8px; }
.stat-value { font-size: 32px; font-weight: 700; }
.stat-label { font-size: 13px; color: var(--text-muted); margin-top: 4px; }

.actions-row {
  display: flex;
  gap: 12px;
  margin-bottom: 24px;
  flex-wrap: wrap;
}

.action-btn {
  padding: 10px 20px;
  border-radius: var(--radius);
  font-weight: 600;
  font-size: 14px;
  transition: opacity .15s;
}
.action-btn:hover { opacity: .88; }

.recent-section { margin-top: 8px; }
.section-title { font-size: 16px; font-weight: 700; margin-bottom: 16px; }
</style>
