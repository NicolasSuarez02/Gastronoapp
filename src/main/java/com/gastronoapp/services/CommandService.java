package com.gastronoapp.services;

import com.gastronoapp.dto.CommandDto;
import com.gastronoapp.entity.Command;
import com.gastronoapp.entity.MenuItem;
import com.gastronoapp.repositories.CommandRepository;
import com.gastronoapp.repositories.DiningTableRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.function.Function;
import java.util.stream.Collectors;

@Service
public class CommandService {

    private static final Logger logger = LoggerFactory.getLogger(CommandService.class);
    private static final List<String> ACTIVE_STATUSES = List.of("RECIBIDO", "EN_CURSO", "LISTO");

    @Autowired
    private CommandRepository commandRepository;

    @Autowired
    private MenuItemService menuItemService;

    @Autowired
    private DiningTableRepository diningTableRepository;

    public Command saveCommand(CommandDto commandDto) {
        logger.info("Saving command for table {}", commandDto.getNumber());

        List<MenuItem> menuItems = menuItemService.getItemById(commandDto.getMenuItemIds());

        Command command = new Command();
        command.setDate(commandDto.getDate());
        command.setMenuItems(menuItems);
        command.setObservations(commandDto.getObservations());
        command.setNumber(commandDto.getNumber());
        command.setStatus("RECIBIDO");

        Command saved = commandRepository.save(command);

        // Descontar stock
        Map<Integer, Long> idCounts = commandDto.getMenuItemIds().stream()
                .collect(Collectors.groupingBy(Function.identity(), Collectors.counting()));

        idCounts.forEach((itemId, qty) ->
            menuItemService.getMenuItemById(itemId).ifPresent(item -> {
                int newQty = Math.max(0, item.getQuantity() - qty.intValue());
                item.setQuantity(newQty);
                menuItemService.saveMenuItem(item);
            })
        );

        // Mark dining table as OCCUPIED
        diningTableRepository.findByTableNumber(commandDto.getNumber()).ifPresent(table -> {
            table.setStatus("OCCUPIED");
            diningTableRepository.save(table);
        });

        logger.info("Command saved: id={}", saved.getId());
        return saved;
    }

    public List<Command> getAllCommands() {
        return commandRepository.findAll();
    }

    public Optional<Command> getCommandById(int id) {
        return commandRepository.findById(id);
    }

    public Command updateCommand(int id, CommandDto dto) {
        Command command = commandRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Command not found: " + id));

        // Counts of current items
        Map<Integer, Long> currentCounts = command.getMenuItems().stream()
                .collect(Collectors.groupingBy(item -> item.getId(), Collectors.counting()));

        // Counts of new items
        Map<Integer, Long> newCounts = dto.getMenuItemIds().stream()
                .collect(Collectors.groupingBy(Function.identity(), Collectors.counting()));

        // Restore stock for removed or reduced items
        currentCounts.forEach((itemId, currentQty) -> {
            long newQty = newCounts.getOrDefault(itemId, 0L);
            long diff = currentQty - newQty;
            if (diff > 0) {
                menuItemService.getMenuItemById(itemId).ifPresent(item -> {
                    item.setQuantity(item.getQuantity() + (int) diff);
                    menuItemService.saveMenuItem(item);
                });
            }
        });

        // Deduct stock for added or increased items
        newCounts.forEach((itemId, newQty) -> {
            long currentQty = currentCounts.getOrDefault(itemId, 0L);
            long diff = newQty - currentQty;
            if (diff > 0) {
                menuItemService.getMenuItemById(itemId).ifPresent(item -> {
                    item.setQuantity(Math.max(0, item.getQuantity() - (int) diff));
                    menuItemService.saveMenuItem(item);
                });
            }
        });

        command.setMenuItems(menuItemService.getItemById(dto.getMenuItemIds()));
        command.setObservations(dto.getObservations());
        Command saved = commandRepository.save(command);
        logger.info("Command updated: id={}", saved.getId());
        return saved;
    }

    public Command updateCommandStatus(int id, String status) {
        Command command = commandRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Command not found: " + id));
        command.setStatus(status);
        Command saved = commandRepository.save(command);

        // If delivered, free the table when no more active commands exist for it
        if ("ENTREGADO".equals(status)) {
            long activeCount = commandRepository.countByTableNumberAndStatusIn(command.getNumber(), ACTIVE_STATUSES);
            if (activeCount == 0) {
                diningTableRepository.findByTableNumber(command.getNumber()).ifPresent(table -> {
                    table.setStatus("FREE");
                    diningTableRepository.save(table);
                });
            }
        }

        return saved;
    }
}
