package com.hotel.dao;

import com.hotel.model.ReservationChambre;
import java.time.LocalDate;
import java.util.List;

public interface ReservationChambreDAO {
    boolean affecterChambre(ReservationChambre reservationChambre);
    boolean libererChambre(int idReservation, int idChambre);
    List<ReservationChambre> listerParReservation(int idReservation); // Permet de voir toutes les chambres d'un groupe


    boolean estChambreOccupee(int idChambre, LocalDate arrivee, LocalDate depart);
}