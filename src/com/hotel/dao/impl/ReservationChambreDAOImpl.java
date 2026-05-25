package com.hotel.dao;

import com.hotel.model.ReservationChambre;
import com.hotel.util.DatabaseConnection;

import java.sql.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class ReservationChambreDAOImpl implements ReservationChambreDAO {

    Connection conn = DatabaseConnection.getConnection();

    @Override
    public boolean affecterChambre(ReservationChambre rc) {
        String sql = "INSERT INTO reservation_chambres (id_reservation, id_chambre, date_arrivee, date_depart, prix_applique) VALUES (?, ?, ?, ?, ?)";
        try (
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, rc.getIdReservation());
            ps.setInt(2, rc.getIdChambre());
            ps.setDate(3, Date.valueOf(rc.getDateArrivee()));
            ps.setDate(4, Date.valueOf(rc.getDateDepart()));
            ps.setDouble(5, rc.getPrixApplique());
            return ps.executeUpdate() > 0;
        } catch (SQLException e) { e.printStackTrace(); return false; }
    }

    @Override
    public boolean libererChambre(int idReservation, int idChambre) {
        String sql = "DELETE FROM reservation_chambres WHERE id_reservation = ? AND id_chambre = ?";
        try (
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, idReservation);
            ps.setInt(2, idChambre);
            return ps.executeUpdate() > 0;
        } catch (SQLException e) { e.printStackTrace(); return false; }
    }

    @Override
    public List<ReservationChambre> listerParReservation(int idReservation) {
        List<ReservationChambre> liste = new ArrayList<>();
        String sql = "SELECT * FROM reservation_chambres WHERE id_reservation = ?";
        try (
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, idReservation);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                liste.add(new ReservationChambre(
                        rs.getInt("id_reservation"),
                        rs.getInt("id_chambre"),
                        rs.getDate("date_arrivee").toLocalDate(),
                        rs.getDate("date_depart").toLocalDate(),
                        rs.getDouble("prix_applique")
                ));
            }
        } catch (SQLException e) { e.printStackTrace(); }
        return liste;
    }

    @Override
    public boolean estChambreOccupee(int idChambre, LocalDate arrivee, LocalDate depart) {
        String sql = "SELECT COUNT(*) FROM reservation_chambres rc JOIN reservations r ON rc.id_reservation = r.id_reservation " +
                "WHERE rc.id_chambre = ? AND r.statut_reservation != 'ANNULEE' AND (rc.date_arrivee < ? AND rc.date_depart > ?)";
        try (
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, idChambre);
            ps.setDate(2, Date.valueOf(depart));
            ps.setDate(3, Date.valueOf(arrivee));
            ResultSet rs = ps.executeQuery();
            if (rs.next()) return rs.getInt(1) > 0;
        } catch (SQLException e) { e.printStackTrace(); }
        return false;
    }
}