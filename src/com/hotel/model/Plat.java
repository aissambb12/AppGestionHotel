package com.hotel.model;

public class Plat {

    private int idPlat;
    private String nom;
    private String description;
    private double prix;
    private boolean disponible;

    public Plat() {
    }

    public Plat(int idPlat, String nom, String description, double prix, boolean disponible) {
        this.idPlat = idPlat;
        this.nom = nom;
        this.description = description;
        this.prix = prix;
        this.disponible = disponible;
    }

    public int getIdPlat() {
        return idPlat;
    }

    public void setIdPlat(int idPlat) {
        this.idPlat = idPlat;
    }

    public String getNom() {
        return nom;
    }

    public void setNom(String nom) {
        this.nom = nom;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public double getPrix() {
        return prix;
    }

    public void setPrix(double prix) {
        this.prix = prix;
    }

    public boolean isDisponible() {
        return disponible;
    }

    public void setDisponible(boolean disponible) {
        this.disponible = disponible;
    }

    @Override
    public String toString() {
        return "Plat{" +
                "idPlat=" + idPlat +
                ", nom='" + nom + '\'' +
                ", description='" + description + '\'' +
                ", prix=" + prix +
                ", disponible=" + disponible +
                '}';
    }
}
