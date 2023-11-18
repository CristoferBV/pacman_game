package Controller;

import Model.PacManModel;
import Model.PacManView;
import javafx.fxml.FXML;
import javafx.event.EventHandler;
import javafx.scene.control.Label;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.application.Platform;
import java.util.Timer;
import java.util.TimerTask;
import javafx.event.ActionEvent;
import javafx.scene.layout.AnchorPane;

/**
 * FXML Controller class
 *
 * @author Cristofer
 */
public class Controller implements EventHandler<KeyEvent> {
    final private static double FRAMES_PER_SECOND = 5.0;

    @FXML private Label scoreLabel;
    @FXML private Label levelLabel;
    @FXML private Label gameOverLabel;
    @FXML private Label lifeLabel;
    @FXML private AnchorPane scenePrincipal;
    private PacManModel pacManModel;
    @FXML
    private PacManView pacManView;
    private static final String[] levelFiles = {
        "src/levels/level1.txt",
        "src/levels/level2.txt",
        "src/levels/level3.txt",
        "src/levels/level4.txt",
        "src/levels/level5.txt",
        "src/levels/level6.txt",
        "src/levels/level7.txt",
        "src/levels/level8.txt",
        "src/levels/level9.txt",
        "src/levels/level10.txt"
    };

    private Timer timer;
    private static int ghostEatingModeCounter;
    private boolean paused;
    


    public Controller() {
        this.paused = false;
    }

    /**
     * Inicialice y actualice el modelo y vea desde el primer archivo de texto e inicie el temporizador.
     */
    public void initialize() {
        String file = this.getLevelFile(0);
        this.pacManModel = new PacManModel();
        this.update(PacManModel.Direction.NONE);
        ghostEatingModeCounter = 25;
        this.startTimer();
        
        // Establecer restricciones de tamaño para el BorderPane
        AnchorPane.setTopAnchor(scenePrincipal, 0.0);
        AnchorPane.setRightAnchor(scenePrincipal, 0.0);
        AnchorPane.setBottomAnchor(scenePrincipal, 0.0);
        AnchorPane.setLeftAnchor(scenePrincipal, 0.0);
        
    }

    /**
     * Programa el modelo para que se actualice según el temporizador.
     */
    private void startTimer() {
        this.timer = new java.util.Timer();
        TimerTask timerTask = new TimerTask() {
            public void run() {
                Platform.runLater(new Runnable() {
                    public void run() {
                        update(pacManModel.getCurrentDirection());
                    }
                });
            }
        };

        long frameTimeInMilliseconds = (long)(1000.0 / FRAMES_PER_SECOND);
        this.timer.schedule(timerTask, 0, frameTimeInMilliseconds);
    }

    /**
     * Muestra el PacManModel, actualiza la vista, actualiza la puntuación y el nivel, muestra Game Over/You Won e instrucciones sobre cómo jugar.
     * @param direction la dirección ingresada más recientemente para que PacMan se mueva
     */
    private void update(PacManModel.Direction direction) {
        this.pacManModel.step(direction);
        this.pacManView.update(pacManModel);
        this.scoreLabel.setText(String.format("Score: %d", this.pacManModel.getScore()));
        this.levelLabel.setText(String.format("Level: %d", this.pacManModel.getLevel()));
        //this.lifeLabel.setText(String.format("Life: %d", this.pacManModel.getLife()));
        if (pacManModel.isGameOver()) {
            this.gameOverLabel.setText(String.format("GAME OVER"));
            pause();
        }
        if (pacManModel.isYouWon()) {
            this.gameOverLabel.setText(String.format("YOU WON!"));
        }
        //cuando PacMan está en modo GhostEatingMode, cuenta regresiva el GhostEatingModeCounter para restablecer GhostEatingMode a falso cuando el contador es 0.
        if (pacManModel.isGhostEatingMode()) {
            ghostEatingModeCounter--;
        }
        if (ghostEatingModeCounter == 0 && pacManModel.isGhostEatingMode()) {
            pacManModel.setGhostEatingMode(false);
        }
    }
    
    /**
    * Lógica común para reiniciar el juego.
    */
    private void resetGameLogic() {
        pause();
        this.pacManModel.startNewGame();
        this.gameOverLabel.setText("");
        paused = false;
        this.startTimer();
    }

    /**
     * Acepta la entrada del teclado del usuario para controlar el movimiento de PacMan e iniciar nuevos juegos.
     * @param keyEvent clic de la tecla del usuario
     */
    @Override
    public void handle(KeyEvent keyEvent) {
        boolean keyRecognized = true;
        KeyCode code = keyEvent.getCode();
        PacManModel.Direction direction = PacManModel.Direction.NONE;
        if (null == code) {
            keyRecognized = false;
        } else switch (code) {
            case LEFT:
                direction = PacManModel.Direction.LEFT;
                break;
            case RIGHT:
                direction = PacManModel.Direction.RIGHT;
                break;
            case UP:
                direction = PacManModel.Direction.UP;
                break;
            case DOWN:
                direction = PacManModel.Direction.DOWN;
                break;
            case G:
                resetGameLogic();
                break;
            default:
                keyRecognized = false;
                break;
        }
        if (keyRecognized) {
            keyEvent.consume();
            pacManModel.setCurrentDirection(direction);
            pacManModel.setWaitingForKeyPress(false); // Aquí se establece waitingForKeyPress como false
        }
    }
    
    @FXML
    private void startGame(ActionEvent event) {
        pacManModel.startGame();
        scenePrincipal.requestFocus(); // Cambiar el foco del teclado al área principal
        pacManModel.setCurrentDirection(PacManModel.Direction.NONE);
    }

    @FXML
    private void resetGame(ActionEvent event) {
        pacManModel.stopGame();
        resetGameLogic();
        scenePrincipal.requestFocus(); // Cambiar el foco del teclado al área principal
        pacManModel.setCurrentDirection(PacManModel.Direction.NONE);
    }

    @FXML
    private void exitGame(ActionEvent event) {
        System.exit(1);
    }

    /**
     * pausar el cronómetro
     */
    public void pause() {
        this.timer.cancel();
        this.paused = true;
    }

    public double getBoardWidth() {
        return PacManView.CELL_WIDTH * this.pacManView.getColumnCount();
    }

    public double getBoardHeight() {
        return PacManView.CELL_WIDTH * this.pacManView.getRowCount();
    }

    public static void setGhostEatingModeCounter() {
        ghostEatingModeCounter = 25;
    }

    public static int getGhostEatingModeCounter() {
        return ghostEatingModeCounter;
    }

    public static String getLevelFile(int x)
    {
        return levelFiles[x];
    }

    public boolean getPaused() {
        return paused;
    }

}