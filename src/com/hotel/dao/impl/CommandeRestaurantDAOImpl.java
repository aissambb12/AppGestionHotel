package com.hotel.dao.impl;

import com.hotel.dao.CommandeRestaurantDAO;
import com.hotel.model.*;
import com.hotel.util.DatabaseConnection;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class CommandeRestaurantDAOImpl implements CommandeRestaurantDAO {

    @Override
    public void ajouter(CommandeRestaurant commande) {
        String sql = "INSERT INTO commande_restaurant(id_reservation, date_commande, statut) VALUES (?, ?, ?)";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, commande.getReservation().getIdReservation());
            ps.setDate(2, new java.sql.Date(commande.getDateCommande().getTime()));
            ps.setString(3, commande.getStatut().name());

            ps.executeUpdate();

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void modifier(CommandeRestaurant commande) {
        String sql = "UPDATE commande_restaurant SET id_reservation=?, date_commande=?, statut=? WHERE id_commande=?";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, commande.getReservation().getIdReservation());
            ps.setDate(2, new java.sql.Date(commande.getDateCommande().getTime()));
            ps.setString(3, commande.getStatut().name());
            ps.setInt(4, commande.getIdCommande());

            ps.executeUpdate();

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void supprimer(int idCommande) {
        String sql = "DELETE FROM commande_restaurant WHERE id_commande=?";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, idCommande);
            ps.executeUpdate();

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public CommandeRestaurant rechercherParId(int idCommande) {
        String sql = "SELECT * FROM commande_restaurant WHERE id_commande=?";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, idCommande);

            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return construireCommande(rs);
                }
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return null;
    }

    @Override
    public List<CommandeRestaurant> listerTous() {
        List<CommandeRestaurant> commandes = new ArrayList<>();
        String sql = "SELECT * FROM commande_restaurant";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                commandes.add(construireCommande(rs));
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return commandes;
    }

    @Override
    public List<CommandeRestaurant> listerParReservation(int idReservation) {
        List<CommandeRestaurant> commandes = new ArrayList<>();
        String sql = "SELECT * FROM commande_restaurant WHERE id_reservation=?";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, idReservation);

            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    commandes.add(construireCommande(rs));
                }
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return commandes;
    }

    @Override
    public void changerStatut(int idCommande, StatutCommandeRestaurant statut) {
        String sql = "UPDATE commande_restaurant SET statut=? WHERE id_commande=?";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, statut.name());
            ps.setInt(2, idCommande);

            ps.executeUpdate();

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private CommandeRestaurant construireCommande(ResultSet rs) throws SQLException {
        CommandeRestaurant commande = new CommandeRestaurant();

        Reservation reservation = new Reservation();
        reservation.setIdReservation(rs.getInt("id_reservation"));

        commande.setIdCommande(rs.getInt("id_commande"));
        commande.setReservation(reservation);
        commande.setDateCommande(rs.getDate("date_commande"));
        commande.setStatut(StatutCommandeRestaurant.valueOf(rs.getString("statut")));

        return commande;
    }
}