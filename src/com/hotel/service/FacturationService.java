package com.hotel.service;

import com.hotel.dao.FactureDAO;
import com.hotel.dao.LigneCommandeRestaurantDAO;
import com.hotel.dao.ReservationDAO;
import com.hotel.dao.impl.FactureDAOImpl;
import com.hotel.dao.impl.LigneCommandeRestaurantDAOImpl;
import com.hotel.dao.impl.ReservationDAOImpl;
import com.hotel.model.Facture;
import com.hotel.model.Reservation;
import com.hotel.model.enumeration.StatutFacture;
import com.hotel.util.DateUtil;

import java.util.Date;
import java.util.List;

public class FacturationService {

    private FactureDAO factureDAO = new FactureDAOImpl();
    private ReservationDAO reservationDAO = new ReservationDAOImpl();
    private LigneCommandeRestaurantDAO ligneDAO = new LigneCommandeRestaurantDAOImpl();

    public void genererFacture(int idReservation) {

        Reservation reservation = reservationDAO.rechercherParId(idReservation);

        if (reservation == null) {
            System.out.println("Réservation introuvable.");
            return;
        }

        Facture factureExistante = factureDAO.rechercherParReservation(idReservation);

        if (factureExistante != null) {
            System.out.println("Une facture existe déjà pour cette réservation.");
            return;
        }

        int nombreNuits = DateUtil.calculerNombreNuits(
                reservation.getDateDebut(),
                reservation.getDateFin()
        );

        double prixParNuit = reservation.getChambre().getPrixParNuit();
        double montantHebergement = nombreNuits * prixParNuit;

        double montantRestaurant = ligneDAO.calculerTotalRestaurantParReservation(idReservation);

        double montantTotal = montantHebergement + montantRestaurant;

        Facture facture = new Facture();
        facture.setReservation(reservation);
        facture.setDateFacture(new Date());
        facture.setMontantHebergement(montantHebergement);
        facture.setMontantRestaurant(montantRestaurant);
        facture.setMontantTotal(montantTotal);
        facture.setStatut(StatutFacture.NON_PAYEE);

        factureDAO.ajouter(facture);

        System.out.println("Facture générée avec succès.");
    }

    public void modifierFacture(Facture facture) {
        factureDAO.modifier(facture);
    }

    public void supprimerFacture(int idFacture) {
        factureDAO.supprimer(idFacture);
    }

    public Facture rechercherFacture(int idFacture) {
        return factureDAO.rechercherParId(idFacture);
    }

    public Facture rechercherFactureParReservation(int idReservation) {
        return factureDAO.rechercherParReservation(idReservation);
    }

    public List<Facture> listerFactures() {
        return factureDAO.listerTous();
    }
}