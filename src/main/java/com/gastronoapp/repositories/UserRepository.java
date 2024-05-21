package com.gastronoapp.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.gastronoapp.entity.User;

// El repository permite hacer queries a la bd
@Repository
public interface UserRepository extends JpaRepository<User, Integer> {

    
}
