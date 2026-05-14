package com.hotel.dao;


import com.hotel.model.Reservation;
import com.hotel.model.enumeration.StatutReservation;
import java.util.List;

public interface ReservationDAO {

    void ajouter(Reservation reservation);
    void modifier(Reservation reservation);
    void supprimer(int idReservation);
    Reservation rechercherParId(int idReservation);
    List<Reservation> listerTous();

    void changerStatut(int idReservation, StatutReservation statut);
    List<Reservation> listerReservationsEnCours();
}