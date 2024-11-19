package com.gastronoapp.services;

import com.gastronoapp.dto.CommandMenuItemDTO;
import com.gastronoapp.entity.CommandMenuItem;
import com.gastronoapp.entity.CommandMenuItemId;
import com.gastronoapp.repositories.CommandMenuItemRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class CommandMenuItemService {

    @Autowired
    private CommandMenuItemRepository commandMenuItemRepository;

    public Optional<CommandMenuItemDTO> getCommandMenuItemDTO(int commandId, int menuItemId) {
        CommandMenuItemId id = new CommandMenuItemId(commandId, menuItemId);
        return commandMenuItemRepository.findById(id)
                .map(commandMenuItem -> new CommandMenuItemDTO(
                        // Mapear a DTO aquí
                ));
    }

    public List<CommandMenuItemDTO> getAllCommandMenuItems() {
        return commandMenuItemRepository.findAll().stream()
                .map(commandMenuItem -> new CommandMenuItemDTO(
                        // Mapear a DTO aquí
                ))
                .collect(Collectors.toList());
    }
}
