package com.gastronoapp.controllers;

import com.gastronoapp.dto.CommandDto;
import com.gastronoapp.entity.Command;
import com.gastronoapp.services.CommandService;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/command")
public class CommandController {

    @Autowired
    CommandService commandService;

    @PostMapping("/save")
    public ResponseEntity<Command> createCommand(@RequestBody CommandDto commandDto) {
        Command command = commandService.saveCommand(commandDto);
        return ResponseEntity.ok(command);
    }
    
    @GetMapping("/all")
    public ResponseEntity<List<Command>> getAllCommands() {
        List<Command> command = commandService.getAllCommands();
        return ResponseEntity.ok(command);
    }
    
    @GetMapping("/find/{id}")
    public ResponseEntity<Command> getCommandById(@PathVariable int id) {
        return commandService.getCommandById(id)
                .map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }
    
    

}
