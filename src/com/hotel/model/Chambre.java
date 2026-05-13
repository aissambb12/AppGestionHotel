package com.hotel.model;

public class Chambre {

    private int idChambre;
    private String numero;
    private String type;
    private double prixParNuit;
    private StatutChambre statut;

    public Chambre() {
    }

    public Chambre(int idChambre, String numero, String type, double prixParNuit, StatutChambre statut) {
        this.idChambre = idChambre;
        this.numero = numero;
        this.type = type;
        this.prixParNuit = prixParNuit;
        this.statut = statut;
    }

    public int getIdChambre() {
        return idChambre;
    }

    public void setIdChambre(int idChambre) {
        this.idChambre = idChambre;
    }

    public String getNumero() {
        return numero;
    }

    public void setNumero(String numero) {
        this.numero = numero;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public double getPrixParNuit() {
        return prixParNuit;
    }

    public void setPrixParNuit(double prixParNuit) {
        this.prixParNuit = prixParNuit;
    }

    public StatutChambre getStatut() {
        return statut;
    }

    public void setStatut(StatutChambre statut) {
        this.statut = statut;
    }

    @Override
    public String toString() {
        return "Chambre{" +
                "idChambre=" + idChambre +
                ", numero='" + numero + '\'' +
                ", type='" + type + '\'' +
                ", prixParNuit=" + prixParNuit +
                ", statut=" + statut +
                '}';
    }
}
