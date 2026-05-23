package com.hotel.dao;

import com.hotel.model.ReservationServices;
import java.util.List;

public interface ReservationServicesDAO {
    boolean enregistrerConsommation(ReservationServices reservationService);
    List<ReservationServices> listerConsommationsParReservation(int idReservation);
    boolean supprimerConsommation(int idConsommation);
    List<ReservationServices> listerParReservation(int idReservation); // Indispensable pour dresser la facture
}