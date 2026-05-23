package com.hotel.dao;

import com.hotel.model.ReservationChambre;
import java.time.LocalDate;
import java.util.List;

public interface ReservationChambreDAO {
    boolean affecterChambre(ReservationChambre reservationChambre);
    boolean libererChambre(int idReservation, int idChambre);
    List<ReservationChambre> listerParReservation(int idReservation); // Permet de voir toutes les chambres d'un groupe

    /**
     * Vérifie si une chambre spécifique est occupée ou réservée sur une période donnée.
     * Sécurité double au niveau du DAO avant l'insertion finale.
     */
    boolean estChambreOccupee(int idChambre, LocalDate arrivee, LocalDate depart);
}