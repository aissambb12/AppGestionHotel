package com.hotel.service;

import com.hotel.dao.ChambreDAO;
import com.hotel.dao.impl.ChambreDAOImpl;
import com.hotel.model.Chambre;
import com.hotel.model.enumeration.StatutChambre;
import com.hotel.util.ValidationUtil;

import java.time.LocalDate;
import java.util.List;

public class ChambreService {

    private ChambreDAO chambreDAO;

    public ChambreService() {
        this.chambreDAO = new ChambreDAOImpl();
    }

    public boolean ajouterChambre(Chambre chambre) {
        if (ValidationUtil.estVide(chambre.getNumero())) {
            throw new IllegalArgumentException("Le numéro de la chambre est obligatoire.");
        }
        if (!ValidationUtil.estPrixValide(chambre.getPrixUnitaire())) {
            throw new IllegalArgumentException("Le prix de la chambre ne peut pas être négatif.");
        }

        // Par défaut, une nouvelle chambre est disponible
        chambre.setStatutChambre(StatutChambre.DISPONIBLE);
        return chambreDAO.ajouter(chambre);
    }

    public boolean modifierChambre(Chambre chambre) {
        if (!ValidationUtil.estPrixValide(chambre.getPrixUnitaire())) {
            throw new IllegalArgumentException("Le prix de la chambre ne peut pas être négatif.");
        }
        return chambreDAO.modifier(chambre);
    }

    /**
     * Moteur de recherche pour le réceptionniste : trouve les chambres libres.
     */
    public List<Chambre> rechercherChambresDisponibles(LocalDate arrivee, LocalDate depart, String categorie) {
        // 1. Validation stricte des dates grâce à notre utilitaire
        if (!ValidationUtil.sontDatesReservationValides(arrivee, depart)) {
            throw new IllegalArgumentException("La date de départ doit être ultérieure à la date d'arrivée.");
        }
        if (!ValidationUtil.estDateDansLeFuturOuAujourdhui(arrivee)) {
            throw new IllegalArgumentException("Impossible de rechercher une disponibilité dans le passé.");
        }

        // 2. Appel au DAO si les dates sont logiques
        return chambreDAO.listerChambresDisponibles(arrivee, depart, categorie);
    }

    /**
     * Récupère tous les détails d'une chambre via son ID.
     */
    public Chambre obtenirDetailsChambre(int idChambre) {
        if (idChambre <= 0) {
            throw new IllegalArgumentException("ID de chambre invalide.");
        }
        return chambreDAO.trouverParId(idChambre);
    }

    /**
     * Modifie le statut d'une chambre (ex: DISPONIBLE, EN_MAINTENANCE)
     */
    public boolean modifierStatutChambre(int idChambre, String nouveauStatut) {
        if (idChambre <= 0) {
            throw new IllegalArgumentException("ID de chambre invalide.");
        }
        if (nouveauStatut == null || nouveauStatut.trim().isEmpty()) {
            throw new IllegalArgumentException("Le statut ne peut pas être vide.");
        }
        if (!"DISPONIBLE".equals(nouveauStatut) && !"MAINTENANCE".equals(nouveauStatut)) {
            throw new IllegalArgumentException("Statut chambre invalide : seuls DISPONIBLE et MAINTENANCE sont permis.");
        }
        return chambreDAO.modifierStatut(idChambre, nouveauStatut);
    }

    public List<Chambre> listerToutesLesChambres() {
        return chambreDAO.listerToutes();
    }
}