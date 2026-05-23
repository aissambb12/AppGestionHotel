package com.hotel.dao;

import com.hotel.model.Facture;
import com.hotel.model.enumeration.StatutFacture;
import com.hotel.util.DatabaseConnection;

import java.sql.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class FactureDAOImpl implements FactureDAO {

    @Override
    public boolean creerFacture(Facture f) {
        String sql = "INSERT INTO factures (id_reservation, montant_total, statut_facture) VALUES (?, ?, ?)";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, f.getIdReservation());
            ps.setDouble(2, f.getMontantTotal());
            ps.setString(3, f.getStatutFacture().name());
            return ps.executeUpdate() > 0;
        } catch (SQLException e) { e.printStackTrace(); return false; }
    }

    @Override
    public boolean modifierStatut(int idFacture, String statut) {
        String sql = "UPDATE factures SET statut_facture=? WHERE id_facture=?";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, statut); ps.setInt(2, idFacture);
            return ps.executeUpdate() > 0;
        } catch (SQLException e) { e.printStackTrace(); return false; }
    }

    @Override
    public Facture trouverParId(int idFacture) {
        String sql = "SELECT * FROM factures WHERE id_facture = ?";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, idFacture); ResultSet rs = ps.executeQuery();
            if (rs.next()) return mapperFacture(rs);
        } catch (SQLException e) { e.printStackTrace(); }
        return null;
    }

    @Override
    public double calculerChiffreAffaires(LocalDate dateDebut, LocalDate dateFin) {
        String sql = "SELECT SUM(montant_total) as total FROM factures WHERE statut_facture = 'PAYEE' AND DATE(date_facture) BETWEEN ? AND ?";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setDate(1, java.sql.Date.valueOf(dateDebut));
            ps.setDate(2, java.sql.Date.valueOf(dateFin));

            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return rs.getDouble("total");
                }
            }
        } catch (SQLException e) {
            System.err.println("Erreur lors du calcul du CA : " + e.getMessage());
        }
        return 0.0;
    }

    @Override
    public Facture trouverParReservation(int idReservation) {
        String sql = "SELECT * FROM factures WHERE id_reservation = ?";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, idReservation); ResultSet rs = ps.executeQuery();
            if (rs.next()) return mapperFacture(rs);
        } catch (SQLException e) { e.printStackTrace(); }
        return null;
    }

    @Override
    public List<Facture> listerToutes() {
        List<Facture> liste = new ArrayList<>();
        String sql = "SELECT * FROM factures";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) liste.add(mapperFacture(rs));
        } catch (SQLException e) { e.printStackTrace(); }
        return liste;
    }

    @Override
    public double calculerMontantTotal(int idReservation) {
        // Calcule (Prix des chambres * Nuits) + (Prix Extras * Quantité)
        String sql = "SELECT " +
                " (SELECT COALESCE(SUM(prix_applique * DATEDIFF(date_depart, date_arrivee)), 0) FROM reservation_chambres WHERE id_reservation = ?) + " +
                " (SELECT COALESCE(SUM(rs.quantite * ss.prix_service), 0) FROM reservation_services rs JOIN services_supplementaires ss ON rs.id_service = ss.id_service WHERE rs.id_reservation = ?) " +
                "AS total";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, idReservation); ps.setInt(2, idReservation);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) return rs.getDouble("total");
        } catch (SQLException e) { e.printStackTrace(); }
        return 0.0;
    }

    private Facture mapperFacture(ResultSet rs) throws SQLException {
        return new Facture(
                rs.getInt("id_facture"),
                rs.getInt("id_reservation"),
                rs.getDouble("montant_total"),
                rs.getTimestamp("date_facture").toLocalDateTime(),
                StatutFacture.valueOf(rs.getString("statut_facture"))
        );
    }
}