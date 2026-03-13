import { createRouter, createWebHistory } from 'vue-router'
import { auth } from '../stores/auth'

import LoginView from '../views/LoginView.vue'
import DashboardView from '../views/DashboardView.vue'
import CommandsView from '../views/CommandsView.vue'
import NewCommandView from '../views/NewCommandView.vue'
import MenuItemsView from '../views/MenuItemsView.vue'
import MenuCategoriesView from '../views/MenuCategoriesView.vue'
import UsersView from '../views/UsersView.vue'
import KitchenView from '../views/KitchenView.vue'
import TablesView from '../views/TablesView.vue'

const routes = [
  { path: '/', redirect: '/dashboard' },
  { path: '/login', component: LoginView, meta: { public: true } },
  { path: '/dashboard', component: DashboardView },
  { path: '/tables', component: TablesView },
  { path: '/commands', component: CommandsView },
  { path: '/commands/new', component: NewCommandView },
  { path: '/menu-items', component: MenuItemsView },
  { path: '/menu-categories', component: MenuCategoriesView },
  { path: '/users', component: UsersView },
  { path: '/kitchen', component: KitchenView },
]

const router = createRouter({
  history: createWebHistory(),
  routes,
})

router.beforeEach((to) => {
  if (!to.meta.public && !auth.isLoggedIn) {
    return '/login'
  }
})

export default router
