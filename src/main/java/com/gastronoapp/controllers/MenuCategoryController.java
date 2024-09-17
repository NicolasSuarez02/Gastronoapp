package com.gastronoapp.controllers;

import com.gastronoapp.entity.MenuCategory;
import com.gastronoapp.services.MenuCategoryService;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/menucategory")
public class MenuCategoryController {

    @Autowired
    MenuCategoryService menuCategoryService;

    @PostMapping("/save")
    public ResponseEntity<MenuCategory> createMenuCategory(@RequestBody MenuCategory menuCategory) {
        MenuCategory savedMenuCategory = menuCategoryService.saveMenuCategory(menuCategory);
        return ResponseEntity.ok(savedMenuCategory);

    }

    @GetMapping("/all")
    public ResponseEntity<List<MenuCategory>> getAllMenuCategories() {
        List<MenuCategory> menuCategories = menuCategoryService.getAllMenuCategories();
        return ResponseEntity.ok(menuCategories);
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<Void> deleteMenuCategory(@PathVariable int id) {
        menuCategoryService.deleteMenuCategory(id);
        return ResponseEntity.noContent().build();
    }

}
