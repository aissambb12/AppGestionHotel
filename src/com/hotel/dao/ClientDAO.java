package com.hotel.dao;

import com.hotel.model.Client;
import java.util.List;

public interface ClientDAO {
    boolean ajouter(Client client);
    boolean modifier(Client client);
    boolean supprimer(int idClient);
    Client trouverParId(int idClient);
    Client trouverParCin(String cin); // Exigence éviter les doublons
    List<Client> listerTous();
    List<Client> rechercherParMotCle(String motCle); // Recherche dynamique (par nom, prénom ou CIN)
}