USE hotel_db;

-- 1. Utilisateurs
INSERT INTO utilisateurs (nom, prenom, email, mot_de_passe, role, statut) VALUES
('Admin', 'Principal',  'admin@hotel.ma',       'admin123',       'ADMIN',          'ACTIF'),
('Reception', 'Ahmed',  'reception@hotel.ma',   'reception123',   'RECEPTIONNISTE', 'ACTIF'),
('Maintenance', 'Hassan','maintenance@hotel.ma','maintenance123', 'MAINTENANCE',    'ACTIF');

-- 2. Clients
INSERT INTO clients (nom, prenom, cin, email, telephone) VALUES
('EL AMRANI', 'Yassine', 'AB123456', 'yassine@gmail.com',  '0611111111'),
('BENALI',    'Sara',    'CD789456', 'sara@gmail.com',     '0622222222'),
('AIT OMAR',  'Karim',   'EF456123', 'karim@gmail.com',    '0633333333'),
('MANSOURI',  'Nadia',   'GH741852', 'nadia@gmail.com',    '0644444444');

-- 3. Chambres
INSERT INTO chambres (numero, categorie, prix_unitaire, statut) VALUES
('101', 'SIMPLE', 250.00, 'DISPONIBLE'),
('102', 'SIMPLE', 250.00, 'DISPONIBLE'),
('103', 'SIMPLE', 280.00, 'DISPONIBLE'),
('201', 'DOUBLE', 450.00, 'DISPONIBLE'),
('202', 'DOUBLE', 450.00, 'DISPONIBLE'),
('203', 'DOUBLE', 480.00, 'DISPONIBLE'),
('301', 'SUITE',  900.00, 'DISPONIBLE'),
('302', 'SUITE',  950.00, 'MAINTENANCE');

-- 4. Maintenances
INSERT INTO maintenances (id_chambre, date_debut, date_fin, description, statut_maintenance) VALUES
(8, '2026-05-20', '2026-05-30', 'Réparation climatisation suite 302', 'EN_COURS');

-- 7. Services supplémentaires (RESTAURANT / PARKING / SPA uniquement)
INSERT INTO services_supplementaires (nom_service, type_service, prix_service) VALUES
('Petit déjeuner Buffet',  'RESTAURANT', 120.00),
('Déjeuner Premium',       'RESTAURANT', 250.00),
('Dîner Gastronomique',    'RESTAURANT', 400.00),
('Parking Standard',       'PARKING',     50.00),
('Parking VIP couvert',    'PARKING',    100.00),
('Massage Relaxant',       'SPA',        300.00),
('Hammam et Soins',        'SPA',        450.00);
