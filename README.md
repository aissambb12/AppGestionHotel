# 🏨 AppGestionHotel - Application de Gestion d'Hôtel

## 📌 Vue d'ensemble

**AppGestionHotel** est une application de gestion d'hôtel complète et professionnelle développée en **Java**. Elle permet de gérer intégralement les opérations d'un hôtel : réservations, clients, chambres, maintenance, services supplémentaires et facturation.

### ✨ Fonctionnalités principales

- 🛏️ **Gestion des chambres** : création, modification, suivi des disponibilités et maintenance
- 👥 **Gestion des clients** : création, modification, recherche et historique
- 📅 **Gestion des réservations** : création, modification, annulation avec assignation automatique de chambres
- 🔑 **Check-in / Check-out** : enregistrement des arrivées/départs avec génération automatique de factures
- 🍽️ **Services supplémentaires** : restaurant, parking, spa avec gestion des consommations
- 💳 **Facturation et paiements** : génération automatique de factures, suivi des paiements multimodes
- 🔧 **Gestion de la maintenance** : planification et suivi des travaux d'entretien
- 🔐 **Authentification par rôles** : Admin, Réceptionniste, Maintenance avec permissions spécifiques

---

## 👥 Informations sur le groupe

| # | Nom | Prénom | Rôle |
|----|-----|--------|------|
| 1 | *À compléter* | *À compléter* | Développeur |
| 2 | *À compléter* | *À compléter* | Développeur |
| 3 | *À compléter* | *À compléter* | Développeur |

**Email de contact** : *À compléter*

---

## 🔧 Prérequis système

- **JDK** : 17 ou supérieur
- **MySQL** : 5.7+ ou MariaDB 10.3+
- **IDE** (optionnel) : IntelliJ IDEA, Eclipse, ou NetBeans
- **RAM** : 2 GB minimum
- **Espace disque** : 500 MB

---

## 📦 Installation et Configuration

### **Étape 1 : Préparation de la base de données**

#### Sur Linux/Mac :
```bash
# Démarrer le service MySQL
sudo systemctl start mysql

# Ou avec Homebrew (Mac)
brew services start mysql
```

#### Sur Windows :
```powershell
# Démarrer le service MySQL
net start MySQL80

# Ou via Services Windows (services.msc)
```

### **Étape 2 : Créer et initialiser la base de données**

**Option A : Via ligne de commande MySQL**

```bash
# Se connecter à MySQL
mysql -u root -p

# Dans le terminal MySQL, exécuter les scripts :
source database/schema.sql;
source database/data.sql;

# Ou en une seule commande :
exit
```

**Option B : Exécution directe (Linux/Mac)**

```bash
mysql -u root -p < database/schema.sql
mysql -u root -p < database/data.sql
```

**Option C : Exécution directe (Windows PowerShell)**

```powershell
mysql -u root -p < database/schema.sql
mysql -u root -p < database/data.sql
```

### **Étape 3 : Configurer les identifiants de connexion**

Ouvrir le fichier `src/com/hotel/util/DatabaseConnection.java` et modifier si nécessaire :

```java
private static final String URL = "jdbc:mysql://localhost:3306/hotel_db?useUnicode=true&characterEncoding=utf-8&useSSL=false&serverTimezone=UTC";
private static final String USER = "root";        // Votre utilisateur MySQL
private static final String PASSWORD = "";         // Votre mot de passe MySQL (vide par défaut)
```

**Exemple avec authentification :**

```java
private static final String URL = "jdbc:mysql://localhost:3306/hotel_db?useUnicode=true&characterEncoding=utf-8&useSSL=true&serverTimezone=UTC";
private static final String USER = "hotel_user";
private static final String PASSWORD = "hotel_password_123";
```

### **Étape 4 : Vérifier l'installation de la base de données**

```bash
mysql -u root -p -e "USE hotel_db; SHOW TABLES;"
```

**Résultat attendu :**
```
| Tables_in_hotel_db              |
|---------------------------------|
| chambres                        |
| clients                         |
| factures                        |
| maintenances                    |
| paiements                       |
| reservation_chambres            |
| reservation_services            |
| reservations                    |
| services_supplementaires        |
| utilisateurs                    |
```

---

## 🚀 Compilation et Exécution

### **Méthode 1 : Via IntelliJ IDEA (Recommandée)**

1. **Ouvrir le projet**
   - File → Open → Sélectionner le dossier `AppGestionHotel`

2. **Configurer le JDK**
   - File → Project Structure → SDK → Sélectionner JDK 17+

3. **Compiler**
   - Build → Build Project (Ctrl+F9)

4. **Exécuter**
   - Clic droit sur `src/Main.java` → Run 'Main.main()'
   - Ou utiliser Shift+F10

### **Méthode 2 : En ligne de commande (Linux/Mac)**

```bash
# Se placer dans le répertoire du projet
cd /chemin/vers/AppGestionHotel

# Compilation
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

# Exécution
java -cp bin:lib/mysql-connector-java-5.1.29.jar Main
```

### **Méthode 3 : En ligne de commande (Windows PowerShell)**

```powershell
# Se placer dans le répertoire du projet
cd C:\chemin\vers\AppGestionHotel

# Compilation
javac -cp "lib/mysql-connector-java-5.1.29.jar" -d bin `
  src/Main.java, `
  src/com/hotel/model/*.java, `
  src/com/hotel/model/enumeration/*.java, `
  src/com/hotel/dao/*.java, `
  src/com/hotel/dao/impl/*.java, `
  src/com/hotel/service/*.java, `
  src/com/hotel/util/*.java, `
  src/com/hotel/exception/*.java, `
  src/com/hotel/vue/*.java

# Exécution
java -cp "bin;lib/mysql-connector-java-5.1.29.jar" Main
```

### **Méthode 4 : Script d'automatisation (Linux/Mac)**

Créer un fichier `run.sh` :

```bash
#!/bin/bash

# Couleurs
GREEN='\033[0;32m'
RED='\033[0;31m'
YELLOW='\033[1;33m'
NC='\033[0m' # No Color

echo -e "${YELLOW}=== Compilation du projet AppGestionHotel ===${NC}"

# Créer le répertoire bin s'il n'existe pas
mkdir -p bin

# Compilation
javac -cp lib/mysql-connector-java-5.1.29.jar -d bin \
  src/Main.java \
  src/com/hotel/model/*.java \
  src/com/hotel/model/enumeration/*.java \
  src/com/hotel/dao/*.java \
  src/com/hotel/dao/impl/*.java \
  src/com/hotel/service/*.java \
  src/com/hotel/util/*.java \
  src/com/hotel/exception/*.java \
  src/com/hotel/vue/*.java 2>&1

if [ $? -eq 0 ]; then
    echo -e "${GREEN}✓ Compilation réussie${NC}"
    echo -e "${YELLOW}=== Lancement de l'application ===${NC}"
    java -cp bin:lib/mysql-connector-java-5.1.29.jar Main
else
    echo -e "${RED}✗ Erreur de compilation${NC}"
    exit 1
fi
```

Exécuter le script :

```bash
chmod +x run.sh
./run.sh
```

---

## 📂 Structure du projet

```
AppGestionHotel/
│
├── 📄 README.md                           # Ce fichier
├── 📄 AppGestionHotel.iml                 # Configuration IntelliJ
├── 📄 .gitignore                          # Fichiers à ignorer dans Git
│
├── 📁 src/                                # Code source principal
│   ├── Main.java                          # Point d'entrée de l'application
│   │
│   └── com/hotel/
│       │
│       ├── 📁 model/                      # Entités métier (Models)
│       │   ├── Client.java                # Classe Client
│       │   ├── Chambre.java               # Classe Chambre
│       │   ├── Reservation.java           # Classe Réservation
│       │   ├── ReservationChambre.java    # Liaison Réservation-Chambre
│       │   ├── ReservationServices.java   # Liaison Réservation-Services
│       │   ├── Facture.java               # Classe Facture
│       │   ├── Paiement.java              # Classe Paiement
│       │   ├── Maintenance.java           # Classe Maintenance
│       │   ├── Utilisateur.java           # Classe Utilisateur
│       │   ├── Service.java               # Classe Service (restaurant, parking, spa)
│       │   │
│       │   └── 📁 enumeration/            # Énumérations
│       │       ├── Role.java              # Rôles : ADMIN, RECEPTIONNISTE, MAINTENANCE
│       │       ├── StatutUtilisateur.java # Statuts : ACTIF, INACTIF
│       │       ├── StatutChambre.java     # Statuts : DISPONIBLE, MAINTENANCE
│       │       ├── StatutReservation.java # Statuts : CONFIRMEE, ANNULEE, TERMINEE
│       │       ├── StatutMaintenance.java # Statuts : EN_COURS, TERMINEE
│       │       ├── StatutFacture.java     # Statuts : EN_ATTENTE, PAYEE, ANNULEE
│       │       └── ModePaiement.java      # Modes : ESPECES, CARTE, VIREMENT
│       │
│       ├── 📁 dao/                        # Data Access Objects (Couche persistance)
│       │   ├── ClientDAO.java             # Interface DAO Client
│       │   ├── ChambreDAO.java            # Interface DAO Chambre
│       │   ├── ReservationDAO.java        # Interface DAO Réservation
│       │   ├── FactureDAO.java            # Interface DAO Facture
│       │   ├── PaiementDAO.java           # Interface DAO Paiement
│       │   ├── MaintenanceDAO.java        # Interface DAO Maintenance
│       │   │
│       │   └── 📁 impl/                   # Implémentations JDBC des DAO
│       │       ├── ClientDAOImpl.java
│       │       ├── ChambreDAOImpl.java
│       │       ├── ReservationDAOImpl.java
│       │       ├── FactureDAOImpl.java
│       │       ├── PaiementDAOImpl.java
│       │       └── MaintenanceDAOImpl.java
│       │
│       ├── 📁 service/                    # Services métier (Logique applicative)
│       │   ├── ClientService.java
│       │   ├── ChambreService.java
│       │   ├── ReservationService.java
│       │   ├── FactureService.java
│       │   ├── PaiementService.java
│       │   └── MaintenanceService.java
│       │
│       ├── 📁 vue/                        # Interface utilisateur (Swing)
│       │   ├── LoginFrame.java            # Écran de connexion
│       │   ├── MainFrame.java             # Fenêtre principale
│       │   ├── ClientPanel.java           # Panneau gestion clients
│       │   ├── ChambrePanel.java          # Panneau gestion chambres
│       │   ├── ReservationPanel.java      # Panneau gestion réservations
│       │   ├── FacturePanel.java          # Panneau gestion factures
│       │   ├── MaintenancePanel.java      # Panneau gestion maintenance
│       │   │
│       │   └── 📁 dialog/                 # Dialogues et formulaires
│       │       ├── AddClientDialog.java
│       │       ├── AddReservationDialog.java
│       │       └── ...
│       │
│       ├── 📁 util/                       # Classes utilitaires
│       │   ├── DatabaseConnection.java    # Gestion connexion BD (Singleton)
│       │   ├── ValidationUtil.java        # Validations (dates, emails, prix, etc.)
│       │   ├── DateUtil.java              # Utilitaires pour les dates
│       │   └── StringUtil.java            # Utilitaires pour les chaînes
│       │
│       ├── 📁 exception/                  # Exceptions personnalisées
│       │   ├── DatabaseException.java
│       │   ├── ValidationException.java
│       │   └── BusinessException.java
│       │
│       └── 📁 resources/                  # Ressources (images, icônes, css)
│           ├── images/
│           ├── icons/
│           └── styles/
│
├── 📁 database/                           # Scripts et données SQL
│   ├── schema.sql                         # Création BD et tables
│   └── data.sql                           # Données de démonstration
│
├── 📁 docs/                               # Documentation du projet
│   ├── RAPPORT.md                         # Rapport détaillé
│   ├── presentation.pptx                  # Présentation pour soutenance
│   ├── manuel_utilisateur.md              # Manuel d'utilisation
│   │
│   └── 📁 diagrams/                       # Diagrammes UML
│       ├── DiagrammeClasses.png
│       ├── DiagrammeCasUtilisation.png
│       ├── DiagrammeSequence.png
│       ├── DiagrammeActivites.png
│       └── DiagrammeEtatTransition.png
│
├── 📁 lib/                                # Dépendances externes
│   └── mysql-connector-java-5.1.29.jar   # Driver MySQL JDBC
│
└── 📁 .idea/                              # Configuration IntelliJ IDEA

```

---

## 🔐 Identifiants de test (données de démonstration)

### **Utilisateurs système**

| Email | Mot de passe | Rôle | Permissions |
|-------|--------------|------|------------|
| `admin@hotel.ma` | `admin123` | **Admin** | Gestion complète, utilisateurs, configuration |
| `reception@hotel.ma` | `reception123` | **Réceptionniste** | Réservations, check-in/out, clients, services |
| `maintenance@hotel.ma` | `maintenance123` | **Maintenance** | Gestion maintenance, disponibilité chambres |

### **Clients de démonstration**

| Nom | Prénom | CIN | Email | Téléphone |
|-----|--------|-----|-------|-----------|
| EL AMRANI | Yassine | AB123456 | yassine@gmail.com | 0611111111 |
| BENALI | Sara | CD789456 | sara@gmail.com | 0622222222 |
| AIT OMAR | Karim | EF456123 | karim@gmail.com | 0633333333 |
| MANSOURI | Nadia | GH741852 | nadia@gmail.com | 0644444444 |

### **Chambres disponibles**

| Numéro | Catégorie | Prix/nuit | Statut | Étage |
|--------|-----------|-----------|--------|-------|
| 101 | SIMPLE | 250 DH | ✅ Disponible | 1 |
| 102 | SIMPLE | 250 DH | ✅ Disponible | 1 |
| 103 | SIMPLE | 280 DH | ✅ Disponible | 1 |
| 201 | DOUBLE | 450 DH | ✅ Disponible | 2 |
| 202 | DOUBLE | 450 DH | ✅ Disponible | 2 |
| 203 | DOUBLE | 480 DH | ✅ Disponible | 2 |
| 301 | SUITE | 900 DH | ✅ Disponible | 3 |
| 302 | SUITE | 950 DH | 🔧 Maintenance | 3 |

### **Services supplémentaires**

**Restaurant :**
- Petit déjeuner Buffet : 120 DH
- Déjeuner Premium : 250 DH
- Dîner Gastronomique : 400 DH

**Parking :**
- Parking Standard : 50 DH/jour
- Parking VIP couvert : 100 DH/jour

**Spa :**
- Massage Relaxant : 300 DH
- Hammam et Soins : 450 DH

---

## 🏗️ Architecture et Design Patterns

### **Architecture globale : MVC (Model-View-Controller)**

```
┌─────────────────────────────────────────────────────┐
│              VIEW (Présentation)                    │
│  LoginFrame, MainFrame, Panels, Dialogs (Swing)    │
└────────────┬────────────────────────────────────────┘
             │ appelle
             ▼
┌─────────────────────────────────────────────────────┐
│          SERVICE (Logique métier)                   │
│  ClientService, ChambreService, ReservationService  │
│  FactureService, PaiementService, MaintenanceService│
└────────────┬────────────────────────────────────────┘
             │ utilise
             ▼
┌─────────────────────────────────────────────────────┐
│          DAO (Accès aux données)                    │
│  ClientDAO, ChambreDAO, ReservationDAO, etc.        │
└────────────┬────────────────────────────────────────┘
             │ accède à
             ▼
┌─────────────────────────────────────────────────────┐
│    DATABASE (MySQL - hotel_db)                      │
│    10 tables avec contraintes d'intégrité           │
└─────────────────────────────────────────────────────┘
```

### **Design Patterns implémentés**

#### 🔹 **DAO Pattern (Data Access Object)**
- Abstraction complète de l'accès aux données
- Interfaces DAO dans `dao/`
- Implémentations JDBC dans `dao/impl/`
- **Avantages** : Facilite les tests, changement de BD transparent

#### 🔹 **Singleton Pattern**
- `DatabaseConnection` : garantit une seule connexion active
- Thread-safe avec synchronisation
- Utilisation : `DatabaseConnection.getConnection()`

#### 🔹 **Service Layer Pattern**
- Logique métier séparée de la présentation
- Services contiennent validation et orchestration
- DAOs font uniquement CRUD

#### 🔹 **Factory Pattern**
- Création centralisée des objets Service
- Classe `ServiceFactory` *(optionnelle)*

#### 🔹 **Strategy Pattern**
- Stratégies de validation (dates, prix, emails)
- Stratégies de calcul (tarification, réductions)

---

## 🗄️ Base de données

### **Schéma relationnel (10 tables)**

```sql
utilisateurs ─────────────┐
                          │
        ┌─────────────────┘
        │
    reservations ──────────────┐
        │                      │
        ├─ reservation_chambres ─── chambres ─── maintenances
        │                      │
        ├─ reservation_services ── services_supplementaires
        │
        └─ factures ─────────────── paiements

clients ──────────────┐
                      │
          reservations

utilisateurs ───┐
                │
         reservation_services
```

### **Contraintes principales**

✅ Clés étrangères avec `ON DELETE CASCADE` ou `ON DELETE RESTRICT`  
✅ Contraintes CHECK pour prix, dates, quantités  
✅ Unicité sur CIN, email, numéro chambre  
✅ Codage UTF-8 pour caractères spéciaux  

---

## 🧪 Procédures de test

### **Scénario 1 : Réservation simple**

1. **Démarrer l'application**
   ```bash
   java -cp bin:lib/mysql-connector-java-5.1.29.jar Main
   ```

2. **Se connecter**
   - Email : `reception@hotel.ma`
   - Mot de passe : `reception123`

3. **Créer une réservation**
   - Menu → Réservations → Nouvelle réservation
   - Sélectionner client : `Yassine EL AMRANI`
   - Choisir dates : 01/06/2026 au 03/06/2026
   - Catégorie chambre : DOUBLE
   - Cliquer "Chercher disponibilités"
   - Sélectionner chambre 201
   - Ajouter service : Petit déjeuner buffet
   - Confirmer réservation

4. **Vérifier**
   - La réservation apparaît dans la liste
   - Facture préliminaire : (450 DH × 2 nuits) + (120 DH × 1 service) = 1020 DH

### **Scénario 2 : Check-in / Check-out**

1. **Trouver la réservation**
   - Menu → Réservations → Rechercher
   - ID Réservation : (obtenu du scénario 1)

2. **Effectuer check-in**
   - Bouton "Check-in"
   - Vérifier les informations client
   - Confirmer

3. **Ajouter services supplémentaires**
   - Bouton "Ajouter service"
   - Sélectionner : Dîner Gastronomique (400 DH)
   - Valider

4. **Effectuer check-out**
   - Bouton "Check-out"
   - Facture finalisée s'affiche
   - Montant total : 1420 DH

5. **Enregistrer paiement**
   - Mode paiement : CARTE
   - Montant : 1420 DH
   - Confirmer

### **Scénario 3 : Gestion maintenance**

1. **Se connecter en tant que Maintenance**
   - Email : `maintenance@hotel.ma`
   - Mot de passe : `maintenance123`

2. **Marquer chambre en maintenance**
   - Menu → Maintenance → Nouvelle maintenance
   - Sélectionner chambre : 301
   - Date début : 05/06/2026
   - Date fin : 07/06/2026
   - Description : Rénovation salle de bain
   - Valider

3. **Vérifier non-disponibilité**
   - Changer rôle → Réceptionniste
   - Rechercher chambres 05/06 au 07/06
   - Chambre 301 n'apparaît pas

4. **Terminer maintenance**
   - Menu → Maintenance
   - Sélectionner maintenance de chambre 301
   - Cliquer "Terminer maintenance"
   - Chambre devient disponible

---

## 🐛 Dépannage

### **Erreur : "Connection refused" (port 3306)**

**Cause** : MySQL n'est pas démarré

**Solution** :
```bash
# Linux
sudo systemctl start mysql

# Mac
brew services start mysql

# Windows
net start MySQL80
```

### **Erreur : "Table 'hotel_db.clients' doesn't exist"**

**Cause** : Scripts SQL non exécutés

**Solution** :
```bash
mysql -u root -p < database/schema.sql
mysql -u root -p < database/data.sql
```

### **Erreur : "Driver not found"**

**Cause** : Fichier JAR manquant

**Solution** :
- Vérifier que `lib/mysql-connector-java-5.1.29.jar` existe
- Ajouter le JAR au classpath lors de la compilation/exécution

### **Erreur : "Access denied for user 'root'@'localhost'"**

**Cause** : Identifiants de connexion incorrects

**Solution** :
1. Vérifier le mot de passe MySQL
2. Éditer `src/com/hotel/util/DatabaseConnection.java`
3. Corriger `USER` et `PASSWORD`
4. Recompiler

### **Erreur : "Port 3306 already in use"**

**Cause** : Plusieurs instances MySQL

**Solution** :
- Utiliser un autre port dans `DatabaseConnection.java`
- Ou arrêter les autres instances

---

## 📊 Statistiques du projet

| Métrique | Valeur |
|----------|--------|
| 📝 Fichiers Java | 30+ |
| 🗄️ Tables BDD | 10 |
| 🎨 Interfaces Swing | 7+ |
| ✍️ Lignes de code | ~5000+ |
| 🕐 Temps moyen résolution | <1s |

---

## 📚 Documentation supplémentaire

Pour plus d'informations, consultez :

- **docs/RAPPORT.md** - Rapport détaillé d'analyse et conception
- **docs/presentation.pptx** - Présentation pour la soutenance
- **docs/manuel_utilisateur.md** - Guide complet d'utilisation
- **docs/diagrams/** - Diagrammes UML (classes, cas d'utilisation, séquences, etc.)

---

## 📞 Support et Contact

Pour toute question concernant le projet :

**Email** : *À compléter*  
**Téléphone** : *À compléter*  
**GitHub** : https://github.com/aissambb12/AppGestionHotel

---

## 📋 Checklist de déploiement

Avant de soumettre le projet, vérifier :

- [ ] MySQL démarré et opérationnel
- [ ] Base `hotel_db` créée avec toutes les tables
- [ ] Données de démonstration insertées
- [ ] Identifiants de connexion configurés
- [ ] Code compilé sans erreurs
- [ ] Application lancée avec succès
- [ ] Login fonctionne avec les identifiants fournis
- [ ] Créer et tester une réservation complète
- [ ] Check-in/check-out fonctionne
- [ ] Facturation automatique se déclenche
- [ ] Paiements enregistrés correctement
- [ ] Maintenance des chambres fonctionne
- [ ] Tous les fichiers présents dans le .zip

---

## 📅 Informations de soumission

**Projet** : Application de Gestion d'Hôtel (Sujet 10)  
**Deadline** : 31 mai 2026 à 23h59  
**Format** : Archive .zip  
**Contenu requis** :
- ✅ code/ (tous les fichiers source)
- ✅ database/ (schema.sql, data.sql)
- ✅ docs/ (rapport, présentation, diagrammes)
- ✅ lib/ (dépendances JDBC)
- ✅ README.md (ce fichier)

**Email de soumission** : said.elkafhali@gmail.com  
**Soutenance** : 02 juin 2026

---

## 📝 Notes importantes

⚠️ **Sécurité** :
- Les mots de passe stockés doivent être hachés (BCrypt/Argon2 en production)
- Ne pas stocker les mots de passe en clair dans le code

⚠️ **Performance** :
- Pour ~1000+ réservations, considérer l'indexation des tables
- Utiliser des pools de connexion en production (HikariCP, etc.)

⚠️ **Maintenance** :
- Effectuer des sauvegardes régulières de la BD
- Monitorer les logs d'erreurs
- Mettre à jour les dépendances régulièrement

---

**Dernière mise à jour** : 30 mai 2026  
**Version** : 1.0.0  
**Licence** : © 2026 - Tous droits réservés
