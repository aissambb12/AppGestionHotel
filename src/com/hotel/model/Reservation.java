package com.hotel.model;

import com.hotel.model.enumeration.StatutReservation;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Date;

public class Reservation {

    private int idReservation;
    private int idClient;
    private int idUtilisateur;
    private LocalDateTime dateCreation;
    private StatutReservation statut;


    public Reservation() {
    }

    public Reservation(int idReservation, int idClient, int idUtilisateur, LocalDateTime dateCreation, StatutReservation statut) {
        this.idReservation = idReservation;
        this.idClient = idClient;
        this.idUtilisateur = idUtilisateur;
        this.dateCreation = dateCreation;
        this.statut = statut;
    }

    public Reservation(int idClient, int idUtilisateur, LocalDateTime dateCreation, StatutReservation statut) {
        this.idClient = idClient;
        this.idUtilisateur = idUtilisateur;
        this.dateCreation = dateCreation;
        this.statut = statut;
    }



    public int getIdReservation() {
        return idReservation;
    }

    public void setIdReservation(int idReservation) {
        this.idReservation = idReservation;
    }

    public int getIdClient() {
        return idClient;
    }

    public void setIdClient(int idClient) {
        this.idClient = idClient;
    }

    public int getIdUtilisateur() {
        return idUtilisateur;
    }

    public void setIdUtilisateur(int idUtilisateur) {
        this.idUtilisateur = idUtilisateur;
    }

    public LocalDateTime getDateCreation() {
        return dateCreation;
    }

    public void setDateCreation(LocalDateTime dateCreation) {
        this.dateCreation = dateCreation;
    }

    public StatutReservation getStatut() {
        return statut;
    }

    public void setStatut(StatutReservation statut) {
        this.statut = statut;
    }

    public String toString() {
        return "Réservation n° " + idReservation + " [Client: " + idClient + ", Créée le: " + dateCreation + "] - " + statut + " par: " + idUtilisateur;
    }
}