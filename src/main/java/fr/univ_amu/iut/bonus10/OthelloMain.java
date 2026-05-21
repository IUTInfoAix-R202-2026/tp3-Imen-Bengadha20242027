package fr.univ_amu.iut.bonus10;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

/**
 * Bonus 10 - étape 5 : programme principal de l'application Othello.
 *
 * <p>Cette classe a la responsabilité de charger la vue principale {@code OthelloView.fxml}, d'en
 * faire la racine du graphe de scène, puis d'afficher la fenêtre.
 *
 * <p>C'est aussi le point d'entrée Java standard ({@link #main(String[])}) ; la méthode {@code
 * main} appelle simplement {@link Application#launch(String...)} pour démarrer la boucle JavaFX,
 * qui en retour appellera {@link #start(Stage)}.
 *
 * <p><b>Mode entraînement examen :</b> ce bonus est conçu comme un <b>test blanc</b> pour préparer
 * le CC3 (mini-application JavaFX sur feuille). Pour vous aider à démarrer, certaines questions ont
 * déjà des éléments de réponse dans les fichiers source (déclarations de champs, signatures de
 * méthodes, statiques, méthodes auxiliaires comme {@code casesCapturable}). Le jour de l'examen,
 * vous devrez en revanche <b>tout écrire à la main</b> : entraînez-vous donc, dans la mesure du
 * possible, à reformuler ces éléments par vous-mêmes plutôt qu'à vous appuyer dessus.
 *
 * <p>Architecture finale de l'application :
 *
 * <ul>
 *   <li>{@link Joueur} (exercice 1) - NOIR, BLANC, PERSONNE + score observable
 *   <li>{@link Case} (exercice 2) - bouton cliquable qui affiche un pion
 *   <li>{@link Othellier} (exercice 3) - GridPane 8x8 qui embarque toute la logique de jeu
 *   <li>{@link OthelloController} (exercice 4) - relie l'entête FXML aux propriétés observables de
 *       l'othellier
 *   <li>{@code OthelloView.fxml} (fourni) - BorderPane : top = entête, center = othellier, bottom =
 *       pied de fenêtre avec bouton « Nouvelle partie » et message de fin
 * </ul>
 */
public class OthelloMain extends Application {

  /** Point d'entrée Java standard : démarre la boucle JavaFX. */
  public static void main(String[] args) {
    // TODO bonus 10 étape 5.1 : appeler launch(args) pour démarrer l'application.
    launch(args);
  }

  /**
   * Méthode invoquée par JavaFX une fois la plateforme prête. C'est ici que l'on charge le FXML,
   * construit la scène et affiche la fenêtre.
   */
  @Override
  public void start(Stage primaryStage) throws Exception {
    // TODO bonus 10 étape 5.2 : démarrer la fenêtre principale en quatre temps :
    // 1. charger la vue racine : Parent racine =
    // FXMLLoader.load(getClass().getResource("OthelloView.fxml"))
    // 2. fixer le titre de la fenêtre : primaryStage.setTitle("Bonus 10 - Othello")
    // 3. créer la scène avec la racine, à une taille raisonnable (par exemple
    // 560x680) :
    // primaryStage.setScene(new Scene(racine, 560, 680))
    // 4. afficher la fenêtre : primaryStage.show()
    Parent racine = FXMLLoader.load(getClass().getResource("OthelloView.fxml"));
    primaryStage.setTitle("Bonus 10 - Othello");
    primaryStage.setScene(new Scene(racine, 560, 680));
    primaryStage.show();
  }
}
