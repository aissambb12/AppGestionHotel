package com.hotel.dao.impl;

import com.hotel.dao.ClientDAO;
import com.hotel.model.Client;
import com.hotel.util.DatabaseConnection;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class ClientDAOImpl implements ClientDAO {

    Connection conn = DatabaseConnection.getConnection();

    @Override
    public boolean ajouter(Client client) {
        String sql = "INSERT INTO clients (nom, prenom, cin, email, telephone) VALUES (?, ?, ?, ?, ?)";
        try (PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, client.getNom());
            ps.setString(2, client.getPrenom());
            ps.setString(3, client.getCin());
            ps.setString(4, client.getEmail());
            ps.setString(5, client.getTelephone());

            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            System.err.println("Erreur ClientDAO.ajouter : " + e.getMessage());
            return false;
        }
    }

    @Override
    public boolean modifier(Client client) {
        String sql = "UPDATE clients SET nom=?, prenom=?, cin=?, email=?, telephone=? WHERE id_client=?";
        try (PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, client.getNom());
            ps.setString(2, client.getPrenom());
            ps.setString(3, client.getCin());
            ps.setString(4, client.getEmail());
            ps.setString(5, client.getTelephone());
            ps.setInt(6, client.getIdClient());

            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            System.err.println("Erreur ClientDAO.modifier : " + e.getMessage());
            return false;
        }
    }

    @Override
    public boolean supprimer(int idClient) {
        String sql = "DELETE FROM clients WHERE id_client=?";
        try (PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, idClient);
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            System.err.println("Erreur ClientDAO.supprimer (Peut-être lié à une réservation existante) : " + e.getMessage());
            return false; // Échouera si le client a déjà des réservations (grâce à ON DELETE RESTRICT)
        }
    }

    @Override
    public Client trouverParId(int idClient) {
        String sql = "SELECT * FROM clients WHERE id_client = ?";
        try (PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, idClient);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return mapperClient(rs);
                }
            }
        } catch (SQLException e) {
            System.err.println("Erreur ClientDAO.trouverParId : " + e.getMessage());
        }
        return null;
    }

    @Override
    public Client trouverParCin(String cin) {
        String sql = "SELECT * FROM clients WHERE cin = ?";
        try (PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, cin);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return mapperClient(rs);
                }
            }
        } catch (SQLException e) {
            System.err.println("Erreur ClientDAO.trouverParCin : " + e.getMessage());
        }
        return null;
    }

    @Override
    public List<Client> listerTous() {
        List<Client> liste = new ArrayList<>();
        String sql = "SELECT * FROM clients ORDER BY nom, prenom";
        try (PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                liste.add(mapperClient(rs));
            }
        } catch (SQLException e) {
            System.err.println("Erreur ClientDAO.listerTous : " + e.getMessage());
        }
        return liste;
    }

    @Override
    public List<Client> rechercherParMotCle(String motCle) {
        List<Client> liste = new ArrayList<>();
        // Recherche dans le nom, prénom ou CIN
        String sql = "SELECT * FROM clients WHERE nom LIKE ? OR prenom LIKE ? OR cin LIKE ?";
        try (PreparedStatement ps = conn.prepareStatement(sql)) {

            String recherche = "%" + motCle + "%"; // Le '%' permet de chercher n'importe où dans la chaîne
            ps.setString(1, recherche);
            ps.setString(2, recherche);
            ps.setString(3, recherche);

            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    liste.add(mapperClient(rs));
                }
            }
        } catch (SQLException e) {
            System.err.println("Erreur ClientDAO.rechercherParMotCle : " + e.getMessage());
        }
        return liste;
    }

    /**
     * Méthode utilitaire pour mapper le ResultSet en objet Client
     */
    private Client mapperClient(ResultSet rs) throws SQLException {
        return new Client(
                rs.getInt("id_client"),
                rs.getString("nom"),
                rs.getString("prenom"),
                rs.getString("cin"),
                rs.getString("email"),
                rs.getString("telephone")
        );
    }
}