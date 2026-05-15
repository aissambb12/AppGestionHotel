package com.hotel.service;

import com.hotel.dao.FactureDAO;
import com.hotel.dao.PaiementDAO;
import com.hotel.dao.impl.FactureDAOImpl;
import com.hotel.dao.impl.PaiementDAOImpl;
import com.hotel.model.*;
import com.hotel.model.enumeration.ModePaiement;
import com.hotel.model.enumeration.StatutFacture;

import java.util.Date;
import java.util.List;

public class PaiementService {

    private PaiementDAO paiementDAO = new PaiementDAOImpl();
    private FactureDAO factureDAO = new FactureDAOImpl();

    public void enregistrerPaiement(int idFacture, double montant, ModePaiement modePaiement) {

        Facture facture = factureDAO.rechercherParId(idFacture);

        if (facture == null) {
            System.out.println("Facture introuvable.");
            return;
        }

        if (montant <= 0) {
            System.out.println("Montant invalide.");
            return;
        }

        Paiement paiement = new Paiement();
        paiement.setFacture(facture);
        paiement.setDatePaiement(new Date());
        paiement.setMontant(montant);
        paiement.setModePaiement(modePaiement);

        paiementDAO.ajouter(paiement);

        double totalPaye = paiementDAO.calculerTotalPaye(idFacture);

        if (totalPaye >= facture.getMontantTotal()) {
            factureDAO.changerStatut(idFacture, StatutFacture.PAYEE);
        } else {
            factureDAO.changerStatut(idFacture, StatutFacture.PARTIELLEMENT_PAYEE);
        }

        System.out.println("Paiement enregistré avec succès.");
    }

    public void modifierPaiement(Paiement paiement) {
        paiementDAO.modifier(paiement);
    }

    public void supprimerPaiement(int idPaiement) {
        paiementDAO.supprimer(idPaiement);
    }

    public Paiement rechercherPaiement(int idPaiement) {
        return paiementDAO.rechercherParId(idPaiement);
    }

    public List<Paiement> listerPaiements() {
        return paiementDAO.listerTous();
    }

    public List<Paiement> listerPaiementsParFacture(int idFacture) {
        return paiementDAO.listerParFacture(idFacture);
    }

    public double calculerTotalPaye(int idFacture) {
        return paiementDAO.calculerTotalPaye(idFacture);
    }
}