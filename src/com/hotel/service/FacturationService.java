package com.hotel.service;

import com.hotel.dao.*;
import com.hotel.dao.ChambreDAOImpl;
import com.hotel.dao.FactureDAOImpl;
import com.hotel.dao.PaiementDAOImpl;
import com.hotel.dao.ReservationChambreDAOImpl;
import com.hotel.dao.ReservationDAOImpl;
import com.hotel.dao.ReservationServicesDAOImpl;
import com.hotel.model.*;
import com.hotel.model.enumeration.ModePaiement;
import com.hotel.model.enumeration.StatutChambre;
import com.hotel.model.enumeration.StatutFacture;
import com.hotel.model.enumeration.StatutReservation;
import com.hotel.util.ValidationUtil;

import java.time.LocalDate;
import java.util.List;

public class FacturationService {

    private FactureDAO factureDAO;
    private ReservationServicesDAO reservationServicesDAO;
    private ReservationDAO reservationDAO;
    private ReservationChambreDAO reservationChambreDAO;
    private ChambreDAO chambreDAO;
    private PaiementDAO paiementDAO;

    public FacturationService() {
        this.factureDAO = new FactureDAOImpl();
        this.reservationServicesDAO = new ReservationServicesDAOImpl();
        this.reservationDAO = new ReservationDAOImpl();
        this.reservationChambreDAO = new ReservationChambreDAOImpl();
        this.chambreDAO = new ChambreDAOImpl();
        this.paiementDAO = new PaiementDAOImpl();
    }

    /**
     * Le client commande un extra (ex: Un repas au restaurant, Spa, Parking)
     */
    public boolean ajouterConsommation(ReservationServices extra) {
        if (!ValidationUtil.estQuantiteValide(extra.getQuantite())) {
            throw new IllegalArgumentException("La quantité commandée doit être supérieure à zéro.");
        }
        return reservationServicesDAO.enregistrerConsommation(extra);
    }

    /**
     * LE CHECK-OUT : Fin du séjour, calcul final et libération automatique des chambres.
     */
    public boolean realiserCheckOut(int idReservation) {
        // 1. Calculer le montant absolu final via le DAO (Chambres + Extras consommés)
        double totalFinal = factureDAO.calculerMontantTotal(idReservation);

        // 2. Récupérer la facture pour mettre à jour son montant si nécessaire
        Facture facture = factureDAO.trouverParReservation(idReservation);
        if (facture != null) {
            facture.setMontantTotal(totalFinal);
            factureDAO.modifierStatut(facture.getIdFacture(), StatutFacture.EN_ATTENTE.name());
        }

        // 3. Libération de toutes les chambres associées à cette réservation
        List<ReservationChambre> chambresOccupees = reservationChambreDAO.listerParReservation(idReservation);
        for (ReservationChambre rc : chambresOccupees) {
            chambreDAO.modifierStatut(rc.getIdChambre(), StatutChambre.DISPONIBLE.name());
        }

        // 4. Clôturer définitivement la réservation
        return reservationDAO.modifierStatut(idReservation, StatutReservation.TERMINEE.name());
    }

    /**
     * Calcule le CA sur une période donnée (factures PAYEES uniquement).
     */
    public double obtenirChiffreAffaires(LocalDate dateDebut, LocalDate dateFin) {
        if (dateDebut == null || dateFin == null) {
            throw new IllegalArgumentException("Les dates sont obligatoires.");
        }
        if (dateDebut.isAfter(dateFin)) {
            throw new IllegalArgumentException("La date de début doit précéder la date de fin.");
        }
        return factureDAO.calculerChiffreAffaires(dateDebut, dateFin);
    }

    /**
     * Récupère la liste de tous les services supplémentaires consommés par une réservation.
     */
    public List<ReservationServices> obtenirDetailsConsommations(int idReservation) {
        if (idReservation <= 0) {
            throw new IllegalArgumentException("ID de réservation invalide.");
        }
        return reservationServicesDAO.listerParReservation(idReservation);
    }

    /**
     * Enregistre un paiement pour une facture
     */
    public boolean enregistrerPaiement(Paiement paiement) {
        if (paiement.getMontantPaye() <= 0) {
            throw new IllegalArgumentException("Le montant du paiement doit être positif.");
        }
        return paiementDAO.enregistrerPaiement(paiement);
    }

    /**
     * Obtient le total payé pour une facture
     */
    public double obtenirTotalPayePourFacture(int idFacture) {
        return paiementDAO.obtenirTotalPayePourFacture(idFacture);
    }

    /**
     * Récupère une facture par son ID
     */
    public Facture obtenirFacture(int idFacture) {
        return factureDAO.trouverParId(idFacture);
    }

    /**
     * Récupère la facture d'une réservation
     */
    public Facture obtenirFactureReservation(int idReservation) {
        return factureDAO.trouverParReservation(idReservation);
    }
}