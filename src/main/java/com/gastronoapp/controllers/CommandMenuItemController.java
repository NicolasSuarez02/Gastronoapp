package com.gastronoapp.controllers;

import com.gastronoapp.entity.CommandMenuItem;
import com.gastronoapp.services.CommandMenuItemService;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/command-menu-item")
public class CommandMenuItemController {

    @Autowired
    CommandMenuItemService commandMenuItemService;

    @GetMapping("/all")
    public ResponseEntity<List<CommandMenuItem>> getAllCommandMenuItems() {
        List<CommandMenuItem> commandMenuItems = commandMenuItemService.getAllCommandMenuItems();
        return ResponseEntity.ok(commandMenuItems);
    }
}
