package com.gastronoapp.services;

import com.gastronoapp.entity.MenuItem;
import com.gastronoapp.repositories.MenuItemRepository;
import java.util.List;
import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class MenuItemService {

    @Autowired
    private MenuItemRepository menuItemRepository;

    public MenuItem saveMenuItem(MenuItem menuItem) {
        return menuItemRepository.save(menuItem);
    }

    public List<MenuItem> getAllMenuItems() {
        return menuItemRepository.findAll();
    }

    public Optional<MenuItem> getMenuItemById(int id) {
        return menuItemRepository.findById(id);
    }

    public Optional<MenuItem> getMenuItemByName(String name) {
        return menuItemRepository.findByName(name);
    }

    public void deleteMenuItem(int id) {
        menuItemRepository.deleteById(id);
    }

    public List<MenuItem> getItemById(List <Integer> ids){
        return menuItemRepository.findAllById(ids);

    }

}
