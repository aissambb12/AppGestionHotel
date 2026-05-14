package com.hotel.model;

public class LigneCommandeRestaurant {

    private int idLigne;
    private int quantite;
    private double prixUnitaire;
    private CommandeRestaurant commandeRestaurant;
    private Plat plat;

    public LigneCommandeRestaurant() {
    }

    public LigneCommandeRestaurant(int idLigneCommande, int quantite, double prixUnitaire, CommandeRestaurant commandeRestaurant, Plat plat) {
        this.idLigne = idLigneCommande;
        this.quantite = quantite;
        this.prixUnitaire = prixUnitaire;
        this.commandeRestaurant = commandeRestaurant;
        this.plat = plat;
    }

    public int getIdLigneCommande() {
        return idLigne;
    }

    public void setIdLigneCommande(int idLigneCommande) {
        this.idLigne = idLigneCommande;
    }

    public int getQuantite() {
        return quantite;
    }

    public void setQuantite(int quantite) {
        this.quantite = quantite;
    }

    public double getPrixUnitaire() {
        return prixUnitaire;
    }

    public void setPrixUnitaire(double prixUnitaire) {
        this.prixUnitaire = prixUnitaire;
    }

    public CommandeRestaurant getCommandeRestaurant() {
        return commandeRestaurant;
    }

    public void setCommandeRestaurant(CommandeRestaurant commandeRestaurant) {
        this.commandeRestaurant = commandeRestaurant;
    }

    public Plat getPlat() {
        return plat;
    }

    public void setPlat(Plat plat) {
        this.plat = plat;
    }

    @Override
    public String toString() {
        return "LigneCommandeRestaurant{" +
                "idLigneCommande=" + idLigne +
                ", quantite=" + quantite +
                ", prixUnitaire=" + prixUnitaire +
                ", commandeRestaurant=" + commandeRestaurant +
                ", plat=" + plat +
                '}';
    }
}
