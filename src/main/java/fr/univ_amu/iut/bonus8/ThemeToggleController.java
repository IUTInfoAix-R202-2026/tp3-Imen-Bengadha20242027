package fr.univ_amu.iut.bonus8;

import javafx.fxml.FXML;
import javafx.scene.Scene;
import javafx.scene.control.ToggleButton;
import javafx.scene.layout.Region;

/**
 * Contrôleur du bonus 8 - bascule de thème.
 *
 * <p>Le clic sur le {@link ToggleButton} remplace la feuille CSS courante de la {@link
 * javafx.scene.Scene} : {@code theme-clair.css} si non sélectionné, {@code theme-sombre.css} sinon.
 */
public class ThemeToggleController {

  static final String CSS_CLAIR = "theme-clair.css";
  static final String CSS_SOMBRE = "theme-sombre.css";

  @FXML private ToggleButton boutonTheme;

  @FXML private Region racine;

  @FXML
  private void initialize() {
    // TODO bonus 8 : à chaque changement de sélection du ToggleButton, remplacer la
    // feuille
    // CSS active sur la Scene par theme-clair.css (non sélectionné) ou
    // theme-sombre.css
    // (sélectionné). On localise les URL via
    // getClass().getResource("nom.css").toExternalForm().
    // Astuce : utiliser scene.getStylesheets().setAll(url) pour remplacer toutes
    // les feuilles
    // en une seule opération.

    boutonTheme
        .selectedProperty()
        .addListener(
            (obs, ancien, nouveau) -> {
              String fichierCss;

              if (nouveau) {
                fichierCss = CSS_SOMBRE;
                boutonTheme.setText(" Mode clair");
              } else {
                fichierCss = CSS_CLAIR;
                boutonTheme.setText(" Mode sombre");
              }

              String url = getClass().getResource(fichierCss).toExternalForm();
              Scene scene = racine.getScene();
              scene.getStylesheets().setAll(url);
            });
  }
}
