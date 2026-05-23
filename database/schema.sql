DROP DATABASE IF EXISTS hotel_db;
CREATE DATABASE hotel_db
CHARACTER SET utf8mb4
COLLATE utf8mb4_unicode_ci;

USE hotel_db;

-- 1. Table Personnel / Gestion des utilisateurs et de la sécurité
CREATE TABLE utilisateurs (
                              id_utilisateur INT AUTO_INCREMENT PRIMARY KEY,
                              nom VARCHAR(50) NOT NULL,
                              prenom VARCHAR(50) NOT NULL,
                              email VARCHAR(100) UNIQUE NOT NULL,
                              mot_de_passe VARCHAR(255) NOT NULL, -- Stockage exclusif de hachages sécurisés (ex: BCrypt)
                              role ENUM('ADMIN', 'RECEPTIONNISTE', 'MAINTENANCE') NOT NULL,
                              statut ENUM('ACTIF', 'INACTIF') DEFAULT 'ACTIF'
) ENGINE=InnoDB;

-- 2. Table Client
CREATE TABLE clients (
                         id_client INT AUTO_INCREMENT PRIMARY KEY,
                         nom VARCHAR(50) NOT NULL,
                         prenom VARCHAR(50) NOT NULL,
                         cin VARCHAR(50) UNIQUE NOT NULL ,
                         email VARCHAR(100) UNIQUE NOT NULL,
                         telephone VARCHAR(20)
) ENGINE=InnoDB;

-- 3. Table Chambre
CREATE TABLE chambres (
                          id_chambre INT AUTO_INCREMENT PRIMARY KEY,
                          numero VARCHAR(10) UNIQUE NOT NULL,
                          categorie ENUM('SIMPLE', 'DOUBLE', 'SUITE') NOT NULL,
                          prix_unitaire DECIMAL(10, 2) NOT NULL,
                          statut ENUM('DISPONIBLE', 'OCCUPEE', 'MAINTENANCE') DEFAULT 'DISPONIBLE',
                          CONSTRAINT chk_prix_chambre CHECK (prix_unitaire >= 0)
) ENGINE=InnoDB;

-- 4. Table Maintenance (Cruciale : gère l'historique et les blocages par date pour entretien)
CREATE TABLE maintenances (
                              id_maintenance INT AUTO_INCREMENT PRIMARY KEY,
                              id_chambre INT NOT NULL,
                              date_debut DATE NOT NULL,
                              date_fin DATE NOT NULL,
                              description TEXT,
                              statut_maintenance ENUM('EN_COURS', 'TERMINEE') DEFAULT 'EN_COURS',
                              FOREIGN KEY (id_chambre) REFERENCES chambres(id_chambre) ON DELETE RESTRICT,
                              CONSTRAINT chk_dates_maint CHECK (date_fin >= date_debut)
) ENGINE=InnoDB;

-- 5. Table Reservation (En-tête de la réservation)
CREATE TABLE reservations (
                              id_reservation INT AUTO_INCREMENT PRIMARY KEY,
                              id_client INT NOT NULL,
                              id_utilisateur INT NOT NULL, -- Traçabilité : quel réceptionniste a créé la réservation
                              date_creation TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
                              statut_reservation ENUM('CONFIRMEE', 'ANNULEE', 'TERMINEE') DEFAULT 'CONFIRMEE',
                              FOREIGN KEY (id_client) REFERENCES clients(id_client) ON DELETE RESTRICT,
                              FOREIGN KEY (id_utilisateur) REFERENCES utilisateurs(id_utilisateur) ON DELETE RESTRICT
) ENGINE=InnoDB;

-- 6. Table de liaison Reservation_Chambres (Gestion des groupes et des nuitées)
CREATE TABLE reservation_chambres (
                                      id_reservation INT NOT NULL,
                                      id_chambre INT NOT NULL,
                                      date_arrivee DATE NOT NULL,
                                      date_depart DATE NOT NULL,
                                      prix_applique DECIMAL(10, 2) NOT NULL, -- Historisation du prix au moment de la réservation
                                      PRIMARY KEY (id_reservation, id_chambre, date_arrivee),
                                      FOREIGN KEY (id_reservation) REFERENCES reservations(id_reservation) ON DELETE CASCADE,
                                      FOREIGN KEY (id_chambre) REFERENCES chambres(id_chambre) ON DELETE RESTRICT,
                                      CONSTRAINT chk_dates_res CHECK (date_depart > date_arrivee),
                                      CONSTRAINT chk_prix_app CHECK (prix_applique >= 0)
) ENGINE=InnoDB;

-- 7. Table Catalogue des Services Supplémentaires
CREATE TABLE services_supplementaires (
                                          id_service INT AUTO_INCREMENT PRIMARY KEY,
                                          nom_service VARCHAR(50) NOT NULL,
                                          type_service ENUM('RESTAURANT', 'PARKING', 'SPA') NOT NULL,
                                          prix_service DECIMAL(10, 2) NOT NULL,
                                          CONSTRAINT chk_prix_service CHECK (prix_service >= 0)
) ENGINE=InnoDB;

-- 8. Table de liaison Reservation_Services (Suivi des consommations durant le séjour)
CREATE TABLE reservation_services (
                                      id_consommation INT AUTO_INCREMENT PRIMARY KEY,
                                      id_reservation INT NOT NULL,
                                      id_service INT NOT NULL,
                                      quantite INT NOT NULL DEFAULT 1,
                                      date_consommation DATE NOT NULL,
                                      FOREIGN KEY (id_reservation) REFERENCES reservations(id_reservation) ON DELETE CASCADE,
                                      FOREIGN KEY (id_service) REFERENCES services_supplementaires(id_service) ON DELETE RESTRICT,
                                      CONSTRAINT chk_quantite CHECK (quantite > 0)
) ENGINE=InnoDB;

-- 9. Table Facture (Générée lors du check-out)
CREATE TABLE factures (
                          id_facture INT AUTO_INCREMENT PRIMARY KEY,
                          id_reservation INT UNIQUE NOT NULL, -- Une seule facture officielle par réservation
                          montant_total DECIMAL(10, 2) NOT NULL DEFAULT 0.00,
                          date_facture TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
                          statut_facture ENUM('EN_ATTENTE', 'PAYEE', 'ANNULEE') DEFAULT 'EN_ATTENTE',
                          FOREIGN KEY (id_reservation) REFERENCES reservations(id_reservation) ON DELETE RESTRICT,
                          CONSTRAINT chk_montant_total CHECK (montant_total >= 0)
) ENGINE=InnoDB;

-- 10. Table Paiement (Suivi des transactions pour solder la facture)
CREATE TABLE paiements (
                           id_paiement INT AUTO_INCREMENT PRIMARY KEY,
                           id_facture INT NOT NULL,
                           montant_paye DECIMAL(10, 2) NOT NULL,
                           date_paiement TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
                           mode_paiement ENUM('ESPECES', 'CARTE', 'VIREMENT') NOT NULL,
                           FOREIGN KEY (id_facture) REFERENCES factures(id_facture) ON DELETE RESTRICT,
                           CONSTRAINT chk_montant_paye CHECK (montant_paye > 0)
) ENGINE=InnoDB;