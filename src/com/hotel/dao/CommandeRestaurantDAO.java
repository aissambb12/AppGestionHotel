package com.hotel.dao;

import com.hotel.model.CommandeRestaurant;
import com.hotel.model.enumeration.StatutCommandeRestaurant;
import java.util.List;

public interface CommandeRestaurantDAO {

    void ajouter(CommandeRestaurant commande);
    void modifier(CommandeRestaurant commande);
    void supprimer(int idCommande);
    CommandeRestaurant rechercherParId(int idCommande);
    List<CommandeRestaurant> listerTous();

    List<CommandeRestaurant> listerParReservation(int idReservation);
    void changerStatut(int idCommande, StatutCommandeRestaurant statut);
}
