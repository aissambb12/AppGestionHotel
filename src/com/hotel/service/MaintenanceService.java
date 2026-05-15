package com.hotel.service;

import com.hotel.dao.ChambreDAO;
import com.hotel.dao.MaintenanceDAO;
import com.hotel.dao.impl.ChambreDAOImpl;
import com.hotel.dao.impl.MaintenanceDAOImpl;
import com.hotel.model.*;
import com.hotel.model.enumeration.StatutChambre;
import com.hotel.model.enumeration.StatutMaintenance;

import java.util.Date;
import java.util.List;

public class MaintenanceService {

    private ChambreDAO chambreDAO = new ChambreDAOImpl();
    private MaintenanceDAO maintenanceDAO = new MaintenanceDAOImpl();

    public void mettreEnMaintenance(int idChambre, String description) {

        Chambre chambre = chambreDAO.rechercherParId(idChambre);

        if (chambre == null) {
            System.out.println("Chambre introuvable.");
            return;
        }

        if (chambre.getStatut() == StatutChambre.OCCUPEE) {
            System.out.println("Impossible : chambre occupée.");
            return;
        }

        if (chambre.getStatut() == StatutChambre.RESERVEE) {
            System.out.println("Impossible : chambre réservée.");
            return;
        }

        Maintenance maintenance = new Maintenance();
        maintenance.setChambre(chambre);
        maintenance.setDateDebut(new Date());
        maintenance.setDateFin(null);
        maintenance.setDescription(description);
        maintenance.setStatut(StatutMaintenance.EN_COURS);

        maintenanceDAO.ajouter(maintenance);
        chambreDAO.changerStatut(idChambre, StatutChambre.MAINTENANCE);

        System.out.println("Chambre mise en maintenance.");
    }

    public void terminerMaintenance(int idMaintenance) {

        Maintenance maintenance = maintenanceDAO.rechercherParId(idMaintenance);

        if (maintenance == null) {
            System.out.println("Maintenance introuvable.");
            return;
        }

        maintenanceDAO.terminerMaintenance(idMaintenance);

        chambreDAO.changerStatut(
                maintenance.getChambre().getIdChambre(),
                StatutChambre.DISPONIBLE
        );

        System.out.println("Maintenance terminée.");
    }

    public void supprimerMaintenance(int idMaintenance) {
        maintenanceDAO.supprimer(idMaintenance);
    }

    public Maintenance rechercherMaintenance(int idMaintenance) {
        return maintenanceDAO.rechercherParId(idMaintenance);
    }

    public List<Maintenance> listerMaintenances() {
        return maintenanceDAO.listerTous();
    }

    public List<Maintenance> listerParChambre(int idChambre) {
        return maintenanceDAO.listerParChambre(idChambre);
    }
}