package com.hotel.dao;

import com.hotel.dao.UtilisateurDAO;
import com.hotel.model.Utilisateur;
import com.hotel.model.enumeration.Role;
import com.hotel.model.enumeration.StatutUtilisateur;
import com.hotel.util.DatabaseConnection;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class UtilisateurDAOImpl implements UtilisateurDAO {

    Connection conn = DatabaseConnection.getConnection();

    @Override
    public boolean ajouter(Utilisateur utilisateur) {
        String sql = "INSERT INTO utilisateurs (nom, prenom, email, mot_de_passe, role, statut) VALUES (?, ?, ?, ?, ?, ?)";
        try (
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, utilisateur.getNom());
            ps.setString(2, utilisateur.getPrenom());
            ps.setString(3, utilisateur.getEmail());
            ps.setString(4, utilisateur.getMotDEPasse());
            ps.setString(5, utilisateur.getRole().name()); // Convertit l'Enum en String
            ps.setString(6, utilisateur.getStatut().name());

            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            System.err.println("Erreur UtilisateurDAO.ajouter : " + e.getMessage());
            return false;
        }
    }

    @Override
    public boolean modifier(Utilisateur utilisateur) {
        String sql = "UPDATE utilisateurs SET nom=?, prenom=?, email=?, role=? WHERE id_utilisateur=?";
        try (
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, utilisateur.getNom());
            ps.setString(2, utilisateur.getPrenom());
            ps.setString(3, utilisateur.getEmail());
            ps.setString(4, utilisateur.getRole().name());
            ps.setInt(5, utilisateur.getIdUtilisateur());

            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            System.err.println("Erreur UtilisateurDAO.modifier : " + e.getMessage());
            return false;
        }
    }

    @Override
    public boolean modifierStatut(int idUtilisateur, String statut) {
        String sql = "UPDATE utilisateurs SET statut=? WHERE id_utilisateur=?";
        try (
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, statut);
            ps.setInt(2, idUtilisateur);

            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            System.err.println("Erreur UtilisateurDAO.modifierStatut : " + e.getMessage());
            return false;
        }
    }

    @Override
    public Utilisateur trouverParId(int idUtilisateur) {
        String sql = "SELECT * FROM utilisateurs WHERE id_utilisateur = ?";
        try (
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, idUtilisateur);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return mapperUtilisateur(rs);
                }
            }
        } catch (SQLException e) {
            System.err.println("Erreur UtilisateurDAO.trouverParId : " + e.getMessage());
        }
        return null;
    }

    @Override
    public Utilisateur trouverParEmail(String email) {
        String sql = "SELECT * FROM utilisateurs WHERE email = ?";
        try (
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, email);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return mapperUtilisateur(rs);
                }
            }
        } catch (SQLException e) {
            System.err.println("Erreur UtilisateurDAO.trouverParEmail : " + e.getMessage());
        }
        return null;
    }

    @Override
    public Utilisateur authentifier(String email, String motDePasse) {
        String sql = "SELECT * FROM utilisateurs WHERE email = ? AND mot_de_passe = ? AND statut = 'ACTIF'";
        try (
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, email);
            ps.setString(2, motDePasse); // En production, on comparerait les Hash
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return mapperUtilisateur(rs);
                }
            }
        } catch (SQLException e) {
            System.err.println("Erreur UtilisateurDAO.authentifier : " + e.getMessage());
        }
        return null; // Retourne null si mauvais identifiants ou compte inactif
    }

    @Override
    public List<Utilisateur> listerTous() {
        List<Utilisateur> liste = new ArrayList<>();
        String sql = "SELECT * FROM utilisateurs";
        try (
             PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                liste.add(mapperUtilisateur(rs));
            }
        } catch (SQLException e) {
            System.err.println("Erreur UtilisateurDAO.listerTous : " + e.getMessage());
        }
        return liste;
    }

    /**
     * Méthode utilitaire privée pour éviter la duplication de code lors de la lecture du ResultSet.
     */
    private Utilisateur mapperUtilisateur(ResultSet rs) throws SQLException {
        return new Utilisateur(
                rs.getInt("id_utilisateur"),
                rs.getString("nom"),
                rs.getString("prenom"),
                rs.getString("email"),
                rs.getString("mot_de_passe"),
                Role.valueOf(rs.getString("role")),
                StatutUtilisateur.valueOf(rs.getString("statut"))
        );
    }
}