package com.hotel.dao;

import com.hotel.model.Plat;
import java.util.List;

public interface PlatDAO {

    void ajouter(Plat plat);
    void modifier(Plat plat);
    void supprimer(int idPlat);
    Plat rechercherParId(int idPlat);
    List<Plat> listerTous();

    List<Plat> listerDisponibles();
}
