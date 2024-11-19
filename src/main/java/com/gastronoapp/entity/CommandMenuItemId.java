package com.gastronoapp.entity;

import jakarta.persistence.Embeddable;
import java.io.Serializable;
import java.util.Objects;

@Embeddable
public class CommandMenuItemId implements Serializable {

    private int command_id;
    private int menu_item_id;

    public CommandMenuItemId() {
    }

    public CommandMenuItemId(int command_id, int menu_item_id) {
        this.command_id = command_id;
        this.menu_item_id = menu_item_id;
    }

    // Getters y Setters
    public int getCommand_id() {
        return command_id;
    }

    public void setCommand_id(int command_id) {
        this.command_id = command_id;
    }

    public int getMenu_item_id() {
        return menu_item_id;
    }

    public void setMenu_item_id(int menu_item_id) {
        this.menu_item_id = menu_item_id;
    }

    // Sobrescribir equals y hashCode
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        CommandMenuItemId that = (CommandMenuItemId) o;
        return command_id == that.command_id && menu_item_id == that.menu_item_id;
    }

    @Override
    public int hashCode() {
        return Objects.hash(command_id, menu_item_id);
    }
}
