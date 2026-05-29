package com.hotel.dao;

import com.hotel.model.Facture;

import java.time.LocalDate;
import java.util.List;

public interface FactureDAO {
    boolean creerFacture(Facture facture);
    boolean modifierStatut(int idFacture, String statut);// 'EN_ATTENTE', 'PAYEE', 'ANNULEE'
    boolean entrerMontant(Facture facture , double montant);
    Facture trouverParId(int idFacture);
    double calculerChiffreAffaires(LocalDate dateDebut, LocalDate dateFin);
    Facture trouverParReservation(int idReservation); // Une seule facture officielle par réservation
    List<Facture> listerParStatut(String statut);
    List<Facture> listerToutes();

    /**
     * Calcule automatiquement le montant total cumulé (Prix des chambres retenues * nombre de nuits
     * + somme des services supplémentaires consommés).
     * Cette méthode sera appelée pour initialiser le montant lors du Check-out.
     */
    double calculerMontantTotal(int idReservation);
}