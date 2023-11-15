
package Model;

/**
 *
 * @author Cristofer
 */
public class Maze {
    private int numRows;
    private int numColumns;
    private char[][] board;  // Representa diferentes elementos (paredes (Wall), puntos (points), espacio vacío (void), etc.).
    private Pacman pacman;

    public Maze(int numRows, int numColumns) {
        this.numRows = numRows;
        this.numColumns = numColumns;
        this.board = new char[numRows][numColumns];
        // Inicialización del laberinto
        initializeMaze();
        
        // Crear una instancia de Pacman
        pacman = new Pacman();
    }

    private void initializeMaze() {
        //Laberinto
        char[][] simpleMaze = {
            {'W', 'W', 'W', 'W', 'W', 'W', 'W', 'W', 'W', 'W', 'W', 'W', 'W', 'W', 'W', 'W', 'W', 'W', 'W', 'W'},
            {'W', '.', '.', '.', '.', '.', '.', '.', '.', '.', '.', '.', '.', '.', '.', '.', '.', '.', '.', 'W'}, 
            {'W', '.', '.', '.', '.', '.', 'W', '.', '.', 'W', '.', '.', '.', '.', '.', 'W', '.', 'W', 'W', 'W'},
            {'W', '.', '.', 'W', 'W', 'W', 'W', '.', '.', 'W', '.', '.', 'W', '.', '.', '.', '.', '.', '.', 'W'},
            {'W', '.', '.', '.', '.', '.', 'W', '.', 'W', 'W', '.', '.', 'W', '.', '.', 'W', '.', 'W', '.', 'W'},
            {'W', '.', '.', '.', '.', '.', 'W', '.', '.', '.', '.', '.', '.', 'W', '.', '.', '.', '.', 'W', 'W'},
            {'W', '.', '.', 'W', 'W', 'W', 'W', '.', 'W', 'W', '.', '.', 'W', 'W', 'W', 'W', 'W', 'W', '.', 'W'},
            {'W', '.', '.', '.', '.', '.', '.', '.', '.', '.', '.', '.', '.', '.', '.', '.', '.', '.', '.', 'W'},
            {'W', 'W', '.', '.', '.', '.', '.', '.', '.', '.', '.', '.', '.', '.', '.', '.', '.', '.', '.', 'W'},
            {'W', '.', '.', '.', '.', '.', 'W', 'W', '.', 'W', 'W', '.', '.', '.', '.', '.', '.', '.', '.', 'W'},
            {'W', '.', '.', '.', '.', '.', 'W', 'G', '.', 'G', 'W', '.', '.', '.', '.', '.', '.', '.', '.', 'W'},
            {'W', '.', '.', '.', '.', '.', 'W', '.', 'G', '.', 'W', '.', '.', '.', '.', '.', '.', '.', '.', 'W'},
            {'W', '.', '.', '.', '.', '.', 'W', 'G', '.', '.', 'W', 'W', 'W', '.', 'W', '.', '.', 'W', 'W', 'W'},
            {'W', '.', 'W', 'W', 'W', 'W', 'W', 'W', 'W', 'W', 'W', '.', '.', '.', '.', '.', '.', '.', '.', 'W'},
            {'W', '.', '.', 'W', '.', '.', '.', '.', 'P', '.', '.', '.', '.', '.', '.', '.', '.', '.', '.', 'W'},
            {'W', '.', '.', 'W', '.', '.', '.', '.', '.', '.', '.', '.', '.', '.', '.', '.', '.', '.', '.', 'W'},
            {'W', '.', '.', '.', '.', '.', '.', '.', 'W', 'W', 'W', '.', '.', '.', '.', '.', '.', '.', '.', 'W'},
            {'W', '.', '.', '.', '.', '.', '.', '.', '.', '.', '.', '.', '.', '.', '.', '.', '.', '.', '.', 'W'},
            {'W', '.', '.', '.', '.', '.', '.', '.', '.', '.', '.', '.', '.', '.', '.', '.', '.', '.', '.', 'W'},
            {'W', 'W', 'W', 'W', 'W', 'W', 'W', 'W', 'W', 'W', 'W', 'W', 'W', 'W', 'W', 'W', 'W', 'W', 'W', 'W'},
        };

        // Copia del diseño del laberinto a la matriz del tablero (GridPane).
        for (int row = 0; row < numRows; row++) {
            for (int col = 0; col < numColumns; col++) {
                board[row][col] = simpleMaze[row][col]; 
            }
        }
    }

    public int getNumRows() {
        return numRows;
    }

    public int getNumColumns() {
        return numColumns;
    }

    public char[][] getBoard() {
        return board;
    }
    
    public Pacman getPacman() {
        return pacman;
    }

    public void setNumRows(int numRows) {
        this.numRows = numRows;
    }

    public void setNumColumns(int numColumns) {
        this.numColumns = numColumns;
    }

    public void setBoard(char[][] board) {
        this.board = board;
    }
    
    public char getCell(int row, int column) {
        return board[row][column];
    }

    public void setCell(int row, int column, char cellValue) {
        board[row][column] = cellValue;
    }

    // Otros métodos para obtener información sobre el laberinto, como verificar si una celda está ocupada, etc.
}
