package com.gastronoapp.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.Table;


@Entity
@Table (name ="menu_item")
public class MenuItem {
    @Id
 @GeneratedValue(strategy = GenerationType.IDENTITY)
 private int id;
 private String name;
 private Double price;
 private String description;
 private int quantity;
 
 @JoinColumn
 private int menu_category_id;

    public MenuItem() {
    }

    public MenuItem(int id, String name, Double price, String description, int quantity, int menu_category_id) {
        this.id = id;
        this.name = name;
        this.price = price;
        this.description = description;
        this.quantity = quantity;
        this.menu_category_id = menu_category_id;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(Double price) {
        this.price = price;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public int getMenu_category_id() {
        return menu_category_id;
    }

    public void setMenu_category_id(int menu_category_id) {
        this.menu_category_id = menu_category_id;
    }
 
 
 
 
}
