package com.hotel.dao.impl;

import com.hotel.dao.MaintenanceDAO;
import com.hotel.model.*;
import com.hotel.util.DatabaseConnection;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class MaintenanceDAOImpl implements MaintenanceDAO {

    @Override
    public void ajouter(Maintenance maintenance) {
        String sql = "INSERT INTO maintenance(id_chambre, date_debut, date_fin, description, statut) VALUES (?, ?, ?, ?, ?)";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, maintenance.getChambre().getIdChambre());
            ps.setDate(2, new java.sql.Date(maintenance.getDateDebut().getTime()));

            if (maintenance.getDateFin() != null) {
                ps.setDate(3, new java.sql.Date(maintenance.getDateFin().getTime()));
            } else {
                ps.setNull(3, Types.DATE);
            }

            ps.setString(4, maintenance.getDescription());
            ps.setString(5, maintenance.getStatut().name());

            ps.executeUpdate();

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void modifier(Maintenance maintenance) {
        String sql = "UPDATE maintenance SET id_chambre=?, date_debut=?, date_fin=?, description=?, statut=? WHERE id_maintenance=?";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, maintenance.getChambre().getIdChambre());
            ps.setDate(2, new java.sql.Date(maintenance.getDateDebut().getTime()));

            if (maintenance.getDateFin() != null) {
                ps.setDate(3, new java.sql.Date(maintenance.getDateFin().getTime()));
            } else {
                ps.setNull(3, Types.DATE);
            }

            ps.setString(4, maintenance.getDescription());
            ps.setString(5, maintenance.getStatut().name());
            ps.setInt(6, maintenance.getIdMaintenance());

            ps.executeUpdate();

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void supprimer(int idMaintenance) {
        String sql = "DELETE FROM maintenance WHERE id_maintenance=?";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, idMaintenance);
            ps.executeUpdate();

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public Maintenance rechercherParId(int idMaintenance) {
        String sql = "SELECT * FROM maintenance WHERE id_maintenance=?";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, idMaintenance);

            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return construireMaintenance(rs);
                }
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return null;
    }

    @Override
    public List<Maintenance> listerTous() {
        List<Maintenance> maintenances = new ArrayList<>();
        String sql = "SELECT * FROM maintenance";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                maintenances.add(construireMaintenance(rs));
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return maintenances;
    }

    @Override
    public List<Maintenance> listerParChambre(int idChambre) {
        List<Maintenance> maintenances = new ArrayList<>();
        String sql = "SELECT * FROM maintenance WHERE id_chambre=?";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, idChambre);

            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    maintenances.add(construireMaintenance(rs));
                }
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return maintenances;
    }

    @Override
    public void terminerMaintenance(int idMaintenance) {
        String sql = "UPDATE maintenance SET statut='TERMINEE', date_fin=CURDATE() WHERE id_maintenance=?";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, idMaintenance);
            ps.executeUpdate();

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private Maintenance construireMaintenance(ResultSet rs) throws SQLException {
        Maintenance maintenance = new Maintenance();

        Chambre chambre = new Chambre();
        chambre.setIdChambre(rs.getInt("id_chambre"));

        maintenance.setIdMaintenance(rs.getInt("id_maintenance"));
        maintenance.setChambre(chambre);
        maintenance.setDateDebut(rs.getDate("date_debut"));
        maintenance.setDateFin(rs.getDate("date_fin"));
        maintenance.setDescription(rs.getString("description"));
        maintenance.setStatut(StatutMaintenance.valueOf(rs.getString("statut")));

        return maintenance;
    }
}
