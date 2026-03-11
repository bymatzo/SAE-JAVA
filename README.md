# SAÉ Java – Système de gestion des réservations IUT Lyon 2 / IUT Lumière

---

## 🗂️ Présentation générale

Ce projet est une **application de bureau Java** (Swing) permettant de gérer des réservations de ressources au sein d'un IUT. Elle a été développée dans le cadre d'une **SAÉ (Situation d'Apprentissage et d'Évaluation)**.

L'application permet de gérer :
- des **utilisateurs**
- des **ressources** (organisées par domaine)
- des **réservations** (de types différents)
- des **statistiques visuelles** (graphiques et KPIs)

Les données peuvent être **importées et exportées en CSV**.

---

## 🏗️ Architecture du projet

### Classe principale : `MainFrame`

`MainFrame` étend `JFrame` — c'est la **fenêtre principale** de l'application.  
Elle contient toute la logique d'interface et orchestre la navigation entre les pages.

### Classes associées (inférées depuis `MainFrame`)

| Classe | Rôle |
|---|---|
| `HistoriqueReservations` | Stocke et gère l'ensemble des données (utilisateurs, ressources, réservations) |
| `Utilisateur` | Représente un utilisateur |
| `Ressource` | Représente une ressource (nom, domaine, description) |
| `Domaine` | Représente le domaine d'une ressource |
| `Reservation` | Classe mère des réservations |
| `ReservationEmprunt` | Sous-type : emprunt |
| `ReservationCours` | Sous-type : cours |
| `ReservationMaintenance` | Sous-type : maintenance |
| `Statistique` | Calcule les KPIs et génère les graphiques (JFreeChart) |
| `CSVReader` | Lit et charge un fichier CSV dans l'historique |
| `CSVWriter` | Exporte l'historique vers un fichier CSV |

---

## 🖥️ Interface utilisateur

### Sidebar (menu latéral gauche)
- Couleur : `#24445` (bleu-gris foncé)
- 4 boutons de navigation : **Utilisateurs, Ressources, Réservations, Statistiques**
- 2 boutons en bas : **Charger CSV** / **Exporter CSV**

### Zone centrale (`mainPanel`)
Affiche dynamiquement le contenu de la page sélectionnée.  
La page par défaut au démarrage est **Utilisateurs**.

---

## 📄 Pages de l'application

### 1. Page Utilisateurs
- Tableau : Nom, Nombre de réservations, Statut
- Actions : **Ajouter**, **Modifier**, **Supprimer**
- Le nombre de réservations est calculé dynamiquement via `historique.rechercherParUtilisateur(u).size()`

### 2. Page Réservations
- Tableau : ID, Utilisateur, Ressource, Domaine, Début, Durée, Type
- Actions : **Ajouter**, **Modifier**, **Supprimer**
- La date est au format `yyyy-MM-dd HH:mm`
- La durée est affichée en langage naturel (ex : "2 heure(s) et 30 minute(s)")

### 3. Page Ressources
- Tableau : Nom, Domaine, Description, Nombre de réservations
- Actions : **Ajouter**, **Modifier**, **Supprimer**

### 4. Page Statistiques
- **4 KPI cards** : Réservations totales, Utilisateurs totaux, Durée moyenne, Domaine le plus réservé
- **4 graphiques JFreeChart** :
  - Top 10 utilisateurs les plus actifs
  - Répartition des types de réservation
  - Évolution des réservations par mois
  - Durée moyenne par domaine

---

## 🔄 Polymorphisme et héritage

Les réservations utilisent le **polymorphisme** :

```
Reservation (classe mère)
├── ReservationEmprunt
├── ReservationCours
└── ReservationMaintenance
```

La méthode `creerReservationDepuisType()` instancie le bon sous-type selon le choix de l'utilisateur :
```java
if (typeChoisi.equals("COURS")) return new ReservationCours(...);
if (typeChoisi.equals("MAINTENANCE")) return new ReservationMaintenance(...);
return new ReservationEmprunt(...); // par défaut
```

---

## 📁 Gestion des CSV

- **Import** : `CSVReader.charger(historique, chemin)` — ouvre un `JFileChooser`, recharge un nouvel `HistoriqueReservations` depuis le fichier
- **Export** : `CSVWriter.export(historique, chemin)` — sauvegarde tout l'historique, ajoute `.csv` si l'extension est manquante

---

## ⚙️ Détails techniques importants

### `serialVersionUID`
Chaque classe interne (`DefaultTableModel` anonyme) déclare un `serialVersionUID` — bonne pratique pour la sérialisation Java.

### `isCellEditable()`
Toutes les tables ont `isCellEditable()` retournant `false` → les cellules **ne sont pas modifiables directement** dans le tableau.

### `rafraichirPageCourante()`
Après chaque ajout/modification/suppression, la page courante est **rechargée entièrement** via un `switch` sur `pageCourante`.

### `dureeTexte(int minutes)`
Convertit des minutes en texte lisible : semaines → jours → heures → minutes.

### Thread Swing (`EventQueue.invokeLater`)
Le démarrage se fait dans l'EDT (Event Dispatch Thread) — bonne pratique Swing pour éviter les problèmes de concurrence.

### Taille de fenêtre
La fenêtre démarre en `MAXIMIZED_BOTH` (plein écran), avec une taille de repli de `1400x900`.

---

## ❓ Questions/Réponses préparées pour l'oral

**Q : Pourquoi étendre `JFrame` plutôt que d'en créer une instance ?**  
R : Étendre `JFrame` permet d'encapsuler toute la logique de l'interface dans une seule classe, en accédant directement aux méthodes de `JFrame` sans préfixe. C'est une convention courante en Swing.

**Q : Pourquoi utilise-t-on `EventQueue.invokeLater` ?**  
R : Swing n'est pas thread-safe. `invokeLater` garantit que la création de la fenêtre se fait sur l'EDT (Event Dispatch Thread), le seul thread autorisé à modifier l'interface graphique.

**Q : Qu'est-ce que le polymorphisme ici ?**  
R : Les trois types de réservations (`Emprunt`, `Cours`, `Maintenance`) héritent de `Reservation`. On peut les manipuler via la référence de type `Reservation`, et chaque sous-classe peut avoir son propre comportement (ex. calcul de prix, contraintes métier).

**Q : Pourquoi `isCellEditable` retourne `false` ?**  
R : Pour que l'utilisateur ne puisse pas modifier les données directement dans le tableau. Toutes les modifications passent par les boutons dédiés et la logique métier associée.

**Q : Comment fonctionne le chargement CSV ?**  
R : Un `JFileChooser` permet à l'utilisateur de choisir un fichier. Un nouvel `HistoriqueReservations` est créé (repart de zéro), puis `CSVReader.charger()` le remplit avec les données du fichier.

**Q : Pourquoi recréer un nouvel `HistoriqueReservations` à chaque import ?**  
R : Pour éviter les doublons ou les données résiduelles. Repartir d'un objet vide garantit que les données affichées correspondent exactement au fichier importé.

**Q : Qu'est-ce que `JFreeChart` ?**  
R : Une bibliothèque Java open-source permettant de générer des graphiques (barres, camemberts, courbes...). Elle est intégrée via `ChartPanel` dans les panneaux Swing.

**Q : Comment est gérée la navigation entre les pages ?**  
R : La variable `pageCourante` stocke le nom de la page active. `mainPanel.removeAll()` vide le panneau central, puis on reconstruit la nouvelle page et on appelle `revalidate()` + `repaint()` pour forcer l'affichage.

---

## 🛠️ Technologies utilisées

- **Java Swing** — interface graphique
- **JFreeChart** — graphiques statistiques
- **`java.time`** — gestion des dates (`LocalDateTime`, `DateTimeFormatter`)
- **CSV** — persistance des données

---

*Projet SAÉ — IUT Lumière Lyon 2*
