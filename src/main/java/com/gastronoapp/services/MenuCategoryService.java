package com.gastronoapp.services;

import com.gastronoapp.entity.MenuCategory;
import com.gastronoapp.repositories.MenuCategoryRepository;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class MenuCategoryService {

    @Autowired
    MenuCategoryRepository menuCategoryRepository;

    public MenuCategory saveMenuCategory(MenuCategory menuCategory) {
        return menuCategoryRepository.save(menuCategory);

    }

    public List<MenuCategory> getAllMenuCategories() {
        return menuCategoryRepository.findAll();
    }

    public void deleteMenuCategory(int id) {
        menuCategoryRepository.deleteById(id);
    }

    public MenuCategory updateMenuCategory(int id, MenuCategory updates) {
        MenuCategory cat = menuCategoryRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Category not found: " + id));
        if (updates.getName() != null && !updates.getName().isBlank()) {
            cat.setName(updates.getName());
        }
        return menuCategoryRepository.save(cat);
    }
}
