package com.hotel.dao.impl;

import com.hotel.dao.UtilisateurDAO;
import com.hotel.model.enumeration.Role;
import com.hotel.model.Utilisateur;
import com.hotel.util.DatabaseConnection;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class UtilisateurDAOImpl implements UtilisateurDAO {

    @Override
    public void ajouter(Utilisateur utilisateur) {
        String sql = "INSERT INTO utilisateur(nom, login, mot_de_passe, role) VALUES (?, ?, ?, ?)";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, utilisateur.getNom());
            ps.setString(2, utilisateur.getLogin());
            ps.setString(3, utilisateur.getMotDePasse());
            ps.setString(4, utilisateur.getRole().name());

            ps.executeUpdate();

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void modifier(Utilisateur utilisateur) {
        String sql = "UPDATE utilisateur SET nom=?, login=?, mot_de_passe=?, role=? WHERE id_utilisateur=?";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, utilisateur.getNom());
            ps.setString(2, utilisateur.getLogin());
            ps.setString(3, utilisateur.getMotDePasse());
            ps.setString(4, utilisateur.getRole().name());
            ps.setInt(5, utilisateur.getIdUtilisateur());

            ps.executeUpdate();

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void supprimer(int idUtilisateur) {
        String sql = "DELETE FROM utilisateur WHERE id_utilisateur=?";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, idUtilisateur);
            ps.executeUpdate();

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public Utilisateur rechercherParId(int idUtilisateur) {
        String sql = "SELECT * FROM utilisateur WHERE id_utilisateur=?";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, idUtilisateur);

            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return construireUtilisateur(rs);
                }
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return null;
    }

    @Override
    public Utilisateur rechercherParLogin(String login) {
        String sql = "SELECT * FROM utilisateur WHERE login=?";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, login);

            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return construireUtilisateur(rs);
                }
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return null;
    }

    @Override
    public List<Utilisateur> listerTous() {
        List<Utilisateur> utilisateurs = new ArrayList<>();
        String sql = "SELECT * FROM utilisateur";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                utilisateurs.add(construireUtilisateur(rs));
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return utilisateurs;
    }

    private Utilisateur construireUtilisateur(ResultSet rs) throws SQLException {
        Utilisateur utilisateur = new Utilisateur();
        utilisateur.setIdUtilisateur(rs.getInt("id_utilisateur"));
        utilisateur.setNom(rs.getString("nom"));
        utilisateur.setLogin(rs.getString("login"));
        utilisateur.setMotDePasse(rs.getString("mot_de_passe"));
        utilisateur.setRole(Role.valueOf(rs.getString("role")));
        return utilisateur;
    }
}
