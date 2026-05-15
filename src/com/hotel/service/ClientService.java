package com.hotel.service;

import com.hotel.dao.ClientDAO;
import com.hotel.dao.impl.ClientDAOImpl;
import com.hotel.model.Client;
import com.hotel.util.ValidationUtil;

import java.util.List;

public class ClientService {

    private ClientDAO clientDAO = new ClientDAOImpl();

    public void ajouterClient(Client client) {

        if (!ValidationUtil.estNomValide(client.getNom())) {
            System.out.println("Nom invalide.");
            return;
        }

        if (!ValidationUtil.estNomValide(client.getPrenom())) {
            System.out.println("Prénom invalide.");
            return;
        }

        if (!ValidationUtil.estCinValide(client.getCin())) {
            System.out.println("CIN invalide.");
            return;
        }

        if (!ValidationUtil.estTelephoneValide(client.getTelephone())) {
            System.out.println("Téléphone invalide.");
            return;
        }

        if (!ValidationUtil.estEmailValide(client.getEmail())) {
            System.out.println("Email invalide.");
            return;
        }

        clientDAO.ajouter(client);
        System.out.println("Client ajouté avec succès.");
    }

    public void modifierClient(Client client) {

        if (client.getIdClient() <= 0) {
            System.out.println("ID client invalide.");
            return;
        }

        clientDAO.modifier(client);
        System.out.println("Client modifié avec succès.");
    }

    public void supprimerClient(int idClient) {

        if (idClient <= 0) {
            System.out.println("ID client invalide.");
            return;
        }

        clientDAO.supprimer(idClient);
        System.out.println("Client supprimé avec succès.");
    }

    public Client rechercherClient(int idClient) {
        return clientDAO.rechercherParId(idClient);
    }

    public List<Client> listerClients() {
        return clientDAO.listerTous();
    }
}