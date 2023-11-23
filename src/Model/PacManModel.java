package Model;

import Controller.Controller;
import javafx.geometry.Point2D;
import java.io.*;
import java.util.*;
import javafx.geometry.Pos;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundImage;
import javafx.scene.layout.BorderPane;

/**
 * FXML Controller class
 *
 * @author Cristofer
 */
public class PacManModel {
    public enum CellValue {
        EMPTY, SMALLDOT, BIGDOT, WALL, GHOST1HOME, GHOST2HOME, PACMANHOME
    }
    
    public enum Direction {
        UP, DOWN, LEFT, RIGHT, NONE
    }
    
    private static final int MAX_LIVES = 6;
    private int rowCount;
    private int columnCount;
    private CellValue[][] grid;
    private int score;
    private int level;
    private int lives;
    private int dotCount;
    private static boolean gameOver;
    private static boolean youWon;
    private static boolean ghostEatingMode;
    private Point2D pacmanLocation;
    private Point2D pacmanVelocity;
    private Point2D ghost1Location;
    private Point2D ghost1Velocity;
    private Point2D ghost2Location;
    private Point2D ghost2Velocity;
    private static Direction lastDirection;
    private static Direction currentDirection;
    private static boolean gameStarted = false;
    private static boolean waitingForKeyPress;
    
    


    /**
     * Iniciar un nuevo juego al inicializar
     */
    public PacManModel() {
        this.startNewGame();
        this.waitingForKeyPress = false;
    }

    /**
     * Configure la cuadrícula CellValues ​​según el archivo txt y coloque PacMan y los fantasmas en sus ubicaciones iniciales.
     * "W" indica una pared, "E" indica un cuadrado vacío, "B" indica un punto grande, "S" indica
     * un pequeño punto, "1" o "2" indica el hogar de los fantasmas y "P" indica la posición inicial de Pacman.
     *
     * @param fileName archivo txt que contiene la configuración de la board.
     */
    public void initializeLevel(String fileName) {
        File file = new File(fileName);
        Scanner scanner = null;
        try {
            scanner = new Scanner(file);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        while (scanner.hasNextLine()) {
            String line = scanner.nextLine();
            Scanner lineScanner = new Scanner(line);
            while (lineScanner.hasNext()) {
                lineScanner.next();
                columnCount++;
            }
            rowCount++;
        }
        columnCount = columnCount/rowCount;
        Scanner scanner2 = null;
        try {
            scanner2 = new Scanner(file);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        grid = new CellValue[rowCount][columnCount];
        int row = 0;
        int pacmanRow = 0;
        int pacmanColumn = 0;
        int ghost1Row = 0;
        int ghost1Column = 0;
        int ghost2Row = 0;
        int ghost2Column = 0;
        while(scanner2.hasNextLine()){
            int column = 0;
            String line= scanner2.nextLine();
            Scanner lineScanner = new Scanner(line);
            while (lineScanner.hasNext()){
                String value = lineScanner.next();
                CellValue thisValue;
                switch (value) {
                    case "W":
                        thisValue = CellValue.WALL;
                        break;
                    case "S":
                        thisValue = CellValue.SMALLDOT;
                        dotCount++;
                        break;
                    case "B":
                        thisValue = CellValue.BIGDOT;
                        dotCount++;
                        break;
                    case "1":
                        thisValue = CellValue.GHOST1HOME;
                        ghost1Row = row;
                        ghost1Column = column;
                        break;
                    case "2":
                        thisValue = CellValue.GHOST2HOME;
                        ghost2Row = row;
                        ghost2Column = column;
                        break;
                    case "P":
                        thisValue = CellValue.PACMANHOME;
                        pacmanRow = row;
                        pacmanColumn = column;
                        break;
                    default:
                        thisValue = CellValue.EMPTY;
                        break;
                }
                grid[row][column] = thisValue;
                column++;
            }
            row++;
        }
        pacmanLocation = new Point2D(pacmanRow, pacmanColumn);
        pacmanVelocity = new Point2D(0,0);
        ghost1Location = new Point2D(ghost1Row,ghost1Column);
        ghost1Velocity = new Point2D(-1, 0);
        ghost2Location = new Point2D(ghost2Row,ghost2Column);
        ghost2Velocity = new Point2D(-1, 0);
        currentDirection = Direction.NONE;
        lastDirection = Direction.NONE;
    }

    /** Inicializar valores de variables de instancia e inicializar mapa de niveles
     */
    public void startNewGame() {
        this.gameOver = false;
        this.youWon = false;
        this.ghostEatingMode = false;
        dotCount = 0;
        rowCount = 0;
        columnCount = 0;
        this.score = 0;
        this.level = 1;
        this.lives = MAX_LIVES;
        this.initializeLevel(Controller.getLevelFile(0));
    }

    /** Inicializar el mapa de niveles para el siguiente nivel.
     *
     */
    public void startNextLevel() {
        if (this.isLevelComplete()) {
            this.level++;
            
            rowCount = 0;
            columnCount = 0;
            youWon = false;
            ghostEatingMode = false;
            waitingForKeyPress = true; // Configura la bandera para esperar la tecla
            try {
                this.initializeLevel(Controller.getLevelFile(level - 1));
            } catch (ArrayIndexOutOfBoundsException e) {
                //Si no quedan niveles en la matriz de niveles, el juego termina.
                youWon = true;
                gameOver = true;
                level--;
            }
        }
    }
    
    // Método para establecer el fondo según el nivel y tamaño personalizado
    public void setFondo(BorderPane borderPane, int nivel, double width, double height) {
        Image fondo = null;
        switch (nivel) {
            case 1:
                fondo = new Image("/res/level1.png");
                break;
            case 2:
                fondo = new Image("/res/level2.png");
                break;
            case 3:
                fondo = new Image("/res/level3.png");
                break;
            case 4:
                fondo = new Image("/res/level4.png");
                break;
        }

        // Ajustar el tamaño de la imagen al tamaño del BorderPane
        double originalWidth = fondo.getWidth();
        double originalHeight = fondo.getHeight();
        double aspectRatio = originalWidth / originalHeight;

        if (width / height > aspectRatio) {
            // Si el ancho personalizado es mayor proporcionalmente, ajustar la altura
            height = width / aspectRatio;
        } else {
            // Si la altura personalizada es mayor proporcionalmente, ajustar el ancho
            width = height * aspectRatio;
        }

        // Crear un objeto BackgroundImage con la imagen de fondo
        BackgroundImage backgroundImage = new BackgroundImage(fondo, null, null, null, null);

        // Crear un objeto Background con la imagen de fondo
        Background background = new Background(backgroundImage);

        // Establecer el fondo en el BorderPane
        borderPane.setBackground(background);

        // Centrar el contenido en el centro del BorderPane
        BorderPane.setAlignment(borderPane, Pos.CENTER);
    }


    /**
     * Mueva PacMan según la dirección indicada por el usuario (según la entrada del teclado del controlador)
     * @param direction la dirección ingresada más recientemente para que PacMan se mueva
     */
    public void movePacman(Direction direction) {
        Point2D potentialPacmanVelocity = changeVelocity(direction);
        Point2D potentialPacmanLocation = pacmanLocation.add(potentialPacmanVelocity);
        //si PacMan sale de la pantalla, envuélvete
        potentialPacmanLocation = setGoingOffscreenNewLocation(potentialPacmanLocation);
        //determina si PacMan debe cambiar de dirección o continuar en su dirección más reciente
        //si la entrada de dirección más reciente es la misma que la entrada de dirección anterior, verifique si hay paredes
        if (direction.equals(lastDirection)) {
            //si moverse en la misma dirección resultaría en golpear una pared, deja de moverte
            if (grid[(int) potentialPacmanLocation.getX()][(int) potentialPacmanLocation.getY()] == CellValue.WALL){
                pacmanVelocity = changeVelocity(Direction.NONE);
                setLastDirection(Direction.NONE);
            }
            else {
                pacmanVelocity = potentialPacmanVelocity;
                pacmanLocation = potentialPacmanLocation;
            }
        }
        //si la entrada de dirección más reciente no es la misma que la entrada anterior, verifique si hay paredes y esquinas antes de ir en una nueva dirección
        else {
            //si PacMan golpeara una pared con la nueva dirección ingresada, verifique que no golpearía una pared diferente si continúa en su dirección anterior
            if (grid[(int) potentialPacmanLocation.getX()][(int) potentialPacmanLocation.getY()] == CellValue.WALL){
                potentialPacmanVelocity = changeVelocity(lastDirection);
                potentialPacmanLocation = pacmanLocation.add(potentialPacmanVelocity);
                //si al cambiar de dirección chocaría contra otra pared, deja de moverte
                if (grid[(int) potentialPacmanLocation.getX()][(int) potentialPacmanLocation.getY()] == CellValue.WALL){
                    pacmanVelocity = changeVelocity(Direction.NONE);
                    setLastDirection(Direction.NONE);
                }
                else {
                    pacmanVelocity = changeVelocity(lastDirection);
                    pacmanLocation = pacmanLocation.add(pacmanVelocity);
                }
            }
            //de lo contrario, cambia de dirección y sigue moviéndote
            else {
                pacmanVelocity = potentialPacmanVelocity;
                pacmanLocation = potentialPacmanLocation;
                setLastDirection(direction);
            }
        }
    }

    /**
     * Mueve los fantasmas para seguir a PacMan según lo establecido en el método moveAGhost()
     */
    public void moveGhosts() {
        if (gameStarted && !waitingForKeyPress) {
            Point2D[] ghost1Data = moveAGhost(ghost1Velocity, ghost1Location);
            Point2D[] ghost2Data = moveAGhost(ghost2Velocity, ghost2Location);
            ghost1Velocity = ghost1Data[0];
            ghost1Location = ghost1Data[1];
            ghost2Velocity = ghost2Data[0];
            ghost2Location = ghost2Data[1];
        }
    }

    /**
     * Mueve un fantasma para seguir a PacMan si está en la misma fila o columna, o aléjate de PacMan si está en modo GhostEatingMode; de ​​lo contrario, muévete aleatoriamente cuando golpee una pared.
     * @param velocity la velocidad actual del fantasma especificado
     * @param location la ubicación actual del fantasma especificado
     * @return una matriz de Point2D que contiene una nueva velocidad y ubicación para el fantasma
     */
    public Point2D[] moveAGhost(Point2D velocity, Point2D location){
        Random generator = new Random();
        //si el fantasma está en la misma fila o columna que PacMan y no en GhostEatingMode,
        // ve en su dirección hasta llegar a una pared, luego ve en una dirección diferente
        //de lo contrario, ve en una dirección aleatoria, y si chocas contra una pared, ve en una dirección aleatoria diferente
        if (!ghostEatingMode) {
            //comprueba si el fantasma está en la columna de PacMan y avanza hacia él
            if (location.getY() == pacmanLocation.getY()) {
                if (location.getX() > pacmanLocation.getX()) {
                    velocity = changeVelocity(Direction.UP);
                } else {
                    velocity = changeVelocity(Direction.DOWN);
                }
                Point2D potentialLocation = location.add(velocity);
                //si el fantasma fuera a salir de la pantalla, envuélvete
                potentialLocation = setGoingOffscreenNewLocation(potentialLocation);
                //genera nuevas direcciones aleatorias hasta que el fantasma pueda moverse sin golpear una pared
                while (grid[(int) potentialLocation.getX()][(int) potentialLocation.getY()] == CellValue.WALL) {
                    int randomNum = generator.nextInt(4);
                    Direction direction = intToDirection(randomNum);
                    velocity = changeVelocity(direction);
                    potentialLocation = location.add(velocity);
                }
                location = potentialLocation;
            }
            //comprueba si el fantasma está en la fila de PacMan y avanza hacia él
            else if (location.getX() == pacmanLocation.getX()) {
                if (location.getY() > pacmanLocation.getY()) {
                    velocity = changeVelocity(Direction.LEFT);
                } else {
                    velocity = changeVelocity(Direction.RIGHT);
                }
                Point2D potentialLocation = location.add(velocity);
                potentialLocation = setGoingOffscreenNewLocation(potentialLocation);
                while (grid[(int) potentialLocation.getX()][(int) potentialLocation.getY()] == CellValue.WALL) {
                    int randomNum = generator.nextInt(4);
                    Direction direction = intToDirection(randomNum);
                    velocity = changeVelocity(direction);
                    potentialLocation = location.add(velocity);
                }
                location = potentialLocation;
            }
            //muévete en una dirección aleatoria consistente hasta que golpee una pared, luego elige una nueva dirección aleatoria
            else{
                Point2D potentialLocation = location.add(velocity);
                potentialLocation = setGoingOffscreenNewLocation(potentialLocation);
                while(grid[(int) potentialLocation.getX()][(int) potentialLocation.getY()] == CellValue.WALL){
                    int randomNum = generator.nextInt( 4);
                    Direction direction = intToDirection(randomNum);
                    velocity = changeVelocity(direction);
                    potentialLocation = location.add(velocity);
                }
                location = potentialLocation;
            }
        }
        //si el fantasma está en la misma fila o columna que Pacman y en GhostEatingMode, ve en la dirección opuesta
        // hasta que choca contra una pared, luego toma una dirección diferente
        //de lo contrario, ve en una dirección aleatoria, y si golpea una pared, ve en una dirección aleatoria diferente
        if (ghostEatingMode) {
            if (location.getY() == pacmanLocation.getY()) {
                if (location.getX() > pacmanLocation.getX()) {
                    velocity = changeVelocity(Direction.DOWN);
                } else {
                    velocity = changeVelocity(Direction.UP);
                }
                Point2D potentialLocation = location.add(velocity);
                potentialLocation = setGoingOffscreenNewLocation(potentialLocation);
                while (grid[(int) potentialLocation.getX()][(int) potentialLocation.getY()] == CellValue.WALL) {
                    int randomNum = generator.nextInt(4);
                    Direction direction = intToDirection(randomNum);
                    velocity = changeVelocity(direction);
                    potentialLocation = location.add(velocity);
                }
                location = potentialLocation;
            } else if (location.getX() == pacmanLocation.getX()) {
                if (location.getY() > pacmanLocation.getY()) {
                    velocity = changeVelocity(Direction.RIGHT);
                } else {
                    velocity = changeVelocity(Direction.LEFT);
                }
                Point2D potentialLocation = location.add(velocity);
                potentialLocation = setGoingOffscreenNewLocation(potentialLocation);
                while (grid[(int) potentialLocation.getX()][(int) potentialLocation.getY()] == CellValue.WALL) {
                    int randomNum = generator.nextInt(4);
                    Direction direction = intToDirection(randomNum);
                    velocity = changeVelocity(direction);
                    potentialLocation = location.add(velocity);
                }
                location = potentialLocation;
            }
            else{
                Point2D potentialLocation = location.add(velocity);
                potentialLocation = setGoingOffscreenNewLocation(potentialLocation);
                while(grid[(int) potentialLocation.getX()][(int) potentialLocation.getY()] == CellValue.WALL){
                    int randomNum = generator.nextInt( 4);
                    Direction direction = intToDirection(randomNum);
                    velocity = changeVelocity(direction);
                    potentialLocation = location.add(velocity);
                }
                location = potentialLocation;
            }
        }
        Point2D[] data = {velocity, location};
        return data;
    }

    /**
     * Envuelve el tablero de juego si la ubicación del objeto estaría fuera de la pantalla.
     * @param objectLocation la ubicación del objeto especificado
     * @return Point2D nueva ubicación envolvente
     */
    public Point2D setGoingOffscreenNewLocation(Point2D objectLocation) {
        //if object goes offscreen on the right
        if (objectLocation.getY() >= columnCount) {
            objectLocation = new Point2D(objectLocation.getX(), 0);
        }
        //if object goes offscreen on the left
        if (objectLocation.getY() < 0) {
            objectLocation = new Point2D(objectLocation.getX(), columnCount - 1);
        }
        return objectLocation;
    }

    /**
     * Conecta cada Dirección a un número entero 0-3
     * @param x un número entero
     * @return la Dirección correspondiente
     */
    public Direction intToDirection(int x){
        switch (x) {
            case 0:
                return Direction.LEFT;
            case 1:
                return Direction.RIGHT;
            case 2:
                return Direction.UP;
            default:
                return Direction.DOWN;
        }
    }

    /**
     * Restablece la ubicación y velocidad de Ghost1 a su estado original.
    */
    public void sendGhost1Home() {
        for (int row = 0; row < this.rowCount; row++) {
            for (int column = 0; column < this.columnCount; column++) {
                if (grid[row][column] == CellValue.GHOST1HOME) {
                    ghost1Location = new Point2D(row, column);
                }
            }
        }
        ghost1Velocity = new Point2D(-1, 0);
    }

    /**
     * Restablece la ubicación y velocidad de Ghost2 a su estado original.
    */
    public void sendGhost2Home() {
        for (int row = 0; row < this.rowCount; row++) {
            for (int column = 0; column < this.columnCount; column++) {
                if (grid[row][column] == CellValue.GHOST2HOME) {
                    ghost2Location = new Point2D(row, column);
                }
            }
        }
        ghost2Velocity = new Point2D(-1, 0);
    }
    
    /**
    * Restablece la ubicación de Pac-Man y su dirección a su estado original.
    */
    public void sendPacmanHome() {
        for (int row = 0; row < this.rowCount; row++) {
            for (int column = 0; column < this.columnCount; column++) {
                if (grid[row][column] == CellValue.PACMANHOME) {
                    pacmanLocation = new Point2D(row, column);
                }
            }
        }
        pacmanVelocity = new Point2D(0, 0); // Puedes ajustar la dirección según lo necesario
    }


    /**
     * Actualiza el modelo para reflejar el movimiento de PacMan y los fantasmas y el cambio de estado de cualquier objeto comido.
     * durante el transcurso de estos movimientos. Cambia el estado del juego hacia o desde el modo devorador de fantasmas.
     * @param direction la dirección ingresada más recientemente para que PacMan se mueva
     */
    public void step(Direction direction) {
        if (gameStarted) {
            this.movePacman(direction);
            //si PacMan está en un punto pequeño, elimina el punto pequeño
            CellValue pacmanLocationCellValue = grid[(int) pacmanLocation.getX()][(int) pacmanLocation.getY()];
            if (pacmanLocationCellValue == CellValue.SMALLDOT) {
                grid[(int) pacmanLocation.getX()][(int) pacmanLocation.getY()] = CellValue.EMPTY;
                dotCount--;
                score += 10;
            }
            //si PacMan está en un punto grande, borra el punto grande y cambia el estado del juego al modo devorador de fantasmas e inicializa el contador
            if (pacmanLocationCellValue == CellValue.BIGDOT) {
                grid[(int) pacmanLocation.getX()][(int) pacmanLocation.getY()] = CellValue.EMPTY;
                dotCount--;
                score += 50;
                ghostEatingMode = true;
                Controller.setGhostEatingModeCounter();
            }
            //envía el fantasma de regreso a la casa fantasma si PacMan está sobre un fantasma en modo devorador de fantasmas
            if (ghostEatingMode) {
                if (pacmanLocation.equals(ghost1Location)) {
                    sendGhost1Home();
                    score += 100;
                }
                if (pacmanLocation.equals(ghost2Location)) {
                    sendGhost2Home();
                    score += 100;
                }
            }
            //el juego termina si PacMan es devorado por un fantasma
            else {
                if (pacmanLocation.equals(ghost1Location)) {
                    lives--;
                    sendPacmanHome();
                    if(lives == 0){
                        gameOver = true;
                        pacmanVelocity = new Point2D(0,0);
                    } 
                }
                if (pacmanLocation.equals(ghost2Location)) {
                    lives--;
                    sendPacmanHome();
                    if(lives == 0){
                       gameOver = true;
                       pacmanVelocity = new Point2D(0,0); 
                    }   
                }
            }
            //mueve los fantasmas y comprueba nuevamente si se comen los fantasmas o PacMan (repetir estas comprobaciones ayuda a tener en cuenta los números pares/impares de cuadrados entre los fantasmas y PacMan)
            this.moveGhosts();
            if (ghostEatingMode) {
                if (pacmanLocation.equals(ghost1Location)) {
                    sendGhost1Home();
                    score += 100;
                }
                if (pacmanLocation.equals(ghost2Location)) {
                    sendGhost2Home();
                    score += 100;
                }
            }
            else {
                if (pacmanLocation.equals(ghost1Location)) {
                    lives--;
                    sendPacmanHome();
                    if(lives == 0){
                        gameOver = true;
                        pacmanVelocity = new Point2D(0,0);
                    } 
                }
                if (pacmanLocation.equals(ghost2Location)) {
                    lives--;
                    sendPacmanHome();
                    if(lives == 0){
                       gameOver = true;
                       pacmanVelocity = new Point2D(0,0); 
                    }   
                }
            }
            //comienza un nuevo nivel si el nivel está completo
            if (this.isLevelComplete()) {
                pacmanVelocity = new Point2D(0,0);
                startNextLevel();
            }
        }
    }

    /**
     * Conecta cada dirección a los vectores de velocidad Point2D (Izquierda = (-1,0), Derecha = (1,0), Arriba = (0,-1), Abajo = (0,1))
     * @param direction
     * @return Vector de velocidad Point2D
     */
    public Point2D changeVelocity(Direction direction){
        if(null == direction){
            return new Point2D(0,0);
        }
        else switch (direction) {
            case LEFT:
                return new Point2D(0,-1);
            case RIGHT:
                return new Point2D(0,1);
            case UP:
                return new Point2D(-1,0);
            case DOWN:
                return new Point2D(1,0);
            default:
                return new Point2D(0,0);
        }
    }

    public static boolean isGhostEatingMode() {
        return ghostEatingMode;
    }

    public static void setGhostEatingMode(boolean ghostEatingModeBool) {
        ghostEatingMode = ghostEatingModeBool;
    }

    public static boolean isYouWon() {
        return youWon;
    }

    /**
     * Cuando se comen todos los puntos, el nivel está completo.
     * @return booleano
     */
    public boolean isLevelComplete() {
        return this.dotCount == 0;
    }

    public static boolean isGameOver() {
        return gameOver;
    }

    public CellValue[][] getGrid() {
        return grid;
    }

    /**
     * @param row
     * @param column
     * @return el valor de celda de la celda (row, column)
     */
    public CellValue getCellValue(int row, int column) {
        assert row >= 0 && row < this.grid.length && column >= 0 && column < this.grid[0].length;
        return this.grid[row][column];
    }

    public static Direction getCurrentDirection() {
        return currentDirection;
    }

    public void setCurrentDirection(Direction direction) {
        currentDirection = direction;
    }

    public static Direction getLastDirection() {
        return lastDirection;
    }

    public void setLastDirection(Direction direction) {
        lastDirection = direction;
    }

    public int getScore() {
        return score;
    }

    public void setScore(int score) {
        this.score = score;
    }

    /** Añade nuevos puntos a la puntuación.
     *
     * @param points
     */
    public void addToScore(int points) {
        this.score += points;
    }

    public int getLevel() {
        return level;
    }

    public void setLevel(int level) {
        this.level = level;
    }

    public void setLives(int lives) {
        this.lives = lives;
    }

    public int getLives() {
        return lives;
    }
    
    /**
     * @return el número total de puntos que quedan (grandes y pequeños)
     */
    public int getDotCount() {
        return dotCount;
    }

    public void setDotCount(int dotCount) {
        this.dotCount = dotCount;
    }

    public int getRowCount() {
        return rowCount;
    }

    public void setRowCount(int rowCount) {
        this.rowCount = rowCount;
    }

    public int getColumnCount() {
        return columnCount;
    }

    public void setColumnCount(int columnCount) {
        this.columnCount = columnCount;
    }

    public Point2D getPacmanLocation() {
        return pacmanLocation;
    }

    public void setPacmanLocation(Point2D pacmanLocation) {
        this.pacmanLocation = pacmanLocation;
    }

    public Point2D getGhost1Location() {
        return ghost1Location;
    }

    public void setGhost1Location(Point2D ghost1Location) {
        this.ghost1Location = ghost1Location;
    }

    public Point2D getGhost2Location() {
        return ghost2Location;
    }

    public void setGhost2Location(Point2D ghost2Location) {
        this.ghost2Location = ghost2Location;
    }

    public Point2D getPacmanVelocity() {
        return pacmanVelocity;
    }

    public void setPacmanVelocity(Point2D velocity) {
        this.pacmanVelocity = velocity;
    }

    public Point2D getGhost1Velocity() {
        return ghost1Velocity;
    }

    public void setGhost1Velocity(Point2D ghost1Velocity) {
        this.ghost1Velocity = ghost1Velocity;
    }

    public Point2D getGhost2Velocity() {
        return ghost2Velocity;
    }

    public void setGhost2Velocity(Point2D ghost2Velocity) {
        this.ghost2Velocity = ghost2Velocity;
    }
    
    public static void startGame() {
        gameStarted = true;
    }
    
    public static void stopGame() {
        gameStarted = false;
    }
    
    public void setWaitingForKeyPress(boolean value) {
        this.waitingForKeyPress = value;
    }

}