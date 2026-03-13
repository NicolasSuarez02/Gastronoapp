package com.gastronoapp.repositories;

import com.gastronoapp.entity.DiningTable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface DiningTableRepository extends JpaRepository<DiningTable, Integer> {
    Optional<DiningTable> findByTableNumber(int tableNumber);
}
