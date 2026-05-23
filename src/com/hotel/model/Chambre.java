package com.hotel.model;

import com.hotel.model.enumeration.StatutChambre;

public class Chambre {
    private int idChambre;
    private String numero;
    private String categorie;
    private double prixUnitaire;
    private StatutChambre statutChambre;

    public Chambre() {
    }

    public Chambre(int idChambre, String numero, String categorie, double prixUnitaire, StatutChambre statut) {
        this.idChambre = idChambre;
        this.numero = numero;
        this.categorie = categorie;
        this.prixUnitaire = prixUnitaire;
        this.statutChambre = statut;
    }

    public int getIdChambre() { return idChambre; }
    public void setIdChambre(int idChambre) { this.idChambre = idChambre; }

    public String getNumero() { return numero; }
    public void setNumero(String numero) { this.numero = numero; }

    public String getCategorie() { return categorie; }
    public void setCategorie(String categorie) { this.categorie = categorie; }

    public double getPrixUnitaire() { return prixUnitaire; }
    public void setPrixUnitaire(double prixUnitaire) { this.prixUnitaire = prixUnitaire; }

    public StatutChambre getStatutChambre() { return statutChambre; }
    public void setStatutChambre(StatutChambre statutChambre) { this.statutChambre = statutChambre; }

    @Override
    public String toString() {
        return "Chambre " + numero + " [" + categorie + "] - " + statutChambre;
    }
}