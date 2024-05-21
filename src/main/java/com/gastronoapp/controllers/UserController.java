package com.gastronoapp.controllers;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.gastronoapp.entity.User;
import com.gastronoapp.services.UserService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.ArrayList;


@RestController
@RequestMapping("/user")
public class UserController {

    @Autowired
    private UserService userService;

    @GetMapping
    public ArrayList<User> getUsers(){
        return this.userService.getUsers();
    }

    // @PostMapping
    // public UserModel savUser(@RequestBody)
    
}
