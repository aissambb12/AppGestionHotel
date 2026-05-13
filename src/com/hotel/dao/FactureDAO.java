package com.hotel.dao;

import com.hotel.model.Facture;
import com.hotel.model.StatutFacture;
import java.util.List;

public interface FactureDAO {

    void ajouter(Facture facture);
    void modifier(Facture facture);
    void supprimer(int idFacture);
    Facture rechercherParId(int idFacture);
    List<Facture> listerTous();

    Facture rechercherParReservation(int idReservation);
    void changerStatut(int idFacture, StatutFacture statut);
}