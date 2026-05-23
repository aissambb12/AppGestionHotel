package com.hotel.model;

import com.hotel.model.enumeration.StatutFacture;

import java.time.LocalDateTime;
import java.util.Date;

public class Facture {

    private int idFacture;
    private int idReservation;
    private double montantTotal;
    private LocalDateTime dateFacture;
    private StatutFacture statutFacture;

    public Facture() {
    }

    public Facture(int idReservation, int idFacture, double montantTotal, LocalDateTime dateFacture, StatutFacture statutFacture) {
        this.idReservation = idReservation;
        this.idFacture = idFacture;
        this.montantTotal = montantTotal;
        this.dateFacture = dateFacture;
        this.statutFacture = statutFacture;
    }

    public Facture(int idReservation, double montantTotal, LocalDateTime dateFacture, StatutFacture statutFacture) {
        this.idReservation = idReservation;
        this.montantTotal = montantTotal;
        this.dateFacture = dateFacture;
        this.statutFacture = statutFacture;
    }

    public int getIdFacture() {
        return idFacture;
    }

    public void setIdFacture(int idFacture) {
        this.idFacture = idFacture;
    }

    public int getIdReservation() {
        return idReservation;
    }

    public void setIdReservation(int idReservation) {
        this.idReservation = idReservation;
    }

    public double getMontantTotal() {
        return montantTotal;
    }

    public void setMontantTotal(double montantTotal) {
        this.montantTotal = montantTotal;
    }

    public LocalDateTime getDateFacture() {
        return dateFacture;
    }

    public void setDateFacture(LocalDateTime dateFacture) {
        this.dateFacture = dateFacture;
    }

    public StatutFacture getStatutFacture() {
        return statutFacture;
    }

    public void setStatutFacture(StatutFacture statutFacture) {
        this.statutFacture = statutFacture;
    }

    @Override
    public String toString() {
        return "Facture n°" + idFacture + " - Total: " + montantTotal + " [" + statutFacture + "]";
    }
}
