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
        return clientDAO.modifier(client);
    }

    public boolean supprimerClient(int idClient) {
        return clientDAO.supprimer(idClient);
    }

    public Client trouverClientParId(int idClient) {
        return clientDAO.trouverParId(idClient);
    }

    /**
     * Récupère un client par son CIN
     */
    public Client trouverClientParCin(String cin) {
        if (cin == null || cin.trim().isEmpty()) {
            return null;
        }
        return clientDAO.trouverParCin(cin.trim().toUpperCase());
    }

    public List<Client> obtenirTousLesClients() {
        return clientDAO.listerTous();
    }

    /**
     * Recherche un client de manière intelligente
     */
    public List<Client> rechercherClients(String motCle) {
        if (motCle == null || motCle.trim().isEmpty()) {
            return clientDAO.listerTous();
        }
        return clientDAO.rechercherParMotCle(motCle.trim());
    }
}