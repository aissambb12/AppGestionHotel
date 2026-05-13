package com.hotel.dao.impl;

import com.hotel.dao.PaiementDAO;
import com.hotel.model.*;
import com.hotel.util.DatabaseConnection;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class PaiementDAOImpl implements PaiementDAO {

    @Override
    public void ajouter(Paiement paiement) {
        String sql = "INSERT INTO paiement(id_facture, date_paiement, montant, mode_paiement) VALUES (?, ?, ?, ?)";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, paiement.getFacture().getIdFacture());
            ps.setDate(2, new java.sql.Date(paiement.getDatePaiement().getTime()));
            ps.setDouble(3, paiement.getMontant());
            ps.setString(4, paiement.getModePaiement().name());

            ps.executeUpdate();

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void modifier(Paiement paiement) {
        String sql = "UPDATE paiement SET id_facture=?, date_paiement=?, montant=?, mode_paiement=? WHERE id_paiement=?";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, paiement.getFacture().getIdFacture());
            ps.setDate(2, new java.sql.Date(paiement.getDatePaiement().getTime()));
            ps.setDouble(3, paiement.getMontant());
            ps.setString(4, paiement.getModePaiement().name());
            ps.setInt(5, paiement.getIdPaiement());

            ps.executeUpdate();

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void supprimer(int idPaiement) {
        String sql = "DELETE FROM paiement WHERE id_paiement=?";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, idPaiement);
            ps.executeUpdate();

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public Paiement rechercherParId(int idPaiement) {
        String sql = "SELECT * FROM paiement WHERE id_paiement=?";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, idPaiement);

            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return construirePaiement(rs);
                }
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return null;
    }

    @Override
    public List<Paiement> listerTous() {
        List<Paiement> paiements = new ArrayList<>();
        String sql = "SELECT * FROM paiement";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                paiements.add(construirePaiement(rs));
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return paiements;
    }

    @Override
    public List<Paiement> listerParFacture(int idFacture) {
        List<Paiement> paiements = new ArrayList<>();
        String sql = "SELECT * FROM paiement WHERE id_facture=?";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, idFacture);

            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    paiements.add(construirePaiement(rs));
                }
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return paiements;
    }

    @Override
    public double calculerTotalPaye(int idFacture) {
        String sql = "SELECT SUM(montant) AS total FROM paiement WHERE id_facture=?";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, idFacture);

            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return rs.getDouble("total");
                }
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return 0;
    }

    private Paiement construirePaiement(ResultSet rs) throws SQLException {
        Paiement paiement = new Paiement();

        Facture facture = new Facture();
        facture.setIdFacture(rs.getInt("id_facture"));

        paiement.setIdPaiement(rs.getInt("id_paiement"));
        paiement.setFacture(facture);
        paiement.setDatePaiement(rs.getDate("date_paiement"));
        paiement.setMontant(rs.getDouble("montant"));
        paiement.setModePaiement(ModePaiement.valueOf(rs.getString("mode_paiement")));

        return paiement;
    }
}