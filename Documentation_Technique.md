# Documentation Technique - Gestion Restaurant

## 1. Architecture Globale
L'application repose sur le modèle de conception **MVC (Modèle-Vue-Contrôleur)** complété par une couche **Service** et une couche **DAO (Data Access Object)** pour une maintenance simplifiée.

### Hiérarchie des Paquetages
- `com.restaurant.model` : Structures de données (Classes Java simples).
- `com.restaurant.dao` : Requêtes SQL brutes utilisant JDBC.
- `com.restaurant.service` : Logique métier et orchestrateur de données.
- `com.restaurant.controller` : Gestion des événements UI et liens avec les services.
- `com.restaurant.view` : Interfaces Swing (Vues et Sous-Panel).
- `com.restaurant.utils` : Utilitaires système et design.

## 2. Base de Données (Schéma)
Le système utilise MySQL avec les tables principales suivantes :
- **`UTILISATEUR`** : Stocke les accès (nom, mot de passe haché).
- **`CATEGORIE`** : Regroupement logique des produits.
- **`PRODUIT`** : Catalogue incluant les prix, le stock actuel et le seuil d'alerte.
- **`COMMANDE`** : En-tête des ventes (Date, État, Total).
- **`LIG_COMMANDE`** : Détail des produits vendus par commande.
- **`MVT_STOCK`** : Journal complet des entrées/sorties pour l'audit.

## 3. Logique Métier Critique
### Mise à jour du Stock via Commande
Lorsqu'une commande est validée :
1. Le service parcourt chaque ligne de la commande.
2. Pour chaque produit, la quantité vendue est déduite du stock actuel.
3. Un mouvement de type `SORTIE` est automatiquement enregistré dans `MVT_STOCK` avec le motif "Vente #ID_COMMANDE".

### Sécurité (SHA-256)
Nous n'enregistrons aucun mot de passe en clair. La classe `PasswordUtils` génère une empreinte numérique (Hash) unique de 64 caractères hexadécimaux pour chaque mot de passe. Cette opération est irréversible, garantissant que même en cas d'accès direct à la base, les mots de passe restent secrets.

### Validation des Entrées
Le système effectue désormais des contrôles stricts sur les données saisies :
- **Quantités (> 0)** : Toutes les opérations de vente, d'entrée ou de sortie de stock exigent une quantité strictement supérieure à 0. 
- **Gestion des Erreurs** : En cas de saisie invalide, une exception `SQLException` ou `Exception` est levée par la couche Service, capturée par le Contrôleur et affichée de manière compréhensible à l'utilisateur via une fenêtre d'alerte.

## 4. Interface et Design
### Design System
Plutôt que d'utiliser des couleurs codées en dur, l'application utilise `DesignSystem.java`. Cela permet de :
- Modifier la palette de couleurs globale à un seul endroit.
- Uniformiser les polices et espacements sur tous les formulaires.

### État de l'Application (Status Bar)
La barre de statut en bas de la fenêtre principale affiche :
- Un indicateur coloré (Point de statut).
- Le nom de l'utilisateur (via `UtilisateurConnecte`).
- La date système.
- Les messages de fonctionnement (ex: "Connexion réussie", "Produit ajouté").

## 5. Persistance des Préférences
Le logiciel utilise l'API `Preferences` de Java pour gérer l'option "Se souvenir de moi".
- **Nom d'utilisateur** : Mémorisé localement si la case est cochée.
- **État** : Le nom est automatiquement injecté dans le champ de saisie au prochain démarrage.

## 6. Identité Visuelle
Une icône personnalisée (`icon.png`) est embarquée dans le dossier source. Elle est appliquée à la barre des tâches et à toutes les fenêtres de l'application via la méthode `defineIcon()` présente dans `MainView` et `LoginView`.

---
*Documentation Technique - Gestion Restaurant*
