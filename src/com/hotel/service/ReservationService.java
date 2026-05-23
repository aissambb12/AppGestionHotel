package com.hotel.service;

import com.hotel.dao.*;
import com.hotel.dao.FactureDAOImpl;
import com.hotel.dao.ReservationChambreDAOImpl;
import com.hotel.dao.ReservationDAOImpl;
import com.hotel.model.*;
import com.hotel.model.enumeration.StatutFacture;
import com.hotel.model.enumeration.StatutReservation;
import com.hotel.util.DateUtil;
import com.hotel.util.ValidationUtil;

import java.time.LocalDate;
import java.util.List;

public class ReservationService {

    // Ce service a besoin de communiquer avec 3 DAO différents !
    private ReservationDAO reservationDAO;
    private ReservationChambreDAO reservationChambreDAO;
    private FactureDAO factureDAO;

    public ReservationService() {
        this.reservationDAO = new ReservationDAOImpl();
        this.reservationChambreDAO = new ReservationChambreDAOImpl();
        this.factureDAO = new FactureDAOImpl();
    }

    /**
     * Orchestre la création complète d'un dossier de réservation.
     */
    public boolean creerNouvelleReservation(Reservation reservation, List<Chambre> chambresReservees, LocalDate arrivee, LocalDate depart) {

        // 1. Validation des dates
        if (!ValidationUtil.sontDatesReservationValides(arrivee, depart)) {
            throw new IllegalArgumentException("Les dates de séjour sont incohérentes.");
        }

        // 2. Vérification de sécurité (Double Check) : Les chambres sont-elles TOUJOURS libres ?
        for (Chambre c : chambresReservees) {
            if (reservationChambreDAO.estChambreOccupee(c.getIdChambre(), arrivee, depart)) {
                throw new IllegalStateException("Alerte Surbooking : La chambre " + c.getNumero() + " vient d'être réservée par un autre utilisateur.");
            }
        }

        // 3. Étape A : Créer l'entête de la réservation
        reservation.setStatut(StatutReservation.CONFIRMEE);
        int idReservationGenere = reservationDAO.ajouter(reservation);

        if (idReservationGenere == -1) return false; // Échec critique

        // 4. Étape B : Lier les chambres choisies à cette réservation
        double totalEstime = 0.0;
        int nuits = DateUtil.calculerNombreNuits(arrivee, depart);

        for (Chambre c : chambresReservees) {
            ReservationChambre rc = new ReservationChambre(idReservationGenere, c.getIdChambre(), arrivee, depart, c.getPrixUnitaire());
            reservationChambreDAO.affecterChambre(rc);
            totalEstime += (c.getPrixUnitaire() * nuits); // Calcul local provisoire
        }

        // 5. Étape C : Créer la facture initiale (EN_ATTENTE) pour ce client
        Facture factureInitiale = new Facture();
        factureInitiale.setIdReservation(idReservationGenere);
        factureInitiale.setMontantTotal(totalEstime);
        factureInitiale.setDateFacture(DateUtil.dateEtHeureMaintenant());
        factureInitiale.setStatutFacture(StatutFacture.EN_ATTENTE);
        factureDAO.creerFacture(factureInitiale);

        return true; // Succès total du processus !
    }

    /**
     * Valide le Check-In du client et passe la réservation au statut EN_COURS.
     */
    public boolean validerCheckIn(int idReservation) {
        if (idReservation <= 0) {
            throw new IllegalArgumentException("ID de réservation invalide.");
        }
        // Utilisation de votre Enum StatutReservation
        return reservationDAO.modifierStatut(idReservation, StatutReservation.CONFIRMEE.name());
    }

    public List<Reservation> listerToutesLesReservations() {
        return reservationDAO.listerToutes();
    }
}