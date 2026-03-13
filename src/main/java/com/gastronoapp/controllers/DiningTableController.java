package com.gastronoapp.controllers;

import com.gastronoapp.entity.DiningTable;
import com.gastronoapp.services.DiningTableService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/tables")
@CrossOrigin(origins = "*")
public class DiningTableController {

    @Autowired
    private DiningTableService diningTableService;

    @GetMapping("/all")
    public ResponseEntity<List<DiningTable>> getAllTables() {
        return ResponseEntity.ok(diningTableService.getAllTables());
    }

    @PostMapping("/save")
    public ResponseEntity<DiningTable> saveTable(@RequestBody DiningTable table) {
        return ResponseEntity.ok(diningTableService.saveTable(table));
    }

    @PutMapping("/{id}/status")
    public ResponseEntity<DiningTable> updateStatus(@PathVariable int id, @RequestBody Map<String, String> body) {
        return ResponseEntity.ok(diningTableService.updateStatus(id, body.get("status")));
    }

    @PutMapping("/{id}")
    public ResponseEntity<DiningTable> updateTable(@PathVariable int id, @RequestBody DiningTable updates) {
        return ResponseEntity.ok(diningTableService.updateTable(id, updates));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteTable(@PathVariable int id) {
        diningTableService.deleteTable(id);
        return ResponseEntity.noContent().build();
    }
}
