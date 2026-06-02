package com.hotel.dao;

import com.hotel.model.Chambre;
import java.time.LocalDate;
import java.util.List;

public interface ChambreDAO {
    boolean ajouter(Chambre chambre);
    boolean modifier(Chambre chambre);
    boolean modifierStatut(int idChambre, String statut);
    Chambre trouverParId(int idChambre);
    Chambre trouverParNumero(String numero);
    List<Chambre> listerToutes();
    List<Chambre> listerParStatut(String statut);


    List<Chambre> listerChambresDisponibles(LocalDate arrivee, LocalDate depart, String categorie);
}