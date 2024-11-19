package com.gastronoapp.controllers;

import com.gastronoapp.dto.CommandMenuItemDTO;
import com.gastronoapp.services.CommandMenuItemService;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/commandmenuitem")
public class CommandMenuItemController {

    @Autowired
    private CommandMenuItemService commandMenuItemService;

    @GetMapping("/all")
    public ResponseEntity<List<CommandMenuItemDTO>> getAllCommandMenuItems() {
        List<CommandMenuItemDTO> commandMenuItemDTOs = commandMenuItemService.getAllCommandMenuItems();
        return ResponseEntity.ok(commandMenuItemDTOs);
    }
}
