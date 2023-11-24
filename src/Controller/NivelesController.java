
package Controller;

import java.net.URL;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import App.Main;
import java.io.IOException;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;

/**
 * FXML Controller class
 *
 * @author Cristofer
 */
public class NivelesController implements Initializable {

    @FXML
    private AnchorPane escenaPrincipal;

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO
    }    

    @FXML
    private void salir(ActionEvent event) throws IOException {
        Main.Close((Stage) escenaPrincipal.getScene().getWindow());
        Main.setRoot("/View/Menu", 400, 600);
    }
    
}
