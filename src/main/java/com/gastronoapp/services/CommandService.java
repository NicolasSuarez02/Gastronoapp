package com.gastronoapp.services;

import com.gastronoapp.dto.CommandDto;
import com.gastronoapp.entity.Command;
import com.gastronoapp.entity.MenuItem;
import com.gastronoapp.repositories.CommandRepository;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class CommandService {

    @Autowired
    private CommandRepository commandRepository;
    @Autowired
    private MenuItemService menuItemService;

    public Command saveCommand(CommandDto commandDto) {

        List<MenuItem> menuItems = menuItemService.getItemById(commandDto.getMenuItemIds());

        Command command = new Command();
        command.setDate(commandDto.getDate());
        command.setMenuItems(menuItems);
        command.setObservations(commandDto.getObservations());
        command.setNumber(commandDto.getNumber());
        return commandRepository.save(command);
    }

    public List<Command> getAllCommands() {
        return commandRepository.findAll();

    }

}
