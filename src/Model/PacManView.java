package Model;

import Controller.Controller;
import javafx.scene.Group;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import Model.PacManModel.CellValue;

/**
 * FXML Controller class
 *
 * @author Cristofer
 */
public class PacManView extends Group {
    public final static double CELL_WIDTH = 20.0;

    private int rowCount;
    private int columnCount;
    private ImageView[][] cellViews;
    private Image pacmanRightImage;
    private Image pacmanUpImage;
    private Image pacmanDownImage;
    private Image pacmanLeftImage;
    private Image ghost1Image;
    private Image ghost2Image;
    private Image blueGhostImage;
    private Image wallImage;
    private Image bigDotImage;
    private Image smallDotImage;

    /**
     * Inicializa los valores de las variables de instancia de imagen de los archivos.
     */
    public PacManView() {
        this.pacmanRightImage = new Image(getClass().getResourceAsStream("/res/pacmanRight.gif"));
        this.pacmanUpImage = new Image(getClass().getResourceAsStream("/res/pacmanUp.gif"));
        this.pacmanDownImage = new Image(getClass().getResourceAsStream("/res/pacmanDown.gif"));
        this.pacmanLeftImage = new Image(getClass().getResourceAsStream("/res/pacmanLeft.gif"));
        this.ghost1Image = new Image(getClass().getResourceAsStream("/res/redghost.gif"));
        this.ghost2Image = new Image(getClass().getResourceAsStream("/res/ghost2.gif"));
        this.blueGhostImage = new Image(getClass().getResourceAsStream("/res/blueghost.gif"));
        this.wallImage = new Image(getClass().getResourceAsStream("/res/wall.jpg"));
        this.bigDotImage = new Image(getClass().getResourceAsStream("/res/whitedot.png"));
        this.smallDotImage = new Image(getClass().getResourceAsStream("/res/smalldot.png"));
    }

    /**
     * Construye una cuadrícula vacía de ImageViews
     */
    private void initializeGrid() {
        if (this.rowCount > 0 && this.columnCount > 0) {
            this.cellViews = new ImageView[this.rowCount][this.columnCount];
            for (int row = 0; row < this.rowCount; row++) {
                for (int column = 0; column < this.columnCount; column++) {
                    ImageView imageView = new ImageView();
                    imageView.setX((double)column * CELL_WIDTH);
                    imageView.setY((double)row * CELL_WIDTH);
                    imageView.setFitWidth(CELL_WIDTH);
                    imageView.setFitHeight(CELL_WIDTH);
                    this.cellViews[row][column] = imageView;
                    this.getChildren().add(imageView);
                }
            }
        }
    }

    /** Actualiza la vista para reflejar el estado del modelo.
     *
     * @param model
     */
    public void actualizar(PacManModel model) {
        assert model.getRowCount() == this.rowCount && model.getColumnCount() == this.columnCount;
        //para cada ImageView, configura la imagen para que se corresponda con el CellValue de esa celda
        for (int row = 0; row < this.rowCount; row++){
            for (int column = 0; column < this.columnCount; column++){
                CellValue value = model.getCellValue(row, column);
                if (null == value) {
                    this.cellViews[row][column].setImage(null);
                }
                else switch (value) {
                    case WALL:
                        this.cellViews[row][column].setImage(this.wallImage);
                        break;
                    case BIGDOT:
                        this.cellViews[row][column].setImage(this.bigDotImage);
                        break;
                    case SMALLDOT:
                        this.cellViews[row][column].setImage(this.smallDotImage);
                        break;
                    default:
                        this.cellViews[row][column].setImage(null);
                        break;
                }
                //comprueba en qué dirección va PacMan y muestra la imagen correspondiente
                if (row == model.getPacmanLocation().getX() && column == model.getPacmanLocation().getY() && (PacManModel.getLastDirection() == PacManModel.Direction.RIGHT || PacManModel.getLastDirection() == PacManModel.Direction.NONE)) {
                    this.cellViews[row][column].setImage(this.pacmanRightImage);
                }
                else if (row == model.getPacmanLocation().getX() && column == model.getPacmanLocation().getY() && PacManModel.getLastDirection() == PacManModel.Direction.LEFT) {
                    this.cellViews[row][column].setImage(this.pacmanLeftImage);
                }
                else if (row == model.getPacmanLocation().getX() && column == model.getPacmanLocation().getY() && PacManModel.getLastDirection() == PacManModel.Direction.UP) {
                    this.cellViews[row][column].setImage(this.pacmanUpImage);
                }
                else if (row == model.getPacmanLocation().getX() && column == model.getPacmanLocation().getY() && PacManModel.getLastDirection() == PacManModel.Direction.DOWN) {
                    this.cellViews[row][column].setImage(this.pacmanDownImage);
                }
                //hace que los fantasmas "parpadeen" hacia el final de GhostEatingMode (muestra imágenes de fantasmas regulares en actualizaciones alternas del contador)
                if (PacManModel.isGhostEatingMode() && (Controller.getGhostEatingModeCounter() == 6 ||Controller.getGhostEatingModeCounter() == 4 || Controller.getGhostEatingModeCounter() == 2)) {
                    if (row == model.getGhost1Location().getX() && column == model.getGhost1Location().getY()) {
                        this.cellViews[row][column].setImage(this.ghost1Image);
                    }
                    if (row == model.getGhost2Location().getX() && column == model.getGhost2Location().getY()) {
                        this.cellViews[row][column].setImage(this.ghost2Image);
                    }
                }
                //muestra fantasmas azules en GhostEatingMode
                else if (PacManModel.isGhostEatingMode()) {
                    if (row == model.getGhost1Location().getX() && column == model.getGhost1Location().getY()) {
                        this.cellViews[row][column].setImage(this.blueGhostImage);
                    }
                    if (row == model.getGhost2Location().getX() && column == model.getGhost2Location().getY()) {
                        this.cellViews[row][column].setImage(this.blueGhostImage);
                    }
                }
                //muestra imágenes fantasma normales en caso contrario
                else {
                    if (row == model.getGhost1Location().getX() && column == model.getGhost1Location().getY()) {
                        this.cellViews[row][column].setImage(this.ghost1Image);
                    }
                    if (row == model.getGhost2Location().getX() && column == model.getGhost2Location().getY()) {
                        this.cellViews[row][column].setImage(this.ghost2Image);
                    }
                }
            }
        }
    }

    public int getRowCount() {
        return this.rowCount;
    }

    public void setRowCount(int rowCount) {
        this.rowCount = rowCount;
        this.initializeGrid();
    }

    public int getColumnCount() {
        return this.columnCount;
    }

    public void setColumnCount(int columnCount) {
        this.columnCount = columnCount;
        this.initializeGrid();
    }
}