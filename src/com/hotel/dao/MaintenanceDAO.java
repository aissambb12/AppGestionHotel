package com.hotel.dao;

import com.hotel.model.Maintenance;
import java.util.List;

public interface MaintenanceDAO {

    void ajouter(Maintenance maintenance);
    void modifier(Maintenance maintenance);
    void supprimer(int idMaintenance);
    Maintenance rechercherParId(int idMaintenance);
    List<Maintenance> listerTous();

    List<Maintenance> listerParChambre(int idChambre);
    void terminerMaintenance(int idMaintenance);
}