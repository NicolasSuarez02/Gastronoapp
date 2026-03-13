-- ============================================
-- Gastronoapp - Script de base de datos
-- MySQL 8.x
-- ============================================

CREATE DATABASE IF NOT EXISTS gastronoapp
    CHARACTER SET utf8mb4
    COLLATE utf8mb4_unicode_ci;

USE gastronoapp;

-- --------------------------------------------
-- Tabla: user
-- --------------------------------------------
CREATE TABLE IF NOT EXISTS user (
    id       INT          NOT NULL AUTO_INCREMENT,
    name     VARCHAR(255),
    email    VARCHAR(255),
    password VARCHAR(255),
    role     VARCHAR(20)  NOT NULL DEFAULT 'MOZO',
    PRIMARY KEY (id)
);

-- Migración
ALTER TABLE user ADD COLUMN IF NOT EXISTS role VARCHAR(20) NOT NULL DEFAULT 'MOZO';

-- Usuarios de ejemplo
INSERT IGNORE INTO user (id, name, email, password, role) VALUES
    (1, 'admin',   'admin@gastro.com',    '1234', 'ADMIN'),
    (2, 'mozo1',   'mozo1@gastro.com',    '1234', 'MOZO'),
    (3, 'cocina1', 'cocina1@gastro.com',  '1234', 'COCINERO');

-- --------------------------------------------
-- Tabla: client
-- --------------------------------------------
CREATE TABLE IF NOT EXISTS client (
    id   INT          NOT NULL AUTO_INCREMENT,
    name VARCHAR(255),
    PRIMARY KEY (id)
);

-- --------------------------------------------
-- Tabla: menu_category
-- --------------------------------------------
CREATE TABLE IF NOT EXISTS menu_category (
    id   INT          NOT NULL AUTO_INCREMENT,
    name VARCHAR(255),
    PRIMARY KEY (id)
);

-- Categorías base (hardcodeadas en el frontend)
INSERT INTO menu_category (id, name) VALUES
    (1, 'Bebida'),
    (2, 'Plato Principal'),
    (3, 'Postre');

-- --------------------------------------------
-- Tabla: menu_item
-- --------------------------------------------
CREATE TABLE IF NOT EXISTS menu_item (
    id               INT            NOT NULL AUTO_INCREMENT,
    name             VARCHAR(255),
    price            DOUBLE,
    description      VARCHAR(255),
    quantity         INT            DEFAULT 0,
    menu_category_id INT,
    PRIMARY KEY (id),
    CONSTRAINT fk_menu_item_category
        FOREIGN KEY (menu_category_id) REFERENCES menu_category (id)
);

-- --------------------------------------------
-- Tabla: command
-- --------------------------------------------
CREATE TABLE IF NOT EXISTS command (
    id           INT          NOT NULL AUTO_INCREMENT,
    observations VARCHAR(255),
    number       INT,
    date         DATETIME,
    status       VARCHAR(20)  NOT NULL DEFAULT 'RECIBIDO',
    PRIMARY KEY (id)
);

-- Migración: agregar columna status si ya existe la tabla
ALTER TABLE command ADD COLUMN IF NOT EXISTS status VARCHAR(20) NOT NULL DEFAULT 'RECIBIDO';

-- --------------------------------------------
-- Tabla: command_menu_item
-- --------------------------------------------
CREATE TABLE IF NOT EXISTS command_menu_item (
    id           BIGINT NOT NULL AUTO_INCREMENT,
    command_id   INT    NOT NULL,
    menu_item_id INT    NOT NULL,
    PRIMARY KEY (id),
    CONSTRAINT fk_cmi_command
        FOREIGN KEY (command_id)   REFERENCES command   (id),
    CONSTRAINT fk_cmi_menu_item
        FOREIGN KEY (menu_item_id) REFERENCES menu_item (id)
);

-- --------------------------------------------
-- Tabla: dining_table
-- --------------------------------------------
CREATE TABLE IF NOT EXISTS dining_table (
    id       INT          NOT NULL AUTO_INCREMENT,
    number   INT          NOT NULL UNIQUE,
    capacity INT          NOT NULL DEFAULT 4,
    status   VARCHAR(20)  NOT NULL DEFAULT 'FREE',
    PRIMARY KEY (id)
);

-- --------------------------------------------
-- Tabla: command_user
-- --------------------------------------------
CREATE TABLE IF NOT EXISTS command_user (
    id         INT NOT NULL AUTO_INCREMENT,
    user_id    INT,
    command_id INT,
    PRIMARY KEY (id),
    CONSTRAINT fk_cu_user
        FOREIGN KEY (user_id)    REFERENCES user    (id),
    CONSTRAINT fk_cu_command
        FOREIGN KEY (command_id) REFERENCES command (id)
);
