package com.hotel.dao;

import com.hotel.model.ReservationServices;
import com.hotel.util.DatabaseConnection;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class ReservationServicesDAOImpl implements ReservationServicesDAO {

    Connection conn = DatabaseConnection.getConnection();

    @Override
    public boolean enregistrerConsommation(ReservationServices rs) {
        String sql = "INSERT INTO reservation_services (id_reservation, id_service, quantite, date_consommation) VALUES (?, ?, ?, ?)";
        try (PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, rs.getIdReservation());
            ps.setInt(2, rs.getIdService());
            ps.setInt(3, rs.getQuantite());
            ps.setDate(4, Date.valueOf(rs.getDateConsommation()));
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    @Override
    public List<ReservationServices> listerConsommationsParReservation(int idReservation) {
        List<ReservationServices> liste = new ArrayList<>();
        String sql = "SELECT * FROM reservation_services WHERE id_reservation = ?";

        try (PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, idReservation);

            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    // Utilisation de votre constructeur exact avec les bons types !
                    ReservationServices extra = new ReservationServices(
                            rs.getInt("id_consommation"),
                            rs.getInt("id_reservation"),
                            rs.getInt("id_service"),
                            rs.getInt("quantite"),
                            rs.getDate("date_consommation").toLocalDate()
                    );
                    liste.add(extra);
                }
            }
        } catch (SQLException e) {
            System.err.println("Erreur lors de la récupération des services consommés : " + e.getMessage());
        }
        return liste;
    }

    @Override
    public boolean supprimerConsommation(int idConsommation) {
        String sql = "DELETE FROM reservation_services WHERE id_consommation=?";
        try (PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, idConsommation);
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            return false;
        }
    }

    @Override
    public List<ReservationServices> listerParReservation(int idReservation) {
        List<ReservationServices> liste = new ArrayList<>();
        String sql = "SELECT * FROM reservation_services WHERE id_reservation = ?";
        try (PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, idReservation);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) liste.add(new ReservationServices(
                    rs.getInt("id_consommation"),
                    rs.getInt("id_reservation"),
                    rs.getInt("id_service"),
                    rs.getInt("quantite"),
                    rs.getDate("date_consommation").toLocalDate()
            ));
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return liste;
    }
}