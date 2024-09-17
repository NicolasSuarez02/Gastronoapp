package com.gastronoapp.repositories;

import com.gastronoapp.entity.Client;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface ClientRepository extends JpaRepository <Client, Integer>{
    @Query("SELECT c FROM Client c WHERE c.name LIKE %:name%")
    List<Client> findByName(@Param("name") String name);

}
