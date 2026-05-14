package com.hotel.model;

import com.hotel.model.enumeration.StatutReservation;

import java.util.Date;

public class Reservation {

    private int idReservation;
    private Date dateDebut;
    private Date dateFin;
    private StatutReservation statut;

    private Client client;
    private Chambre chambre;

    public Reservation() {
    }

    public Reservation(int idReservation, Date dateDebut, Date dateFin,  StatutReservation statut, Client client, Chambre chambre) {
        this.idReservation = idReservation;
        this.dateDebut = dateDebut;
        this.dateFin = dateFin;

        this.statut = statut;
        this.client = client;
        this.chambre = chambre;
    }

    public int getIdReservation() {
        return idReservation;
    }

    public void setIdReservation(int idReservation) {
        this.idReservation = idReservation;
    }

    public Date getDateDebut() {
        return dateDebut;
    }

    public void setDateDebut(Date dateDebut) {
        this.dateDebut = dateDebut;
    }

    public Date getDateFin() {
        return dateFin;
    }

    public void setDateFin(Date dateFin) {
        this.dateFin = dateFin;
    }



    public StatutReservation getStatut() {
        return statut;
    }

    public void setStatut(StatutReservation statut) {
        this.statut = statut;
    }

    public Client getClient() {
        return client;
    }

    public void setClient(Client client) {
        this.client = client;
    }

    public Chambre getChambre() {
        return chambre;
    }

    public void setChambre(Chambre chambre) {
        this.chambre = chambre;
    }

    @Override
    public String toString() {
        return "Reservation{" +
                "idReservation=" + idReservation +
                ", dateDebut=" + dateDebut +
                ", dateFin=" + dateFin +
                ", statut=" + statut +
                ", client=" + client +
                ", chambre=" + chambre +
                '}';
    }
}
