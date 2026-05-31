package com.hotel.service;

import com.hotel.dao.UtilisateurDAO;
import com.hotel.dao.impl.UtilisateurDAOImpl;
import com.hotel.model.Utilisateur;
import com.hotel.model.enumeration.StatutUtilisateur;

import java.util.List;

public class UtilisateurService {

    // Le service communique avec le DAO
    private UtilisateurDAO utilisateurDAO;

    public UtilisateurService() {
        this.utilisateurDAO = new UtilisateurDAOImpl();
    }

    /**
     * Tente de connecter un utilisateur.
     * @return L'objet Utilisateur si succès, ou null si échec.
     */
    public Utilisateur seConnecter(String email, String motDePasse) {
        if (email == null || email.trim().isEmpty() || motDePasse == null || motDePasse.trim().isEmpty()) {
            throw new IllegalArgumentException("L'email et le mot de passe sont obligatoires.");
        }
        return utilisateurDAO.authentifier(email.trim(), motDePasse);
    }

    /**
     * Ajoute un nouvel employé après avoir vérifié les règles métiers.
     */
    public boolean inscrireEmploye(Utilisateur utilisateur) {
        // 1. Validation des champs obligatoires
        if (utilisateur.getEmail() == null || !utilisateur.getEmail().contains("@")) {
            throw new IllegalArgumentException("Le format de l'email est invalide.");
        }
        if (utilisateur.getMotDEPasse() == null || utilisateur.getMotDEPasse().length() < 4) {
            throw new IllegalArgumentException("Le mot de passe doit contenir au moins 4 caractères.");
        }

        // 2. Règle métier : Vérifier que l'email n'existe pas déjà
        Utilisateur existant = utilisateurDAO.trouverParEmail(utilisateur.getEmail());
        if (existant != null) {
            throw new IllegalArgumentException("Un employé utilise déjà cette adresse email.");
        }

        // 3. Forcer le statut par défaut
        utilisateur.setStatut(StatutUtilisateur.ACTIF);

        // 4. Appel au DAO
        return utilisateurDAO.ajouter(utilisateur);
    }

    public boolean modifierEmploye(Utilisateur utilisateur) {
        return utilisateurDAO.modifier(utilisateur);
    }


    public boolean desactiverEmploye(int idUtilisateur) {
        return utilisateurDAO.modifierStatut(idUtilisateur, StatutUtilisateur.INACTIF.name());
    }

    public boolean activerEmploye(int idUtilisateur){
        return utilisateurDAO.modifierStatut(idUtilisateur , StatutUtilisateur.ACTIF.name());
    }

    public List<Utilisateur> listerTousLesEmployes() {
        return utilisateurDAO.listerTous();
    }
}