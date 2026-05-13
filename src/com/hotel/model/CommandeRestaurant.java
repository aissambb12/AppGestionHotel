package com.hotel.model;

import com.hotel.model.enumeration.StatutCommandeRestaurant;

import java.util.Date;

public class CommandeRestaurant {

    private int idCommande;
    private Date dateCommande;
    private StatutCommandeRestaurant statut;
    private Reservation reservation;

    public CommandeRestaurant() {
    }

    public CommandeRestaurant(int idCommande, Date dateCommande, StatutCommandeRestaurant statut, Reservation reservation) {
        this.idCommande = idCommande;
        this.dateCommande = dateCommande;
        this.statut = statut;
        this.reservation = reservation;
    }

    public int getIdCommande() {
        return idCommande;
    }

    public void setIdCommande(int idCommande) {
        this.idCommande = idCommande;
    }

    public Date getDateCommande() {
        return dateCommande;
    }

    public void setDateCommande(Date dateCommande) {
        this.dateCommande = dateCommande;
    }

    public StatutCommandeRestaurant getStatut() {
        return statut;
    }

    public void setStatut(StatutCommandeRestaurant statut) {
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
        return "CommandeRestaurant{" +
                "idCommande=" + idCommande +
                ", dateCommande=" + dateCommande +
                ", statut=" + statut +
                ", reservation=" + reservation +
                '}';
    }
}
