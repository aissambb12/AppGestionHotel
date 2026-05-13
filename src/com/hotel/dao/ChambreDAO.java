package com.hotel.dao;

import com.hotel.model.Chambre;
import com.hotel.model.StatutChambre;
import java.util.List;

public interface ChambreDAO {

    void ajouter(Chambre chambre);
    void modifier(Chambre chambre);
    void supprimer(int idChambre);
    Chambre rechercherParId(int idChambre);
    List<Chambre> listerTous();

    void changerStatut(int idChambre, StatutChambre statut);
    List<Chambre> listerDisponibles();
}
