package fr.univ_amu.iut.exercice7;

import java.time.LocalTime;
import javafx.animation.PauseTransition;
import javafx.beans.binding.Bindings;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextArea;
import javafx.util.Duration;

/**
 * Contrôleur de la pierre angulaire MVC (parcours P3 - vérification d'une nuit de capture par
 * échantillonnage).
 *
 * <p>L'instance possède son propre modèle ({@link NuitVerification}). Le FXML s'occupe de la
 * structure, le contrôleur du câblage modèle ↔ vue.
 */
public class QualificationController {

  @FXML private TableView<Sequence> tableView;

  @FXML private TableColumn<Sequence, LocalTime> colHorodatage;

  @FXML private TableColumn<Sequence, Number> colFrequence;

  @FXML private TableColumn<Sequence, Number> colDuree;

  @FXML private TableColumn<Sequence, String> colStatut;

  @FXML private Label labelSelection;

  @FXML private Button boutonEcouter;

  @FXML private Label labelLecture;

  @FXML private ChoiceBox<String> choiceBoxVerdict;

  @FXML private TextArea zoneCommentaire;

  @FXML private Label labelVerdictGlobal;

  private final NuitVerification nuit = NuitVerification.genererJeu(10);

  /**
   * Méthode appelée automatiquement après injection des champs {@code @FXML}. Tout le câblage MVC
   * se passe ici.
   */
  @FXML
  private void initialize() {
    // TODO exercice 7 (étape 1) : alimenter la TableView avec les séquences de la nuit, et
    // associer chaque colonne à la propriété du modèle correspondante via setCellValueFactory.
    // - colHorodatage  -> c.getValue().horodatageProperty()
    // - colFrequence   -> c.getValue().frequenceDominanteKHzProperty()
    // - colDuree       -> c.getValue().dureeSecondesProperty()
    // - colStatut      -> c.getValue().statutProperty()
    // Puis : tableView.setItems(nuit.getSequences()).

    colHorodatage.setCellValueFactory(c -> c.getValue().horodatageProperty());
    colFrequence.setCellValueFactory(c -> c.getValue().frequenceDominanteKHzProperty());
    colDuree.setCellValueFactory(c -> c.getValue().dureeSecondesProperty());
    colStatut.setCellValueFactory(c -> c.getValue().statutProperty());

    tableView.setItems(nuit.getSequences());

    // TODO exercice 7 (étape 2) : afficher dans labelSelection la séquence sélectionnée.
    // - sans sélection : "(sélectionnez une séquence dans le tableau)"
    // - avec sélection : "Séquence <horodatage> - <freq> kHz" (1 décimale, ex. "Séquence 21:30
    //   - 45.2 kHz"). Utiliser String.format("%.1f kHz", ...).
    // Astuce : addListener((obs, ancien, nouveau) -> ...) sur
    // tableView.getSelectionModel().selectedItemProperty().

    tableView
        .getSelectionModel()
        .selectedItemProperty()
        .addListener(
            (obs, ancien, nouveau) -> {
              if (nouveau == null) {
                labelSelection.setText("(sélectionnez une séquence dans le tableau)");
              } else {
                labelSelection.setText(
                    "Séquence "
                        + nouveau.getHorodatage()
                        + " - "
                        + String.format("%.1f kHz", nouveau.getFrequenceDominanteKHz()));
              }
            });

    labelSelection.setText("(sélectionnez une séquence dans le tableau)");
    labelLecture.setText("");

    // TODO exercice 7 (étape 3) : le bouton "Écouter" est désactivé tant qu'aucune séquence
    // n'est sélectionnée. Utiliser un binding entre boutonEcouter.disableProperty() et
    // tableView.getSelectionModel().selectedItemProperty().isNull().

    boutonEcouter
        .disableProperty()
        .bind(tableView.getSelectionModel().selectedItemProperty().isNull());

    // TODO exercice 7 (étape 4) : peupler la ChoiceBox avec les trois verdicts possibles :
    // "OK", "Douteux", "À jeter".

    choiceBoxVerdict.setItems(FXCollections.observableArrayList("OK", "Douteux", "À jeter"));

    // TODO exercice 7 (étape 5) : labelVerdictGlobal doit refléter le verdict du modèle.
    // - tant que le verdict est vide : "Verdict global : (à saisir)"
    // - sinon : "Verdict global : <verdict>"
    // Utiliser Bindings.when(...).then(...).otherwise(...).

    labelVerdictGlobal
        .textProperty()
        .bind(
            Bindings.when(nuit.verdictGlobalProperty().isEmpty())
                .then("Verdict global : (à saisir)")
                .otherwise(Bindings.concat("Verdict global : ", nuit.verdictGlobalProperty())));

    // TODO exercice 7 (étape 6) : lier la TextArea de commentaire au modèle (binding
    // bidirectionnel).

    zoneCommentaire.textProperty().bindBidirectional(nuit.commentaireProperty());
  }

  /** Action du bouton « Écouter ». Lecture audio simulée : statut → "Écoutée" + label éphémère. */
  @FXML
  private void ecouter() {
    // TODO exercice 7 (étape 7) : mettre le statut de la séquence sélectionnée à "Écoutée" et
    // afficher "Lecture en cours..." dans labelLecture. Ce texte doit s'effacer après 600 ms
    // (PauseTransition + setOnFinished(...)).

    Sequence selectionnee = tableView.getSelectionModel().getSelectedItem();

    if (selectionnee != null) {
      selectionnee.setStatut("Écoutée");

      labelLecture.setText("Lecture en cours...");

      PauseTransition pause = new PauseTransition(Duration.millis(600));

      pause.setOnFinished(e -> labelLecture.setText(""));

      pause.play();
    }
  }

  /** Action du bouton « Enregistrer le verdict ». Écrit le verdict choisi dans le modèle. */
  @FXML
  private void enregistrerVerdict() {
    // TODO exercice 7 (étape 8) : lire choiceBoxVerdict.getValue() et l'enregistrer dans le
    // modèle via nuit.setVerdictGlobal(...). Ne rien faire si aucun verdict n'est sélectionné.

    String verdict = choiceBoxVerdict.getValue();

    if (verdict != null) {
      nuit.setVerdictGlobal(verdict);
    }
  }

  /** Exposé pour les tests : permet de vérifier l'état du modèle après actions sur la vue. */
  public NuitVerification getNuit() {
    return nuit;
  }
}
