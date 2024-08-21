package com.gastronoapp.repositories;

import com.gastronoapp.entity.MenuCategory;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface MenuCategoryRepository extends JpaRepository <MenuCategory, Integer> {
    
}
