package com.hotel.model;

public class Chambre {

    private int idChambre ;
    private String numero ;
    private String type ;
    private double prixParNuit ;
    private StatutChambre statut;

    public Chambre(StatutChambre statu, double prixParNuit, String type, String numero, int idChambre) {
        this.statut = statu;
        this.prixParNuit = prixParNuit;
        this.type = type;
        this.numero = numero;
        this.idChambre = idChambre;
    }

    public Chambre() {}

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


}
