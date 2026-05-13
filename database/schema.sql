CREATE DATABASE IF NOT EXISTS hotel_db;
USE hotel_db;


CREATE TABLE client (
    id_client INT PRIMARY KEY AUTO_INCREMENT,
    nom VARCHAR(50),
    prenom VARCHAR(50),
    cin VARCHAR(20),
    telephone VARCHAR(20),
    email VARCHAR(100)
);