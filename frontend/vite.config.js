import { defineConfig } from 'vite'
import vue from '@vitejs/plugin-vue'

export default defineConfig({
  plugins: [vue()],
  server: {
    port: 5173,
    proxy: {
      '/user': 'http://localhost:8080',
      '/clients': 'http://localhost:8080',
      '/command': 'http://localhost:8080',
      '/menuitems': 'http://localhost:8080',
      '/menucategory': 'http://localhost:8080',
      '/command-menu-item': 'http://localhost:8080',
      '/tables': 'http://localhost:8080',
    }
  }
})
