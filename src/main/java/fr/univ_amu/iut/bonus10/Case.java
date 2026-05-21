package fr.univ_amu.iut.bonus10;

import javafx.scene.control.Button;
import javafx.scene.image.ImageView;

/**
 * Bonus 10 - étape 2 : une case du plateau de jeu.
 *
 * <p>Pour réaliser le plateau de jeu, il nous faut des boutons qui se souviennent de leur position
 * dans l'othellier. Au moment de leur construction, de tels boutons reçoivent les valeurs des
 * indices ligne et colonne qui définissent leur placement dans la matrice. En plus de ces
 * coordonnées, il faut connaître le joueur qui possède la case pour y dessiner l'image de son
 * jeton.
 *
 * <p>Cette classe étend {@link Button} : chaque case est ainsi cliquable et peut recevoir un
 * gestionnaire d'événement (le contrôleur d'othellier en branchera un seul, partagé par les 64
 * cases).
 */
class Case extends Button {

  // TODO bonus 10 étape 2.1 : déclarer les données membres privées suivantes :
  // - ligne : int - indice de ligne dans la matrice
  // - colonne : int - indice de colonne dans la matrice
  // - imageView : ImageView - composant graphique qui affiche le pion (image du
  // Joueur)
  // - possesseur : Joueur - joueur à qui appartient la case (initialisée à
  // Joueur.PERSONNE)
  private int ligne;
  private int colonne;
  private ImageView imageView;
  private Joueur possesseur = Joueur.PERSONNE;

  /**
   * Construit une case à la position {@code (ligne, colonne)}. Par défaut, la case n'appartient à
   * personne et son pion affiché est celui du joueur {@link Joueur#PERSONNE} (image transparente).
   */
  Case(int ligne, int colonne) {
    // TODO bonus 10 étape 2.2 : initialiser la case :
    // 1. mémoriser les paramètres ligne et colonne dans les données membres
    // correspondantes
    // 2. créer l'imageView avec new ImageView(Joueur.PERSONNE.getImage()) puis
    // fixer ses
    // dimensions (par exemple setFitWidth(56), setFitHeight(56),
    // setPreserveRatio(true))
    // 3. appeler setGraphic(imageView) pour que le bouton affiche le pion
    // 4. appeler setMaxSize(Double.MAX_VALUE, Double.MAX_VALUE) pour que le bouton
    // remplisse sa
    // cellule de la grille
    // 5. (optionnel) appeler setStyle("-fx-background-color: #1e6f3f; ...") pour
    // donner au
    // bouton l'aspect d'une case d'othellier vert foncé
    this.ligne = ligne;
    this.colonne = colonne;
    imageView = new ImageView(Joueur.PERSONNE.getImage());
    imageView.setFitWidth(56);
    imageView.setFitHeight(56);
    imageView.setPreserveRatio(true);
    setGraphic(imageView);
    setMaxSize(Double.MAX_VALUE, Double.MAX_VALUE);
    setStyle("-fx-background-color: #1e6f3f;");
  }

  /** Renvoie le joueur qui possède actuellement la case (NOIR, BLANC ou PERSONNE). */
  Joueur getPossesseur() {
    return possesseur;
  }

  /**
   * Modifie le joueur qui possède la case et met à jour le pion affiché par {@code imageView}.
   *
   * <p>C'est cette méthode qu'utiliseront {@link Othellier#placer(Case, Joueur)} et {@link
   * Othellier#capturer(Case)} pour changer la couleur d'un pion sur le plateau.
   */
  void setPossesseur(Joueur possesseur) {
    // TODO bonus 10 étape 2.3 : mettre à jour le champ possesseur et appeler
    // imageView.setImage(possesseur.getImage()) pour rafraîchir le pion affiché.
    this.possesseur = possesseur;
    imageView.setImage(possesseur.getImage());
  }

  /** Indice de ligne de la case dans l'othellier (entre 0 et TAILLE - 1). */
  int getLigne() {
    return ligne;
  }

  /** Indice de colonne de la case dans l'othellier (entre 0 et TAILLE - 1). */
  int getColonne() {
    return colonne;
  }
}
