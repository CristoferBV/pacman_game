
package Model;

import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Pane;

/**
 *
 * @author Cristofer
 */
public class Pacman extends Pane {
    private double velocidad;
    private int posX;
    private int posY;
    private String direccion;
    private String estaVivo;
    private boolean tienePoder;
    
    // Constructor y métodos getters y setters
    
    public Pacman() { 
        setMinSize(35, 15);
        setMaxSize(20, 20);
        
        Image pacmanImage = new Image("IMG/pacmanRight.gif");
        ImageView imageView = new ImageView(pacmanImage);
        imageView.setFitWidth(20);
        imageView.setFitHeight(20);
        
        // Agrega el ImageView como hijo del Pane.
        getChildren().add(imageView);
        
        // Inicializa la posición del Pacman
        posX = 0;
        posY = 0;
        setLayoutX(posX);
        setLayoutY(posY);
    }

    public Pacman(double velocidad, int posX, int posY, String direccion, String estaVivo, boolean tienePoder) {
        this.velocidad = velocidad;
        this.posX = posX;
        this.posY = posY;
        this.direccion = direccion;
        this.estaVivo = estaVivo;
        this.tienePoder = tienePoder;
    }
    
    public void mover() {
        if (direccion != null) {
            switch (direccion) {
                case "UP":
                    posY -= velocidad;
                    break;
                case "DOWN":
                    posY += velocidad;
                    break;
                case "LEFT":
                    posX -= velocidad;
                    break;
                case "RIGHT":
                    posX += velocidad;
                    break;
                default:
                    break;
            }

            // Actualiza la posición del Pacman en la interfaz gráfica
            setLayoutX(posX);
            setLayoutY(posY);
        }
    }

    public double getVelocidad() {
        return velocidad;
    }

    public int getPosX() {
        return posX;
    }

    public int getPosY() {
        return posY;
    }

    public String getDireccion() {
        return direccion;
    }

    public String getEstaVivo() {
        return estaVivo;
    }

    public boolean isTienePoder() {
        return tienePoder;
    }
    
    public void setVelocidad(double velocidad) {
        this.velocidad = velocidad;
    }

    public void setPosX(int posX) {
        this.posX = posX;
    }

    public void setPosY(int posY) {
        this.posY = posY;
    }

    public void setDireccion(String direccion) {
        this.direccion = direccion;
    }

    public void setEstaVivo(String estaVivo) {
        this.estaVivo = estaVivo;
    }

    public void setTienePoder(boolean tienePoder) {
        this.tienePoder = tienePoder;
    }
    
}