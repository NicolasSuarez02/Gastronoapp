import { reactive } from 'vue'

export const auth = reactive({
  user: JSON.parse(localStorage.getItem('gastrono_user') || 'null'),

  login(user) {
    this.user = user
    localStorage.setItem('gastrono_user', JSON.stringify(user))
  },

  logout() {
    this.user = null
    localStorage.removeItem('gastrono_user')
  },

  get isLoggedIn() {
    return !!this.user
  },

  get isAdmin() {
    return this.user?.role === 'ADMIN'
  }
})
