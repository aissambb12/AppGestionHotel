package com.hotel.dao;

import com.hotel.model.Utilisateur;
import java.util.List;

public interface UtilisateurDAO {

    void ajouter(Utilisateur utilisateur);
    void modifier(Utilisateur utilisateur);
    void supprimer(int idUtilisateur);
    Utilisateur rechercherParId(int idUtilisateur);
    Utilisateur rechercherParLogin(String login);
    List<Utilisateur> listerTous();
}
