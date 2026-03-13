<template>
  <div class="page">
    <div class="page-header">
      <h1 class="page-title">Categorías del menú</h1>
    </div>

    <div class="layout">
      <!-- Add form -->
      <div class="card form-card">
        <h2 class="form-title">Nueva categoría</h2>
        <div class="form-group">
          <label>Nombre</label>
          <input v-model="newName" placeholder="Ej: Entradas, Postres..." @keyup.enter="addCategory" />
        </div>
        <div v-if="formError" class="alert alert-error">{{ formError }}</div>
        <button class="btn-primary" @click="addCategory" :disabled="saving">
          {{ saving ? 'Guardando...' : 'Agregar' }}
        </button>
      </div>

      <!-- List -->
      <div class="card">
        <div v-if="loading" class="spinner"></div>
        <div v-else-if="!categories.length" class="empty-state">No hay categorías.</div>
        <table v-else>
          <thead>
            <tr>
              <th>#</th>
              <th>Nombre</th>
              <th></th>
            </tr>
          </thead>
          <tbody>
            <tr v-for="cat in categories" :key="cat.id">
              <td>{{ cat.id }}</td>
              <td>
                <template v-if="editingId === cat.id">
                  <input
                    v-model="editingName"
                    class="inline-input"
                    @keyup.enter="saveEdit(cat)"
                    @keyup.escape="cancelEdit"
                    ref="editInputRef"
                  />
                </template>
                <strong v-else>{{ cat.name }}</strong>
              </td>
              <td class="actions-cell">
                <template v-if="editingId === cat.id">
                  <button class="btn-sm btn-primary" @click="saveEdit(cat)" :disabled="editSaving">
                    {{ editSaving ? '...' : 'Guardar' }}
                  </button>
                  <button class="btn-sm btn-secondary" @click="cancelEdit">Cancelar</button>
                </template>
                <template v-else>
                  <button class="btn-sm btn-secondary" @click="startEdit(cat)">Editar</button>
                  <button class="btn-sm btn-danger" @click="removeCategory(cat.id)">Eliminar</button>
                </template>
              </td>
            </tr>
          </tbody>
        </table>
      </div>
    </div>
  </div>
</template>

<script setup>
import { ref, nextTick, onMounted } from 'vue'
import { getMenuCategories, saveMenuCategory, updateMenuCategory, deleteMenuCategory } from '../api'

const categories = ref([])
const loading = ref(true)
const saving = ref(false)
const newName = ref('')
const formError = ref('')

const editingId   = ref(null)
const editingName = ref('')
const editSaving  = ref(false)
const editInputRef = ref(null)

onMounted(async () => {
  try {
    const res = await getMenuCategories()
    categories.value = res.data
  } finally {
    loading.value = false
  }
})

async function addCategory() {
  formError.value = ''
  if (!newName.value.trim()) { formError.value = 'El nombre es requerido.'; return }
  saving.value = true
  try {
    const res = await saveMenuCategory({ name: newName.value.trim() })
    categories.value.push(res.data)
    newName.value = ''
  } catch {
    formError.value = 'Error al guardar.'
  } finally {
    saving.value = false
  }
}

function startEdit(cat) {
  editingId.value = cat.id
  editingName.value = cat.name
  nextTick(() => editInputRef.value?.focus())
}

function cancelEdit() {
  editingId.value = null
  editingName.value = ''
}

async function saveEdit(cat) {
  if (!editingName.value.trim()) return
  editSaving.value = true
  try {
    const res = await updateMenuCategory(cat.id, { name: editingName.value.trim() })
    const idx = categories.value.findIndex(c => c.id === cat.id)
    if (idx !== -1) categories.value[idx] = res.data
    cancelEdit()
  } catch {
    // silently keep editing
  } finally {
    editSaving.value = false
  }
}

async function removeCategory(id) {
  if (!confirm('¿Eliminar esta categoría?')) return
  await deleteMenuCategory(id)
  categories.value = categories.value.filter(c => c.id !== id)
}
</script>

<style scoped>
.layout { display: grid; grid-template-columns: 300px 1fr; gap: 20px; align-items: start; }
@media (max-width: 700px) { .layout { grid-template-columns: 1fr; } }
.form-title { font-size: 16px; font-weight: 700; margin-bottom: 16px; }
.form-card button { width: 100%; }

.inline-input {
  width: 100%; padding: 4px 8px;
  border: 1.5px solid var(--primary); border-radius: 6px;
  font-size: 14px; background: var(--surface);
  color: var(--text);
}

.actions-cell { display: flex; gap: 6px; justify-content: flex-end; white-space: nowrap; }

.btn-sm {
  padding: 4px 12px; font-size: 13px;
  border-radius: 6px; font-weight: 600; cursor: pointer; border: none;
}
.btn-sm.btn-primary  { background: var(--primary); color: #fff; }
.btn-sm.btn-secondary { background: var(--surface3); color: var(--text); }
.btn-sm.btn-danger   { background: #e53935; color: #fff; }
.btn-sm:hover { opacity: .85; }
</style>
