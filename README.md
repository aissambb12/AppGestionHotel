# 🏨 AppGestionHotel — Application de Gestion d'Hôtel

Application de bureau (**Java Swing + MySQL**) pour la gestion complète d'un hôtel :
chambres, clients, réservations, check-in / check-out, maintenance, services
supplémentaires, facturation et paiements.

Projet du module **Programmation Orientée Objet en Java** — Faculté des Sciences et
Techniques de Settat, Université Hassan 1er.

---

## 📌 Fonctionnalités

- 🛏️ **Chambres** : création, modification, suivi du statut (disponible / maintenance), recherche des chambres libres sur une période.
- 👥 **Clients** : création, modification, suppression, recherche par mot-clé (identification par la CIN).
- 📅 **Réservations** : création (une ou plusieurs chambres), modification, check-in, avec historisation du prix au moment de la réservation.
- 🔑 **Check-out & facturation** : clôture du séjour et calcul automatique du montant total (nuitées + services).
- 🍽️ **Services supplémentaires** : restaurant, parking, spa, avec suivi des consommations.
- 💳 **Paiements** : plusieurs modes (espèces, carte, virement) et suivi du statut de la facture.
- 🔧 **Maintenance** : déclaration et clôture des interventions, historique des réparations.
- 📊 **Statistiques** : calcul du chiffre d'affaires sur une période (factures payées).
- 🔐 **Authentification par rôle** : Administrateur, Réceptionniste, Agent de maintenance.

---

## 👥 Équipe

| Nom | Prénom |
|-----|--------|
| BOUBRIK | Aissam |
| ELOUAHABI | Zakaria |
| HACHOU | Yassine |
| LAACHA | Mohammed |

**Encadrant** : Pr. Said El Kafhali

---

## 🔧 Prérequis

- **JDK** 17 ou supérieur
- **MySQL** 5.7+ (ou MariaDB)
- Le pilote JDBC est fourni dans `lib/mysql-connector-java-5.1.29.jar`
- IDE conseillé : IntelliJ IDEA

---

## 📦 Installation

### 1. Démarrer MySQL

```bash
# Linux
sudo systemctl start mysql
# Windows
net start MySQL80
```

### 2. Créer et initialiser la base

```bash
mysql -u root -p < database/schema.sql
mysql -u root -p < "database/ data.sql"
```

> Le fichier de données s'appelle `data.sql` (avec une espace au début de son nom dans le dépôt).

### 3. Configurer la connexion

Ouvrir `src/com/hotel/util/DatabaseConnection.java` et adapter si besoin :

```java
private static final String URL =
    "jdbc:mysql://localhost:3306/hotel_db?useUnicode=true&characterEncoding=utf-8&useSSL=false&serverTimezone=UTC";
private static final String USER = "root";   // votre utilisateur MySQL
private static final String PASSWORD = "";    // votre mot de passe MySQL
```

### 4. Vérifier les tables

```bash
mysql -u root -p -e "USE hotel_db; SHOW TABLES;"
```

Les 10 tables attendues : `utilisateurs`, `clients`, `chambres`, `maintenances`,
`reservations`, `reservation_chambres`, `services_supplementaires`,
`reservation_services`, `factures`, `paiements`.

---

## 🚀 Compilation et exécution

### Méthode 1 : IntelliJ IDEA (recommandée)

1. File → Open → sélectionner le dossier `AppGestionHotel`.
2. File → Project Structure → SDK → JDK 17+.
3. Ajouter `lib/mysql-connector-java-5.1.29.jar` aux dépendances du projet.
4. Lancer la classe `src/Main.java`.

### Méthode 2 : ligne de commande (Linux/Mac)

```bash
mkdir -p bin
javac -cp lib/mysql-connector-java-5.1.29.jar -d bin \
  src/Main.java \
  src/com/hotel/model/*.java \
  src/com/hotel/model/enumeration/*.java \
  src/com/hotel/dao/*.java \
  src/com/hotel/dao/impl/*.java \
  src/com/hotel/service/*.java \
  src/com/hotel/util/*.java \
  src/com/hotel/exception/*.java \
  src/com/hotel/vue/*.java

java -cp bin:lib/mysql-connector-java-5.1.29.jar Main
```

### Méthode 3 : ligne de commande (Windows PowerShell)

```powershell
mkdir bin
javac -cp "lib/mysql-connector-java-5.1.29.jar" -d bin `
  src/Main.java `
  src/com/hotel/model/*.java `
  src/com/hotel/model/enumeration/*.java `
  src/com/hotel/dao/*.java `
  src/com/hotel/dao/impl/*.java `
  src/com/hotel/service/*.java `
  src/com/hotel/util/*.java `
  src/com/hotel/exception/*.java `
  src/com/hotel/vue/*.java

java -cp "bin;lib/mysql-connector-java-5.1.29.jar" Main
```

---

## 📂 Structure du projet

```
AppGestionHotel/
├── README.md
├── AppGestionHotel.iml
│
├── src/
│   ├── Main.java                          # Point d'entrée
│   │
│   └── com/hotel/
│       ├── model/                         # Entités métier (POJO)
│       │   ├── Utilisateur.java
│       │   ├── Client.java
│       │   ├── Chambre.java
│       │   ├── Maintenance.java
│       │   ├── Reservation.java
│       │   ├── ReservationChambre.java
│       │   ├── ServiceSupplementaire.java
│       │   ├── ReservationServices.java
│       │   ├── Facture.java
│       │   ├── Paiement.java
│       │   └── enumeration/               # Énumérations
│       │       ├── Role.java              # ADMIN, RECEPTIONNISTE, MAINTENANCE
│       │       ├── StatutUtilisateur.java # ACTIF, INACTIF
│       │       ├── StatutChambre.java     # DISPONIBLE, MAINTENANCE
│       │       ├── StatutReservation.java # CONFIRMEE, ANNULEE, TERMINEE
│       │       ├── StatutFacture.java     # EN_ATTENTE, PAYEE, ANNULEE
│       │       ├── StatutMaintenance.java # EN_COURS, TERMINEE
│       │       ├── ModePaiement.java      # ESPECES, CARTE, VIREMENT
│       │       └── TypeService.java       # RESTAURANT, PARKING, SPA
│       │
│       ├── dao/                           # Interfaces DAO
│       │   ├── UtilisateurDAO.java
│       │   ├── ClientDAO.java
│       │   ├── ChambreDAO.java
│       │   ├── MaintenanceDAO.java
│       │   ├── ReservationDAO.java
│       │   ├── ReservationChambreDAO.java
│       │   ├── ServiceSupplementaireDAO.java
│       │   ├── ReservationServicesDAO.java
│       │   ├── FactureDAO.java
│       │   ├── PaiementDAO.java
│       │   └── impl/                      # Implémentations JDBC
│       │       └── ...DAOImpl.java
│       │
│       ├── service/                       # Logique métier
│       │   ├── UtilisateurService.java
│       │   ├── ClientService.java
│       │   ├── ChambreService.java
│       │   ├── ReservationService.java
│       │   ├── FacturationService.java
│       │   └── MaintenanceService.java
│       │
│       ├── vue/                           # Interfaces Swing
│       │   ├── LoginFrame.java
│       │   ├── DashboardAdminFrame.java
│       │   ├── DashboardReceptionnisteFrame.java
│       │   ├── DashboardMaintenanceFrame.java
│       │   ├── GestionPersonnelFrame.java
│       │   ├── GestionChambresFrame.java
│       │   ├── GestionClientsFrame.java
│       │   ├── GestionReservationsFrame.java
│       │   ├── CreerReservationFrame.java
│       │   ├── ModifierReservationFrame.java
│       │   ├── DetailsReservationFrame.java
│       │   ├── CheckoutFrame.java
│       │   ├── FactureFrame.java
│       │   ├── FacturesAnnuleesFrame.java
│       │   ├── InterventionsFrame.java
│       │   ├── HistoriqueFrame.java
│       │   ├── StatistiquesFrame.java
│       │   └── ThemeUtil.java
│       │
│       ├── util/                          # Utilitaires
│       │   ├── DatabaseConnection.java    # Connexion BD (Singleton)
│       │   ├── ValidationUtil.java
│       │   ├── DateUtil.java
│       │   ├── NavigationManager.java
│       │   ├── DatePickerUtil.java
│       │   ├── ImageUtil.java
│       │   ├── IconLoader.java
│       │   └── UIUtil.java
│       │
│       ├── exception/                     # Exceptions métier
│       │   ├── HotelException.java
│       │   └── ChambreNonDisponibleException.java
│       │
│       └── resources/                     # Images et icônes
│
├── database/
│   ├── schema.sql                         # Création de la base et des tables
│   └── data.sql                           # Données de démonstration
│
└── lib/
    └── mysql-connector-java-5.1.29.jar    # Pilote MySQL JDBC
```

---

## 🔐 Comptes de démonstration

| Email | Mot de passe | Rôle |
|-------|--------------|------|
| `admin@hotel.ma` | `admin123` | Administrateur |
| `reception@hotel.ma` | `reception123` | Réceptionniste |
| `maintenance@hotel.ma` | `maintenance123` | Agent de maintenance |

### Chambres de démonstration

| Numéro | Catégorie | Prix/nuit | Statut |
|--------|-----------|-----------|--------|
| 101 | SIMPLE | 250 MAD | Disponible |
| 102 | SIMPLE | 250 MAD | Disponible |
| 103 | SIMPLE | 280 MAD | Disponible |
| 201 | DOUBLE | 450 MAD | Disponible |
| 202 | DOUBLE | 450 MAD | Disponible |
| 203 | DOUBLE | 480 MAD | Disponible |
| 301 | SUITE | 900 MAD | Disponible |
| 302 | SUITE | 950 MAD | Maintenance |

### Services supplémentaires

- **Restaurant** : Petit déjeuner Buffet (120 MAD), Déjeuner Premium (250 MAD), Dîner Gastronomique (400 MAD)
- **Parking** : Parking Standard (50 MAD), Parking VIP couvert (100 MAD)
- **Spa** : Massage Relaxant (300 MAD), Hammam et Soins (450 MAD)

---

## 🏗️ Architecture

Architecture en couches avec séparation stricte des responsabilités :

```
┌──────────────────────────────────────────────┐
│  vue (Présentation)                          │
│  Frames Swing : Login, Dashboards, gestion   │
└───────────────┬──────────────────────────────┘
                │
┌───────────────▼──────────────────────────────┐
│  service (Logique métier)                    │
│  Chambre, Client, Reservation,               │
│  Facturation, Maintenance, Utilisateur       │
└───────────────┬──────────────────────────────┘
                │
┌───────────────▼──────────────────────────────┐
│  dao + dao/impl (Accès aux données)          │
│  Une interface + une implémentation JDBC     │
│  par entité                                  │
└───────────────┬──────────────────────────────┘
                │
┌───────────────▼──────────────────────────────┐
│  MySQL (hotel_db) — 10 tables                │
└──────────────────────────────────────────────┘
```

Packages transversaux : `util` (connexion, validation, navigation, dates, images)
et `exception` (erreurs métier).

### Patrons de conception utilisés

- **MVC** : séparation entre la vue (Swing), la logique (service) et le modèle.
- **DAO** : une interface par entité, isolant le code SQL dans les implémentations.
- **Singleton** : `DatabaseConnection` garantit une connexion unique (thread-safe).

---

## 🗄️ Base de données

Base relationnelle MySQL `hotel_db`, 10 tables interconnectées.

Principales décisions de conception :

- **Tables de liaison** : `reservation_chambres` et `reservation_services` portent les attributs propres (dates, prix appliqué, quantité).
- **Historisation du prix** : `reservation_chambres.prix_applique` fige le tarif au moment de la réservation.
- **Intégrité référentielle** : clés étrangères avec `ON DELETE RESTRICT` / `ON DELETE CASCADE` selon le lien.
- **Contraintes CHECK** : dates de départ postérieures aux dates d'arrivée, montants et quantités positifs.
- **Unicité** : colonnes `email` et `cin`.

---

## 🧪 Tests fonctionnels

Scénarios critiques validés :

| Cas testé | Résultat attendu |
|-----------|------------------|
| Connexion avec identifiants valides | Redirection vers le tableau de bord du rôle |
| Connexion avec identifiants invalides | Accès refusé |
| Réservation sur une période déjà occupée | Chambre exclue des résultats |
| Réservation d'une chambre en maintenance | Chambre exclue des résultats |
| Email ou CIN invalide | Saisie rejetée |
| Check-out d'une réservation déjà terminée | Opération bloquée |
| Paiement d'un montant négatif ou nul | Paiement rejeté |
| Calcul du chiffre d'affaires sur une période | Total des factures payées |

---

## 🐛 Dépannage

**« Connection refused » (port 3306)** : MySQL n'est pas démarré → démarrer le service.

**« Table 'hotel_db.clients' doesn't exist »** : scripts non exécutés → relancer `schema.sql` puis `data.sql`.

**« Driver not found »** : vérifier que `lib/mysql-connector-java-5.1.29.jar` est bien dans le classpath.

**« Access denied for user 'root' »** : corriger `USER` et `PASSWORD` dans `DatabaseConnection.java`.

---

## 📝 Notes

⚠️ Les mots de passe sont actuellement stockés en clair (contexte pédagogique). Une
amélioration prévue consiste à les hacher (BCrypt) pour un usage réel.

**GitHub** : https://github.com/aissambb12/AppGestionHotel