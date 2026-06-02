package com.hotel.dao;

import com.hotel.model.Paiement;
import java.util.List;

public interface PaiementDAO {
    boolean enregistrerPaiement(Paiement paiement);
    List<Paiement> listerParFacture(int idFacture);


    double obtenirTotalPayePourFacture(int idFacture);
}