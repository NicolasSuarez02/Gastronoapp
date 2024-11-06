package com.gastronoapp.services;

import com.gastronoapp.dto.CommandDto;
import com.gastronoapp.entity.Command;
import com.gastronoapp.entity.MenuItem;
import com.gastronoapp.repositories.CommandRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class CommandService {

    private static final Logger logger = LoggerFactory.getLogger(CommandService.class);

    @Autowired
    private CommandRepository commandRepository;

    @Autowired
    private MenuItemService menuItemService;

    public Command saveCommand(CommandDto commandDto) {
        logger.info("Received CommandDto: {}", commandDto);
        System.out.println("Entro al save command");
        List<MenuItem> menuItems = menuItemService.getItemById(commandDto.getMenuItemIds());
        logger.info("Retrieved MenuItems: {}", menuItems);

        Command command = new Command();
        command.setDate(commandDto.getDate());
        command.setMenuItems(menuItems);
        command.setObservations(commandDto.getObservations());
        command.setNumber(commandDto.getNumber());

        try {
            Command savedCommand = commandRepository.save(command);
            logger.info("Saved Command: {}", savedCommand);
            return savedCommand;
        } catch (Exception e) {
            logger.error("Error saving command: ", e);
            throw e; // O maneja la excepción de otra forma según tus necesidades
        }
    }

    public List<Command> getAllCommands() {
        return commandRepository.findAll();
    }
    public Optional<Command> getCommandById(int id) {
        return commandRepository.findById(id);
    }
}
