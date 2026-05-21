package fr.univ_amu.iut.bonus8;

import static org.assertj.core.api.Assertions.assertThat;

import javafx.scene.control.ToggleButton;
import javafx.stage.Stage;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.junit.jupiter.api.extension.ExtendWith;
import org.testfx.api.FxRobot;
import org.testfx.framework.junit5.ApplicationExtension;
import org.testfx.framework.junit5.Start;

/** Tests du bonus 8 - bascule de thème par échange de feuille CSS. */
@ExtendWith(ApplicationExtension.class)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class ThemeToggleControllerTest {

  @Start
  void start(Stage stage) throws Exception {
    stage.setScene(null);
    new ThemeToggle().start(stage);
  }

  // @Disabled("Retire cette annotation pour activer le test")
  @Test
  @Order(1)
  void le_theme_clair_est_actif_au_demarrage(FxRobot robot) {
    ToggleButton bouton = robot.lookup("#boutonTheme").queryAs(ToggleButton.class);
    String css = bouton.getScene().getStylesheets().get(0);
    assertThat(css)
        .as("au démarrage, theme-clair.css doit être la feuille CSS active")
        .endsWith("/theme-clair.css");
    assertThat(bouton.isSelected())
        .as("au démarrage, le ToggleButton n'est pas sélectionné")
        .isFalse();
  }

  // @Disabled("Retire cette annotation pour activer le test")
  @Test
  @Order(2)
  void cliquer_sur_le_toggle_bascule_vers_le_theme_sombre(FxRobot robot) {
    ToggleButton bouton = robot.lookup("#boutonTheme").queryAs(ToggleButton.class);
    robot.interact(bouton::fire);
    assertThat(bouton.getScene().getStylesheets())
        .as("après bascule, la feuille CSS active doit être theme-sombre.css")
        .hasSize(1);
    assertThat(bouton.getScene().getStylesheets().get(0)).endsWith("/theme-sombre.css");
  }

  // @Disabled("Retire cette annotation pour activer le test")
  @Test
  @Order(3)
  void un_second_clic_sur_le_toggle_revient_au_theme_clair(FxRobot robot) {
    ToggleButton bouton = robot.lookup("#boutonTheme").queryAs(ToggleButton.class);
    robot.interact(bouton::fire);
    robot.interact(bouton::fire);
    assertThat(bouton.getScene().getStylesheets().get(0))
        .as("un second clic doit revenir à theme-clair.css")
        .endsWith("/theme-clair.css");
  }

  // @Disabled("Retire cette annotation pour activer le test")
  @Test
  @Order(4)
  void le_libelle_du_toggle_reflete_le_theme_actif(FxRobot robot) {
    ToggleButton bouton = robot.lookup("#boutonTheme").queryAs(ToggleButton.class);
    assertThat(bouton.getText())
        .as("en mode clair, le bouton invite à passer au mode sombre")
        .contains("Mode sombre");
    robot.interact(bouton::fire);
    assertThat(bouton.getText())
        .as("en mode sombre, le bouton invite à revenir au mode clair")
        .contains("Mode clair");
  }
}
