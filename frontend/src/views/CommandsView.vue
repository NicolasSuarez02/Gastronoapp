<template>
  <div class="page">
    <div class="page-header">
      <h1 class="page-title">Pedidos</h1>
      <RouterLink to="/commands/new" class="btn-primary" style="padding:8px 18px;border-radius:var(--radius);font-weight:600;font-size:14px;">+ Nuevo pedido</RouterLink>
    </div>

    <div class="filters card" style="margin-bottom:20px;">
      <div class="filter-row">
        <label>Filtrar por estado</label>
        <select v-model="statusFilter">
          <option value="">Todos</option>
          <option value="RECIBIDO">Recibido</option>
          <option value="EN_CURSO">En curso</option>
          <option value="LISTO">Listo</option>
          <option value="ENTREGADO">Entregado</option>
        </select>
      </div>
    </div>

    <div v-if="loading" class="spinner"></div>
    <div v-else-if="!filtered.length" class="empty-state card">No hay pedidos{{ statusFilter ? ' con ese estado' : '' }}.</div>
    <div v-else class="card">
      <table>
        <thead>
          <tr>
            <th>#</th>
            <th>Mesa</th>
            <th>Fecha</th>
            <th>Observaciones</th>
            <th>Ítems</th>
            <th>Estado</th>
            <th></th>
          </tr>
        </thead>
        <tbody>
          <tr v-for="cmd in filtered" :key="cmd.id">
            <td><strong>{{ cmd.id }}</strong></td>
            <td>Mesa {{ cmd.number }}</td>
            <td>{{ formatDate(cmd.date) }}</td>
            <td>{{ cmd.observations || '-' }}</td>
            <td>
              <span v-for="item in cmd.menuItems" :key="item.id" class="item-tag">{{ item.name }}</span>
            </td>
            <td><span :class="['badge', badgeClass(cmd.status)]">{{ cmd.status }}</span></td>
            <td>
              <select class="status-select" :value="cmd.status" @change="changeStatus(cmd, $event.target.value)">
                <option value="RECIBIDO">Recibido</option>
                <option value="EN_CURSO">En curso</option>
                <option value="LISTO">Listo</option>
                <option value="ENTREGADO">Entregado</option>
              </select>
            </td>
            <td>
              <RouterLink
                v-if="cmd.status !== 'ENTREGADO'"
                :to="`/commands/new?edit=${cmd.id}`"
                class="btn-edit"
              >Editar</RouterLink>
            </td>
          </tr>
        </tbody>
      </table>
    </div>
  </div>
</template>

<script setup>
import { ref, computed, onMounted } from 'vue'
import { RouterLink } from 'vue-router'
import { getCommands, updateCommandStatus } from '../api'

const commands = ref([])
const loading = ref(true)
const statusFilter = ref('')

onMounted(async () => {
  try {
    const res = await getCommands()
    commands.value = res.data.reverse()
  } finally {
    loading.value = false
  }
})

const filtered = computed(() =>
  statusFilter.value ? commands.value.filter(c => c.status === statusFilter.value) : commands.value
)

async function changeStatus(cmd, newStatus) {
  if (newStatus === cmd.status) return
  await updateCommandStatus(cmd.id, newStatus)
  cmd.status = newStatus
}

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
.filter-row {
  display: flex;
  align-items: center;
  gap: 12px;
}
.filter-row label { margin: 0; white-space: nowrap; }
.filter-row select { max-width: 200px; }

.item-tag {
  display: inline-block;
  background: var(--surface3);
  color: var(--text-dim);
  border-radius: 4px;
  padding: 2px 6px;
  font-size: 12px;
  margin: 1px 2px;
}

.status-select {
  width: auto;
  padding: 4px 8px;
  font-size: 13px;
}

.btn-edit {
  display: inline-block;
  padding: 4px 12px;
  background: var(--surface3);
  color: var(--text);
  border-radius: 6px;
  font-size: 13px;
  font-weight: 600;
  text-decoration: none;
  white-space: nowrap;
}
.btn-edit:hover { opacity: .8; }
</style>
