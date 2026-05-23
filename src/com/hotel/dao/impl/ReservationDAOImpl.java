package com.hotel.dao;

import com.hotel.model.Reservation;
import com.hotel.model.enumeration.StatutReservation;
import com.hotel.util.DatabaseConnection;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class ReservationDAOImpl implements ReservationDAO {

    @Override
    public int ajouter(Reservation r) {
        // Clé générée par MySQL est retournée
        String sql = "INSERT INTO reservations (id_client, id_utilisateur, statut_reservation) VALUES (?, ?, ?)";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            ps.setInt(1, r.getIdClient());
            ps.setInt(2, r.getIdUtilisateur());
            ps.setString(3, r.getStatut().name());

            int affectedRows = ps.executeUpdate();
            if (affectedRows > 0) {
                try (ResultSet generatedKeys = ps.getGeneratedKeys()) {
                    if (generatedKeys.next()) {
                        return generatedKeys.getInt(1); // Retourne l'id_reservation généré
                    }
                }
            }
        } catch (SQLException e) { e.printStackTrace(); }
        return -1; // Échec
    }

    @Override
    public boolean modifierStatut(int idReservation, String statut) {
        String sql = "UPDATE reservations SET statut_reservation = ? WHERE id_reservation = ?";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, statut);
            ps.setInt(2, idReservation);
            return ps.executeUpdate() > 0;
        } catch (SQLException e) { e.printStackTrace(); return false; }
    }

    @Override
    public Reservation trouverParId(int idReservation) {
        String sql = "SELECT * FROM reservations WHERE id_reservation = ?";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, idReservation);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) return mapperReservation(rs);
        } catch (SQLException e) { e.printStackTrace(); }
        return null;
    }

    @Override
    public List<Reservation> listerToutes() { return executerSelect("SELECT * FROM reservations"); }

    @Override
    public List<Reservation> listerParClient(int idClient) {
        String sql = "SELECT * FROM reservations WHERE id_client = ?";
        return executerSelectParam(sql, idClient);
    }

    @Override
    public List<Reservation> listerParStatut(String statut) {
        List<Reservation> liste = new ArrayList<>();
        String sql = "SELECT * FROM reservations WHERE statut_reservation = ?";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, statut);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) liste.add(mapperReservation(rs));
        } catch (SQLException e) { e.printStackTrace(); }
        return liste;
    }

    private List<Reservation> executerSelect(String sql) {
        List<Reservation> liste = new ArrayList<>();
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) liste.add(mapperReservation(rs));
        } catch (SQLException e) { e.printStackTrace(); }
        return liste;
    }

    private List<Reservation> executerSelectParam(String sql, int param) {
        List<Reservation> liste = new ArrayList<>();
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, param);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) liste.add(mapperReservation(rs));
        } catch (SQLException e) { e.printStackTrace(); }
        return liste;
    }

    private Reservation mapperReservation(ResultSet rs) throws SQLException {
        return new Reservation(
                rs.getInt("id_reservation"),
                rs.getInt("id_client"),
                rs.getInt("id_utilisateur"),
                rs.getTimestamp("date_creation").toLocalDateTime(),
                StatutReservation.valueOf(rs.getString("statut_reservation"))
        );
    }
}