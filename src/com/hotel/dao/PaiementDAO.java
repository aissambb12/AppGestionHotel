package com.hotel.dao;

import com.hotel.model.Paiement;
import java.util.List;

public interface PaiementDAO {
    boolean enregistrerPaiement(Paiement paiement);
    List<Paiement> listerParFacture(int idFacture);

    /**
     * Permet de savoir si la somme des paiements effectués couvre la totalité du montant de la facture.
     * Utile pour autoriser le changement de statut de la facture à 'PAYEE'.
     */
    double obtenirTotalPayePourFacture(int idFacture);
}