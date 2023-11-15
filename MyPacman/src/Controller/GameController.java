package Controller;

import Model.Maze;
import Model.Pacman;
import Model.Wall;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.GridPane;
import javafx.scene.Node;
import javafx.scene.layout.Region;

import java.net.URL;
import java.util.ResourceBundle;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;

public class GameController implements Initializable {
    @FXML
    private AnchorPane escenaPrincipal;
    @FXML
    private GridPane mazeGrid;
    
    private Maze maze;
    private Pacman pacman;

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        maze = new Maze(20, 20); 
        pacman = new Pacman();
        
        initializeMazeView();
    }    
    
    private void initializeMazeView() {
        char[][] mazeData = new char[maze.getNumRows()][maze.getNumColumns()];
        for (int row = 0; row < maze.getNumRows(); row++) {
            for (int col = 0; col < maze.getNumColumns(); col++) {
                mazeData[row][col] = maze.getCell(row, col);
            }
        }
    
        mazeGrid.getChildren().clear();
        for (int row = 0; row < mazeData.length; row++) {
            for (int col = 0; col < mazeData[row].length; col++) {
                char cellValue = mazeData[row][col];
                Node cellNode = createMazeCell(cellValue);
                mazeGrid.add(cellNode, col, row);
            }
        }  
    }  
    
    private Node createMazeCell(char cellValue) {
        switch (cellValue) {
            case 'W':
                Wall wall = new Wall();
                return wall;
            case 'P':
                Pacman pacman = new Pacman();
                return pacman;
            case 'G':
                // Crea una representación gráfica para fantasmas (usando un círculo rojo).
                Circle ghost = new Circle(10, Color.RED);
                ghost.setCenterX(18);  // Establece la posición X en el centro
                ghost.setCenterY(14);  // Establece la posición Y en el centro
                Pane ghostPane = new Pane(ghost);
                return ghostPane;
            case '.':
                // Crea una representación gráfica para puntos (usando un círculo blanco).
                Circle point = new Circle(2, Color.BEIGE);
                point.setCenterX(18);  // Establece la posición X en el centro
                point.setCenterY(12);  // Establece la posición Y en el centro
                Pane pointPane = new Pane(point);
                return pointPane;
            case ' ':
                // Crea una celda vacía (usando un Pane con fondo blanco).
                Pane emptyCell = new Pane();
                emptyCell.setMinSize(30, 30);
                emptyCell.setMaxSize(30, 30);
                emptyCell.setStyle("-fx-background-color: white;");
                return emptyCell;
            default:
                break;
        }
        // En caso de otros valores, devuelve un nodo vacío.
        return new Region();
    }
    
    @FXML
    private void movimientosPacman(KeyEvent event) {
        switch (event.getCode()) {
            case UP:
                pacman.setDireccion("UP");
                break;
            case DOWN:
                pacman.setDireccion("DOWN");
                break;
            case LEFT:
                pacman.setDireccion("LEFT");
                break;
            case RIGHT:
                pacman.setDireccion("RIGHT");
                break;
            default:
                break;
        }

        pacman.mover();
    }

    
}