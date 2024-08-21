package com.gastronoapp.services;

import com.gastronoapp.entity.CommandUser;
import com.gastronoapp.repositories.CommandUserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class CommandUserService {

    @Autowired
    CommandUserRepository commandUserRepository;

    public CommandUser saveCommandUser(CommandUser commandUser) {
        return commandUserRepository.save(commandUser);
    }

}
