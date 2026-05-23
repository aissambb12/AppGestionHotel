package com.hotel.dao;

import com.hotel.model.Maintenance;
import com.hotel.model.enumeration.StatutMaintenance;
import com.hotel.util.DatabaseConnection;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class MaintenanceDAOImpl implements MaintenanceDAO {

    @Override
    public boolean planifier(Maintenance m) {
        String sql = "INSERT INTO maintenances (id_chambre, date_debut, date_fin, description, statut_maintenance) VALUES (?, ?, ?, ?, ?)";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, m.getIdChambre());
            ps.setDate(2, Date.valueOf(m.getDateDebut()));
            ps.setDate(3, Date.valueOf(m.getDateFin()));
            ps.setString(4, m.getDescription());
            ps.setString(5, StatutMaintenance.EN_COURS.name());
            return ps.executeUpdate() > 0;
        } catch (SQLException e) { e.printStackTrace(); return false; }
    }

    @Override
    public boolean terminerMaintenance(int idMaintenance, String statut) {
        String sql = "UPDATE maintenances SET statut_maintenance = ? WHERE id_maintenance = ?";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, statut);
            ps.setInt(2, idMaintenance);
            return ps.executeUpdate() > 0;
        } catch (SQLException e) { e.printStackTrace(); return false; }
    }

    @Override
    public Maintenance trouverParId(int idMaintenance) {
        String sql = "SELECT * FROM maintenances WHERE id_maintenance = ?";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, idMaintenance);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) return mapperMaintenance(rs);
        } catch (SQLException e) { e.printStackTrace(); }
        return null;
    }

    @Override
    public List<Maintenance> listerToutes() { return executerSelect("SELECT * FROM maintenances"); }

    @Override
    public List<Maintenance> listerActives() { return executerSelect("SELECT * FROM maintenances WHERE statut_maintenance = 'EN_COURS'"); }

    @Override
    public List<Maintenance> listerParChambre(int idChambre) {
        String sql = "SELECT * FROM maintenances WHERE id_chambre = ?";
        List<Maintenance> liste = new ArrayList<>();
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, idChambre);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) liste.add(mapperMaintenance(rs));
        } catch (SQLException e) { e.printStackTrace(); }
        return liste;
    }

    private List<Maintenance> executerSelect(String sql) {
        List<Maintenance> liste = new ArrayList<>();
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) liste.add(mapperMaintenance(rs));
        } catch (SQLException e) { e.printStackTrace(); }
        return liste;
    }

    private Maintenance mapperMaintenance(ResultSet rs) throws SQLException {
        return new Maintenance(
                rs.getInt("id_maintenance"),
                rs.getInt("id_chambre"),
                rs.getDate("date_debut").toLocalDate(),
                rs.getDate("date_fin").toLocalDate(),
                rs.getString("description"),
                StatutMaintenance.valueOf(rs.getString("statut_maintenance"))
        );
    }
}