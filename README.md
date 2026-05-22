# AppGestionHotel

Application de bureau (Java Swing + MySQL) pour la gestion d'un hôtel :
chambres, clients, réservations, check-in/check-out, maintenance,
restauration et facturation.

## Prérequis
- JDK 17 ou supérieur
- MySQL 5.7+ (ou MariaDB)
- Le pilote JDBC est fourni dans `lib/mysql-connector-java-5.1.29.jar`

## Installation de la base
1. Lancer MySQL.
2. Exécuter `database/schema.sql` (crée la base `hotel_db` et les tables).
3. Exécuter `database/data.sql` (données de démonstration).
4. Vérifier les identifiants de connexion dans
   `src/com/hotel/util/DatabaseConnection.java` (URL, USER, PASSWORD).

## Lancement
- Sous IntelliJ : ouvrir le projet, lancer la classe `Main`.
- En ligne de commande :