package com.gastronoapp.repositories;

import com.gastronoapp.dto.CommandDto;
import com.gastronoapp.entity.Command;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface CommandRepository extends JpaRepository <Command, Integer> {
}
