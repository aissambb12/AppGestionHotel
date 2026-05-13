DROP DATABASE IF EXISTS hotel_db;
CREATE DATABASE hotel_db
CHARACTER SET utf8mb4
COLLATE utf8mb4_general_ci;

USE hotel_db;

CREATE TABLE utilisateur (
    id_utilisateur INT AUTO_INCREMENT PRIMARY KEY,
    nom VARCHAR(100) NOT NULL,
    login VARCHAR(50) NOT NULL UNIQUE,
    mot_de_passe VARCHAR(100) NOT NULL,
    role ENUM('ADMIN', 'RECEPTIONNISTE', 'MAINTENANCE', 'RESTAURANT') NOT NULL
);

CREATE TABLE client (
    id_client INT AUTO_INCREMENT PRIMARY KEY,
    nom VARCHAR(100) NOT NULL,
    prenom VARCHAR(100) NOT NULL,
    cin VARCHAR(20) NOT NULL UNIQUE,
    telephone VARCHAR(20),
    email VARCHAR(100)
);

CREATE TABLE chambre (
    id_chambre INT AUTO_INCREMENT PRIMARY KEY,
    numero VARCHAR(10) NOT NULL UNIQUE,
    type VARCHAR(50) NOT NULL,
    prix_par_nuit DECIMAL(10,2) NOT NULL,
    statut ENUM('DISPONIBLE', 'RESERVEE', 'OCCUPEE', 'MAINTENANCE') NOT NULL DEFAULT 'DISPONIBLE'
);

CREATE TABLE reservation (
    id_reservation INT AUTO_INCREMENT PRIMARY KEY,
    id_client INT NOT NULL,
    id_chambre INT NOT NULL,
    date_debut DATE NOT NULL,
    date_fin DATE NOT NULL,
    statut ENUM('RESERVEE', 'EN_COURS', 'ANNULEE', 'TERMINEE') NOT NULL DEFAULT 'RESERVEE',

    CONSTRAINT fk_reservation_client
        FOREIGN KEY (id_client) REFERENCES client(id_client)
        ON DELETE CASCADE
        ON UPDATE CASCADE,

    CONSTRAINT fk_reservation_chambre
        FOREIGN KEY (id_chambre) REFERENCES chambre(id_chambre)
        ON DELETE CASCADE
        ON UPDATE CASCADE,

    CONSTRAINT chk_dates_reservation
        CHECK (date_fin > date_debut)
);

CREATE TABLE maintenance (
    id_maintenance INT AUTO_INCREMENT PRIMARY KEY,
    id_chambre INT NOT NULL,
    date_debut DATE NOT NULL,
    date_fin DATE,
    description VARCHAR(255) NOT NULL,
    statut ENUM('EN_COURS', 'TERMINEE', 'ANNULEE') NOT NULL DEFAULT 'EN_COURS',

    CONSTRAINT fk_maintenance_chambre
        FOREIGN KEY (id_chambre) REFERENCES chambre(id_chambre)
        ON DELETE CASCADE
        ON UPDATE CASCADE
);

CREATE TABLE plat (
    id_plat INT AUTO_INCREMENT PRIMARY KEY,
    nom VARCHAR(100) NOT NULL,
    description VARCHAR(255),
    prix DECIMAL(10,2) NOT NULL,
    disponible BOOLEAN NOT NULL DEFAULT TRUE
);

CREATE TABLE commande_restaurant (
    id_commande INT AUTO_INCREMENT PRIMARY KEY,
    id_reservation INT NOT NULL,
    date_commande DATE NOT NULL,
    statut ENUM('EN_ATTENTE', 'PREPAREE', 'SERVIE', 'ANNULEE') NOT NULL DEFAULT 'EN_ATTENTE',

    CONSTRAINT fk_commande_reservation
        FOREIGN KEY (id_reservation) REFERENCES reservation(id_reservation)
        ON DELETE CASCADE
        ON UPDATE CASCADE
);

CREATE TABLE ligne_commande_restaurant (
    id_ligne INT AUTO_INCREMENT PRIMARY KEY,
    id_commande INT NOT NULL,
    id_plat INT NOT NULL,
    quantite INT NOT NULL,
    prix_unitaire DECIMAL(10,2) NOT NULL,

    CONSTRAINT fk_ligne_commande
        FOREIGN KEY (id_commande) REFERENCES commande_restaurant(id_commande)
        ON DELETE CASCADE
        ON UPDATE CASCADE,

    CONSTRAINT fk_ligne_plat
        FOREIGN KEY (id_plat) REFERENCES plat(id_plat)
        ON DELETE CASCADE
        ON UPDATE CASCADE,

    CONSTRAINT chk_quantite_positive
        CHECK (quantite > 0)
);

CREATE TABLE facture (
    id_facture INT AUTO_INCREMENT PRIMARY KEY,
    id_reservation INT NOT NULL UNIQUE,
    date_facture DATE NOT NULL,
    montant_hebergement DECIMAL(10,2) NOT NULL DEFAULT 0,
    montant_restaurant DECIMAL(10,2) NOT NULL DEFAULT 0,
    montant_total DECIMAL(10,2) NOT NULL DEFAULT 0,
    statut ENUM('NON_PAYEE', 'PARTIELLEMENT_PAYEE', 'PAYEE') NOT NULL DEFAULT 'NON_PAYEE',

    CONSTRAINT fk_facture_reservation
        FOREIGN KEY (id_reservation) REFERENCES reservation(id_reservation)
        ON DELETE CASCADE
        ON UPDATE CASCADE
);

CREATE TABLE paiement (
    id_paiement INT AUTO_INCREMENT PRIMARY KEY,
    id_facture INT NOT NULL,
    date_paiement DATE NOT NULL,
    montant DECIMAL(10,2) NOT NULL,
    mode_paiement ENUM('ESPECES', 'CARTE', 'VIREMENT') NOT NULL,

    CONSTRAINT fk_paiement_facture
        FOREIGN KEY (id_facture) REFERENCES facture(id_facture)
        ON DELETE CASCADE
        ON UPDATE CASCADE,

    CONSTRAINT chk_montant_paiement
        CHECK (montant > 0)
);