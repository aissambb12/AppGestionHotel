USE hotel_db;

INSERT INTO utilisateur (nom, login, mot_de_passe, role) VALUES
('Administrateur Principal', 'admin', 'admin123', 'ADMIN'),
('Receptionniste 1', 'reception', 'reception123', 'RECEPTIONNISTE'),
('Agent Maintenance', 'maintenance', 'maintenance123', 'MAINTENANCE'),
('Responsable Restaurant', 'restaurant', 'restaurant123', 'RESTAURANT');

INSERT INTO client (nom, prenom, cin, telephone, email) VALUES
('EL AMRANI', 'Yassine', 'AB123456', '0611111111', 'yassine@gmail.com'),
('BENALI', 'Sara', 'CD789456', '0622222222', 'sara@gmail.com'),
('AIT OMAR', 'Karim', 'EF456123', '0633333333', 'karim@gmail.com'),
('MANSOURI', 'Nadia', 'GH741852', '0644444444', 'nadia@gmail.com');

INSERT INTO chambre (numero, type, prix_par_nuit, statut) VALUES
('101', 'Simple', 250.00, 'DISPONIBLE'),
('102', 'Simple', 250.00, 'DISPONIBLE'),
('201', 'Double', 400.00, 'RESERVEE'),
('202', 'Double', 400.00, 'OCCUPEE'),
('301', 'Suite', 800.00, 'DISPONIBLE'),
('302', 'Suite', 850.00, 'MAINTENANCE');

INSERT INTO reservation (id_client, id_chambre, date_debut, date_fin, statut) VALUES
(1, 3, '2026-05-20', '2026-05-23', 'RESERVEE'),
(2, 4, '2026-05-10', '2026-05-15', 'EN_COURS'),
(3, 1, '2026-04-01', '2026-04-04', 'TERMINEE');

INSERT INTO maintenance (id_chambre, date_debut, date_fin, description, statut) VALUES
(6, '2026-05-12', NULL, 'Réparation de la climatisation', 'EN_COURS');

INSERT INTO plat (nom, description, prix, disponible) VALUES
('Petit déjeuner complet', 'Café, jus, pain, fromage, omelette', 60.00, TRUE),
('Sandwich poulet', 'Sandwich au poulet avec frites', 45.00, TRUE),
('Pizza margherita', 'Pizza tomate, fromage et basilic', 70.00, TRUE),
('Tajine poulet', 'Tajine marocain au poulet et légumes', 90.00, TRUE),
('Salade marocaine', 'Tomate, oignon, concombre et olives', 35.00, TRUE),
('Jus orange', 'Jus orange frais', 25.00, TRUE);

INSERT INTO commande_restaurant (id_reservation, date_commande, statut) VALUES
(2, '2026-05-11', 'SERVIE'),
(2, '2026-05-12', 'EN_ATTENTE');

INSERT INTO ligne_commande_restaurant (id_commande, id_plat, quantite, prix_unitaire) VALUES
(1, 1, 2, 60.00),
(1, 6, 2, 25.00),
(2, 4, 1, 90.00),
(2, 5, 1, 35.00);

INSERT INTO facture (
    id_reservation,
    date_facture,
    montant_hebergement,
    montant_restaurant,
    montant_total,
    statut
) VALUES
(3, '2026-04-04', 750.00, 0.00, 750.00, 'PAYEE');

INSERT INTO paiement (id_facture, date_paiement, montant, mode_paiement) VALUES
(1, '2026-04-04', 750.00, 'ESPECES');