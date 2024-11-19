package com.gastronoapp.entity;

import jakarta.persistence.EmbeddedId;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;

@Entity
@Table(name = "command_menu_item")
public class CommandMenuItem {

    @EmbeddedId
    private CommandMenuItemId id;

    public CommandMenuItem() {
    }

    public CommandMenuItem(CommandMenuItemId id) {
        this.id = id;
    }

    // Getters y Setters
    public CommandMenuItemId getId() {
        return id;
    }

    public void setId(CommandMenuItemId id) {
        this.id = id;
    }
}
