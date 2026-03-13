package com.gastronoapp.repositories;

import com.gastronoapp.entity.Command;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CommandRepository extends JpaRepository<Command, Integer> {
    long countByTableNumberAndStatusIn(int tableNumber, List<String> statuses);
}
