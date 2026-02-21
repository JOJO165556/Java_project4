# Guide d'Utilisation — Gestion Restaurant

## 1. Connexion

Lancez l'application et saisissez votre identifiant et mot de passe.

- **Rôle Admin** : accès complet (tableau de bord, produits, stock, statistiques, utilisateurs, sauvegarde).
- **Rôle Caissier** : accès aux commandes uniquement.

> Au premier lancement (base vide), le formulaire de création du compte Administrateur s'affiche automatiquement.

---

## 2. Gestion des commandes

1. Cliquez sur **Commandes** dans la sidebar.
2. Cliquez sur **Nouvelle commande** pour créer une commande.
3. Tapez le nom d'un produit dans le champ de recherche et sélectionnez-le.
4. Ajustez la quantité puis cliquez **Ajouter**.
5. Cliquez **Valider** pour finaliser (stock déduit automatiquement).
6. Cliquez **Imprimer** pour générer le reçu.

> Une commande vide ne peut pas être validée. Une annulation restitue le stock.

---

## 3. Gestion des produits (Admin)

- **Ajouter** : remplir le formulaire (nom, catégorie, prix, stock, seuil d'alerte) puis cliquer **Ajouter**.
- **Modifier** : sélectionner un produit dans la liste puis cliquer **Modifier**.
- **Supprimer** : impossible si le produit est lié à des commandes existantes.
- **Import CSV** : importer une liste de produits depuis un fichier `.csv`.
- **Export CSV** : exporter la liste actuelle vers un fichier `.csv`.

---

## 4. Gestion du stock (Admin)

1. Cliquer sur **Gestion Stock** dans la sidebar.
2. Sélectionner un produit, choisir le type (Entrée/Sortie), saisir la quantité et le motif.
3. Valider le mouvement.

> Les produits dont le stock est inférieur au seuil d'alerte apparaissent en orange (alerte) ou rouge (rupture). Un badge rouge s'affiche dans la sidebar.

---

## 5. Statistiques (Admin)

- **CA journalier** et **CA par période** (plage de dates personnalisable).
- **Top produits** vendus (graphique en barres).
- Export **PDF** et **CSV** disponibles.

---

## 6. Gestion des utilisateurs (Admin)

Accessible via **Utilisateurs** dans la sidebar.

- **Ajouter** un employé (nom, mot de passe, rôle).
- **Modifier** un employé sélectionné.
- **Supprimer** un employé *(impossible de supprimer son propre compte)*.
- **🔑 Changer mon MDP** : modifie directement le mot de passe de l'admin connecté (saisie de l'ancien mot de passe requise).

---

## 7. Sauvegarde & Restauration (Admin)

Accessible via **Sauvegarde** dans la sidebar.

- **Exporter** : crée une copie du fichier `gestion_restaurant.db` à l'emplacement choisi.
- **Restaurer** : remplace la base actuelle par un fichier de sauvegarde. ⚠️ Les données actuelles sont écrasées.

---

## 8. Dépannage

| Problème | Solution |
|---|---|
| Impossible de charger les données | Vérifier que `data/gestion_restaurant.db` est présent et accessible |
| Produit impossible à supprimer | Produit lié à des commandes — archivez-le ou supprimez les commandes liées |
| Impression sans résultat | Vérifier qu'une imprimante système est configurée |
| Export CSV vide | Vérifier qu'il existe des commandes validées pour la période |
