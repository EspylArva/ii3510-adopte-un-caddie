# Réunion 1 - Planification de projet

## Architecture & Gestion de projet
Architecture à implémenter : MVVM  
[Documentation](https://medium.com/hongbeomi-dev/create-android-app-with-mvvm-pattern-simply-using-android-architecture-component-529d983eaabe)

## Features à implémenter
### Login
Feature pas nécessaire à implémenter. On peut garder les informations (panier) en local.  
On pourrait implémenter cette feature dans des prochaines itérations, mais ça ne fera pas partie du MVP.

### Paiement
[API Google Payment](https://developers.google.com/pay/api)  
Implémentation à voir pour plus tard, selon si des frais sont prélevés à chaque transaction :
- Si oui, on gardera le paiement en PoC
- Si non, on peut implémenter cette feature et se faire des virements entre nous (régularisés) pour faire des tests

Le paiement nécessite cependant de récupérer des prix, et donc d'avoir une base de données.

On devra donc potentiellement effectuer du scrapping pour générer notre propre BDD et éventuellement notre propre API.  
Certaines APIs existent déjà, mais elles sont limitées (en traffic, en taille et surement en fonctionnalités), comme [celle-ci](https://rapidapi.com/Datagram/api/products?endpoint=5a60769be4b0fe142a18deeb)

### Settings
Un problème se pose pour trouver le prix d'un article : pour un produit donné, le prix peut varier d'un magasin à un autre. On a donc besoin de sélectionner un magasin dans lequel on cherchera le prix de l'article.

On pourrait éventuellement sélectionner ou proposer automatiquement un magasin selon la géolocalisation de l'utilisateur et les coordonnées géographiques du magasin.

### Panier
Feature du MVP. Nécessite un écran pour gérer le panier et un écran pour ajouter un article.
#### Gestion du panier
(Reprise du README) Besoin de pouvoir ajouter ou retirer des produits du panier. Eventuellement, ajuster la quantité de produits dans le panier.
#### Scan des produits
On peut utiliser l'[API Google](https://developers.google.com/android/reference/com/google/android/gms/vision/barcode/Barcode).

# Tâches attribuées
- Lucas :
    - Regarder l'exemple d'API
	- Scrapper si nécessaire
	- Générer une BDD et une API si nécessaire
- Antoine :
    - Logique du panier
- Tchong-Kite :
	- Wireframe
	- Géopositionnement
- Reste à faire :
    - Scan des produits (code-barre)
    - Settings
    - Paiement
- Idées Futures :
    - Comparateur de prix
    - Login

Prochaine réunion prévue le 11 novembre 2020.
    