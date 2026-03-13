package com.gastronoapp.controllers;

import com.gastronoapp.dto.CommandDto;
import com.gastronoapp.entity.Command;
import com.gastronoapp.services.CommandService;
import jakarta.validation.Valid;
import java.util.List;
import java.util.Map;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/command")
@CrossOrigin(origins = "*")
public class CommandController {

    @Autowired
    CommandService commandService;

    @PostMapping("/save")
    public ResponseEntity<Command> createCommand(@Valid @RequestBody CommandDto commandDto) {
        return ResponseEntity.ok(commandService.saveCommand(commandDto));
    }

    @GetMapping("/all")
    public ResponseEntity<List<Command>> getAllCommands() {
        return ResponseEntity.ok(commandService.getAllCommands());
    }

    @GetMapping("/find/{id}")
    public ResponseEntity<Command> getCommandById(@PathVariable int id) {
        return commandService.getCommandById(id)
                .map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    @PutMapping("/{id}")
    public ResponseEntity<Command> updateCommand(@PathVariable int id, @Valid @RequestBody CommandDto commandDto) {
        return ResponseEntity.ok(commandService.updateCommand(id, commandDto));
    }

    @PutMapping("/{id}/status")
    public ResponseEntity<Command> updateStatus(@PathVariable int id, @RequestBody Map<String, String> body) {
        return ResponseEntity.ok(commandService.updateCommandStatus(id, body.get("status")));
    }
}
