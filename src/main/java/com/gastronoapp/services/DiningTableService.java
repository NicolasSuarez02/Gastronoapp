package com.gastronoapp.services;

import com.gastronoapp.entity.DiningTable;
import com.gastronoapp.repositories.DiningTableRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class DiningTableService {

    @Autowired
    private DiningTableRepository diningTableRepository;

    public List<DiningTable> getAllTables() {
        return diningTableRepository.findAll();
    }

    public DiningTable saveTable(DiningTable table) {
        return diningTableRepository.save(table);
    }

    public void deleteTable(int id) {
        diningTableRepository.deleteById(id);
    }

    public Optional<DiningTable> findByTableNumber(int tableNumber) {
        return diningTableRepository.findByTableNumber(tableNumber);
    }

    public DiningTable updateStatus(int id, String status) {
        DiningTable table = diningTableRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Table not found: " + id));
        table.setStatus(status);
        return diningTableRepository.save(table);
    }

    public DiningTable updateTable(int id, DiningTable updates) {
        DiningTable table = diningTableRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Table not found: " + id));
        if (updates.getTableNumber() > 0) table.setTableNumber(updates.getTableNumber());
        if (updates.getCapacity() > 0) table.setCapacity(updates.getCapacity());
        return diningTableRepository.save(table);
    }
}
