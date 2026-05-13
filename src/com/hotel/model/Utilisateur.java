package com.hotel.model;

import com.hotel.model.enumeration.Role;

public class Utilisateur {
    private int idUtilisateur ;
    private String nom ;
    private String login ;
    private String motDePasse ;
    private Role role ;

    public Utilisateur(int idUtilisateur, Role role, String motDePasse, String login, String nom) {
        this.idUtilisateur = idUtilisateur;
        this.role = role;
        this.motDePasse = motDePasse;
        this.login = login;
        this.nom = nom;
    }

    public int getIdUtilisateur() {
        return idUtilisateur;
    }

    public void setIdUtilisateur(int idUtilisateur) {
        this.idUtilisateur = idUtilisateur;
    }

    public Role getRole() {
        return role;
    }

    public void setRole(Role role) {
        this.role = role;
    }

    public String getMotDePasse() {
        return motDePasse;
    }

    public void setMotDePasse(String motDePasse) {
        this.motDePasse = motDePasse;
    }

    public String getLogin() {
        return login;
    }

    public void setLogin(String login) {
        this.login = login;
    }

    public String getNom() {
        return nom;
    }

    public void setNom(String nom) {
        this.nom = nom;
    }
}
