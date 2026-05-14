package com.hotel.dao.impl;

import com.hotel.dao.FactureDAO;
import com.hotel.model.*;
import com.hotel.model.enumeration.StatutFacture;
import com.hotel.util.DatabaseConnection;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class FactureDAOImpl implements FactureDAO {

    @Override
    public void ajouter(Facture facture) {
        String sql = """
                INSERT INTO facture(
                    id_reservation,
                    date_facture,
                    montant_hebergement,
                    montant_restaurant,
                    montant_total,
                    statut
                ) VALUES (?, ?, ?, ?, ?, ?)
                """;

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, facture.getReservation().getIdReservation());
            ps.setDate(2, new java.sql.Date(facture.getDateFacture().getTime()));
            ps.setDouble(3, facture.getMontantHebergement());
            ps.setDouble(4, facture.getMontantRestaurant());
            ps.setDouble(5, facture.getMontantTotal());
            ps.setString(6, facture.getStatut().name());

            ps.executeUpdate();

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void modifier(Facture facture) {
        String sql = """
                UPDATE facture 
                SET id_reservation=?, date_facture=?, montant_hebergement=?, 
                    montant_restaurant=?, montant_total=?, statut=?
                WHERE id_facture=?
                """;

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, facture.getReservation().getIdReservation());
            ps.setDate(2, new java.sql.Date(facture.getDateFacture().getTime()));
            ps.setDouble(3, facture.getMontantHebergement());
            ps.setDouble(4, facture.getMontantRestaurant());
            ps.setDouble(5, facture.getMontantTotal());
            ps.setString(6, facture.getStatut().name());
            ps.setInt(7, facture.getIdFacture());

            ps.executeUpdate();

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void supprimer(int idFacture) {
        String sql = "DELETE FROM facture WHERE id_facture=?";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, idFacture);
            ps.executeUpdate();

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public Facture rechercherParId(int idFacture) {
        String sql = "SELECT * FROM facture WHERE id_facture=?";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, idFacture);

            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return construireFacture(rs);
                }
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return null;
    }

    @Override
    public List<Facture> listerTous() {
        List<Facture> factures = new ArrayList<>();
        String sql = "SELECT * FROM facture";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                factures.add(construireFacture(rs));
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return factures;
    }

    @Override
    public Facture rechercherParReservation(int idReservation) {
        String sql = "SELECT * FROM facture WHERE id_reservation=?";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, idReservation);

            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return construireFacture(rs);
                }
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return null;
    }

    @Override
    public void changerStatut(int idFacture, StatutFacture statut) {
        String sql = "UPDATE facture SET statut=? WHERE id_facture=?";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, statut.name());
            ps.setInt(2, idFacture);

            ps.executeUpdate();

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private Facture construireFacture(ResultSet rs) throws SQLException {
        Facture facture = new Facture();

        Reservation reservation = new Reservation();
        reservation.setIdReservation(rs.getInt("id_reservation"));

        facture.setIdFacture(rs.getInt("id_facture"));
        facture.setReservation(reservation);
        facture.setDateFacture(rs.getDate("date_facture"));
        facture.setMontantHebergement(rs.getDouble("montant_hebergement"));
        facture.setMontantRestaurant(rs.getDouble("montant_restaurant"));
        facture.setMontantTotal(rs.getDouble("montant_total"));
        facture.setStatut(StatutFacture.valueOf(rs.getString("statut")));

        return facture;
    }
}