<template>
  <div class="login-page">
    <div class="login-card">
      <div class="login-header">
        <span class="login-icon">🍽</span>
        <h1>Gastronoapp</h1>
        <p>Ingresá con tus credenciales</p>
      </div>

      <form @submit.prevent="handleLogin">
        <div class="form-group">
          <label>Nombre</label>
          <input v-model="form.name" type="text" placeholder="Tu nombre" required autofocus />
        </div>
        <div class="form-group">
          <label>Contraseña</label>
          <input v-model="form.password" type="password" placeholder="••••••••" required />
        </div>
        <div v-if="error" class="alert alert-error">{{ error }}</div>
        <button type="submit" class="btn-primary login-btn" :disabled="loading">
          {{ loading ? 'Ingresando...' : 'Ingresar' }}
        </button>
      </form>
    </div>
  </div>
</template>

<script setup>
import { ref } from 'vue'
import { useRouter } from 'vue-router'
import { loginUser } from '../api'
import { auth } from '../stores/auth'

const router = useRouter()
const form = ref({ name: '', password: '' })
const error = ref('')
const loading = ref(false)

async function handleLogin() {
  error.value = ''
  loading.value = true
  try {
    const res = await loginUser(form.value.name, form.value.password)
    auth.login(res.data)
    const role = res.data.role
    if (role === 'COCINERO') router.push('/kitchen')
    else if (role === 'MOZO') router.push('/tables')
    else router.push('/dashboard')
  } catch (e) {
    error.value = e.response?.status === 401
      ? 'Nombre o contraseña incorrectos.'
      : 'Error al conectar con el servidor.'
  } finally {
    loading.value = false
  }
}
</script>

<style scoped>
.login-page {
  min-height: 100vh;
  display: flex;
  align-items: center;
  justify-content: center;
  background: var(--bg);
  background-image: radial-gradient(ellipse at 30% 60%, rgba(224,48,48,0.08) 0%, transparent 55%);
}

.login-card {
  background: var(--surface);
  border: 1px solid var(--border);
  border-radius: 12px;
  box-shadow: 0 8px 32px rgba(0,0,0,.45);
  padding: 40px;
  width: 100%;
  max-width: 380px;
}

.login-header {
  text-align: center;
  margin-bottom: 28px;
}

.login-icon { font-size: 40px; }

.login-header h1 {
  font-size: 26px;
  font-weight: 700;
  margin: 8px 0 4px;
}

.login-header p { color: var(--text-muted); font-size: 14px; }

.login-btn { width: 100%; padding: 10px; font-size: 15px; font-weight: 600; }
</style>
