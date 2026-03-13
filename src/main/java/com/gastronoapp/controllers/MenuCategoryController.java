package com.gastronoapp.controllers;

import com.gastronoapp.entity.MenuCategory;
import com.gastronoapp.services.MenuCategoryService;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/menucategory")
@CrossOrigin(origins = "*")
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

    @PutMapping("/{id}")
    public ResponseEntity<MenuCategory> updateMenuCategory(@PathVariable int id, @RequestBody MenuCategory updates) {
        return ResponseEntity.ok(menuCategoryService.updateMenuCategory(id, updates));
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<Void> deleteMenuCategory(@PathVariable int id) {
        menuCategoryService.deleteMenuCategory(id);
        return ResponseEntity.noContent().build();
    }

}
