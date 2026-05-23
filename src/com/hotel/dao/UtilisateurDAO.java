package com.hotel.dao;

import com.hotel.model.Utilisateur;
import java.util.List;

public interface UtilisateurDAO {
    boolean ajouter(Utilisateur utilisateur);
    boolean modifier(Utilisateur utilisateur);
    boolean modifierStatut(int idUtilisateur, String statut);
    Utilisateur trouverParId(int idUtilisateur);
    Utilisateur trouverParEmail(String email); // Crucial pour la vérification unique et le profil
    Utilisateur authentifier(String email, String motDePasse); // Pour l'écran de Login (Vérification des accès)
    List<Utilisateur> listerTous();
}