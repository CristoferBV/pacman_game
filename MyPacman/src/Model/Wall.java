
package Model;

import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Pane;

/**
 *
 * @author Cristofer
 */
public class Wall extends Pane {
    public Wall() {
        setMinSize(35, 15);
        setMaxSize(30, 30);

        // Carga la imagen desde un archivo (ajusta la ruta según tu ubicación).
        Image wallImage = new Image("IMG/wall.png");

        // Crea un ImageView con la imagen y ajusta su tamaño.
        ImageView imageView = new ImageView(wallImage);
        imageView.setFitWidth(30);
        imageView.setFitHeight(30);

        // Agrega el ImageView como hijo del Pane.
        getChildren().add(imageView);
    }
}
