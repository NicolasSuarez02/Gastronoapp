<template>
  <aside class="sidebar">
    <div class="sidebar-logo">
      <span class="logo-icon">🍽</span>
      <span class="logo-name">Gastronoapp</span>
    </div>

    <div class="sidebar-user">
      <div class="user-avatar">{{ initials }}</div>
      <div class="user-info">
        <div class="user-display-name">{{ auth.user?.name }}</div>
        <div class="user-display-role">{{ auth.user?.role }}</div>
      </div>
    </div>

    <nav class="sidebar-nav">
      <RouterLink to="/dashboard" class="nav-item">
        <span class="nav-icon">
          <svg width="16" height="16" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2"><rect x="3" y="3" width="7" height="7"/><rect x="14" y="3" width="7" height="7"/><rect x="3" y="14" width="7" height="7"/><rect x="14" y="14" width="7" height="7"/></svg>
        </span>
        Panel
      </RouterLink>
      <RouterLink to="/tables" class="nav-item">
        <span class="nav-icon">
          <svg width="16" height="16" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2"><path d="M3 9h18M3 15h18M9 3v18M15 3v18"/><rect x="3" y="3" width="18" height="18" rx="2"/></svg>
        </span>
        Mesas
      </RouterLink>
      <RouterLink to="/commands" class="nav-item">
        <span class="nav-icon">
          <svg width="16" height="16" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2"><path d="M9 5H7a2 2 0 0 0-2 2v12a2 2 0 0 0 2 2h10a2 2 0 0 0 2-2V7a2 2 0 0 0-2-2h-2"/><rect x="9" y="3" width="6" height="4" rx="1"/><line x1="9" y1="12" x2="15" y2="12"/><line x1="9" y1="16" x2="13" y2="16"/></svg>
        </span>
        Pedidos
      </RouterLink>
      <RouterLink to="/commands/new" class="nav-item nav-item-sub">
        <span class="nav-icon">
          <svg width="16" height="16" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2"><circle cx="12" cy="12" r="10"/><line x1="12" y1="8" x2="12" y2="16"/><line x1="8" y1="12" x2="16" y2="12"/></svg>
        </span>
        Nuevo pedido
      </RouterLink>
      <RouterLink to="/kitchen" class="nav-item">
        <span class="nav-icon">
          <svg width="16" height="16" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2"><path d="M12 2a7 7 0 0 1 7 7c0 4-3 6-3 9H8c0-3-3-5-3-9a7 7 0 0 1 7-7z"/><line x1="8" y1="22" x2="16" y2="22"/><line x1="9" y1="18" x2="15" y2="18"/></svg>
        </span>
        Cocina
      </RouterLink>

      <div class="nav-divider"></div>

      <RouterLink to="/menu-items" class="nav-item">
        <span class="nav-icon">
          <svg width="16" height="16" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2"><path d="M12 2C6.48 2 2 6.48 2 12s4.48 10 10 10 10-4.48 10-10S17.52 2 12 2z"/><path d="M12 6v6l4 2"/></svg>
        </span>
        Menú
      </RouterLink>
      <RouterLink to="/menu-categories" class="nav-item">
        <span class="nav-icon">
          <svg width="16" height="16" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2"><line x1="8" y1="6" x2="21" y2="6"/><line x1="8" y1="12" x2="21" y2="12"/><line x1="8" y1="18" x2="21" y2="18"/><line x1="3" y1="6" x2="3.01" y2="6"/><line x1="3" y1="12" x2="3.01" y2="12"/><line x1="3" y1="18" x2="3.01" y2="18"/></svg>
        </span>
        Categorías
      </RouterLink>
      <RouterLink v-if="auth.isAdmin" to="/users" class="nav-item">
        <span class="nav-icon">
          <svg width="16" height="16" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2"><path d="M17 21v-2a4 4 0 0 0-4-4H5a4 4 0 0 0-4 4v2"/><circle cx="9" cy="7" r="4"/><path d="M23 21v-2a4 4 0 0 0-3-3.87"/><path d="M16 3.13a4 4 0 0 1 0 7.75"/></svg>
        </span>
        Usuarios
      </RouterLink>
    </nav>

    <div class="sidebar-footer">
      <button class="nav-item logout-btn" @click="handleLogout">
        <span class="nav-icon">
          <svg width="16" height="16" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2"><path d="M9 21H5a2 2 0 0 1-2-2V5a2 2 0 0 1 2-2h4"/><polyline points="16 17 21 12 16 7"/><line x1="21" y1="12" x2="9" y2="12"/></svg>
        </span>
        Salir
      </button>
    </div>
  </aside>
</template>

<script setup>
import { computed } from 'vue'
import { auth } from '../stores/auth'
import { useRouter } from 'vue-router'

const router = useRouter()

const initials = computed(() => {
  const name = auth.user?.name ?? ''
  return name.slice(0, 2).toUpperCase()
})

function handleLogout() {
  auth.logout()
  router.push('/login')
}
</script>

<style scoped>
.sidebar {
  width: 240px;
  min-height: 100vh;
  background: #1a1a26;
  border-right: 1px solid #2a2a3a;
  display: flex;
  flex-direction: column;
  position: fixed;
  top: 0;
  left: 0;
  z-index: 50;
}

.sidebar-logo {
  display: flex;
  align-items: center;
  gap: 10px;
  padding: 20px 20px 16px;
  border-bottom: 1px solid #2a2a3a;
}
.logo-icon { font-size: 22px; }
.logo-name  { font-size: 16px; font-weight: 700; color: #fff; letter-spacing: .02em; }

.sidebar-user {
  display: flex;
  align-items: center;
  gap: 10px;
  padding: 16px 20px;
  border-bottom: 1px solid #2a2a3a;
}
.user-avatar {
  width: 36px; height: 36px;
  border-radius: 50%;
  background: var(--primary);
  color: #fff;
  display: flex; align-items: center; justify-content: center;
  font-size: 13px; font-weight: 700;
  flex-shrink: 0;
}
.user-display-name { font-size: 13px; font-weight: 600; color: #e8e8f0; }
.user-display-role {
  font-size: 11px;
  color: #666680;
  text-transform: uppercase;
  letter-spacing: .05em;
}

.sidebar-nav {
  flex: 1;
  padding: 12px 0;
  overflow-y: auto;
}

.nav-item {
  display: flex;
  align-items: center;
  gap: 10px;
  padding: 10px 20px;
  font-size: 14px;
  font-weight: 500;
  color: #888899;
  cursor: pointer;
  transition: background .15s, color .15s;
  border: none;
  background: transparent;
  width: 100%;
  text-align: left;
  font-family: inherit;
  text-decoration: none;
  border-left: 3px solid transparent;
}
.nav-item:hover {
  background: rgba(255,255,255,.05);
  color: #e8e8f0;
  opacity: 1;
}
.nav-item.router-link-active {
  color: #fff;
  background: rgba(224,48,48,.15);
  border-left-color: var(--primary);
}
.nav-item-sub { padding-left: 46px; font-size: 13px; }
.nav-item-sub.router-link-active { border-left-color: transparent; background: rgba(255,255,255,.05); color: #e8e8f0; }

.nav-icon {
  display: flex;
  align-items: center;
  flex-shrink: 0;
  opacity: .7;
}
.nav-item.router-link-active .nav-icon,
.nav-item:hover .nav-icon { opacity: 1; }

.nav-divider {
  height: 1px;
  background: #2a2a3a;
  margin: 8px 0;
}

.sidebar-footer {
  border-top: 1px solid #2a2a3a;
  padding: 8px 0;
}
.logout-btn { color: #666680; }
.logout-btn:hover { color: var(--danger); background: rgba(239,68,68,.08); opacity: 1; }

@media (max-width: 768px) {
  .sidebar { display: none; }
}
</style>
