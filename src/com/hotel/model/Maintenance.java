package com.hotel.model;

import com.hotel.model.enumeration.StatutMaintenance;

import java.util.Date;

public class Maintenance {

    private int idMaintenance;
    private Date dateDebut;
    private Date dateFin;
    private String description;
    private StatutMaintenance statut;
    private Chambre chambre;

    public Maintenance() {
    }

    public Maintenance(int idMaintenance, Date dateDebut, Date dateFin, String description, StatutMaintenance statut, Chambre chambre) {
        this.idMaintenance = idMaintenance;
        this.dateDebut = dateDebut;
        this.dateFin = dateFin;
        this.description = description;
        this.statut = statut;
        this.chambre = chambre;
    }

    public int getIdMaintenance() {
        return idMaintenance;
    }

    public void setIdMaintenance(int idMaintenance) {
        this.idMaintenance = idMaintenance;
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

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public StatutMaintenance getStatut() {
        return statut;
    }

    public void setStatut(StatutMaintenance statut) {
        this.statut = statut;
    }

    public Chambre getChambre() {
        return chambre;
    }

    public void setChambre(Chambre chambre) {
        this.chambre = chambre;
    }

    @Override
    public String toString() {
        return "Maintenance{" +
                "idMaintenance=" + idMaintenance +
                ", dateDebut=" + dateDebut +
                ", dateFin=" + dateFin +
                ", description='" + description + '\'' +
                ", statut=" + statut +
                ", chambre=" + chambre +
                '}';
    }
}
