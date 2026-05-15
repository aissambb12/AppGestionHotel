package com.hotel.service;

import com.hotel.dao.ChambreDAO;
import com.hotel.dao.ClientDAO;
import com.hotel.dao.ReservationDAO;
import com.hotel.dao.impl.ChambreDAOImpl;
import com.hotel.dao.impl.ClientDAOImpl;
import com.hotel.dao.impl.ReservationDAOImpl;
import com.hotel.model.*;
import com.hotel.model.enumeration.StatutChambre;
import com.hotel.model.enumeration.StatutReservation;
import com.hotel.util.DateUtil;

import java.util.Date;
import java.util.List;

public class ReservationService {

    private ClientDAO clientDAO = new ClientDAOImpl();
    private ChambreDAO chambreDAO = new ChambreDAOImpl();
    private ReservationDAO reservationDAO = new ReservationDAOImpl();

    public void creerReservation(int idClient, int idChambre, String dateDebutTexte, String dateFinTexte) {

        Client client = clientDAO.rechercherParId(idClient);

        if (client == null) {
            System.out.println("Client introuvable.");
            return;
        }

        Chambre chambre = chambreDAO.rechercherParId(idChambre);

        if (chambre == null) {
            System.out.println("Chambre introuvable.");
            return;
        }

        if (chambre.getStatut() != StatutChambre.DISPONIBLE) {
            System.out.println("Chambre non disponible.");
            return;
        }

        Date dateDebut = DateUtil.stringVersDate(dateDebutTexte);
        Date dateFin = DateUtil.stringVersDate(dateFinTexte);

        if (dateDebut == null || dateFin == null) {
            System.out.println("Format de date invalide.");
            return;
        }

        if (!DateUtil.estDateFinApresDateDebut(dateDebut, dateFin)) {
            System.out.println("La date de fin doit être après la date de début.");
            return;
        }

        Reservation reservation = new Reservation();
        reservation.setClient(client);
        reservation.setChambre(chambre);
        reservation.setDateDebut(dateDebut);
        reservation.setDateFin(dateFin);
        reservation.setStatut(StatutReservation.RESERVEE);

        reservationDAO.ajouter(reservation);
        chambreDAO.changerStatut(idChambre, StatutChambre.RESERVEE);

        System.out.println("Réservation créée avec succès.");
    }

    public void effectuerCheckIn(int idReservation) {

        Reservation reservation = reservationDAO.rechercherParId(idReservation);

        if (reservation == null) {
            System.out.println("Réservation introuvable.");
            return;
        }

        if (reservation.getStatut() != StatutReservation.RESERVEE) {
            System.out.println("La réservation n'est pas valide pour le check-in.");
            return;
        }

        reservationDAO.changerStatut(idReservation, StatutReservation.EN_COURS);
        chambreDAO.changerStatut(
                reservation.getChambre().getIdChambre(),
                StatutChambre.OCCUPEE
        );

        System.out.println("Check-in effectué avec succès.");
    }

    public void effectuerCheckOut(int idReservation) {

        Reservation reservation = reservationDAO.rechercherParId(idReservation);

        if (reservation == null) {
            System.out.println("Réservation introuvable.");
            return;
        }

        if (reservation.getStatut() != StatutReservation.EN_COURS) {
            System.out.println("La réservation n'est pas en cours.");
            return;
        }

        FacturationService facturationService = new FacturationService();
        facturationService.genererFacture(idReservation);

        reservationDAO.changerStatut(idReservation, StatutReservation.TERMINEE);
        chambreDAO.changerStatut(
                reservation.getChambre().getIdChambre(),
                StatutChambre.DISPONIBLE
        );

        System.out.println("Check-out effectué avec succès.");
    }

    public void annulerReservation(int idReservation) {

        Reservation reservation = reservationDAO.rechercherParId(idReservation);

        if (reservation == null) {
            System.out.println("Réservation introuvable.");
            return;
        }

        reservationDAO.changerStatut(idReservation, StatutReservation.ANNULEE);
        chambreDAO.changerStatut(
                reservation.getChambre().getIdChambre(),
                StatutChambre.DISPONIBLE
        );

        System.out.println("Réservation annulée.");
    }

    public Reservation rechercherReservation(int idReservation) {
        return reservationDAO.rechercherParId(idReservation);
    }

    public List<Reservation> listerReservations() {
        return reservationDAO.listerTous();
    }

    public List<Reservation> listerReservationsEnCours() {
        return reservationDAO.listerReservationsEnCours();
    }
}