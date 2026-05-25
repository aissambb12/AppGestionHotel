package com.hotel.dao;

import com.hotel.model.ServiceSupplementaire;
import com.hotel.model.enumeration.TypeService;
import com.hotel.util.DatabaseConnection;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class ServiceSupplementaireDAOImpl implements ServiceSupplementaireDAO {

    Connection conn = DatabaseConnection.getConnection();

    @Override
    public boolean ajouter(ServiceSupplementaire s) {
        String sql = "INSERT INTO services_supplementaires (nom_service, type_service, prix_service) VALUES (?, ?, ?)";
        try ( PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, s.getNomService());
            ps.setString(2, s.getTypeService().name());
            ps.setDouble(3, s.getPrixService());
            return ps.executeUpdate() > 0;
        } catch (SQLException e) { e.printStackTrace(); return false; }
    }

    @Override
    public boolean modifier(ServiceSupplementaire s) {
        String sql = "UPDATE services_supplementaires SET nom_service=?, type_service=?, prix_service=? WHERE id_service=?";
        try ( PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, s.getNomService());
            ps.setString(2, s.getTypeService().name());
            ps.setDouble(3, s.getPrixService());
            ps.setInt(4, s.getIdService());
            return ps.executeUpdate() > 0;
        } catch (SQLException e) { e.printStackTrace(); return false; }
    }

    @Override
    public boolean supprimer(int idService) {
        String sql = "DELETE FROM services_supplementaires WHERE id_service=?";
        try ( PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, idService); return ps.executeUpdate() > 0;
        } catch (SQLException e) { return false; }
    }

    @Override
    public ServiceSupplementaire trouverParId(int idService) {
        String sql = "SELECT * FROM services_supplementaires WHERE id_service = ?";
        try ( PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, idService);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) return mapperService(rs);
        } catch (SQLException e) { e.printStackTrace(); }
        return null;
    }

    @Override
    public List<ServiceSupplementaire> listerTous() {
        List<ServiceSupplementaire> liste = new ArrayList<>();
        try ( PreparedStatement ps = conn.prepareStatement("SELECT * FROM services_supplementaires"); ResultSet rs = ps.executeQuery()) {
            while (rs.next()) liste.add(mapperService(rs));
        } catch (SQLException e) { e.printStackTrace(); }
        return liste;
    }

    @Override
    public List<ServiceSupplementaire> listerParType(String type) {
        List<ServiceSupplementaire> liste = new ArrayList<>();
        try ( PreparedStatement ps = conn.prepareStatement("SELECT * FROM services_supplementaires WHERE type_service=?")) {
            ps.setString(1, type); ResultSet rs = ps.executeQuery();
            while (rs.next()) liste.add(mapperService(rs));
        } catch (SQLException e) { e.printStackTrace(); }
        return liste;
    }

    private ServiceSupplementaire mapperService(ResultSet rs) throws SQLException {
        return new ServiceSupplementaire(
                rs.getInt("id_service"),
                rs.getString("nom_service"),
                TypeService.valueOf(rs.getString("type_service")),
                rs.getDouble("prix_service"));
    }
}