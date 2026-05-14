package com.hotel.model;


import com.hotel.model.enumeration.ModePaiement;

import java.util.Date;

public class Paiement {

    private int idPaiement;
    private Date datePaiement;
    private double montant;
    private ModePaiement modePaiement;
    private Facture facture;

    public Paiement() {
    }

    public Paiement(int idPaiement, Date datePaiement, double montant, ModePaiement modePaiement, Facture facture) {
        this.idPaiement = idPaiement;
        this.datePaiement = datePaiement;
        this.montant = montant;
        this.modePaiement = modePaiement;
        this.facture = facture;
    }

    public int getIdPaiement() {
        return idPaiement;
    }

    public void setIdPaiement(int idPaiement) {
        this.idPaiement = idPaiement;
    }

    public Date getDatePaiement() {
        return datePaiement;
    }

    public void setDatePaiement(Date datePaiement) {
        this.datePaiement = datePaiement;
    }

    public double getMontant() {
        return montant;
    }

    public void setMontant(double montant) {
        this.montant = montant;
    }

    public ModePaiement getModePaiement() {
        return modePaiement;
    }

    public void setModePaiement(ModePaiement modePaiement) {
        this.modePaiement = modePaiement;
    }

    public Facture getFacture() {
        return facture;
    }

    public void setFacture(Facture facture) {
        this.facture = facture;
    }

    @Override
    public String toString() {
        return "Paiement{" +
                "idPaiement=" + idPaiement +
                ", datePaiement=" + datePaiement +
                ", montant=" + montant +
                ", modePaiement=" + modePaiement +
                ", facture=" + facture +
                '}';
    }
}
