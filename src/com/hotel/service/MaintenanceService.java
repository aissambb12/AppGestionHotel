package com.hotel.service;

import com.hotel.dao.ChambreDAO;
import com.hotel.dao.MaintenanceDAO;
import com.hotel.dao.impl.ChambreDAOImpl;
import com.hotel.dao.impl.MaintenanceDAOImpl;
import com.hotel.model.Maintenance;
import com.hotel.model.enumeration.StatutChambre;
import com.hotel.model.enumeration.StatutMaintenance;
import com.hotel.util.ValidationUtil;

import java.time.LocalDate;
import java.util.List;

public class MaintenanceService {

    private MaintenanceDAO maintenanceDAO;
    private ChambreDAO chambreDAO;

    public MaintenanceService() {
        this.maintenanceDAO = new MaintenanceDAOImpl();
        this.chambreDAO = new ChambreDAOImpl();
    }

    /**
     * Conserve l'ancienne API (panne déjà construite en Maintenance).
     */
    public boolean declarerPanne(Maintenance maintenance) {
        if (!ValidationUtil.sontDatesReservationValides(maintenance.getDateDebut(), maintenance.getDateFin())) {
            throw new IllegalArgumentException("Les dates de maintenance sont incohérentes.");
        }
        boolean succes = maintenanceDAO.planifier(maintenance);
        if (succes) {
            chambreDAO.modifierStatut(maintenance.getIdChambre(), StatutChambre.MAINTENANCE.name());
        }
        return succes;
    }

    /**
     * Helper utilisé par GestionChambresFrame quand l'admin déclare une panne.
     * Crée la ligne maintenance ET passe la chambre en MAINTENANCE.
     */
    public boolean declarerPanneAvecChambre(int idChambre, String description,
                                            LocalDate dateDebut, LocalDate dateFin) {
        if (idChambre <= 0) {
            throw new IllegalArgumentException("Chambre invalide.");
        }
        if (description == null || description.trim().isEmpty()) {
            throw new IllegalArgumentException("Description obligatoire.");
        }
        if (dateDebut == null) dateDebut = LocalDate.now();
        if (dateFin == null)   dateFin   = dateDebut.plusDays(7);
        if (dateFin.isBefore(dateDebut)) {
            throw new IllegalArgumentException("Date de fin antérieure à la date de début.");
        }

        Maintenance m = new Maintenance();
        m.setIdChambre(idChambre);
        m.setDescription(description.trim());
        m.setDateDebut(dateDebut);
        m.setDateFin(dateFin);
        m.setStatutMaintenance(StatutMaintenance.EN_COURS);

        boolean succes = maintenanceDAO.planifier(m);
        if (succes) {
            chambreDAO.modifierStatut(idChambre, StatutChambre.MAINTENANCE.name());
        }
        return succes;
    }

    /**
     * Termine la maintenance active sur une chambre ET la libère.
     * Utilisé soit depuis InterventionsFrame (côté maintenance),
     * soit depuis GestionChambresFrame quand l'admin veut remettre directement la chambre disponible.
     */
    public boolean terminerMaintenance(int idMaintenance) {
        Maintenance m = maintenanceDAO.trouverParId(idMaintenance);
        if (m == null) return false;

        boolean succes = maintenanceDAO.terminerMaintenance(idMaintenance, StatutMaintenance.TERMINEE.name());
        if (succes) {
            chambreDAO.modifierStatut(m.getIdChambre(), StatutChambre.DISPONIBLE.name());
        }
        return succes;
    }

    /**
     * Termine TOUTES les maintenances EN_COURS d'une chambre et la passe en DISPONIBLE.
     * Utilisé quand l'admin clique "Marquer disponible" depuis GestionChambresFrame.
     */
    public boolean libererChambre(int idChambre) {
        List<Maintenance> liste = maintenanceDAO.listerParChambre(idChambre);
        boolean okGlobal = true;
        for (Maintenance m : liste) {
            if (m.getStatutMaintenance() == StatutMaintenance.EN_COURS) {
                boolean ok = maintenanceDAO.terminerMaintenance(m.getIdMaintenance(), StatutMaintenance.TERMINEE.name());
                if (!ok) okGlobal = false;
            }
        }
        // Dans tous les cas, on remet la chambre disponible
        chambreDAO.modifierStatut(idChambre, StatutChambre.DISPONIBLE.name());
        return okGlobal;
    }

    public List<Maintenance> listerMaintenancesEnCours() {
        return maintenanceDAO.listerActives();
    }
}
