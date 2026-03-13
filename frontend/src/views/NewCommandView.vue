<template>
  <div class="page">
    <div class="page-header">
      <h1 class="page-title">{{ isEditing ? 'Editar pedido #' + editId : 'Nuevo pedido' }}</h1>
      <RouterLink to="/tables" class="btn-secondary" style="padding:8px 16px;border-radius:var(--radius);font-size:14px;">← Volver</RouterLink>
    </div>

    <div v-if="loadingCommand" class="spinner"></div>
    <div v-else class="layout">
      <!-- Form -->
      <div class="card form-card">
        <div class="form-group">
          <label>Número de mesa</label>
          <input v-model.number="form.number" type="number" min="1" max="99" placeholder="Ej: 5" :disabled="isEditing" />
        </div>
        <div class="form-group">
          <label>Observaciones</label>
          <textarea v-model="form.observations" rows="3" placeholder="Sin TACC, alergia a mariscos..."></textarea>
        </div>

        <div v-if="error" class="alert alert-error">{{ error }}</div>
        <div v-if="success" class="alert alert-success">
          {{ isEditing ? 'Pedido actualizado.' : 'Pedido creado correctamente.' }}
        </div>

        <div class="form-footer">
          <span class="selected-count">{{ selectedIds.length }} ítem(s) seleccionado(s)</span>
          <button class="btn-primary" @click="submit" :disabled="submitting || !selectedIds.length || !form.number">
            {{ submitting ? 'Guardando...' : (isEditing ? 'Guardar cambios' : 'Crear pedido') }}
          </button>
        </div>
      </div>

      <!-- Menu selector -->
      <div class="menu-panel">
        <div class="menu-search">
          <input v-model="search" placeholder="Buscar plato..." />
        </div>

        <div v-if="loadingMenu" class="spinner"></div>
        <div v-else>
          <div v-for="cat in filteredByCategory" :key="cat.id" class="category-section">
            <h3 class="cat-title">{{ cat.name }}</h3>
            <div class="items-grid">
              <div
                v-for="item in cat.items"
                :key="item.id"
                :class="['menu-item-card', { selected: isSelected(item.id), 'out-of-stock': effectiveStock(item) <= 0 && countOf(item.id) === 0 }]"
                @click="addItem(item)"
              >
                <div class="item-name">{{ item.name }}</div>
                <div class="item-price">${{ item.price?.toFixed(2) }}</div>
                <div class="item-stock" :class="{ low: effectiveStock(item) < 3 }">
                  Stock: {{ effectiveStock(item) }}
                </div>
                <div class="item-count" v-if="countOf(item.id) > 0" @click.stop="removeOne(item)" title="Quitar uno">
                  × {{ countOf(item.id) }} ✕
                </div>
              </div>
            </div>
          </div>
          <div v-if="!filteredByCategory.length" class="empty-state">Sin resultados.</div>
        </div>
      </div>
    </div>
  </div>
</template>

<script setup>
import { ref, computed, onMounted } from 'vue'
import { useRouter, useRoute } from 'vue-router'
import { getMenuItems, getMenuCategories, saveCommand, updateCommand, getCommandById } from '../api'

const router = useRouter()
const route  = useRoute()

const editId    = route.query.edit ? Number(route.query.edit) : null
const isEditing = !!editId

const form = ref({ number: route.query.table ? Number(route.query.table) : null, observations: '' })
const selectedIds  = ref([])
const originalIds  = ref([])   // ids as they were when the command was loaded
const search       = ref('')
const error        = ref('')
const success      = ref(false)
const submitting   = ref(false)
const loadingMenu  = ref(true)
const loadingCommand = ref(isEditing)

const menuItems  = ref([])
const categories = ref([])

onMounted(async () => {
  try {
    const reqs = [getMenuItems(), getMenuCategories()]
    if (isEditing) reqs.push(getCommandById(editId))
    const results = await Promise.all(reqs)
    menuItems.value  = results[0].data
    categories.value = results[1].data
    if (isEditing) {
      const cmd = results[2].data
      form.value.number       = cmd.number
      form.value.observations = cmd.observations || ''
      const ids = cmd.menuItems.map(i => i.id)
      selectedIds.value = [...ids]
      originalIds.value = [...ids]
    }
  } finally {
    loadingMenu.value    = false
    loadingCommand.value = false
  }
})

// Available stock = real stock + what's already in this command (so editing doesn't block re-selecting same items)
function effectiveStock(item) {
  const alreadySelected = originalIds.value.filter(id => id === item.id).length
  return item.quantity + alreadySelected
}

const filteredItems = computed(() => {
  if (!search.value) return menuItems.value
  const q = search.value.toLowerCase()
  return menuItems.value.filter(i => i.name.toLowerCase().includes(q))
})

const filteredByCategory = computed(() =>
  categories.value
    .map(cat => ({
      ...cat,
      items: filteredItems.value.filter(i => i.menu_category_id === cat.id || i.menuCategory?.id === cat.id)
    }))
    .filter(cat => cat.items.length > 0)
)

function isSelected(id) { return selectedIds.value.includes(id) }
function countOf(id)    { return selectedIds.value.filter(x => x === id).length }

function addItem(item) {
  if (effectiveStock(item) <= countOf(item.id)) return  // at max or no stock
  selectedIds.value.push(item.id)
}

function removeOne(item) {
  const idx = selectedIds.value.lastIndexOf(item.id)
  if (idx !== -1) selectedIds.value.splice(idx, 1)
}

async function submit() {
  error.value   = ''
  success.value = false
  if (!form.value.number || form.value.number < 1 || form.value.number > 99) {
    error.value = 'El número de mesa debe estar entre 1 y 99.'
    return
  }
  if (!selectedIds.value.length) {
    error.value = 'Seleccioná al menos un ítem.'
    return
  }
  submitting.value = true
  try {
    const dto = {
      number:       form.value.number,
      observations: form.value.observations || null,
      date:         new Date().toISOString(),
      menuItemIds:  selectedIds.value,
    }
    if (isEditing) {
      await updateCommand(editId, dto)
    } else {
      await saveCommand(dto)
    }
    success.value = true
    setTimeout(() => router.push('/tables'), 1200)
  } catch (e) {
    error.value = e.response?.data?.message || 'Error al guardar el pedido.'
  } finally {
    submitting.value = false
  }
}
</script>

<style scoped>
.layout {
  display: grid;
  grid-template-columns: 340px 1fr;
  gap: 20px;
  align-items: start;
}

@media (max-width: 768px) {
  .layout { grid-template-columns: 1fr; }
}

.form-card textarea { resize: vertical; }

.form-footer {
  display: flex;
  justify-content: space-between;
  align-items: center;
  margin-top: 8px;
}
.selected-count { font-size: 13px; color: var(--text-muted); }

.menu-panel { background: var(--surface); border-radius: var(--radius); box-shadow: var(--shadow); overflow: hidden; }
.menu-search { padding: 16px; border-bottom: 1px solid var(--border); }

.category-section { padding: 16px; }
.cat-title { font-size: 13px; font-weight: 700; color: var(--text-muted); text-transform: uppercase; letter-spacing: .05em; margin-bottom: 12px; }

.items-grid {
  display: grid;
  grid-template-columns: repeat(auto-fill, minmax(130px, 1fr));
  gap: 10px;
}

.menu-item-card {
  border: 2px solid var(--border);
  border-radius: var(--radius);
  padding: 12px;
  cursor: pointer;
  transition: border-color .15s, background .15s;
  position: relative;
  user-select: none;
}
.menu-item-card:hover:not(.out-of-stock) { border-color: var(--primary); background: rgba(224,48,48,.08); }
.menu-item-card.selected { border-color: var(--primary); background: rgba(224,48,48,.12); }
.menu-item-card.out-of-stock { opacity: .45; cursor: not-allowed; }

.item-name  { font-weight: 600; font-size: 14px; margin-bottom: 4px; }
.item-price { color: var(--primary); font-weight: 700; font-size: 14px; }
.item-stock { font-size: 12px; color: var(--text-muted); margin-top: 4px; }
.item-stock.low { color: var(--warning); }

.item-count {
  position: absolute;
  top: 6px;
  right: 8px;
  background: var(--primary);
  color: #fff;
  border-radius: 999px;
  font-size: 11px;
  font-weight: 700;
  padding: 1px 6px;
}
</style>
