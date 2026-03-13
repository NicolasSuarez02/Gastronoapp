import axios from 'axios'

const api = axios.create({ baseURL: '' })

// Users
export const getUsers = () => api.get('/user')
export const saveUser = (user) => api.post('/user/save', user)
export const loginUser = (name, password) => api.post('/user/login', { name, password })
export const deleteUser = (id) => api.delete(`/user/${id}`)

// Clients
export const getClients = () => api.get('/clients/all')
export const saveClient = (client) => api.post('/clients/new_client', client)
export const searchClientByName = (name) => api.get('/clients/name', { params: { name } })

// Commands
export const getCommands = () => api.get('/command/all')
export const getCommandById = (id) => api.get(`/command/find/${id}`)
export const saveCommand = (dto) => api.post('/command/save', dto)
export const updateCommand = (id, dto) => api.put(`/command/${id}`, dto)
export const updateCommandStatus = (id, status) => api.put(`/command/${id}/status`, { status })

// Menu Items
export const getMenuItems = () => api.get('/menuitems/all')
export const getMenuItemById = (id) => api.get(`/menuitems/find/${id}`)
export const saveMenuItem = (item) => api.post('/menuitems/save', item)
export const updateMenuItem = (id, data) => api.put(`/menuitems/${id}`, data)
export const deleteMenuItem = (id) => api.delete(`/menuitems/${id}`)

// Menu Categories
export const getMenuCategories = () => api.get('/menucategory/all')
export const saveMenuCategory = (cat) => api.post('/menucategory/save', cat)
export const updateMenuCategory = (id, data) => api.put(`/menucategory/${id}`, data)
export const deleteMenuCategory = (id) => api.delete(`/menucategory/delete/${id}`)

// Dining Tables
export const getTables = () => api.get('/tables/all')
export const saveTable = (table) => api.post('/tables/save', table)
export const updateTableStatus = (id, status) => api.put(`/tables/${id}/status`, { status })
export const updateTable = (id, data) => api.put(`/tables/${id}`, data)
export const deleteTable = (id) => api.delete(`/tables/${id}`)
