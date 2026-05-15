package com.hotel.service;

import com.hotel.dao.ChambreDAO;
import com.hotel.dao.impl.ChambreDAOImpl;
import com.hotel.model.Chambre;
import com.hotel.model.enumeration.StatutChambre;
import com.hotel.util.ValidationUtil;

import java.util.List;

public class ChambreService {

    private ChambreDAO chambreDAO = new ChambreDAOImpl();

    public void ajouterChambre(Chambre chambre) {

        if (ValidationUtil.estVide(chambre.getNumero())) {
            System.out.println("Numéro de chambre obligatoire.");
            return;
        }

        if (ValidationUtil.estVide(chambre.getType())) {
            System.out.println("Type de chambre obligatoire.");
            return;
        }

        if (!ValidationUtil.estPrixValide(chambre.getPrixParNuit())) {
            System.out.println("Prix invalide.");
            return;
        }

        if (chambre.getStatut() == null) {
            chambre.setStatut(StatutChambre.DISPONIBLE);
        }

        chambreDAO.ajouter(chambre);
        System.out.println("Chambre ajoutée avec succès.");
    }

    public void modifierChambre(Chambre chambre) {

        if (chambre.getIdChambre() <= 0) {
            System.out.println("ID chambre invalide.");
            return;
        }

        chambreDAO.modifier(chambre);
        System.out.println("Chambre modifiée avec succès.");
    }

    public void supprimerChambre(int idChambre) {

        if (idChambre <= 0) {
            System.out.println("ID chambre invalide.");
            return;
        }

        chambreDAO.supprimer(idChambre);
        System.out.println("Chambre supprimée avec succès.");
    }

    public Chambre rechercherChambre(int idChambre) {
        return chambreDAO.rechercherParId(idChambre);
    }

    public List<Chambre> listerChambres() {
        return chambreDAO.listerTous();
    }

    public List<Chambre> listerChambresDisponibles() {
        return chambreDAO.listerDisponibles();
    }

    public void changerStatut(int idChambre, StatutChambre statut) {
        chambreDAO.changerStatut(idChambre, statut);
    }
}