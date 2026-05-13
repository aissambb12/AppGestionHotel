package com.hotel.dao;

import com.hotel.model.Paiement;
import java.util.List;

public interface PaiementDAO {

    void ajouter(Paiement paiement);
    void modifier(Paiement paiement);
    void supprimer(int idPaiement);
    Paiement rechercherParId(int idPaiement);
    List<Paiement> listerTous();

    List<Paiement> listerParFacture(int idFacture);
    double calculerTotalPaye(int idFacture);
}
