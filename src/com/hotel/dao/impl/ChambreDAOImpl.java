package com.hotel.dao.impl;

import com.hotel.dao.ChambreDAO;
import com.hotel.model.Chambre;
import com.hotel.model.enumeration.StatutChambre;
import com.hotel.util.DatabaseConnection;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class ChambreDAOImpl implements ChambreDAO {

    @Override
    public void ajouter(Chambre chambre) {
        String sql = "INSERT INTO chambre(numero, type, prix_par_nuit, statut) VALUES (?, ?, ?, ?)";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, chambre.getNumero());
            ps.setString(2, chambre.getType());
            ps.setDouble(3, chambre.getPrixParNuit());
            ps.setString(4, chambre.getStatut().name());

            ps.executeUpdate();

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void modifier(Chambre chambre) {
        String sql = "UPDATE chambre SET numero=?, type=?, prix_par_nuit=?, statut=? WHERE id_chambre=?";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, chambre.getNumero());
            ps.setString(2, chambre.getType());
            ps.setDouble(3, chambre.getPrixParNuit());
            ps.setString(4, chambre.getStatut().name());
            ps.setInt(5, chambre.getIdChambre());

            ps.executeUpdate();

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void supprimer(int idChambre) {
        String sql = "DELETE FROM chambre WHERE id_chambre=?";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, idChambre);
            ps.executeUpdate();

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public Chambre rechercherParId(int idChambre) {
        String sql = "SELECT * FROM chambre WHERE id_chambre=?";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, idChambre);

            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return construireChambre(rs);
                }
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return null;
    }

    @Override
    public List<Chambre> listerTous() {
        List<Chambre> chambres = new ArrayList<>();
        String sql = "SELECT * FROM chambre";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                chambres.add(construireChambre(rs));
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return chambres;
    }

    @Override
    public void changerStatut(int idChambre, StatutChambre statut) {
        String sql = "UPDATE chambre SET statut=? WHERE id_chambre=?";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, statut.name());
            ps.setInt(2, idChambre);

            ps.executeUpdate();

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public List<Chambre> listerDisponibles() {
        List<Chambre> chambres = new ArrayList<>();
        String sql = "SELECT * FROM chambre WHERE statut='DISPONIBLE'";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                chambres.add(construireChambre(rs));
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return chambres;
    }

    private Chambre construireChambre(ResultSet rs) throws SQLException {
        Chambre chambre = new Chambre();
        chambre.setIdChambre(rs.getInt("id_chambre"));
        chambre.setNumero(rs.getString("numero"));
        chambre.setType(rs.getString("type"));
        chambre.setPrixParNuit(rs.getDouble("prix_par_nuit"));
        chambre.setStatut(StatutChambre.valueOf(rs.getString("statut")));
        return chambre;
    }
}