package com.hotel.model;

import com.hotel.model.enumeration.StatutFacture;

import java.util.Date;

public class Facture {

    private int idFacture;
    private Date dateFacture;
    private double montantHebergement;
    private double montantRestaurant;
    private double montantTotal;
    private StatutFacture statut;
    private Reservation reservation;

    public Facture() {
    }

    public Facture(int idFacture, Date dateFacture, double montantHebergement, double montantRestaurant, double montantTotal, StatutFacture statut, Reservation reservation) {
        this.idFacture = idFacture;
        this.dateFacture = dateFacture;
        this.montantHebergement = montantHebergement;
        this.montantRestaurant = montantRestaurant;
        this.montantTotal = montantTotal;
        this.statut = statut;
        this.reservation = reservation;
    }

    public int getIdFacture() {
        return idFacture;
    }

    public void setIdFacture(int idFacture) {
        this.idFacture = idFacture;
    }

    public Date getDateFacture() {
        return dateFacture;
    }

    public void setDateFacture(Date dateFacture) {
        this.dateFacture = dateFacture;
    }

    public double getMontantHebergement() {
        return montantHebergement;
    }

    public void setMontantHebergement(double montantHebergement) {
        this.montantHebergement = montantHebergement;
    }

    public double getMontantRestaurant() {
        return montantRestaurant;
    }

    public void setMontantRestaurant(double montantRestaurant) {
        this.montantRestaurant = montantRestaurant;
    }

    public double getMontantTotal() {
        return montantTotal;
    }

    public void setMontantTotal(double montantTotal) {
        this.montantTotal = montantTotal;
    }

    public StatutFacture getStatut() {
        return statut;
    }

    public void setStatut(StatutFacture statut) {
        this.statut = statut;
    }

    public Reservation getReservation() {
        return reservation;
    }

    public void setReservation(Reservation reservation) {
        this.reservation = reservation;
    }

    @Override
    public String toString() {
        return "Facture{" +
                "idFacture=" + idFacture +
                ", dateFacture=" + dateFacture +
                ", montantHebergement=" + montantHebergement +
                ", montantRestaurant=" + montantRestaurant +
                ", montantTotal=" + montantTotal +
                ", statut=" + statut +
                ", reservation=" + reservation +
                '}';
    }
}
