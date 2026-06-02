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


    double calculerMontantTotal(int idReservation);
}