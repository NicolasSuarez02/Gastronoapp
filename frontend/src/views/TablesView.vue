<template>
  <div class="page">
    <div class="page-header">
      <h1 class="page-title">Mesas</h1>
      <div class="header-actions">
        <button class="btn-secondary" @click="showForm = !showForm">
          {{ showForm ? 'Cancelar' : '+ Agregar mesa' }}
        </button>
        <button class="btn-secondary icon-btn" @click="fetchData" title="Actualizar">
          <svg width="15" height="15" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2.5"><polyline points="23 4 23 10 17 10"/><path d="M20.49 15a9 9 0 1 1-2.12-9.36L23 10"/></svg>
        </button>
      </div>
    </div>

    <!-- Legend -->
    <div class="legend">
      <span class="legend-dot" style="background:#43a047"></span><span class="legend-label">Libre</span>
      <span class="legend-dot" style="background:#e53935"></span><span class="legend-label">Ocupada</span>
      <span class="legend-dot" style="background:#fb8c00"></span><span class="legend-label">Reservada</span>
      <span class="legend-dot" style="background:#546e7a"></span><span class="legend-label">Fuera de servicio</span>
    </div>

    <!-- Add form -->
    <div v-if="showForm" class="card form-card">
      <div class="form-row">
        <div class="form-group">
          <label>Número de mesa</label>
          <input v-model.number="form.number" type="number" min="1" max="99" placeholder="Ej: 5" />
        </div>
        <div class="form-group">
          <label>Capacidad (personas)</label>
          <input v-model.number="form.capacity" type="number" min="1" placeholder="4" />
        </div>
      </div>
      <div v-if="formError" class="alert alert-error">{{ formError }}</div>
      <button class="btn-primary" @click="addTable" :disabled="saving">
        {{ saving ? 'Guardando...' : 'Agregar mesa' }}
      </button>
    </div>

    <div v-if="loading" class="spinner"></div>
    <div v-else-if="!tables.length" class="empty-state card">
      No hay mesas configuradas. Agregá la primera.
    </div>

    <div v-else class="tables-grid">
      <div
        v-for="table in sortedTables"
        :key="table.id"
        :class="['table-card', cardClass(table.status)]"
        @click="openTableMenu(table)"
      >
        <button class="tc-delete" @click.stop="removeTable(table.id)" title="Eliminar">✕</button>
        <span v-if="hasListo(table.tableNumber)" class="listo-dot" title="Pedido listo para entregar"></span>

        <div class="tc-label">M {{ String(table.tableNumber).padStart(2, '0') }}</div>

        <!-- FREE -->
        <div v-if="!table.status || table.status === 'FREE'" class="tc-body tc-body-free">
          <svg width="28" height="28" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="3"><polyline points="20 6 9 17 4 12"/></svg>
        </div>

        <!-- OCCUPIED -->
        <div v-else-if="table.status === 'OCCUPIED'" class="tc-body tc-body-occ">
          <template v-if="activeCommand(table.tableNumber)">
            <div class="tc-time">⊙ {{ timeAgo(activeCommand(table.tableNumber)) }}</div>
            <div class="tc-cmd">Pedido #{{ activeCommand(table.tableNumber).id }}</div>
          </template>
          <svg v-else width="24" height="24" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2.5"><polyline points="20 6 9 17 4 12"/></svg>
        </div>

        <!-- RESERVED -->
        <div v-else-if="table.status === 'RESERVED'" class="tc-body tc-body-free">
          <svg width="24" height="24" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2.5"><polyline points="20 6 9 17 4 12"/></svg>
        </div>

        <!-- OUT OF SERVICE -->
        <div v-else class="tc-body tc-body-free">
          <svg width="24" height="24" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2.5"><line x1="18" y1="6" x2="6" y2="18"/><line x1="6" y1="6" x2="18" y2="18"/></svg>
        </div>
      </div>
    </div>

    <!-- Modal -->
    <div v-if="selected" class="modal-overlay" @click.self="closeModal">
      <div class="modal card">
        <!-- Header -->
        <div class="modal-head">
          <h2 class="modal-title">Mesa {{ selected.tableNumber }}</h2>
          <button class="modal-close-x" @click="closeModal">✕</button>
        </div>

        <!-- Edit form (toggle) -->
        <div v-if="editMode" class="edit-form">
          <div class="form-row">
            <div class="form-group">
              <label>Número de mesa</label>
              <input v-model.number="editForm.number" type="number" min="1" max="99" />
            </div>
            <div class="form-group">
              <label>Capacidad</label>
              <input v-model.number="editForm.capacity" type="number" min="1" />
            </div>
          </div>
          <div v-if="editError" class="alert alert-error">{{ editError }}</div>
          <div style="display:flex;gap:8px;">
            <button class="btn-primary" @click="saveEdit" :disabled="editSaving">
              {{ editSaving ? 'Guardando...' : 'Guardar cambios' }}
            </button>
            <button class="btn-secondary" @click="editMode = false">Cancelar</button>
          </div>
        </div>

        <!-- Info mode -->
        <template v-else>
          <div class="modal-info-row">
            <div class="modal-info-item">
              <span class="modal-info-label">Capacidad</span>
              <span class="modal-info-value">{{ selected.capacity }} personas</span>
            </div>
            <div class="modal-info-item">
              <span class="modal-info-label">Estado</span>
              <span class="modal-info-value">
                <span :class="['status-pill', `sp-${(selected.status || 'FREE').toLowerCase()}`]">
                  {{ statusLabel(selected.status) }}
                </span>
              </span>
            </div>
          </div>

          <!-- Active commands -->
          <div class="modal-commands" v-if="tableCommands(selected.tableNumber).length">
            <h3>Pedidos activos</h3>
            <table>
              <thead><tr><th>#</th><th>Estado</th><th>Ítems</th><th></th></tr></thead>
              <tbody>
                <tr v-for="cmd in tableCommands(selected.tableNumber)" :key="cmd.id">
                  <td>{{ cmd.id }}</td>
                  <td><span :class="['badge', badgeClass(cmd.status)]">{{ cmd.status }}</span></td>
                  <td>{{ cmd.menuItems?.length ?? 0 }}</td>
                  <td class="cmd-actions">
                    <RouterLink
                      :to="`/commands/new?edit=${cmd.id}`"
                      class="btn-cmd-edit"
                      @click="closeModal"
                    >Editar</RouterLink>
                    <button
                      v-if="cmd.status === 'LISTO'"
                      class="btn-deliver"
                      @click="deliverCommand(cmd)"
                    >✓ Entregar</button>
                  </td>
                </tr>
              </tbody>
            </table>
          </div>

          <!-- Change status buttons -->
          <div class="section-label">Cambiar estado</div>
          <div class="status-buttons">
            <button
              v-for="s in ['FREE','OCCUPIED','RESERVED','OUT_OF_SERVICE']"
              :key="s"
              :class="['status-btn', `tc-btn-${s.toLowerCase()}`, { active: selected.status === s }]"
              @click="changeTableStatus(selected, s)"
            >
              {{ statusLabel(s) }}
            </button>
          </div>

          <!-- Actions -->
          <div class="modal-actions">
            <RouterLink
              :to="`/commands/new?table=${selected.tableNumber}`"
              class="btn-primary modal-action-btn"
              @click="closeModal"
            >
              + Nuevo pedido
            </RouterLink>
            <button class="btn-secondary modal-action-btn" @click="openEdit">
              ✎ Editar mesa
            </button>
            <button class="btn-danger modal-action-btn" @click="removeTable(selected.id)">
              Eliminar
            </button>
          </div>
        </template>
      </div>
    </div>
  </div>
</template>

<script setup>
import { ref, computed, onMounted } from 'vue'
import { RouterLink } from 'vue-router'
import { getTables, saveTable, deleteTable, updateTableStatus, updateTable, getCommands, updateCommandStatus } from '../api'

const tables    = ref([])
const commands  = ref([])
const loading   = ref(true)
const showForm  = ref(false)
const saving    = ref(false)
const formError = ref('')
const selected  = ref(null)

const editMode   = ref(false)
const editSaving = ref(false)
const editError  = ref('')
const editForm   = ref({ number: null, capacity: null })

const form = ref({ number: null, capacity: 4 })

const sortedTables = computed(() =>
  [...tables.value].sort((a, b) => a.tableNumber - b.tableNumber)
)

async function fetchData() {
  loading.value = true
  try {
    const [tRes, cRes] = await Promise.all([getTables(), getCommands()])
    tables.value   = tRes.data
    commands.value = cRes.data
  } catch (e) {
    console.error('Error al cargar datos:', e)
  } finally {
    loading.value = false
  }
}

onMounted(fetchData)

function cardClass(status) {
  const s = (status || 'FREE').toUpperCase()
  return { FREE: 'tc-free', OCCUPIED: 'tc-occupied', RESERVED: 'tc-reserved', OUT_OF_SERVICE: 'tc-out_of_service' }[s] ?? 'tc-free'
}

function activeCommand(tableNumber) {
  return commands.value.find(
    c => c.number === tableNumber && ['RECIBIDO','EN_CURSO','LISTO'].includes(c.status)
  ) ?? null
}

function tableCommands(tableNumber) {
  return commands.value.filter(
    c => c.number === tableNumber && ['RECIBIDO','EN_CURSO','LISTO'].includes(c.status)
  )
}

function hasListo(tableNumber) {
  return commands.value.some(c => c.number === tableNumber && c.status === 'LISTO')
}

async function deliverCommand(cmd) {
  await updateCommandStatus(cmd.id, 'ENTREGADO')
  commands.value = commands.value.filter(c => c.id !== cmd.id)
  // Refetch tables to reflect possible status change to FREE
  const tRes = await getTables()
  tables.value = tRes.data
  if (selected.value) {
    const updated = tables.value.find(t => t.id === selected.value.id)
    if (updated) selected.value = { ...updated }
  }
}

function timeAgo(cmd) {
  if (!cmd?.date) return '—'
  const diff = Date.now() - new Date(cmd.date).getTime()
  const mins = Math.floor(diff / 60000)
  if (mins < 1) return 'ahora'
  if (mins < 60) return `hace ${mins} min`
  return `hace ${Math.floor(mins / 60)} h`
}

async function addTable() {
  formError.value = ''
  if (!form.value.number) { formError.value = 'El número es requerido.'; return }
  saving.value = true
  try {
    const res = await saveTable({ tableNumber: form.value.number, capacity: form.value.capacity, status: 'FREE' })
    tables.value.push(res.data)
    showForm.value = false
    form.value = { number: null, capacity: 4 }
  } catch (e) {
    formError.value = e.response?.data?.message ?? 'Error al guardar.'
  } finally {
    saving.value = false
  }
}

async function removeTable(id) {
  if (!confirm('¿Eliminar esta mesa?')) return
  await deleteTable(id)
  tables.value = tables.value.filter(t => t.id !== id)
  if (selected.value?.id === id) selected.value = null
}

function openTableMenu(table) {
  selected.value = { ...table }
  editMode.value = false
}

function closeModal() {
  selected.value = null
  editMode.value = false
}

function openEdit() {
  editForm.value = { number: selected.value.tableNumber, capacity: selected.value.capacity }
  editError.value = ''
  editMode.value = true
}

async function saveEdit() {
  editError.value = ''
  if (!editForm.value.number) { editError.value = 'El número es requerido.'; return }
  editSaving.value = true
  try {
    const res = await updateTable(selected.value.id, { tableNumber: editForm.value.number, capacity: editForm.value.capacity })
    const idx = tables.value.findIndex(t => t.id === selected.value.id)
    if (idx !== -1) tables.value[idx] = res.data
    selected.value = { ...res.data }
    editMode.value = false
  } catch (e) {
    editError.value = e.response?.data?.message ?? 'Error al guardar.'
  } finally {
    editSaving.value = false
  }
}

async function changeTableStatus(table, status) {
  await updateTableStatus(table.id, status)
  const t = tables.value.find(t => t.id === table.id)
  if (t) t.status = status
  selected.value = { ...table, status }
}

function statusLabel(s) {
  return { FREE: 'Libre', OCCUPIED: 'Ocupada', RESERVED: 'Reservada', OUT_OF_SERVICE: 'Fuera de servicio' }[s] ?? (s || 'Libre')
}

function badgeClass(s) {
  return { RECIBIDO: 'badge-recibido', EN_CURSO: 'badge-en-curso', LISTO: 'badge-listo', ENTREGADO: 'badge-entregado' }[s] ?? ''
}
</script>

<style scoped>
.header-actions { display: flex; gap: 8px; align-items: center; }
.icon-btn { display: flex; align-items: center; justify-content: center; padding: 8px 10px; }

/* Legend */
.legend {
  display: flex; align-items: center; gap: 6px; flex-wrap: wrap;
  margin-bottom: 24px; font-size: 13px; color: var(--text-dim);
}
.legend-dot { width: 12px; height: 12px; border-radius: 3px; flex-shrink: 0; margin-left: 10px; }
.legend-dot:first-child { margin-left: 0; }
.legend-label { font-weight: 500; }

.form-card { margin-bottom: 24px; }
.form-row  { display: grid; grid-template-columns: 1fr 1fr; gap: 12px; }

/* Grid */
.tables-grid { display: flex; flex-wrap: wrap; gap: 12px; }

/* Table cards */
.table-card {
  position: relative;
  width: 120px; height: 120px;
  border-radius: 10px;
  display: flex; flex-direction: column;
  align-items: center; justify-content: flex-start;
  cursor: pointer;
  transition: transform .15s, box-shadow .15s;
  user-select: none;
  padding: 10px 8px 8px;
  box-shadow: 0 2px 8px rgba(0,0,0,.25);
  color: #fff;
}
.table-card:hover { transform: translateY(-3px); box-shadow: 0 6px 18px rgba(0,0,0,.35); }

.tc-free         { background: #43a047; }
.tc-occupied     { background: #e53935; }
.tc-reserved     { background: #fb8c00; }
.tc-out_of_service { background: #546e7a; }

.tc-label {
  font-size: 15px; font-weight: 700;
  color: rgba(255,255,255,.95);
  align-self: flex-start; line-height: 1;
}
.tc-body {
  flex: 1; display: flex; flex-direction: column;
  align-items: center; justify-content: center; width: 100%;
}
.tc-body-free { color: rgba(255,255,255,.9); }
.tc-body-occ  { gap: 4px; color: #fff; }
.tc-time { font-size: 11px; color: rgba(255,255,255,.85); font-weight: 500; }
.tc-cmd  { font-size: 12px; font-weight: 700; color: #fff; text-align: center; }

.listo-dot {
  position: absolute; top: 6px; left: 6px;
  width: 10px; height: 10px; border-radius: 50%;
  background: #ffe000;
  box-shadow: 0 0 0 0 rgba(255,224,0,.6);
  animation: listo-pulse 1.4s infinite;
}
@keyframes listo-pulse {
  0%   { box-shadow: 0 0 0 0 rgba(255,224,0,.7); }
  70%  { box-shadow: 0 0 0 7px rgba(255,224,0,0); }
  100% { box-shadow: 0 0 0 0 rgba(255,224,0,0); }
}

.cmd-actions { display: flex; gap: 6px; align-items: center; }

.btn-cmd-edit {
  display: inline-block;
  padding: 3px 10px;
  background: var(--surface3);
  color: var(--text);
  border-radius: 6px;
  font-size: 12px; font-weight: 600;
  text-decoration: none; white-space: nowrap;
}
.btn-cmd-edit:hover { opacity: .8; }

.btn-deliver {
  background: #43a047; color: #fff;
  border: none; border-radius: 6px;
  font-size: 12px; font-weight: 700;
  padding: 4px 10px; cursor: pointer;
  white-space: nowrap;
}
.btn-deliver:hover { background: #388e3c; }

.tc-delete {
  position: absolute; top: 4px; right: 5px;
  background: rgba(0,0,0,.25); color: rgba(255,255,255,.7);
  font-size: 10px; padding: 1px 5px; border-radius: 4px;
  opacity: 0; transition: opacity .15s; line-height: 1.6;
}
.table-card:hover .tc-delete { opacity: 1; }
.tc-delete:hover { background: rgba(0,0,0,.5); color: #fff; opacity: 1; }

/* Modal */
.modal-overlay {
  position: fixed; inset: 0;
  background: rgba(0,0,0,.65);
  display: flex; align-items: center; justify-content: center;
  z-index: 100; backdrop-filter: blur(4px);
}
.modal { width: 100%; max-width: 460px; padding: 24px; }

.modal-head {
  display: flex; align-items: center; justify-content: space-between;
  margin-bottom: 16px;
}
.modal-title { font-size: 20px; font-weight: 700; }
.modal-close-x {
  background: transparent; color: var(--text-dim);
  font-size: 16px; padding: 4px 8px; border-radius: 6px;
}
.modal-close-x:hover { background: var(--surface3); color: var(--text); }

.modal-info-row { display: flex; gap: 24px; margin-bottom: 20px; }
.modal-info-item { display: flex; flex-direction: column; gap: 2px; }
.modal-info-label { font-size: 11px; font-weight: 700; text-transform: uppercase; color: var(--text-dim); letter-spacing: .05em; }
.modal-info-value { font-size: 15px; font-weight: 600; }

.status-pill {
  display: inline-block; padding: 3px 10px;
  border-radius: 999px; font-size: 13px; font-weight: 600;
}
.sp-free         { background: rgba(67,160,71,.2);  color: #43a047; }
.sp-occupied     { background: rgba(229,57,53,.2);  color: #e53935; }
.sp-reserved     { background: rgba(251,140,0,.2);  color: #fb8c00; }
.sp-out_of_service { background: var(--surface3);   color: var(--text-dim); }

.modal-commands { margin-bottom: 20px; }
.modal-commands h3 { font-size: 12px; font-weight: 700; color: var(--text-dim); text-transform: uppercase; margin-bottom: 10px; }

.section-label { font-size: 11px; font-weight: 700; text-transform: uppercase; color: var(--text-dim); letter-spacing: .05em; margin-bottom: 8px; }

.status-buttons { display: grid; grid-template-columns: 1fr 1fr; gap: 8px; margin-bottom: 20px; }
.status-btn {
  border: 2px solid transparent; background: var(--surface3);
  color: var(--text-dim); font-weight: 600; font-size: 12px;
  padding: 8px; border-radius: var(--radius);
  text-transform: uppercase; letter-spacing: .04em;
  transition: border-color .15s, background .15s, color .15s;
}
.status-btn.tc-btn-free:hover,           .status-btn.tc-btn-free.active           { background: #43a047; color: #fff; border-color: #43a047; }
.status-btn.tc-btn-occupied:hover,       .status-btn.tc-btn-occupied.active       { background: #e53935; color: #fff; border-color: #e53935; }
.status-btn.tc-btn-reserved:hover,       .status-btn.tc-btn-reserved.active       { background: #fb8c00; color: #fff; border-color: #fb8c00; }
.status-btn.tc-btn-out_of_service:hover, .status-btn.tc-btn-out_of_service.active { background: #546e7a; color: #fff; border-color: #546e7a; }

.modal-actions { display: flex; gap: 8px; flex-wrap: wrap; }
.modal-action-btn {
  padding: 9px 16px; border-radius: var(--radius);
  font-weight: 600; font-size: 14px; flex: 1; text-align: center;
}

.edit-form { margin-bottom: 4px; }
</style>
