# Gestion Restaurant

Application de bureau Java pour la gestion d'un restaurant, développée avec une architecture **MVC 4 couches** et une interface Swing moderne.

## 🏗️ Architecture

```
com.restaurant/
├── model      → Entités (Categorie, Produit, Commande, LigneCommande, MouvementStock, Utilisateur + enums)
├── dao        → Accès SQLite JDBC (ConnectionDB, CategorieDAO, ProduitDAO, CommandeDAO…)
├── service    → Logique métier (AuthService, CommandeService, StockService, StatistiqueService…)
├── controller → Orchestration Vue ↔ Service (Login, Produit, Commande, Stock, Stat, Admin, DB)
├── view       → Interfaces Swing (SplashScreen, LoginView, MainView, ProduitView…)
└── utils      → Design System, Validation, PasswordUtils, DateUtils, ResourceUtils
```

## 🗄️ Base de données (SQLite)

Base de données autonome dans `data/gestion_restaurant.db` — aucun serveur requis.

```sql
CATEGORIE      (id_cat, libelle_cat)
PRODUIT        (id_pro, nom_pro, id_cat, prix_vente, stock_actu, seuil_alerte)
COMMANDE       (id_cmde, date, etat, total)
LIGNE_COMMANDE (id_lig, id_cmde, id_pro, qte_lig, prix_unit)
MVT_STOCK      (id_mvt, id_pro, type, quantite, date, motif)
UTILISATEUR    (id_uti, nom_util, mdp, role)
```

## 🚀 Installation

### Prérequis

- **Java 21+**
- **Ant** (pour le build manuel)
- Bibliothèques dans `/lib` : JFreeChart, iText, Apache POI, Log4j2, SQLite JDBC

### Build manuel

```bash
ant jar        # Compilation
ant package    # Installeur Linux (deb/rpm)
```

### Releases automatiques (GitHub)

Chaque tag déclenche un build GitHub Actions qui publie automatiquement :

```bash
git tag v1.0.0
git push origin v1.0.0
```

➡️ **[Télécharger la dernière version](../../releases/latest)**

| Plateforme | Format |
|---|---|
| 🐧 Debian/Ubuntu | `.deb` |
| 🧅 Fedora/RHEL | `.rpm` |
| 🪟 Windows | `.exe` |

## 📱 Fonctionnalités

### 🔐 Authentification & Sécurité
- Connexion avec identifiant + mot de passe (SHA-256)
- Rôles **Admin** et **Caissier** avec accès différenciés
- Déconnexion automatique après 10 min d'inactivité

### 🛒 Commandes
- Création, ajout de produits (auto-complétion), modification de quantité
- Validation atomique (stock déduit + état mis à jour en transaction)
- Annulation avec restitution du stock
- Impression : **Reçu Client**, **Format Gestion**, ou les deux

### 📦 Stock & Produits
- CRUD catégories et produits, Import/Export **CSV**
- Mouvements de stock (entrée/sortie) avec historique filtrable
- Badge d'alerte sidebar si stock critique

### 📊 Statistiques
- CA journalier / par période, top produits
- Graphiques JFreeChart, export **PDF** et **CSV**

### 👤 Administration (Admin uniquement)
- Gestion des comptes employés (ajout, modification, suppression)
- 🚫 Auto-suppression bloquée — impossible de supprimer son propre compte
- 🔑 **Changer mon mot de passe** directement depuis le panneau admin
- **Sauvegarde & Restauration** du fichier SQLite

## 🔧 Règles métier

- Prix de vente > 0, stock ≥ 0, quantité mouvement > 0
- Sortie refusée si stock insuffisant
- Commande vide non validable
- Produit lié à des commandes non supprimable
- Login unique par utilisateur

## 🎯 Points forts

- ✅ **Zéro Configuration** : base SQLite autonome, aucun serveur
- ✅ **Multi-plateforme** : releases `.deb`, `.rpm`, `.exe` via GitHub Actions
- ✅ Architecture MVC rigoureuse, transactions atomiques
- ✅ Logging Log4j2, statistiques avancées, exports PDF/CSV

## 🐛 Dépannage

| Problème | Solution |
|---|---|
| DB introuvable | Vérifier que `data/gestion_restaurant.db` existe |
| Driver SQLite manquant | Vérifier que `sqlite-jdbc-*.jar` est dans `/lib` |
| Produit non supprimable | Produit lié à des commandes — archivez-le |
| Compilation échoue | Vérifier les JARs dans `/lib` et Java 21+ |
