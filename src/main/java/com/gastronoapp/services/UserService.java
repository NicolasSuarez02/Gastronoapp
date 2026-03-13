package com.gastronoapp.services;

import com.gastronoapp.entity.User;
import com.gastronoapp.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Optional;

@Service
public class UserService {

    @Autowired
    UserRepository userRepository;

    public ArrayList<User> getUsers() {
        return (ArrayList<User>) userRepository.findAll();
    }

    public User saveUser(User user) {
        return userRepository.save(user);
    }

    public Optional<User> login(String name, String password) {
        return userRepository.findByNameAndPassword(name, password);
    }

    public void deleteUser(int id) {
        userRepository.deleteById(id);
    }
}
