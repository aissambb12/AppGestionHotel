import com.hotel.dao.ClientDAO;
import com.hotel.dao.impl.ClientDAOImpl;
import com.hotel.model.Client;

import java.util.List;

public class Main {

    public static void main(String[] args) {

        ClientDAO clientDAO = new ClientDAOImpl();

        // 1. Créer un nouveau client
        Client client = new Client();
        client.setNom("EL FASSI");
        client.setPrenom("Omar");
        client.setCin("JK123456");
        client.setTelephone("0655555555");
        client.setEmail("omar.elfassi@gmail.com");

        // 2. Tester l'ajout avec vérification du CIN
        System.out.println("=== TEST AJOUT CLIENT ===");
        clientDAO.ajouter(client);


        // 4. Lister tous les clients
        System.out.println("\n=== LISTE DES CLIENTS ===");
        List<Client> clients = clientDAO.listerTous();

        for (Client c : clients) {
            System.out.println(
                    c.getIdClient() + " | " +
                            c.getNom() + " | " +
                            c.getPrenom() + " | " +
                            c.getCin() + " | " +
                            c.getTelephone() + " | " +
                            c.getEmail()
            );
        }

        // 5. Rechercher un client par ID
        System.out.println("\n=== RECHERCHE PAR ID ===");
        Client clientTrouve = clientDAO.rechercherParId(1);

        if (clientTrouve != null) {
            System.out.println("Client trouvé : " + clientTrouve.getNom() + " " + clientTrouve.getPrenom());
        } else {
            System.out.println("Aucun client trouvé avec cet ID.");
        }

        // 6. Modifier un client
        System.out.println("\n=== TEST MODIFICATION ===");

        Client clientAModifier = clientDAO.rechercherParId(1);

        if (clientAModifier != null) {
            clientAModifier.setTelephone("0699999999");
            clientAModifier.setEmail("nouveau.email@gmail.com");

            clientDAO.modifier(clientAModifier);

            System.out.println("Client modifié avec succès.");
        } else {
            System.out.println("Impossible de modifier : client introuvable.");
        }

        // 7. Supprimer un client
        // Attention : ne supprime pas un client utilisé dans une réservation.
        System.out.println("\n=== TEST SUPPRESSION ===");

        int idClientASupprimer = 4;

        Client clientASupprimer = clientDAO.rechercherParId(idClientASupprimer);

        if (clientASupprimer != null) {
            clientDAO.supprimer(idClientASupprimer);
            System.out.println("Client supprimé avec succès.");
        } else {
            System.out.println("Aucun client à supprimer avec ID = " + idClientASupprimer);
        }
    }
}