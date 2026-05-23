package com.hotel.dao;

import com.hotel.model.Facture;
import java.util.List;

public interface FactureDAO {
    boolean creerFacture(Facture facture);
    boolean modifierStatut(int idFacture, String statut); // 'EN_ATTENTE', 'PAYEE', 'ANNULEE'
    Facture trouverParId(int idFacture);
    Facture trouverParReservation(int idReservation); // Une seule facture officielle par réservation
    List<Facture> listerToutes();

    /**
     * Calcule automatiquement le montant total cumulé (Prix des chambres retenues * nombre de nuits
     * + somme des services supplémentaires consommés).
     * Cette méthode sera appelée pour initialiser le montant lors du Check-out.
     */
    double calculerMontantTotal(int idReservation);
}