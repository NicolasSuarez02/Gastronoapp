package com.gastronoapp.dto;



public class CommandMenuItemDTO {
    private String observations;
    private int quantity;
    private String name;
    private int command_id;
    private Double price;

    public CommandMenuItemDTO() {
    }

    public CommandMenuItemDTO(String observations, int quantity, String name, int command_id, Double price) {
        this.observations = observations;
        this.quantity = quantity;
        this.name = name;
        this.command_id = command_id;
        this.price = price;
    }

    public String getObservations() {
        return observations;
    }

    public void setObservations(String observations) {
        this.observations = observations;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getCommand_id() {
        return command_id;
    }

    public void setCommand_id(int command_id) {
        this.command_id = command_id;
    }

    public Double getPrice() {
        return price;
    }

    public void setPrice(Double price) {
        this.price = price;
    }
   
}
