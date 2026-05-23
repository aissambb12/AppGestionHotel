package com.hotel.model;

import com.hotel.model.enumeration.Role;
import com.hotel.model.enumeration.StatutUtilisateur;

public class Utilisateur {

    private int idUtilisateur;
    private String nom;
    private String prenom;
    private String email;
    private String motDEPasse;
    private Role role;
    private StatutUtilisateur statut;

    public Utilisateur() {
    }

    public Utilisateur(int idUtilisateur, String nom, String prenom, String email, String motDEPasse, Role role, StatutUtilisateur statu) {
        this.idUtilisateur = idUtilisateur;
        this.nom = nom;
        this.prenom = prenom;
        this.email = email;
        this.motDEPasse = motDEPasse;
        this.role = role;
        this.statut = statu;
    }

    public Utilisateur(String nom, String prenom, String email, String motDEPasse, Role role, StatutUtilisateur statu) {
        this.nom = nom;
        this.prenom = prenom;
        this.email = email;
        this.motDEPasse = motDEPasse;
        this.role = role;
        this.statut = statu;
    }

    public int getIdUtilisateur() {
        return idUtilisateur;
    }

    public void setIdUtilisateur(int idUtilisateur) {
        this.idUtilisateur = idUtilisateur;
    }

    public String getNom() {
        return nom;
    }

    public void setNom(String nom) {
        this.nom = nom;
    }

    public String getPrenom() {
        return prenom;
    }

    public void setPrenom(String prenom) {
        this.prenom = prenom;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getMotDEPasse() {
        return motDEPasse;
    }

    public void setMotDEPasse(String motDEPasse) {
        this.motDEPasse = motDEPasse;
    }

    public Role getRole() {
        return role;
    }

    public void setRole(Role role) {
        this.role = role;
    }

    public StatutUtilisateur getStatut() {
        return statut;
    }

    public void setStatut(StatutUtilisateur statut) {
        this.statut = statut;
    }

    @Override
    public String toString() {
        return nom + " " + prenom + " (" + role + ") - " + statut;
    }
}
