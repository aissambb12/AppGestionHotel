package com.hotel.dao;

import com.hotel.model.ServiceSupplementaire;
import java.util.List;

public interface ServiceSupplementaireDAO {
    boolean ajouter(ServiceSupplementaire service);
    boolean modifier(ServiceSupplementaire service);
    boolean supprimer(int idService);
    ServiceSupplementaire trouverParId(int idService);
    List<ServiceSupplementaire> listerTous();
    List<ServiceSupplementaire> listerParType(String type); // 'RESTAURANT', 'PARKING', 'SPA', 'AUTRE'
}