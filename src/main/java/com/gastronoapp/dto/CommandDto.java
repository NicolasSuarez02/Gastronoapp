package com.gastronoapp.dto;

import java.util.Date;
import java.util.List;

public class CommandDto {

    private String observations;
    private int number;
    private Date date;
    private List <Integer> menuItemIds;

    public CommandDto() {
    }

    public CommandDto(String observations, int number, Date date, List<Integer> menuItemIds) {
        this.observations = observations;
        this.number = number;
        this.date = date;
        this.menuItemIds = menuItemIds;
    }

    public String getObservations() {
        return observations;
    }

    public void setObservations(String observations) {
        this.observations = observations;
    }

    public int getNumber() {
        return number;
    }

    public void setNumber(int number) {
        this.number = number;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public List <Integer> getMenuItemIds() {
        System.out.println("Entro al getMenuItemIds"+menuItemIds);
        return menuItemIds;
    }

    public void setMenuItemIds(List <Integer> menuItemIds) {
        this.menuItemIds = menuItemIds;
    }

}
