package com.hotel.service;

import com.hotel.dao.UtilisateurDAO;
import com.hotel.dao.impl.UtilisateurDAOImpl;
import com.hotel.model.Utilisateur;

public class AuthService {

    private UtilisateurDAO utilisateurDAO = new UtilisateurDAOImpl();

    public Utilisateur authentifier(String login, String motDePasse) {

        if (login == null || login.trim().isEmpty()) {
            return null;
        }

        if (motDePasse == null || motDePasse.trim().isEmpty()) {
            return null;
        }

        Utilisateur utilisateur = utilisateurDAO.rechercherParLogin(login);

        if (utilisateur == null) {
            return null;
        }

        if (utilisateur.getMotDePasse().equals(motDePasse)) {
            return utilisateur;
        }

        return null;
    }
}