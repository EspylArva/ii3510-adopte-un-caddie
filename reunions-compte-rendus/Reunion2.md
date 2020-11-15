# Réunion 2 - Premiers développements (11/11/2020)
## Point sur ce qui a été fait
### Panier
L'affichage des articles du panier se fait avec un RecyclerView, conformément au wireframe donné. 

L'utilisateur peut donc afficher une liste d'articles, avec son nom, la quantité et leur prix.

Un bouton redirigeant vers la page de paiement a été implémenté.

Un bouton redirigeant vers la page de scan d'article a été implémenté.

La sélection de magasin a sommairement été implémentée, mais nécessitera un re-design.

*Fonctionnalités manquantes* :
- Changement du nombre d'article
- Retirer un article
- Ajout d'article sans passer par le scan

*Fonctionnalité à retravailler* :
- Sélection de magasin

### Géolocalisation
Afin de proposer automatiquement le magasin le plus proche, nous souhaitions implémenter la géolocalisation. Celle-ci fonctionne partiellement : on peut récupérer la dernière position connue, mais actuellement, on ne peut pas faire de requête de la position actuelle.

Toutes les problématiques de permissions et de service non-activé ont été prises en compte.

*Fonctionnalités manquantes* :
- Requête de position
- Entrer une adresse manuellement pour avoir une géolocalisation approximative

### Paramètres
De manière générale, il serait bon d'avoir un stockage des paramètres et panier dans la mémoire de l'appareil en utilisant des sharedPreferences.

La page de paramètres n'a pas encore été commencée.

### Scan du code-barre
Le code est en cours d'écriture, l'implémentation dans une activité est gérée, mais il faut à présent l'adapter à un fragment.

Il faudra également être capable de gérer la réponse du scan, et simuler une entrée de code-barre manuelle

*Fonctionnalités manquantes* :
- Ajout sans scan
- Implémentation dans un fragment
- Traiter le numéro de code-barre

### API
La structure de base est prête. Il faudra donc rajouter des endpoints pour pouvoir faire appel à l'API.

A l'heure actuelle, trois endpoints sont disponibles :
- produit(s) par id
- produit(s) par nom
- magasins par id du produit

## Pages à commencer
- Page de paiement
- Page de paramètres
- Drawer
