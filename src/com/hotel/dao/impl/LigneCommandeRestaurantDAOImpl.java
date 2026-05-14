package com.hotel.dao.impl;

import com.hotel.dao.LigneCommandeRestaurantDAO;
import com.hotel.model.*;
import com.hotel.util.DatabaseConnection;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class LigneCommandeRestaurantDAOImpl implements LigneCommandeRestaurantDAO {

    @Override
    public void ajouter(LigneCommandeRestaurant ligne) {
        String sql = "INSERT INTO ligne_commande_restaurant(id_commande, id_plat, quantite, prix_unitaire) VALUES (?, ?, ?, ?)";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, ligne.getCommandeRestaurant().getIdCommande());
            ps.setInt(2, ligne.getPlat().getIdPlat());
            ps.setInt(3, ligne.getQuantite());
            ps.setDouble(4, ligne.getPrixUnitaire());

            ps.executeUpdate();

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void modifier(LigneCommandeRestaurant ligne) {
        String sql = "UPDATE ligne_commande_restaurant SET id_commande=?, id_plat=?, quantite=?, prix_unitaire=? WHERE id_ligne=?";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, ligne.getCommandeRestaurant().getIdCommande());
            ps.setInt(2, ligne.getPlat().getIdPlat());
            ps.setInt(3, ligne.getQuantite());
            ps.setDouble(4, ligne.getPrixUnitaire());
            ps.setInt(5, ligne.getIdLigneCommande());

            ps.executeUpdate();

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void supprimer(int idLigne) {
        String sql = "DELETE FROM ligne_commande_restaurant WHERE id_ligne=?";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, idLigne);
            ps.executeUpdate();

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public LigneCommandeRestaurant rechercherParId(int idLigne) {
        String sql = "SELECT * FROM ligne_commande_restaurant WHERE id_ligne=?";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, idLigne);

            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return construireLigne(rs);
                }
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return null;
    }

    @Override
    public List<LigneCommandeRestaurant> listerTous() {
        List<LigneCommandeRestaurant> lignes = new ArrayList<>();
        String sql = "SELECT * FROM ligne_commande_restaurant";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                lignes.add(construireLigne(rs));
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return lignes;
    }

    @Override
    public List<LigneCommandeRestaurant> listerParCommande(int idCommande) {
        List<LigneCommandeRestaurant> lignes = new ArrayList<>();
        String sql = "SELECT * FROM ligne_commande_restaurant WHERE id_commande=?";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, idCommande);

            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    lignes.add(construireLigne(rs));
                }
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return lignes;
    }

    @Override
    public double calculerTotalCommande(int idCommande) {
        String sql = "SELECT SUM(quantite * prix_unitaire) AS total FROM ligne_commande_restaurant WHERE id_commande=?";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, idCommande);

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

    @Override
    public double calculerTotalRestaurantParReservation(int idReservation) {
        String sql = """
                SELECT SUM(l.quantite * l.prix_unitaire) AS total
                FROM ligne_commande_restaurant l
                JOIN commande_restaurant c ON l.id_commande = c.id_commande
                WHERE c.id_reservation = ?
                """;

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, idReservation);

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

    private LigneCommandeRestaurant construireLigne(ResultSet rs) throws SQLException {
        LigneCommandeRestaurant ligne = new LigneCommandeRestaurant();

        CommandeRestaurant commande = new CommandeRestaurant();
        commande.setIdCommande(rs.getInt("id_commande"));

        Plat plat = new Plat();
        plat.setIdPlat(rs.getInt("id_plat"));

        ligne.setIdLigneCommande(rs.getInt("id_ligne"));
        ligne.setCommandeRestaurant(commande);
        ligne.setPlat(plat);
        ligne.setQuantite(rs.getInt("quantite"));
        ligne.setPrixUnitaire(rs.getDouble("prix_unitaire"));

        return ligne;
    }
}
