-- ============================================
-- Gastronoapp - Script de migración
-- Ejecutar una sola vez sobre la DB existente
-- ============================================

USE gastronoapp;

-- Agregar columna role a user (si no existe)
ALTER TABLE user ADD COLUMN IF NOT EXISTS role VARCHAR(20) NOT NULL DEFAULT 'MOZO';

-- Agregar columna status a command (si no existe)
ALTER TABLE command ADD COLUMN IF NOT EXISTS status VARCHAR(20) NOT NULL DEFAULT 'RECIBIDO';

-- Setear roles correctos para usuarios existentes
UPDATE user SET role = 'ADMIN'    WHERE name = 'admin';
UPDATE user SET role = 'COCINERO' WHERE name = 'cocina1';
UPDATE user SET role = 'MOZO'     WHERE role IS NULL OR role = '';

-- Crear tabla dining_table si no existe
CREATE TABLE IF NOT EXISTS dining_table (
    id       INT         NOT NULL AUTO_INCREMENT,
    number   INT         NOT NULL UNIQUE,
    capacity INT         NOT NULL DEFAULT 4,
    status   VARCHAR(20) NOT NULL DEFAULT 'FREE',
    PRIMARY KEY (id)
);

-- Arreglar filas con status NULL (si las hay)
UPDATE dining_table SET status = 'FREE' WHERE status IS NULL OR status = '';

-- Insertar usuarios de ejemplo si no existen
INSERT IGNORE INTO user (id, name, email, password, role) VALUES
    (1, 'admin',   'admin@gastro.com',   '1234', 'ADMIN'),
    (2, 'mozo1',   'mozo1@gastro.com',   '1234', 'MOZO'),
    (3, 'cocina1', 'cocina1@gastro.com', '1234', 'COCINERO');
