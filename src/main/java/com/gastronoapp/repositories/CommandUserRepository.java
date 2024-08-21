package com.gastronoapp.repositories;

import com.gastronoapp.entity.CommandUser;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CommandUserRepository extends JpaRepository <CommandUser, Integer>{
    
}
