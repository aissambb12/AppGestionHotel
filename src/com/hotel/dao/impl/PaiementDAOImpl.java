package com.hotel.dao;

import com.hotel.model.Paiement;
import com.hotel.model.enumeration.ModePaiement;
import com.hotel.util.DatabaseConnection;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class PaiementDAOImpl implements PaiementDAO {

    Connection conn = DatabaseConnection.getConnection();

    @Override
    public boolean enregistrerPaiement(Paiement p) {
        String sql = "INSERT INTO paiements (id_facture, montant_paye, mode_paiement) VALUES (?, ?, ?)";
        try (PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, p.getIdFacture());
            ps.setDouble(2, p.getMontantPaye());
            ps.setString(3, p.getModePaiement().name());
            return ps.executeUpdate() > 0;
        } catch (SQLException e) { e.printStackTrace(); return false; }
    }

    @Override
    public List<Paiement> listerParFacture(int idFacture) {
        List<Paiement> liste = new ArrayList<>();
        String sql = "SELECT * FROM paiements WHERE id_facture = ?";
        try (PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, idFacture); ResultSet rs = ps.executeQuery();
            while (rs.next()) liste.add(new Paiement(
                    rs.getInt("id_paiement"),
                    rs.getInt("id_facture"),
                    rs.getDouble("montant_paye"),
                    rs.getTimestamp("date_paiement").toLocalDateTime(),
                    ModePaiement.valueOf(rs.getString("mode_paiement"))
            ));
        } catch (SQLException e) { e.printStackTrace(); }
        return liste;
    }

    @Override
    public double obtenirTotalPayePourFacture(int idFacture) {
        String sql = "SELECT SUM(montant_paye) FROM paiements WHERE id_facture = ?";
        try (PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, idFacture);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) return rs.getDouble(1);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return 0.0;
    }
}