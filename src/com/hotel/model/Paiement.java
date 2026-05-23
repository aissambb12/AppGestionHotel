package com.hotel.model;


import com.hotel.model.enumeration.ModePaiement;

import java.time.LocalDateTime;
import java.util.Date;

public class Paiement {

    private int idPaiement;
    private int idFacture;
    private double montantPaye;
    private LocalDateTime datePaiement;
    private ModePaiement modePaiement;

    public Paiement() {
    }

    public Paiement(int idPaiement, int idFacture, double montantPaye, LocalDateTime datePaiement, ModePaiement modePaiement) {
        this.idPaiement = idPaiement;
        this.idFacture = idFacture;
        this.montantPaye = montantPaye;
        this.datePaiement = datePaiement;
        this.modePaiement = modePaiement;
    }

    public Paiement(int idFacture, double montantPaye, LocalDateTime datePaiement, ModePaiement modePaiement) {
        this.idFacture = idFacture;
        this.montantPaye = montantPaye;
        this.datePaiement = datePaiement;
        this.modePaiement = modePaiement;
    }

    public int getIdPaiement() {
        return idPaiement;
    }

    public void setIdPaiement(int idPaiement) {
        this.idPaiement = idPaiement;
    }

    public int getIdFacture() {
        return idFacture;
    }

    public void setIdFacture(int idFacture) {
        this.idFacture = idFacture;
    }

    public double getMontantPaye() {
        return montantPaye;
    }

    public void setMontantPaye(double montantPaye) {
        this.montantPaye = montantPaye;
    }

    public LocalDateTime getDatePaiement() {
        return datePaiement;
    }

    public void setDatePaiement(LocalDateTime datePaiement) {
        this.datePaiement = datePaiement;
    }

    public ModePaiement getModePaiement() {
        return modePaiement;
    }

    public void setModePaiement(ModePaiement modePaiement) {
        this.modePaiement = modePaiement;
    }

    @Override
    public String toString() {
        return "Paiement de " + montantPaye + " via " + modePaiement + " le " + datePaiement;
    }
}
