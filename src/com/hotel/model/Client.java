package com.hotel.model;

public class Client {

    private int idClient ;
    private String nom ;
    private String prenom ;
    private String cin ;
    private String telephone ;
    private String email ;

    public Client(String email, String telephone, String cin, String prenom, String nom, int idClient) {
        this.email = email;
        this.telephone = telephone;
        this.cin = cin;
        this.prenom = prenom;
        this.nom = nom;
        this.idClient = idClient;
    }

    public Client() {}

    public int getIdClient() {
        return idClient;
    }

    public void setIdClient(int idClient) {
        this.idClient = idClient;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getCin() {
        return cin;
    }

    public void setCin(String cin) {
        this.cin = cin;
    }

    public String getTelephone() {
        return telephone;
    }

    public void setTelephone(String tlephone) {
        this.telephone = tlephone;
    }

    public String getPrenom() {
        return prenom;
    }

    public void setPrenom(String prenom) {
        this.prenom = prenom;
    }

    public String getNom() {
        return nom;
    }

    public void setNom(String nom) {
        this.nom = nom;
    }
}
