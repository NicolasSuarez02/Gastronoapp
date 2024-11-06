package com.gastronoapp.services;

import com.gastronoapp.entity.CommandMenuItem;
import com.gastronoapp.repositories.CommandMenuItemRepository;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class CommandMenuItemService {

    @Autowired
    CommandMenuItemRepository commandMenuItemRepository;

    public List<CommandMenuItem> getAllCommandMenuItems() {
        return commandMenuItemRepository.findAll();
    }

}
