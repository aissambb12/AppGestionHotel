package com.hotel.dao;

import com.hotel.model.Maintenance;
import java.util.List;

public interface MaintenanceDAO {
    boolean planifier(Maintenance maintenance);
    boolean terminerMaintenance(int idMaintenance, String statut); // Passe le statut à 'TERMINEE'
    Maintenance trouverParId(int idMaintenance);
    List<Maintenance> listerToutes();
    List<Maintenance> listerActives(); // Uniquement celles 'EN_COURS' qui bloquent les chambres
    List<Maintenance> listerParChambre(int idChambre);
}