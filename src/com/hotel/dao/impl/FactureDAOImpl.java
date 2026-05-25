package com.hotel.dao.impl;

import com.hotel.dao.FactureDAO;
import com.hotel.model.Facture;
import com.hotel.model.enumeration.StatutFacture;
import com.hotel.util.DatabaseConnection;

import java.sql.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class FactureDAOImpl implements FactureDAO {

    Connection conn = DatabaseConnection.getConnection();

    @Override
    public boolean creerFacture(Facture f) {
        String sql = "INSERT INTO factures (id_reservation, montant_total, statut_facture) VALUES (?, ?, ?)";
        try (PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, f.getIdReservation());
            ps.setDouble(2, f.getMontantTotal());
            ps.setString(3, f.getStatutFacture().name());
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            System.err.println("❌ FactureDAO.creerFacture : " + e.getMessage());
            return false;
        }
    }

    @Override
    public boolean modifierStatut(int idFacture, String statut) {
        String sql = "UPDATE factures SET statut_facture=? WHERE id_facture=?";
        try (PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, statut);
            ps.setInt(2, idFacture);
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            System.err.println("❌ FactureDAO.modifierStatut : " + e.getMessage());
            return false;
        }
    }

    @Override
    public Facture trouverParId(int idFacture) {
        String sql = "SELECT * FROM factures WHERE id_facture = ?";
        try (PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, idFacture);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) return mapperFacture(rs);
            }
        } catch (SQLException e) {
            System.err.println("❌ FactureDAO.trouverParId : " + e.getMessage());
        }
        return null;
    }

    @Override
    public double calculerChiffreAffaires(LocalDate dateDebut, LocalDate dateFin) {
        String sql = "SELECT SUM(montant_total) as total FROM factures WHERE statut_facture = 'PAYEE' AND DATE(date_facture) BETWEEN ? AND ?";

        try (PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setDate(1, java.sql.Date.valueOf(dateDebut));
            ps.setDate(2, java.sql.Date.valueOf(dateFin));

            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    double total = rs.getDouble("total");
                    return rs.wasNull() ? 0.0 : total;
                }
            }
        } catch (SQLException e) {
            System.err.println("❌ FactureDAO.calculerChiffreAffaires : " + e.getMessage());
        }
        return 0.0;
    }

    @Override
    public Facture trouverParReservation(int idReservation) {
        String sql = "SELECT * FROM factures WHERE id_reservation = ?";
        try (PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, idReservation);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) return mapperFacture(rs);
            }
        } catch (SQLException e) {
            System.err.println("❌ FactureDAO.trouverParReservation : " + e.getMessage());
        }
        return null;
    }

    @Override
    public List<Facture> listerToutes() {
        List<Facture> liste = new ArrayList<>();
        String sql = "SELECT * FROM factures ORDER BY date_facture DESC";
        try (PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) liste.add(mapperFacture(rs));
        } catch (SQLException e) {
            System.err.println("❌ FactureDAO.listerToutes : " + e.getMessage());
        }
        return liste;
    }

    @Override
    public double calculerMontantTotal(int idReservation) {
        String sql = "SELECT COALESCE(SUM(rc.prix_applique * DATEDIFF(rc.date_depart, rc.date_arrivee)), 0) as total_chambres " +
                "FROM reservation_chambres rc " +
                "WHERE rc.id_reservation = ?";

        try (PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, idReservation);

            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return rs.getDouble("total_chambres");
                }
            }
        } catch (SQLException e) {
            System.err.println("❌ FactureDAO.calculerMontantTotal : " + e.getMessage());
        }
        return 0.0;
    }

    private Facture mapperFacture(ResultSet rs) throws SQLException {
        return new Facture(
                rs.getInt("id_facture"),
                rs.getInt("id_reservation"),
                rs.getDouble("montant_total"),
                rs.getTimestamp("date_facture") != null ? rs.getTimestamp("date_facture").toLocalDateTime() : null,
                StatutFacture.valueOf(rs.getString("statut_facture"))
        );
    }
}