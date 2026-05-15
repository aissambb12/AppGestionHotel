package com.hotel.service;

import com.hotel.dao.CommandeRestaurantDAO;
import com.hotel.dao.LigneCommandeRestaurantDAO;
import com.hotel.dao.PlatDAO;
import com.hotel.dao.ReservationDAO;
import com.hotel.dao.impl.CommandeRestaurantDAOImpl;
import com.hotel.dao.impl.LigneCommandeRestaurantDAOImpl;
import com.hotel.dao.impl.PlatDAOImpl;
import com.hotel.dao.impl.ReservationDAOImpl;
import com.hotel.model.*;
import com.hotel.model.enumeration.StatutCommandeRestaurant;
import com.hotel.model.enumeration.StatutReservation;
import com.hotel.util.ValidationUtil;

import java.util.Date;
import java.util.List;

public class RestaurantService {

    private PlatDAO platDAO = new PlatDAOImpl();
    private ReservationDAO reservationDAO = new ReservationDAOImpl();
    private CommandeRestaurantDAO commandeDAO = new CommandeRestaurantDAOImpl();
    private LigneCommandeRestaurantDAO ligneDAO = new LigneCommandeRestaurantDAOImpl();

    public void ajouterPlat(Plat plat) {

        if (ValidationUtil.estVide(plat.getNom())) {
            System.out.println("Nom du plat obligatoire.");
            return;
        }

        if (!ValidationUtil.estPrixValide(plat.getPrix())) {
            System.out.println("Prix du plat invalide.");
            return;
        }

        platDAO.ajouter(plat);
        System.out.println("Plat ajouté avec succès.");
    }

    public void modifierPlat(Plat plat) {

        if (plat.getIdPlat() <= 0) {
            System.out.println("ID plat invalide.");
            return;
        }

        platDAO.modifier(plat);
        System.out.println("Plat modifié avec succès.");
    }

    public void supprimerPlat(int idPlat) {

        if (idPlat <= 0) {
            System.out.println("ID plat invalide.");
            return;
        }

        platDAO.supprimer(idPlat);
        System.out.println("Plat supprimé avec succès.");
    }

    public Plat rechercherPlat(int idPlat) {
        return platDAO.rechercherParId(idPlat);
    }

    public List<Plat> listerPlats() {
        return platDAO.listerTous();
    }

    public List<Plat> listerPlatsDisponibles() {
        return platDAO.listerDisponibles();
    }

    public void creerCommandeRestaurant(int idReservation) {

        Reservation reservation = reservationDAO.rechercherParId(idReservation);

        if (reservation == null) {
            System.out.println("Réservation introuvable.");
            return;
        }

        if (reservation.getStatut() != StatutReservation.EN_COURS) {
            System.out.println("La commande restaurant est possible seulement pour une réservation en cours.");
            return;
        }

        CommandeRestaurant commande = new CommandeRestaurant();
        commande.setReservation(reservation);
        commande.setDateCommande(new Date());
        commande.setStatut(StatutCommandeRestaurant.EN_ATTENTE);

        commandeDAO.ajouter(commande);

        System.out.println("Commande restaurant créée.");
    }

    public void ajouterPlatCommande(int idCommande, int idPlat, int quantite) {

        if (!ValidationUtil.estQuantiteValide(quantite)) {
            System.out.println("Quantité invalide.");
            return;
        }

        CommandeRestaurant commande = commandeDAO.rechercherParId(idCommande);

        if (commande == null) {
            System.out.println("Commande introuvable.");
            return;
        }

        Plat plat = platDAO.rechercherParId(idPlat);

        if (plat == null) {
            System.out.println("Plat introuvable.");
            return;
        }

        if (!plat.isDisponible()) {
            System.out.println("Plat indisponible.");
            return;
        }

        LigneCommandeRestaurant ligne = new LigneCommandeRestaurant();
        ligne.setCommandeRestaurant(commande);
        ligne.setPlat(plat);
        ligne.setQuantite(quantite);
        ligne.setPrixUnitaire(plat.getPrix());

        ligneDAO.ajouter(ligne);

        System.out.println("Plat ajouté à la commande.");
    }

    public void changerStatutCommande(int idCommande, StatutCommandeRestaurant statut) {
        commandeDAO.changerStatut(idCommande, statut);
    }

    public List<CommandeRestaurant> listerCommandes() {
        return commandeDAO.listerTous();
    }

    public List<CommandeRestaurant> listerCommandesParReservation(int idReservation) {
        return commandeDAO.listerParReservation(idReservation);
    }

    public List<LigneCommandeRestaurant> listerLignesParCommande(int idCommande) {
        return ligneDAO.listerParCommande(idCommande);
    }

    public double calculerTotalCommande(int idCommande) {
        return ligneDAO.calculerTotalCommande(idCommande);
    }

    public double calculerTotalRestaurantParReservation(int idReservation) {
        return ligneDAO.calculerTotalRestaurantParReservation(idReservation);
    }
}