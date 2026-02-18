# Gestion Restaurant - Application de Gestion de Restaurant

## 📋 Description

Application de bureau Java pour la gestion d'un restaurant, développée avec le pattern MVC et une interface Swing.

## 🏗️ Architecture

L'application suit une architecture MVC (Modèle-Vue-Contrôleur) :

```
com.restaurant/
├── model          → Entités (classes métier)
│   ├── Categorie.java
│   ├── Produit.java
│   ├── MouvementStock.java
│   ├── Commande.java
│   ├── LigneCommande.java
│   ├── Utilisateur.java
│   └── enums
│        ├── TypeMouvement.java
│        └── EtatCommande.java
│
├── dao            → Accès à la base de données (JDBC)
│   ├── ConnectionDB.java
│   ├── CategorieDAO.java
│   ├── ProduitDAO.java
│   ├── MouvementStockDAO.java
│   ├── CommandeDAO.java
│   ├── LigneCommandeDAO.java
│   └── UtilisateurDAO.java
│
├── service        → Logique métier (validation + règles de gestion)
│   ├── CategorieService.java
│   ├── ProduitService.java
│   ├── StockService.java
│   ├── CommandeService.java
│   └── AuthService.java
│
├── controller     → Contrôleurs (liaison View ↔ Service)
│   ├── LoginController.java
│   ├── ProduitController.java
│   ├── StockController.java
│   ├── CommandeController.java
│   └── StatistiqueController.java
│
├── view           → Interfaces graphiques (Swing)
│   ├── LoginView.java
│   ├── MainView.java
│   ├── ProduitView.java
│   ├── StockView.java
│   ├── CommandeView.java
│   └── StatistiqueView.java
│
└── utils          → Classes utilitaires
    ├── ValidationUtils.java
    ├── DateUtils.java
    └── AlertUtils.java
```

## 🗄️ Base de données

### Schéma SQL

```sql
CREATE DATABASE gestion_restaurant;

CREATE TABLE CATEGORIE (
    id_cat INT NOT NULL AUTO_INCREMENT,
    libelle_cat VARCHAR(30) NOT NULL UNIQUE,
    PRIMARY KEY(id_cat)
);

CREATE TABLE PRODUIT (
    id_pro INT NOT NULL AUTO_INCREMENT,
    nom_pro VARCHAR(50) NOT NULL,
    id_cat INT NOT NULL,
    prix_vente DECIMAL(10,2) NOT NULL CHECK(prix_vente > 0),
    stock_actu INT NOT NULL CHECK(stock_actu >= 0),
    seuil_alerte INT NOT NULL,
    PRIMARY KEY(id_pro),
    FOREIGN KEY(id_cat) REFERENCES CATEGORIE(id_cat)
);

CREATE TABLE MVT_STOCK(
    id_stock INT NOT NULL AUTO_INCREMENT,
    type CHAR(6) NOT NULL CHECK(type IN('ENTREE', 'SORTIE')),
    id_pro INT NOT NULL,
    qte_stock INT NOT NULL CHECK(qte_stock > 0),
    date DATE NOT NULL,
    motif VARCHAR(50),
    PRIMARY KEY(id_stock),
    FOREIGN KEY(id_pro) REFERENCES PRODUIT(id_pro)
);

CREATE TABLE COMMANDE (
    id_cmde INT NOT NULL AUTO_INCREMENT,
    date DATE NOT NULL,
    etat VARCHAR(8) NOT NULL DEFAULT 'EN_COURS' CHECK(etat IN('EN_COURS', 'VALIDEE', 'ANNULEE')),
    total DECIMAL(10,2) NOT NULL,
    PRIMARY KEY(id_cmde)
);

CREATE TABLE LIG_COMMANDE (
    id_lig INT NOT NULL AUTO_INCREMENT,
    id_cmde INT NOT NULL,
    id_pro INT NOT NULL,
    qte_lig INT NOT NULL CHECK(qte_lig > 0),
    prix_unit DECIMAL(10,2) NOT NULL,
    montant DECIMAL(10,2) AS (qte_lig * prix_unit) STORED,
    PRIMARY KEY(id_lig),
    FOREIGN KEY(id_pro) REFERENCES PRODUIT(id_pro),
    FOREIGN KEY(id_cmde) REFERENCES COMMANDE(id_cmde)
);

CREATE TABLE UTILISATEUR(
    id_util INT NOT NULL AUTO_INCREMENT,
    nom_util VARCHAR(50) NOT NULL UNIQUE,
    mdp VARCHAR(256) NOT NULL,
    PRIMARY KEY(id_util)
);
```

## 🚀 Installation et Configuration

### Prérequis

- Java 8 ou supérieur
- MySQL Server 5.7 ou supérieur
- NetBeans IDE (recommandé)

### Configuration

1. **Base de données**
   - Démarrer le serveur MySQL
   - Créer la base de données `gestion_restaurant`
   - Exécuter le script SQL ci-dessus

2. **Connexion**
   - Les paramètres de connexion sont dans `ConnectionDB.java` :
   ```java
   private static final String URL = "jdbc:mysql://localhost:3306/gestion_restaurant";
   private static final String USER = "root";
   private static final String PASSWORD = "";
   ```

3. **Driver MySQL**
   - Ajouter le driver JDBC MySQL au classpath
   - Télécharger : https://dev.mysql.com/downloads/connector/j/

### Compilation et Exécution

```bash
# Compilation
javac -cp ".:mysql-connector-java.jar" src/com/restaurant/Main.java

# Exécution
java -cp ".:src:mysql-connector-java.jar" com.restaurant.Main
```

## 📱 Fonctionnalités

### 🍽 Gestion des produits et catégories
- Ajouter, modifier, supprimer des catégories
- Ajouter, modifier, supprimer des produits
- Gestion des prix et des stocks
- Association produit-catégorie

### 📦 Gestion du stock
- Enregistrer les entrées de stock
- Enregistrer les sorties de stock
- Consulter l'historique des mouvements
- Alertes pour les stocks faibles

### 🛒 Gestion des commandes
- Créer de nouvelles commandes
- Ajouter des produits aux commandes
- Modifier les quantités
- Valider les commandes (avec déduction du stock)
- Annuler les commandes (avec restauration du stock)

### 📊 Statistiques et rapports
- Chiffre d'affaires par jour/période
- Top produits vendus
- Produits en rupture de stock
- Produits sous le seuil d'alerte
- Tableau de bord général

### 👤 Gestion des utilisateurs
- Création de comptes
- Authentification sécurisée
- Gestion des mots de passe

## 🔧 Règles métier

- Le prix de vente doit être strictement positif
- Le stock ne peut pas être négatif
- La quantité de mouvement doit être positive
- Interdire une sortie si la quantité dépasse le stock disponible
- Au moins une ligne pour valider une commande
- Mot de passe masqué à l'écran
- Login unique par utilisateur

## 🎯 Points forts

- ✅ Architecture MVC respectée
- ✅ Code commenté et maintenable
- ✅ Validation complète des données
- ✅ Gestion des exceptions
- ✅ Interface utilisateur intuitive
- ✅ Persistance des données
- ✅ Statistiques détaillées

## 🐛 Dépannage

### Problèmes courants

1. **Driver MySQL introuvable**
   - Vérifier que le driver MySQL JDBC est dans le classpath
   - Télécharger le driver depuis le site officiel MySQL

2. **Connexion refusée**
   - Vérifier que le serveur MySQL est démarré
   - Vérifier les identifiants dans `ConnectionDB.java`
   - Vérifier que la base de données existe

3. **Compilation échoue**
   - Vérifier que toutes les dépendances sont présentes
   - Vérifier la version de Java (minimum Java 8)

## 👨‍💻 Auteurs

Développé dans le cadre du projet de POO Java à l'IAI-TOGO (2025-2026)

## 📄 Licence

Ce projet est développé à des fins pédagogiques.
