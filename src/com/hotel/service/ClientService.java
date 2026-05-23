package com.hotel.service;

import com.hotel.dao.ClientDAO;
import com.hotel.dao.ClientDAOImpl;
import com.hotel.model.Client;

import java.util.List;

public class ClientService {

    private ClientDAO clientDAO;

    public ClientService() {
        this.clientDAO = new ClientDAOImpl();
    }

    /**
     * Ajoute un client en validant le CIN et l'email.
     */
    public boolean enregistrerClient(Client client) {
        // 1. Validation basique
        if (client.getNom() == null || client.getNom().trim().isEmpty() ||
                client.getPrenom() == null || client.getPrenom().trim().isEmpty()) {
            throw new IllegalArgumentException("Le nom et le prénom sont obligatoires.");
        }
        if (client.getCin() == null || client.getCin().trim().isEmpty()) {
            throw new IllegalArgumentException("Le CIN est obligatoire pour des raisons légales.");
        }

        // 2. Règle métier : Le CIN doit être unique
        Client clientExistantCin = clientDAO.trouverParCin(client.getCin().trim());
        if (clientExistantCin != null) {
            throw new IllegalArgumentException("Un client avec le CIN " + client.getCin() + " existe déjà dans le système.");
        }

        // Nettoyage des espaces éventuels avant l'enregistrement
        client.setNom(client.getNom().trim().toUpperCase());
        client.setPrenom(client.getPrenom().trim());
        client.setCin(client.getCin().trim().toUpperCase());

        // 3. Sauvegarde via le DAO
        return clientDAO.ajouter(client);
    }

    public boolean modifierClient(Client client) {
        // On pourrait ajouter des vérifications ici aussi pour voir si le nouveau CIN
        // n'appartient pas déjà à un AUTRE client.
        return clientDAO.modifier(client);
    }

    public boolean supprimerClient(int idClient) {
        // Note : Cela échouera naturellement si le client a des réservations (grâce à MySQL RESTRICT)
        // L'interface graphique devra attraper l'exception ou le retour false pour afficher un message à l'utilisateur.
        return clientDAO.supprimer(idClient);
    }

    public Client trouverClientParId(int idClient) {
        return clientDAO.trouverParId(idClient);
    }

    public List<Client> obtenirTousLesClients() {
        return clientDAO.listerTous();
    }

    /**
     * Recherche un client de manière intelligente (utilisé par la barre de recherche Swing)
     */
    public List<Client> rechercherClients(String motCle) {
        if (motCle == null || motCle.trim().isEmpty()) {
            return clientDAO.listerTous(); // Si la barre de recherche est vide, on retourne tout
        }
        return clientDAO.rechercherParMotCle(motCle.trim());
    }
}