package com.hotel.dao;

import com.hotel.model.Chambre;
import com.hotel.model.enumeration.StatutChambre;
import com.hotel.util.DatabaseConnection;

import java.sql.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class ChambreDAOImpl implements ChambreDAO {

    @Override
    public boolean ajouter(Chambre chambre) {
        String sql = "INSERT INTO chambres (numero, categorie, prix_unitaire, statut) VALUES (?, ?, ?, ?)";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, chambre.getNumero());
            ps.setString(2, chambre.getCategorie());
            ps.setDouble(3, chambre.getPrixUnitaire());
            ps.setString(4, chambre.getStatutChambre().name());
            return ps.executeUpdate() > 0;
        } catch (SQLException e) { e.printStackTrace(); return false; }
    }

    @Override
    public boolean modifier(Chambre chambre) {
        String sql = "UPDATE chambres SET numero=?, categorie=?, prix_unitaire=?, statut=? WHERE id_chambre=?";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, chambre.getNumero());
            ps.setString(2, chambre.getCategorie());
            ps.setDouble(3, chambre.getPrixUnitaire());
            ps.setString(4, chambre.getStatutChambre().name());
            ps.setInt(5, chambre.getIdChambre());
            return ps.executeUpdate() > 0;
        } catch (SQLException e) { e.printStackTrace(); return false; }
    }

    @Override
    public boolean modifierStatut(int idChambre, String statut) {
        String sql = "UPDATE chambres SET statut=? WHERE id_chambre=?";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, statut);
            ps.setInt(2, idChambre);
            return ps.executeUpdate() > 0;
        } catch (SQLException e) { e.printStackTrace(); return false; }
    }

    @Override
    public Chambre trouverParId(int idChambre) {
        String sql = "SELECT * FROM chambres WHERE id_chambre = ?";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, idChambre);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) return mapperChambre(rs);
        } catch (SQLException e) { e.printStackTrace(); }
        return null;
    }

    @Override
    public Chambre trouverParNumero(String numero) {
        String sql = "SELECT * FROM chambres WHERE numero = ?";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, numero);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) return mapperChambre(rs);
        } catch (SQLException e) { e.printStackTrace(); }
        return null;
    }

    @Override
    public List<Chambre> listerToutes() {
        List<Chambre> liste = new ArrayList<>();
        String sql = "SELECT * FROM chambres";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) liste.add(mapperChambre(rs));
        } catch (SQLException e) { e.printStackTrace(); }
        return liste;
    }

    @Override
    public List<Chambre> listerParStatut(String statut) {
        List<Chambre> liste = new ArrayList<>();
        String sql = "SELECT * FROM chambres WHERE statut = ?";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, statut);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) liste.add(mapperChambre(rs));
        } catch (SQLException e) { e.printStackTrace(); }
        return liste;
    }

    @Override
    public List<Chambre> listerChambresDisponibles(LocalDate arrivee, LocalDate depart, String categorie) {
        List<Chambre> liste = new ArrayList<>();
        // Requête avancée : Vérifie que la chambre n'est pas déjà dans 'reservation_chambres' pour ces dates
        String sql = "SELECT * FROM chambres c WHERE c.categorie = ? AND c.statut = 'DISPONIBLE' " +
                "AND c.id_chambre NOT IN (SELECT rc.id_chambre FROM reservation_chambres rc " +
                "JOIN reservations r ON rc.id_reservation = r.id_reservation " +
                "WHERE r.statut_reservation != 'ANNULEE' AND (rc.date_arrivee < ? AND rc.date_depart > ?))";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, categorie);
            ps.setDate(2, java.sql.Date.valueOf(depart));
            ps.setDate(3, java.sql.Date.valueOf(arrivee));
            ResultSet rs = ps.executeQuery();
            while (rs.next()) liste.add(mapperChambre(rs));
        } catch (SQLException e) { e.printStackTrace(); }
        return liste;
    }

    private Chambre mapperChambre(ResultSet rs) throws SQLException {
        return new Chambre(
                rs.getInt("id_chambre"),
                rs.getString("numero"),
                rs.getString("categorie"),
                rs.getDouble("prix_unitaire"),
                StatutChambre.valueOf(rs.getString("statut"))
        );
    }
}