package com.hotel.dao;

import com.hotel.model.Reservation;
import java.util.List;

public interface ReservationDAO {
    /**
     * Insère la réservation et retourne l'ID généré par la base de données.
     * Cet ID est obligatoire pour pouvoir insérer ensuite dans 'reservation_chambres'.
     */
    int ajouter(Reservation reservation);
    boolean modifierStatut(int idReservation, String statut);
    Reservation trouverParId(int idReservation);
    List<Reservation> listerToutes();
    List<Reservation> listerParClient(int idClient);
    List<Reservation> listerParStatut(String statut);
}