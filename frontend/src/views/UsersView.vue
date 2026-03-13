<template>
  <div class="page">
    <div class="page-header">
      <h1 class="page-title">Usuarios</h1>
      <button class="btn-primary" @click="showForm = !showForm">
        {{ showForm ? 'Cancelar' : '+ Nuevo usuario' }}
      </button>
    </div>

    <div v-if="showForm" class="card form-card">
      <h2 class="form-title">Nuevo usuario</h2>
      <div class="form-row">
        <div class="form-group">
          <label>Nombre</label>
          <input v-model="form.name" placeholder="Nombre" />
        </div>
        <div class="form-group">
          <label>Email</label>
          <input v-model="form.email" type="email" placeholder="email@ejemplo.com" />
        </div>
      </div>
      <div class="form-row">
        <div class="form-group">
          <label>Contraseña</label>
          <input v-model="form.password" type="password" placeholder="••••••••" />
        </div>
        <div class="form-group">
          <label>Rol</label>
          <select v-model="form.role">
            <option value="MOZO">MOZO</option>
            <option value="ADMIN">ADMIN</option>
            <option value="COCINERO">COCINERO</option>
          </select>
        </div>
      </div>
      <div v-if="formError" class="alert alert-error">{{ formError }}</div>
      <button class="btn-primary" @click="addUser" :disabled="saving">
        {{ saving ? 'Guardando...' : 'Guardar usuario' }}
      </button>
    </div>

    <div v-if="loading" class="spinner"></div>
    <div v-else-if="!users.length" class="empty-state card">No hay usuarios.</div>
    <div v-else class="card">
      <table>
        <thead>
          <tr>
            <th>#</th>
            <th>Nombre</th>
            <th>Email</th>
            <th>Rol</th>
            <th></th>
          </tr>
        </thead>
        <tbody>
          <tr v-for="user in users" :key="user.id">
            <td>{{ user.id }}</td>
            <td><strong>{{ user.name }}</strong></td>
            <td>{{ user.email || '-' }}</td>
            <td>
              <span class="badge" :class="user.role === 'ADMIN' ? 'badge-recibido' : 'badge-entregado'">
                {{ user.role }}
              </span>
            </td>
            <td>
              <button
                class="btn-danger"
                style="padding:4px 12px;font-size:13px;"
                :disabled="user.id === auth.user?.id"
                @click="removeUser(user.id)"
              >
                Eliminar
              </button>
            </td>
          </tr>
        </tbody>
      </table>
    </div>
  </div>
</template>

<script setup>
import { ref, onMounted } from 'vue'
import { getUsers, saveUser, deleteUser } from '../api'
import { auth } from '../stores/auth'

const users = ref([])
const loading = ref(true)
const showForm = ref(false)
const saving = ref(false)
const formError = ref('')

const form = ref({ name: '', email: '', password: '', role: 'MOZO' })

onMounted(async () => {
  try {
    const res = await getUsers()
    users.value = res.data
  } finally {
    loading.value = false
  }
})

async function addUser() {
  formError.value = ''
  if (!form.value.name || !form.value.password) {
    formError.value = 'Nombre y contraseña son requeridos.'
    return
  }
  saving.value = true
  try {
    const res = await saveUser({ ...form.value })
    users.value.push(res.data)
    showForm.value = false
    form.value = { name: '', email: '', password: '', role: 'MOZO' }
  } catch {
    formError.value = 'Error al guardar el usuario.'
  } finally {
    saving.value = false
  }
}

async function removeUser(id) {
  if (!confirm('¿Eliminar este usuario?')) return
  await deleteUser(id)
  users.value = users.value.filter(u => u.id !== id)
}
</script>

<style scoped>
.form-card { margin-bottom: 20px; }
.form-title { font-size: 16px; font-weight: 700; margin-bottom: 16px; }
.form-row { display: grid; grid-template-columns: 1fr 1fr; gap: 12px; }
@media (max-width: 600px) { .form-row { grid-template-columns: 1fr; } }
</style>
