package com.gastronoapp.repositories;

import com.gastronoapp.entity.CommandMenuItem;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CommandMenuItemRepository extends JpaRepository <CommandMenuItem, Integer>{
    
}
