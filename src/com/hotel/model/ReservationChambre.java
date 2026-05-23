package com.hotel.model;

import java.time.LocalDate;

public class ReservationChambre {
    private int idReservation;
    private int idChambre;
    private LocalDate dateArrivee;
    private LocalDate dateDepart;
    private double prixApplique;

    public ReservationChambre() {
    }

    public ReservationChambre(int idReservation, int idChambre, LocalDate dateArrivee, LocalDate dateDepart, double prixApplique){
        this.idReservation = idReservation;
        this.idChambre = idChambre;
        this.dateDepart = dateDepart;
        this.dateArrivee = dateArrivee;
        this.prixApplique = prixApplique;
    }

    public ReservationChambre(int idChambre, LocalDate dateArrivee, LocalDate dateDepart, double prixApplique) {
        this.idChambre = idChambre;
        this.dateArrivee = dateArrivee;
        this.dateDepart = dateDepart;
        this.prixApplique = prixApplique;
    }

    public int getIdReservation() {
        return idReservation;
    }

    public void setIdReservation(int idReservation) {
        this.idReservation = idReservation;
    }

    public int getIdChambre() {
        return idChambre;
    }

    public void setIdChambre(int idChambre) {
        this.idChambre = idChambre;
    }

    public LocalDate getDateArrivee() {
        return dateArrivee;
    }

    public void setDateArrivee(LocalDate dateArrivee) {
        this.dateArrivee = dateArrivee;
    }

    public LocalDate getDateDepart() {
        return dateDepart;
    }

    public void setDateDepart(LocalDate dateDepart) {
        this.dateDepart = dateDepart;
    }

    public double getPrixApplique() {
        return prixApplique;
    }

    public void setPrixApplique(double prixApplique) {
        this.prixApplique = prixApplique;
    }

    public String toString() {
        return "Liaison [Réservation: " + idReservation + ", Chambre: " + idChambre + ", Du: " + dateArrivee + " Au: " + dateDepart + ", Prix: " + prixApplique + "]";
    }
}
