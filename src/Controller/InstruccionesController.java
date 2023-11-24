
package Controller;

import App.Main;
import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;

/**
 * FXML Controller class
 *
 * @author Cristofer
 */
public class InstruccionesController implements Initializable {

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
    private void volver(ActionEvent event) throws IOException {
        Main.Close((Stage) escenaPrincipal.getScene().getWindow());
        Main.setRoot("/View/Menu", 400, 600);
    }
    
}
