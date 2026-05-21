package fr.univ_amu.iut.bonus10;

import java.util.ArrayList;
import java.util.List;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Point2D;
import javafx.scene.layout.ColumnConstraints;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Priority;
import javafx.scene.layout.RowConstraints;

/**
 * Bonus 10 - étape 3 : plateau de jeu complet (modèle + logique).
 *
 * <p>L'othellier est un composant Java auto-suffisant : il étend {@link GridPane} et instancie les
 * 64 {@link Case} dans son constructeur. Toute la logique du jeu (validité d'un coup, capture dans
 * les huit directions, fin de partie) est encapsulée ici, ce qui en fait un beau cas d'usage MVC :
 * la vue FXML n'a qu'à inclure cet othellier, le contrôleur n'aura qu'à câbler quelques bindings
 * sur les propriétés exposées (étape 4).
 *
 * <p><b>Méthodes fournies :</b> le moteur de capture ({@link #casesCapturable(Case)} et {@link
 * #casesCapturable(Case, Point2D)}) ainsi que {@link #estIndicesValides(int, int)} sont livrés tels
 * quels. Ils parcourent les huit directions pour identifier les pions adverses encadrés. Tout le
 * reste (initialisation, démarrage de partie, gestion du tour) s'appuie dessus et est à votre
 * charge.
 */
public class Othellier extends GridPane {

  /** Les huit directions de propagation (horizontales, verticales, diagonales). */
  private static final Point2D[] DIRECTIONS = {
    new Point2D(1, 0),
    new Point2D(1, 1),
    new Point2D(0, 1),
    new Point2D(-1, 1),
    new Point2D(-1, 0),
    new Point2D(-1, -1),
    new Point2D(0, -1),
    new Point2D(1, -1)
  };

  /** Taille du plateau (8x8 dans la version standard du jeu). */
  public static final int TAILLE = 8;

  // TODO bonus 10 étape 3.1 : déclarer les données membres privées suivantes :
  // - cases : une matrice Case[TAILLE][TAILLE] qui représente le plateau de jeu
  // - joueurCourant : un ObjectProperty<Joueur> initialisé à Joueur.NOIR (NOIR
  // commence toujours)
  // - partieTerminee : un BooleanProperty initialisé à false
  private final Case[][] cases = new Case[TAILLE][TAILLE];

  private final ObjectProperty<Joueur> joueurCourant =
      new SimpleObjectProperty<>(this, "joueurCourant", Joueur.NOIR);

  private final BooleanProperty partieTerminee =
      new SimpleBooleanProperty(this, "partieTerminee", false);

  /**
   * Gestionnaire d'événement partagé par toutes les cases du plateau.
   *
   * <p>Une seule instance est réutilisée pour les 64 boutons : c'est le motif courant en JavaFX
   * pour ne pas multiplier inutilement les écouteurs.
   */
  private final EventHandler<ActionEvent> caseListener =
      event -> {
        // TODO bonus 10 étape 3.10 : implémenter ce gestionnaire avec une expression
        // lambda :
        // 1. récupérer la case ayant produit l'événement avec (Case) event.getSource()
        // 2. vérifier que la position choisie est jouable avec estPositionJouable(...)
        // 3. si oui, appeler jouer(...) pour poser le pion, déclencher les captures et
        // passer la
        // main au joueur suivant. Sinon, on ignore le clic (le joueur courant reste le
        // même).
        Case caseSelectionnee = (Case) event.getSource();
        if (estPositionJouable(caseSelectionnee)) {
          jouer(caseSelectionnee);
        }
      };

  /**
   * Construit un othellier neuf : applique les contraintes de la grille, instancie les 64 cases,
   * branche l'écouteur et démarre une nouvelle partie.
   */
  public Othellier() {
    // TODO bonus 10 étape 3.2 : initialiser le composant graphique :
    // 1. fixer setHgap(1) et setVgap(1) pour aérer le plateau
    // 2. (optionnel) appeler setStyle("-fx-background-color: #145830;") pour le
    // fond vert foncé
    // 3. appeler adapterLesLignesEtColonnes() pour fixer les contraintes de la
    // grille
    // 4. appeler remplirOthellier() pour créer les 64 cases et brancher l'écouteur
    // partagé
    // 5. appeler nouvellePartie() pour positionner la configuration de départ
    setHgap(1);
    setVgap(1);
    setStyle("-fx-background-color: #145830;");
    adapterLesLignesEtColonnes();
    remplirOthellier();
    nouvellePartie();
  }

  public ObjectProperty<Joueur> joueurCourantProperty() {
    return joueurCourant;
  }

  public Joueur getJoueurCourant() {
    return joueurCourant.get();
  }

  public BooleanProperty partieTermineeProperty() {
    return partieTerminee;
  }

  public Case getCase(int ligne, int colonne) {
    return cases[ligne][colonne];
  }

  private void adapterLesLignesEtColonnes() {
    for (int i = 0; i < TAILLE; i++) {
      ColumnConstraints column = new ColumnConstraints();
      column.setHgrow(Priority.ALWAYS);
      column.setPercentWidth(100.0 / TAILLE);
      getColumnConstraints().add(column);

      RowConstraints row = new RowConstraints();
      row.setVgrow(Priority.ALWAYS);
      row.setPercentHeight(100.0 / TAILLE);
      getRowConstraints().add(row);
    }
  }

  /** Instancie les 64 cases, leur branche l'écouteur partagé et les ajoute à la grille. */
  private void remplirOthellier() {
    // TODO bonus 10 étape 3.3 : pour chaque (ligne, colonne) de la matrice :
    // 1. instancier une Case c = new Case(ligne, colonne)
    // 2. brancher l'écouteur partagé via c.setOnAction(caseListener)
    // 3. mémoriser la case dans cases[ligne][colonne]
    // 4. ajouter la case à la grille avec add(c, colonne, ligne)
    // (attention à l'ordre : la méthode add de GridPane prend (column, row) !)
    for (int ligne = 0; ligne < TAILLE; ligne++) {
      for (int colonne = 0; colonne < TAILLE; colonne++) {
        Case c = new Case(ligne, colonne);
        c.setOnAction(caseListener);
        cases[ligne][colonne] = c;
        add(c, colonne, ligne);
      }
    }
  }

  private void positionnerPionsDebutPartie() {
    // TODO bonus 10 étape 3.4 : placer les quatre pions du début de partie sur le
    // plateau.
    int m = TAILLE / 2;
    placer(cases[m - 1][m - 1], Joueur.BLANC);
    placer(cases[m - 1][m], Joueur.NOIR);
    placer(cases[m][m - 1], Joueur.NOIR);
    placer(cases[m][m], Joueur.BLANC);
  }

  public void nouvellePartie() {
    // TODO bonus 10 étape 3.5 : enchaîner les étapes suivantes :
    vider();
    Joueur.initialiserScores();
    positionnerPionsDebutPartie();
    joueurCourant.set(Joueur.NOIR);
    partieTerminee.set(false);
  }

  private void vider() {
    // TODO bonus 10 étape 3.6 : parcourir toutes les cases et leur affecter
    // Joueur.PERSONNE via
    // setPossesseur.
    for (int ligne = 0; ligne < TAILLE; ligne++) {
      for (int colonne = 0; colonne < TAILLE; colonne++) {
        cases[ligne][colonne].setPossesseur(Joueur.PERSONNE);
      }
    }
  }

  private void jouer(Case caseSelectionnee) {
    // TODO bonus 10 étape 3.7 : orchestrer un coup en trois temps :
    placer(caseSelectionnee, joueurCourant.get());
    for (Case c : casesCapturable(caseSelectionnee)) {
      capturer(c);
    }
    tourSuivant();
  }

  private void placer(Case c, Joueur joueur) {
    // TODO bonus 10 étape 3.7bis : changer le possesseur de la case
    // (c.setPossesseur(joueur))
    // et incrémenter le score du joueur (joueur.incrementerScore()).
    c.setPossesseur(joueur);
    joueur.incrementerScore();
  }

  private void capturer(Case caseCapturee) {
    // TODO bonus 10 étape 3.8 : effectuer la capture d'un pion :
    Joueur ancien = caseCapturee.getPossesseur();
    ancien.decrementerScore();
    Joueur nouveau = ancien.suivant();
    caseCapturee.setPossesseur(nouveau);
    nouveau.incrementerScore();
  }

  private void tourSuivant() {
    // TODO bonus 10 étape 3.9 : implémenter la rotation des joueurs :
    Joueur prochain = joueurCourant.get().suivant();
    joueurCourant.set(prochain);
    if (!peutJouer()) {
      joueurCourant.set(prochain.suivant());
    }
    if (!peutJouer()) {
      partieTerminee.set(true);
    }
  }

  public boolean estPositionJouable(Case caseSelectionnee) {
    // TODO bonus 10 étape 3.11 : retourner true si la case est vide
    // (caseSelectionnee.getPossesseur() == Joueur.PERSONNE) ET si la liste
    // retournée par
    // casesCapturable(caseSelectionnee) n'est pas vide (au moins un pion à
    // capturer).
    return caseSelectionnee.getPossesseur() == Joueur.PERSONNE
        && !casesCapturable(caseSelectionnee).isEmpty();
  }

  public List<Case> casesJouables() {
    // TODO bonus 10 étape 3.12 : parcourir toutes les cases et retourner la liste
    // de celles qui
    // sont jouables par le joueur courant. Indice : utiliser
    // estPositionJouable(...).
    List<Case> jouables = new ArrayList<>();
    for (int ligne = 0; ligne < TAILLE; ligne++) {
      for (int colonne = 0; colonne < TAILLE; colonne++) {
        if (estPositionJouable(cases[ligne][colonne])) {
          jouables.add(cases[ligne][colonne]);
        }
      }
    }
    return jouables;
  }

  public boolean peutJouer() {
    // TODO bonus 10 étape 3.13 : retourner true si casesJouables() n'est pas vide.
    return !casesJouables().isEmpty();
  }

  public List<Case> casesCapturable(Case caseSelectionnee) {
    List<Case> resultat = new ArrayList<>();
    for (Point2D direction : DIRECTIONS) {
      resultat.addAll(casesCapturable(caseSelectionnee, direction));
    }
    return resultat;
  }

  private List<Case> casesCapturable(Case caseSelectionnee, Point2D direction) {
    List<Case> casesCapturable = new ArrayList<>();

    int indiceLigne = caseSelectionnee.getLigne() + (int) direction.getY();
    int indiceColonne = caseSelectionnee.getColonne() + (int) direction.getX();

    while (estIndicesValides(indiceLigne, indiceColonne)) {
      Joueur possesseur = cases[indiceLigne][indiceColonne].getPossesseur();
      if (possesseur != joueurCourant.get().suivant()) {
        break;
      }
      casesCapturable.add(cases[indiceLigne][indiceColonne]);
      indiceLigne += direction.getY();
      indiceColonne += direction.getX();
    }

    if (estIndicesValides(indiceLigne, indiceColonne)
        && cases[indiceLigne][indiceColonne].getPossesseur() == joueurCourant.get()) {
      return casesCapturable;
    }
    return new ArrayList<>();
  }

  private boolean estIndicesValides(int indiceLigne, int indiceColonne) {
    return estIndiceValide(indiceLigne) && estIndiceValide(indiceColonne);
  }

  private boolean estIndiceValide(int indice) {
    return indice >= 0 && indice < TAILLE;
  }
}
