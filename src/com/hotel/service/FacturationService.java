package com.hotel.service;

import com.hotel.dao.*;
import com.hotel.dao.impl.ChambreDAOImpl;
import com.hotel.dao.impl.FactureDAOImpl;
import com.hotel.dao.impl.PaiementDAOImpl;
import com.hotel.dao.impl.ReservationChambreDAOImpl;
import com.hotel.dao.impl.ReservationDAOImpl;
import com.hotel.dao.impl.ReservationServicesDAOImpl;
import com.hotel.model.*;
import com.hotel.model.enumeration.ModePaiement;
import com.hotel.model.enumeration.StatutChambre;
import com.hotel.model.enumeration.StatutFacture;
import com.hotel.model.enumeration.StatutReservation;
import com.hotel.util.ValidationUtil;

import java.sql.PreparedStatement;
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
        Reservation resa = reservationDAO.trouverParId(idReservation);
        if (resa == null) {
            throw new IllegalArgumentException("Réservation introuvable.");
        }
        if (resa.getStatut() == StatutReservation.TERMINEE) {
            throw new IllegalStateException("Cette réservation a déjà été check-outée.");
        }
        if (resa.getStatut() == StatutReservation.ANNULEE) {
            throw new IllegalStateException("Cette réservation a été ANNULEE. Impossible d'effectuer un check-out.");
        }

        // 1. Calculer le montant final
        double totalFinal = factureDAO.calculerMontantTotal(idReservation);

        // 2. Mettre à jour le montant de la facture
        Facture facture = factureDAO.trouverParReservation(idReservation);
        if (facture != null) {
            factureDAO.entrerMontant(facture, totalFinal);
        }

        // 3. Clôture définitive de la réservation
        // (les chambres n'ont plus de statut OCCUPEE — l'occupation est portée par reservation_chambres)
        return reservationDAO.modifierStatut(idReservation, StatutReservation.TERMINEE.name());
    }

    /**
     * Calcule le CA sur une période donnée .
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

    public double recalculerEtMettreAJourMontantFacture(int idReservation) {
        double total = factureDAO.calculerMontantTotal(idReservation);
        Facture f = factureDAO.trouverParReservation(idReservation);
        if (f != null) {
            f.setMontantTotal(total);
            // On utilise modifierStatut juste pour déclencher l'update du champ via une variante :
            // ici on ré-écrit complet via une requête dédiée si on a la méthode, sinon UPDATE direct.
            try (java.sql.PreparedStatement ps = com.hotel.util.DatabaseConnection.getConnection()
                    .prepareStatement("UPDATE factures SET montant_total=? WHERE id_facture=?")) {
                ps.setDouble(1, total);
                ps.setInt(2, f.getIdFacture());
                ps.executeUpdate();
            } catch (java.sql.SQLException e) {
                System.err.println("recalculerEtMettreAJourMontantFacture : " + e.getMessage());
            }
        }
        return total;
    }

    /**
     * Liste toutes les factures par statut (EN_ATTENTE, PAYEE, ANNULEE).
     */
    public List<Facture> listerFacturesParStatut(StatutFacture statut) {
        return factureDAO.listerParStatut(statut.name());
    }

    /**
     * Réactive une facture annulée : repasse son statut à PAYEE (recompte dans le CA)
     * ou à EN_ATTENTE selon le choix.
     */
    public boolean reactiverFacture(int idFacture, StatutFacture nouveauStatut) {
        if (nouveauStatut == null || nouveauStatut == StatutFacture.ANNULEE) {
            throw new IllegalArgumentException("Nouveau statut invalide pour une réactivation.");
        }
        return factureDAO.modifierStatut(idFacture, nouveauStatut.name());
    }

    public Reservation obtenirReservation(int idReservation) {
        return reservationDAO.trouverParId(idReservation);
    }

    public boolean mettreAJourStatutFacture(int idFacture, StatutFacture nouveauStatut) {
        if (idFacture <= 0 || nouveauStatut == null) return false;
        return factureDAO.modifierStatut(idFacture, nouveauStatut.name());
    }

}