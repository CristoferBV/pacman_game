package Controller;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.geometry.Rectangle2D;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Pane;
import javafx.stage.Screen;
import javafx.stage.Stage;

/**
 * FXML Controller class
 *
 * @author Cristofer
 */
public class MenuController implements Initializable {

    @FXML
    private Pane escenaMenu;
    @FXML
    private AnchorPane escenaPrincipal;

    // Variables locales
    static double w = 1000;
    static double h = 600;

    /**
     * Initializes the controller class.
     *
     * @param url
     * @param rb
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        escenaMenu.setVisible(true);
    }

    @FXML
    private void instructionsGame(ActionEvent event) throws IOException {  
    }

    @FXML
    private void viewLevels(ActionEvent event) {
    }

    @FXML
    private void newGame(ActionEvent event) throws IOException {
        // Obtener el Stage desde el evento ActionEvent
        Stage currentStage = (Stage) ((Node) event.getSource()).getScene().getWindow();

        FXMLLoader loader = new FXMLLoader(getClass().getResource("/View/pacman.fxml"));
        Parent root = loader.load();

        currentStage.setTitle("PACMAN 2D");

        Controller controller = loader.getController();
        root.setOnKeyPressed(controller);

        double sceneWidth = controller.getBoardWidth() + 440.0;
        double sceneHeight = controller.getBoardHeight() + 180.0;

        currentStage.setScene(new Scene(root, sceneWidth, sceneHeight));
        currentStage.show();

        // Centrar el Stage en la pantalla
        centerStageOnScreen(currentStage);

        root.requestFocus();
    }

    // Método para centrar el Stage en la pantalla
    private void centerStageOnScreen(Stage stage) {
        Screen screen = Screen.getPrimary();
        Rectangle2D bounds = screen.getVisualBounds();

        double centerX = bounds.getMinX() + (bounds.getWidth() - stage.getWidth()) / 2;
        double centerY = bounds.getMinY() + (bounds.getHeight() - stage.getHeight()) / 2;

        stage.setX(centerX);
        stage.setY(centerY);
    }

}