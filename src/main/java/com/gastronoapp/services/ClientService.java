package com.gastronoapp.services;

import com.gastronoapp.entity.Client;
import com.gastronoapp.repositories.ClientRepository;
import java.util.List;
import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ClientService {

    @Autowired
    public ClientRepository clientRepository;

    public Client saveClient(Client client) {
        return clientRepository.save(client);

    }
    
    public List <Client> getClientByName(String name){
    return clientRepository.findByName(name);
    }

    public List<Client> getAllClients() {
        return clientRepository.findAll();

    }
    public void deleteClient(int id){
    clientRepository.deleteById(id);
    
    }
}
