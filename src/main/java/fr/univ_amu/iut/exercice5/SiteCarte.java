package fr.univ_amu.iut.exercice5;

import java.io.IOException;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;

/**
 * Composant graphique réutilisable représentant une carte de site de suivi VigieChiro.
 *
 * <p>Cette classe illustre le pattern <b>{@code fx:root}</b> : le même objet sert à la fois de
 * <i>racine</i> visuelle (le {@link HBox}) et de <i>contrôleur</i> (les {@code @FXML} sont injectés
 * sur ses propres champs). C'est le moyen standard en JavaFX pour transformer une vue FXML en
 * composant graphique instanciable comme n'importe quel autre Node : {@code new SiteCarte()}.
 *
 * <p><b>Lien avec la SAE 2.01</b> : cette carte correspond à la maquette M-Sites du brief. Sur le
 * tableau de bord, chaque site déclaré par l'utilisatrice (Marie, Karim, Samuel) est rendu sous
 * forme d'une telle carte, avec un <b>badge de fraîcheur</b> coloré qui indique d'un coup d'œil
 * depuis combien de jours le dernier passage a été importé :
 *
 * <ul>
 *   <li>{@code badge-fresh} (vert) si dernier passage strictement &lt; 7 jours
 *   <li>{@code badge-stale} (orange) si dernier passage entre 7 et 30 jours
 *   <li>{@code badge-cold} (gris) si dernier passage &gt; 30 jours, ou jamais (-1)
 * </ul>
 *
 * <p>Concepts mis en pratique :
 *
 * <ul>
 *   <li>{@code <fx:root type="..."/>} dans le FXML
 *   <li>{@link FXMLLoader#setRoot(Object)} et {@link FXMLLoader#setController(Object)} dans le
 *       constructeur Java
 *   <li>exposition des champs sous forme de {@link StringProperty} / {@link IntegerProperty}
 *       observables (style JavaBeans : {@code numeroCarreProperty()}, {@code getNumeroCarre()},
 *       {@code setNumeroCarre()})
 *   <li>réaction à un changement de propriété via un {@code ChangeListener} pour mettre à jour la
 *       classe CSS du badge
 * </ul>
 */
public class SiteCarte extends HBox {

  @FXML private Label labelCarre;

  @FXML private Label labelNom;

  @FXML private Label labelBadge;

  @FXML private Label labelNbPoints;

  @FXML private Label labelNbPassages;

  private final StringProperty numeroCarre = new SimpleStringProperty(this, "numeroCarre", "");

  private final StringProperty nomConvivial = new SimpleStringProperty(this, "nomConvivial", "");

  private final IntegerProperty nombrePoints = new SimpleIntegerProperty(this, "nombrePoints", 0);

  private final IntegerProperty nombrePassages =
      new SimpleIntegerProperty(this, "nombrePassages", 0);

  /** Nombre de jours depuis le dernier passage importé. La valeur {@code -1} signifie "jamais". */
  private final IntegerProperty joursDepuisDernierPassage =
      new SimpleIntegerProperty(this, "joursDepuisDernierPassage", -1);

  /**
   * Construit le composant en chargeant son FXML dans cet objet (qui joue à la fois le rôle de
   * racine et de contrôleur).
   */
  public SiteCarte() {
    // TODO exercice 5 : assembler le FXMLLoader pour le pattern fx:root.
    //
    // 1. Construire un FXMLLoader avec getClass().getResource("SiteCarte.fxml").
    // 2. Lui dire que la racine du FXML doit être CET objet : loader.setRoot(this).
    // 3. Lui dire que le contrôleur doit être CET objet aussi : loader.setController(this).
    // 4. Appeler loader.load() (qui peut lever IOException, à propager via RuntimeException
    //    pour ne pas surcharger la signature du constructeur).

    FXMLLoader loader = new FXMLLoader(getClass().getResource("SiteCarte.fxml"));
    loader.setRoot(this);
    loader.setController(this);

    try {
      loader.load();
    } catch (IOException e) {
      throw new RuntimeException(e);
    }
  }

  /**
   * Méthode invoquée automatiquement par {@link FXMLLoader} une fois que les champs {@code @FXML}
   * ont été injectés. C'est ici qu'on relie les labels aux propriétés et qu'on installe l'écouteur
   * qui met à jour la classe CSS du badge à chaque changement de fraîcheur.
   */
  @FXML
  private void initialize() {
    // TODO exercice 5 : lier chaque label à sa propriété et installer l'écouteur du
    // badge.
    //
    // 1. labelCarre.textProperty().bind(numeroCarre) -- le numéro brut, sans
    // préfixe.
    // 2. labelNom.textProperty().bind(nomConvivial).
    // 3. labelNbPoints.textProperty().bind(nombrePoints.asString().concat(" points
    // d'écoute")).
    // 4. labelNbPassages.textProperty().bind(nombrePassages.asString().concat("
    // passages")).
    // 5. Installer un écouteur sur joursDepuisDernierPassage qui appelle
    // majBadge(...) à chaque
    // changement, puis appeler majBadge(...) une première fois avec la valeur
    // courante pour
    // initialiser l'affichage.
    labelCarre.textProperty().bind(numeroCarre);
    labelNom.textProperty().bind(nomConvivial);
    labelNbPoints.textProperty().bind(nombrePoints.asString().concat(" points d'écoute"));
    labelNbPassages.textProperty().bind(nombrePassages.asString().concat(" passages"));

    joursDepuisDernierPassage.addListener(
        (observable, ancienneValeur, nouvelleValeur) -> majBadge(nouvelleValeur.intValue()));

    majBadge(joursDepuisDernierPassage.get());
  }

  /**
   * Met à jour le texte et la classe CSS du badge en fonction du nombre de jours écoulés depuis le
   * dernier passage. La valeur {@code -1} est traitée comme "aucun passage jamais importé".
   */
  private void majBadge(int jours) {
    // TODO exercice 5 : implémenter la logique du badge de fraîcheur.
    //
    // - retirer d'abord les trois classes badge-fresh, badge-stale, badge-cold du
    // labelBadge
    // (labelBadge.getStyleClass().removeAll("badge-fresh", "badge-stale",
    // "badge-cold"))
    // - si jours < 0 : texte "Jamais utilisé", classe "badge-cold"
    // - sinon si jours < 7 : texte "Il y a Nj", classe "badge-fresh"
    // - sinon si jours <= 30 : texte "Il y a Nj", classe "badge-stale"
    // - sinon : texte "Il y a Nj", classe "badge-cold"

    labelBadge.getStyleClass().removeAll("badge-fresh", "badge-stale", "badge-cold");

    if (jours < 0) {
      labelBadge.setText("Jamais utilisé");
      labelBadge.getStyleClass().add("badge-cold");
    } else if (jours < 7) {
      labelBadge.setText("Il y a " + jours + "j");
      labelBadge.getStyleClass().add("badge-fresh");
    } else if (jours <= 30) {
      labelBadge.setText("Il y a " + jours + "j");
      labelBadge.getStyleClass().add("badge-stale");
    } else {
      labelBadge.setText("Il y a " + jours + "j");
      labelBadge.getStyleClass().add("badge-cold");
    }
  }

  // ---------------------------------------------------------------------
  // Propriétés observables (convention JavaBeans : property/get/set)
  // ---------------------------------------------------------------------

  public StringProperty numeroCarreProperty() {
    return numeroCarre;
  }

  public String getNumeroCarre() {
    return numeroCarre.get();
  }

  public void setNumeroCarre(String value) {
    numeroCarre.set(value);
  }

  public StringProperty nomConvivialProperty() {
    return nomConvivial;
  }

  public String getNomConvivial() {
    return nomConvivial.get();
  }

  public void setNomConvivial(String value) {
    nomConvivial.set(value);
  }

  public IntegerProperty nombrePointsProperty() {
    return nombrePoints;
  }

  public int getNombrePoints() {
    return nombrePoints.get();
  }

  public void setNombrePoints(int value) {
    nombrePoints.set(value);
  }

  public IntegerProperty nombrePassagesProperty() {
    return nombrePassages;
  }

  public int getNombrePassages() {
    return nombrePassages.get();
  }

  public void setNombrePassages(int value) {
    nombrePassages.set(value);
  }

  public IntegerProperty joursDepuisDernierPassageProperty() {
    return joursDepuisDernierPassage;
  }

  public int getJoursDepuisDernierPassage() {
    return joursDepuisDernierPassage.get();
  }

  public void setJoursDepuisDernierPassage(int value) {
    joursDepuisDernierPassage.set(value);
  }
}
