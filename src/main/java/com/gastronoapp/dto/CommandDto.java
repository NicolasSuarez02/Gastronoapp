package com.gastronoapp.dto;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;

import java.util.Date;
import java.util.List;

public class CommandDto {

    private String observations;

    @Min(value = 1, message = "El número de mesa debe ser mayor a 0")
    @Max(value = 99, message = "El número de mesa no puede superar 99")
    private int number;

    private Date date;

    @NotNull(message = "Debe incluir al menos un producto")
    @NotEmpty(message = "Debe incluir al menos un producto")
    private List<Integer> menuItemIds;

    public CommandDto() {}

    public CommandDto(String observations, int number, Date date, List<Integer> menuItemIds) {
        this.observations = observations;
        this.number = number;
        this.date = date;
        this.menuItemIds = menuItemIds;
    }

    public String getObservations() { return observations; }
    public void setObservations(String observations) { this.observations = observations; }

    public int getNumber() { return number; }
    public void setNumber(int number) { this.number = number; }

    public Date getDate() { return date; }
    public void setDate(Date date) { this.date = date; }

    public List<Integer> getMenuItemIds() { return menuItemIds; }
    public void setMenuItemIds(List<Integer> menuItemIds) { this.menuItemIds = menuItemIds; }
}
