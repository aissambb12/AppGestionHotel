package com.hotel.dao.impl;

import com.hotel.dao.ReservationDAO;
import com.hotel.model.*;
import com.hotel.util.DatabaseConnection;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class ReservationDAOImpl implements ReservationDAO {

    @Override
    public void ajouter(Reservation reservation) {
        String sql = "INSERT INTO reservation(id_client, id_chambre, date_debut, date_fin, statut) VALUES (?, ?, ?, ?, ?)";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, reservation.getClient().getIdClient());
            ps.setInt(2, reservation.getChambre().getIdChambre());
            ps.setDate(3, new java.sql.Date(reservation.getDateDebut().getTime()));
            ps.setDate(4, new java.sql.Date(reservation.getDateFin().getTime()));
            ps.setString(5, reservation.getStatut().name());

            ps.executeUpdate();

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void modifier(Reservation reservation) {
        String sql = "UPDATE reservation SET id_client=?, id_chambre=?, date_debut=?, date_fin=?, statut=? WHERE id_reservation=?";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, reservation.getClient().getIdClient());
            ps.setInt(2, reservation.getChambre().getIdChambre());
            ps.setDate(3, new java.sql.Date(reservation.getDateDebut().getTime()));
            ps.setDate(4, new java.sql.Date(reservation.getDateFin().getTime()));
            ps.setString(5, reservation.getStatut().name());
            ps.setInt(6, reservation.getIdReservation());

            ps.executeUpdate();

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void supprimer(int idReservation) {
        String sql = "DELETE FROM reservation WHERE id_reservation=?";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, idReservation);
            ps.executeUpdate();

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public Reservation rechercherParId(int idReservation) {
        String sql = "SELECT * FROM reservation WHERE id_reservation=?";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, idReservation);

            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return construireReservation(rs);
                }
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return null;
    }

    @Override
    public List<Reservation> listerTous() {
        List<Reservation> reservations = new ArrayList<>();
        String sql = "SELECT * FROM reservation";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                reservations.add(construireReservation(rs));
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return reservations;
    }

    @Override
    public void changerStatut(int idReservation, StatutReservation statut) {
        String sql = "UPDATE reservation SET statut=? WHERE id_reservation=?";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, statut.name());
            ps.setInt(2, idReservation);

            ps.executeUpdate();

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public List<Reservation> listerReservationsEnCours() {
        List<Reservation> reservations = new ArrayList<>();
        String sql = "SELECT * FROM reservation WHERE statut='EN_COURS'";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                reservations.add(construireReservation(rs));
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return reservations;
    }

    private Reservation construireReservation(ResultSet rs) throws SQLException {
        Reservation reservation = new Reservation();

        Client client = new Client();
        client.setIdClient(rs.getInt("id_client"));

        Chambre chambre = new Chambre();
        chambre.setIdChambre(rs.getInt("id_chambre"));

        reservation.setIdReservation(rs.getInt("id_reservation"));
        reservation.setClient(client);
        reservation.setChambre(chambre);
        reservation.setDateDebut(rs.getDate("date_debut"));
        reservation.setDateFin(rs.getDate("date_fin"));
        reservation.setStatut(StatutReservation.valueOf(rs.getString("statut")));

        return reservation;
    }
}
