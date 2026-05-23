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

    // Utilisation du nouveau nom de l'interface suite à votre refactoring
    private FactureDAO factureDAO;
    private ReservationServicesDAO reservationServicesDAO;
    private ReservationDAO reservationDAO;
    private ReservationChambreDAO reservationChambreDAO;
    private ChambreDAO chambreDAO;
    private PaiementDAO paiementDAO;

    public FacturationService() {
        this.factureDAO = new FactureDAOImpl();
        this.reservationServicesDAO = new ReservationServicesDAOImpl(); // Mis à jour avec le "S"
        this.reservationDAO = new ReservationDAOImpl();
        this.reservationChambreDAO = new ReservationChambreDAOImpl();
        this.chambreDAO = new ChambreDAOImpl();
        this.paiementDAO = new PaiementDAOImpl();
    }

    /**
     * Le client commande un extra (ex: Un repas au restaurant, Spa, Parking)
     * Utilisation du modèle renommé "ReservationServices"
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
            // Le DAO mettra à jour le montant_total en base de données si besoin
        }

        // 3. Libération de toutes les chambres associées à cette réservation
        List<ReservationChambre> chambresOccupees = reservationChambreDAO.listerParReservation(idReservation);
        for (ReservationChambre rc : chambresOccupees) {
            // Chaque chambre redevient DISPONIBLE pour de futurs clients !
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
     * Récupère la liste de tous les services supplémentaires (Spa, Restaurant) consommés par une réservation.
     */
    public List<ReservationServices> obtenirDetailsConsommations(int idReservation) {
        if (idReservation <= 0) {
            throw new IllegalArgumentException("ID de réservation invalide.");
        }
        return reservationServicesDAO.listerConsommationsParReservation(idReservation);
    }

    /**
     * METHODE CORRIGÉE : Enregistre un paiement en respectant l'Enum ModePaiement.
     * @param idFacture L'id de la facture concernée
     * @param montantPaye La somme d'argent donnée par le client
     * @param mode Le mode de paiement (Type fort : ModePaiement au lieu de String)
     */
    public boolean payerFacture(int idFacture, double montantPaye, ModePaiement mode) {
        // 1. Validation de sécurité
        if (!ValidationUtil.estPrixValide(montantPaye) || montantPaye == 0) {
            throw new IllegalArgumentException("Le montant du paiement doit être supérieur à 0.");
        }
        if (mode == null) {
            throw new IllegalArgumentException("Le mode de paiement est obligatoire.");
        }

        // 2. Vérifier si la facture existe
        Facture f = factureDAO.trouverParId(idFacture);
        if (f == null) {
            throw new IllegalArgumentException("Aucune facture trouvée avec l'ID : " + idFacture);
        }

        // 3. Création de l'objet Paiement en convertissant l'Enum en String pour le DAO (.name())
        // ID à 0 car auto-incrémenté en BDD, date à null car gérée par le TIMESTAMP de MySQL
        Paiement p = new Paiement(0, idFacture, montantPaye, null, mode);

        boolean succesPaiement = paiementDAO.enregistrerPaiement(p);

        // 4. Si l'enregistrement a réussi, on vérifie si la facture est totalement soldée
        if (succesPaiement) {
            double totalDejaPaye = paiementDAO.obtenirTotalPayePourFacture(idFacture);

            // Si la somme de tous les paiements atteint ou dépasse le montant total de la facture
            if (totalDejaPaye >= f.getMontantTotal()) {
                // On passe le statut de la facture à PAYEE
                factureDAO.modifierStatut(idFacture, StatutFacture.PAYEE.name());
            }
        }

        return succesPaiement;
    }
}