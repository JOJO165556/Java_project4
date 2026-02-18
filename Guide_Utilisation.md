# Guide d'Utilisation - Gestion Restaurant

## 1. Introduction
Ce logiciel est une solution complète pour la gestion d'un restaurant. Il permet de centraliser les ventes, de suivre les stocks en temps réel et d'analyser les performances via un tableau de bord dynamique.

## 2. Démarrage et Installation
### Prérequis
- **Java Runtime Environment (JRE)** : Version 11 ou supérieure.
- **Serveur MySQL** : Le serveur doit être actif (ex: via XAMPP ou WAMP).
- **Base de données** : Le script `database_setup.sql` doit être importé.

### Connexion
1. **Identifiants** : Saisissez votre nom d'utilisateur et mot de passe.
2. **Options** : Cochez "Se souvenir de moi" pour que votre nom d'utilisateur soit mémorisé lors de la prochaine ouverture de l'application.
3. **Première utilisation** : Si vous n'avez pas de compte, cliquez sur "Créer un compte".

## 3. Navigation Principale
L'interface s'articule autour d'une barre latérale gauche :
- **🏠 Accueil** : Tableau de bord affichant le chiffre d'affaires du jour, les ventes effectuées, les alertes de stock et le total des produits.
- **💰 Commandes** : Interface de prise de commande et suivi du panier.
- **📦 Produits** : Catalogue complet (Nom, Catégorie, Prix, Stock).
- **📦 Gestion Stock** : Historique et saisie des mouvements (Entrées/Sorties).
- **📉 Statistiques** : Analyses approfondies par période et alertes de rupture.

## 4. Processus de Vente (Commandes)
Pour passer une commande :
1. Cliquez sur **"Nouvelle Commande"**.
2. **Choix des produits** : Sélectionnez un produit dans la liste déroulante et saisissez la quantité souhaitée.
3. **Ajout** : Cliquez sur le bouton d'ajout pour mettre le produit dans le panier.
4. **Validation** : Une fois le panier complet, cliquez sur **"Valider la commande"**.
   - Le stock des produits sera alors automatiquement déduit.
   - La commande passera en état "VALIDEE".
5. **Annulation** : Vous pouvez annuler une commande en cours tant qu'elle n'a pas été validée.

## 5. Gestion des Stocks
### Mouvements Manuels
Outre les ventes automatiques, vous pouvez enregistrer :
- **Entrées (Achats)** : Pour augmenter le stock lors d'une livraison fournisseur.
- **Sorties (Pertes)** : Pour déduire le stock en cas de casse, péremption ou erreur.
- **Motif** : Il est obligatoire de saisir un motif pour chaque mouvement manuel afin de garantir une traçabilité parfaite.

### Système d'Alertes
Le système surveille trois états critiques :
- **Normal** : Stock suffisant.
- **Alerte** : Le stock est égal ou inférieur au seuil d'alerte défini. Le texte passe en orange.
- **Rupture** : Le stock est à zéro. Le texte passe en rouge.

## 6. Dépannage (FAQ)
### L'application ne se lance pas ou affiche une erreur de connexion ?
- Vérifiez que votre serveur MySQL est bien démarré.
- Assurez-vous que les identifiants de connexion dans `ConnectionDB.java` (si vous modifiez le code) correspondent à votre serveur local.

### Les statistiques ne s'affichent pas ?
- Cliquez sur "Actualiser" dans les onglets spécifiques ou revenez à l'Accueil pour déclencher la mise à jour automatique.

---
*Logiciel de gestion restaurant - Version 1.1*
