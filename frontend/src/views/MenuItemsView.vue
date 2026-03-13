<template>
  <div class="page">
    <div class="page-header">
      <h1 class="page-title">Menú</h1>
      <button class="btn-primary" @click="openAddForm">
        {{ showForm && !editingItem ? 'Cancelar' : '+ Agregar plato' }}
      </button>
    </div>

    <!-- Add / Edit form -->
    <div v-if="showForm" class="card form-card">
      <h2 class="form-title">{{ editingItem ? 'Editar plato' : 'Nuevo plato' }}</h2>
      <div class="form-row">
        <div class="form-group">
          <label>Nombre</label>
          <input v-model="form.name" placeholder="Nombre del plato" />
        </div>
        <div class="form-group">
          <label>Precio</label>
          <input v-model.number="form.price" type="number" step="0.01" placeholder="0.00" />
        </div>
      </div>
      <div class="form-row">
        <div class="form-group">
          <label>Descripción</label>
          <input v-model="form.description" placeholder="Descripción breve" />
        </div>
        <div class="form-group">
          <label>Stock disponible</label>
          <input v-model.number="form.quantity" type="number" placeholder="0" />
        </div>
      </div>
      <div class="form-group">
        <label>Categoría</label>
        <select v-model.number="form.menu_category_id">
          <option value="">Sin categoría</option>
          <option v-for="cat in categories" :key="cat.id" :value="cat.id">{{ cat.name }}</option>
        </select>
      </div>
      <div v-if="formError" class="alert alert-error">{{ formError }}</div>
      <div class="form-actions">
        <button class="btn-primary" @click="submitForm" :disabled="saving">
          {{ saving ? 'Guardando...' : (editingItem ? 'Guardar cambios' : 'Guardar plato') }}
        </button>
        <button class="btn-secondary" @click="closeForm">Cancelar</button>
      </div>
    </div>

    <div v-if="loading" class="spinner"></div>
    <div v-else-if="!items.length" class="empty-state card">No hay platos en el menú.</div>
    <div v-else class="card">
      <table>
        <thead>
          <tr>
            <th>#</th>
            <th>Nombre</th>
            <th>Descripción</th>
            <th>Precio</th>
            <th>Stock</th>
            <th></th>
          </tr>
        </thead>
        <tbody>
          <tr v-for="item in items" :key="item.id">
            <td>{{ item.id }}</td>
            <td><strong>{{ item.name }}</strong></td>
            <td>{{ item.description || '-' }}</td>
            <td>${{ item.price?.toFixed(2) }}</td>
            <td>
              <span :class="item.quantity > 0 ? (item.quantity < 3 ? 'badge-en-curso' : 'badge-listo') : 'badge-entregado'" class="badge">
                {{ item.quantity }}
              </span>
            </td>
            <td class="actions-cell">
              <button class="btn-sm btn-secondary" @click="startEdit(item)">Editar</button>
              <button class="btn-sm btn-danger" @click="removeItem(item.id)">Eliminar</button>
            </td>
          </tr>
        </tbody>
      </table>
    </div>
  </div>
</template>

<script setup>
import { ref, onMounted } from 'vue'
import { getMenuItems, saveMenuItem, updateMenuItem, deleteMenuItem, getMenuCategories } from '../api'

const items = ref([])
const categories = ref([])
const loading = ref(true)
const showForm = ref(false)
const saving = ref(false)
const formError = ref('')
const editingItem = ref(null)

const emptyForm = () => ({ name: '', price: null, description: '', quantity: 0, menu_category_id: '' })
const form = ref(emptyForm())

onMounted(async () => {
  try {
    const [itemRes, catRes] = await Promise.all([getMenuItems(), getMenuCategories()])
    items.value = itemRes.data
    categories.value = catRes.data
  } finally {
    loading.value = false
  }
})

function openAddForm() {
  if (showForm.value && !editingItem.value) {
    closeForm()
    return
  }
  editingItem.value = null
  form.value = emptyForm()
  showForm.value = true
}

function startEdit(item) {
  editingItem.value = item
  form.value = {
    name: item.name,
    price: item.price,
    description: item.description || '',
    quantity: item.quantity,
    menu_category_id: item.menu_category_id || ''
  }
  showForm.value = true
  window.scrollTo({ top: 0, behavior: 'smooth' })
}

function closeForm() {
  showForm.value = false
  editingItem.value = null
  form.value = emptyForm()
  formError.value = ''
}

async function submitForm() {
  formError.value = ''
  if (!form.value.name || form.value.price == null) {
    formError.value = 'Nombre y precio son requeridos.'
    return
  }
  saving.value = true
  try {
    if (editingItem.value) {
      const res = await updateMenuItem(editingItem.value.id, { ...form.value })
      const idx = items.value.findIndex(i => i.id === editingItem.value.id)
      if (idx !== -1) items.value[idx] = res.data
    } else {
      const res = await saveMenuItem({ ...form.value })
      items.value.push(res.data)
    }
    closeForm()
  } catch {
    formError.value = 'Error al guardar el plato.'
  } finally {
    saving.value = false
  }
}

async function removeItem(id) {
  if (!confirm('¿Eliminar este plato?')) return
  await deleteMenuItem(id)
  items.value = items.value.filter(i => i.id !== id)
}
</script>

<style scoped>
.form-card { margin-bottom: 20px; }
.form-title { font-size: 16px; font-weight: 700; margin-bottom: 16px; }
.form-row { display: grid; grid-template-columns: 1fr 1fr; gap: 12px; }
@media (max-width: 600px) { .form-row { grid-template-columns: 1fr; } }

.form-actions { display: flex; gap: 8px; margin-top: 4px; }

.actions-cell { display: flex; gap: 6px; justify-content: flex-end; white-space: nowrap; }

.btn-sm {
  padding: 4px 12px; font-size: 13px;
  border-radius: 6px; font-weight: 600; cursor: pointer; border: none;
}
.btn-sm.btn-secondary { background: var(--surface3); color: var(--text); }
.btn-sm.btn-danger    { background: #e53935; color: #fff; }
.btn-sm:hover { opacity: .85; }
</style>
