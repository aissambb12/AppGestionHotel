package com.hotel.model;

import java.time.LocalDate;

public class ReservationServices {

    private int idConsommation;
    private int idReservation;
    private int idService;
    private int quantite;
    private LocalDate dateConsommation;

    public ReservationServices() {
    }

    public ReservationServices(int idConsommation, int idReservation, int idService, int quantite, LocalDate dateConsommation) {
        this.idConsommation = idConsommation;
        this.idReservation = idReservation;
        this.idService = idService;
        this.quantite = quantite;
        this.dateConsommation = dateConsommation;
    }

    public ReservationServices(int idService, int idReservation, int quantite, LocalDate dateConsommation) {
        this.idService = idService;
        this.idReservation = idReservation;
        this.quantite = quantite;
        this.dateConsommation = dateConsommation;
    }

    public int getIdConsommation() {
        return idConsommation;
    }

    public void setIdConsommation(int idConsommation) {
        this.idConsommation = idConsommation;
    }

    public int getIdReservation() {
        return idReservation;
    }

    public void setIdReservation(int idReservation) {
        this.idReservation = idReservation;
    }

    public int getIdService() {
        return idService;
    }

    public void setIdService(int idService) {
        this.idService = idService;
    }

    public int getQuantite() {
        return quantite;
    }

    public void setQuantite(int quantite) {
        this.quantite = quantite;
    }

    public LocalDate getDateConsommation() {
        return dateConsommation;
    }

    public void setDateConsommation(LocalDate dateConsommation) {
        this.dateConsommation = dateConsommation;
    }

    @Override
    public String toString() {
        return "Conso n°" + idConsommation + " - Qté: " + quantite + " le " + dateConsommation;
    }
}
