package com.hotel.model;

import com.hotel.model.enumeration.StatutMaintenance;

import java.time.LocalDate;
import java.util.Date;

public class Maintenance {
    private int idMaintenance;
    private int idChambre;
    private LocalDate dateDebut;
    private LocalDate dateFin;
    private String description;
    private StatutMaintenance statutMaintenance;

    public Maintenance() {
    }

    public Maintenance(int idMaintenance, int idChambre, LocalDate dateDebut, LocalDate dateFin, String description, StatutMaintenance statutMaintenance) {
        this.idMaintenance = idMaintenance;
        this.idChambre = idChambre;
        this.dateDebut = dateDebut;
        this.dateFin = dateFin;
        this.description = description;
        this.statutMaintenance = statutMaintenance;
    }

    public Maintenance(int idChambre, LocalDate dateDebut, LocalDate dateFin, String description, StatutMaintenance statutMaintenance) {
        this.idChambre = idChambre;
        this.dateDebut = dateDebut;
        this.dateFin = dateFin;
        this.description = description;
        this.statutMaintenance = statutMaintenance;
    }

    public int getIdMaintenance() {
        return idMaintenance;
    }

    public void setIdMaintenance(int idMaintenance) {
        this.idMaintenance = idMaintenance;
    }

    public int getIdChambre() {
        return idChambre;
    }

    public void setIdChambre(int idChambre) {
        this.idChambre = idChambre;
    }

    public LocalDate getDateDebut() {
        return dateDebut;
    }

    public void setDateDebut(LocalDate dateDebut) {
        this.dateDebut = dateDebut;
    }

    public LocalDate getDateFin() {
        return dateFin;
    }

    public void setDateFin(LocalDate dateFin) {
        this.dateFin = dateFin;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public StatutMaintenance getStatutMaintenance() {
        return statutMaintenance;
    }

    public void setStatutMaintenance(StatutMaintenance statutMaintenance) {
        this.statutMaintenance = statutMaintenance;
    }

    public String toString() {
        return "Maintenance Chambre " + idChambre + " [" + statutMaintenance + "]";
    }
}
