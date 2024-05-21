package com.gastronoapp.services;

import java.util.ArrayList;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.gastronoapp.entity.User;
import com.gastronoapp.repositories.UserRepository;

@Service
public class UserService {

    @Autowired //sirve para inyección de dependencias
    UserRepository userRepository;

    public ArrayList<User> getUsers(){
        return (ArrayList<User>) userRepository.findAll();
    }

    public User saveUser(User user){
        return userRepository.save(user);
    }
}
