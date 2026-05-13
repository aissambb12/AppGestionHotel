package com.hotel.dao;

import com.hotel.model.Client;
import java.util.List;

public interface ClientDAO {

    void ajouter(Client client);
    void modifier(Client client);
    void supprimer(int idClient);
    Client rechercherParId(int idClient);
    List<Client> listerTous();
}
