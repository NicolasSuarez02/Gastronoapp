package com.gastronoapp.entity;

import jakarta.persistence.*;

@Entity
@Table(name = "dining_table")
public class DiningTable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Column(name = "number", unique = true, nullable = false)
    private int tableNumber;

    private int capacity;

    @Column(nullable = false)
    private String status = "FREE";

    public DiningTable() {}

    public DiningTable(int tableNumber, int capacity) {
        this.tableNumber = tableNumber;
        this.capacity = capacity;
        this.status = "FREE";
    }

    public int getId() { return id; }
    public void setId(int id) { this.id = id; }

    public int getTableNumber() { return tableNumber; }
    public void setTableNumber(int tableNumber) { this.tableNumber = tableNumber; }

    public int getCapacity() { return capacity; }
    public void setCapacity(int capacity) { this.capacity = capacity; }

    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }
}
