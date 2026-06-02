package com.hotel.exception;

import java.time.LocalDate;

public class ChambreNonDisponibleException extends HotelException{

    public ChambreNonDisponibleException(String numeroChambre, LocalDate debut, LocalDate fin) {
        super("La chambre n° " + numeroChambre + " n'est pas disponible du " + debut + " au " + fin + ".");
    }

}
