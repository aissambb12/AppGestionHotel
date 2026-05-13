package com.hotel.dao.impl;

import com.hotel.dao.PlatDAO;
import com.hotel.model.Plat;
import com.hotel.util.DatabaseConnection;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class PlatDAOImpl implements PlatDAO {

    @Override
    public void ajouter(Plat plat) {
        String sql = "INSERT INTO plat(nom, description, prix, disponible) VALUES (?, ?, ?, ?)";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, plat.getNom());
            ps.setString(2, plat.getDescription());
            ps.setDouble(3, plat.getPrix());
            ps.setBoolean(4, plat.isDisponible());

            ps.executeUpdate();

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void modifier(Plat plat) {
        String sql = "UPDATE plat SET nom=?, description=?, prix=?, disponible=? WHERE id_plat=?";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, plat.getNom());
            ps.setString(2, plat.getDescription());
            ps.setDouble(3, plat.getPrix());
            ps.setBoolean(4, plat.isDisponible());
            ps.setInt(5, plat.getIdPlat());

            ps.executeUpdate();

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void supprimer(int idPlat) {
        String sql = "DELETE FROM plat WHERE id_plat=?";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, idPlat);
            ps.executeUpdate();

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public Plat rechercherParId(int idPlat) {
        String sql = "SELECT * FROM plat WHERE id_plat=?";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, idPlat);

            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return construirePlat(rs);
                }
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return null;
    }

    @Override
    public List<Plat> listerTous() {
        List<Plat> plats = new ArrayList<>();
        String sql = "SELECT * FROM plat";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                plats.add(construirePlat(rs));
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return plats;
    }

    @Override
    public List<Plat> listerDisponibles() {
        List<Plat> plats = new ArrayList<>();
        String sql = "SELECT * FROM plat WHERE disponible=true";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                plats.add(construirePlat(rs));
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return plats;
    }

    private Plat construirePlat(ResultSet rs) throws SQLException {
        Plat plat = new Plat();
        plat.setIdPlat(rs.getInt("id_plat"));
        plat.setNom(rs.getString("nom"));
        plat.setDescription(rs.getString("description"));
        plat.setPrix(rs.getDouble("prix"));
        plat.setDisponible(rs.getBoolean("disponible"));
        return plat;
    }
}
