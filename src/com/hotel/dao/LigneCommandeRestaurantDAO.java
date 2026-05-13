package com.hotel.dao;

import com.hotel.model.LigneCommandeRestaurant;
import java.util.List;

public interface LigneCommandeRestaurantDAO {

    void ajouter(LigneCommandeRestaurant ligne);
    void modifier(LigneCommandeRestaurant ligne);
    void supprimer(int idLigne);
    LigneCommandeRestaurant rechercherParId(int idLigne);
    List<LigneCommandeRestaurant> listerTous();

    List<LigneCommandeRestaurant> listerParCommande(int idCommande);
    double calculerTotalCommande(int idCommande);
    double calculerTotalRestaurantParReservation(int idReservation);
}