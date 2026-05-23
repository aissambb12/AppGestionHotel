package com.hotel.service;

import com.hotel.dao.ChambreDAOImpl;
import com.hotel.dao.ChambreDAO;
import com.hotel.dao.MaintenanceDAO;
import com.hotel.dao.MaintenanceDAOImpl;
import com.hotel.model.Maintenance;
import com.hotel.model.enumeration.StatutChambre;
import com.hotel.model.enumeration.StatutMaintenance;
import com.hotel.util.ValidationUtil;

import java.time.LocalDate;
import java.util.List;

public class MaintenanceService {

    private MaintenanceDAO maintenanceDAO;
    private ChambreDAO chambreDAO; // Besoin d'accès aux chambres pour changer leur statut

    public MaintenanceService() {
        this.maintenanceDAO = new MaintenanceDAOImpl();
        this.chambreDAO = new ChambreDAOImpl();
    }

    public boolean declarerPanne(Maintenance maintenance) {
        if (!ValidationUtil.sontDatesReservationValides(maintenance.getDateDebut(), maintenance.getDateFin())) {
            throw new IllegalArgumentException("Les dates de maintenance sont incohérentes.");
        }

        // 1. Enregistrer la maintenance
        boolean succes = maintenanceDAO.planifier(maintenance);

        // 2. Bloquer physiquement la chambre
        if (succes) {
            chambreDAO.modifierStatut(maintenance.getIdChambre(), StatutChambre.MAINTENANCE.name());
        }
        return succes;
    }

    /**
     * C'est ici que votre remarque prend tout son sens !
     * On termine la maintenance ET on libère la chambre.
     */
    public boolean terminerMaintenance(int idMaintenance) {
        Maintenance m = maintenanceDAO.trouverParId(idMaintenance);
        if (m == null) return false;

        // 1. Clôturer le ticket de maintenance
        boolean succes = maintenanceDAO.terminerMaintenance(idMaintenance, StatutMaintenance.TERMINEE.name());

        // 2. Rendre la chambre de nouveau disponible pour les clients !
        if (succes) {
            chambreDAO.modifierStatut(m.getIdChambre(), StatutChambre.DISPONIBLE.name());
        }
        return succes;
    }

    public List<Maintenance> listerMaintenancesEnCours() {
        return maintenanceDAO.listerActives();
    }
}