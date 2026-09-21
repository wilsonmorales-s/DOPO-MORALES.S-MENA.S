import java.awt.*;
import java.awt.geom.*;

/**
 * Clase que representa un circulo y permite moverlo y cambiarlo.
 * Se usa el Canvas para mostrarlo en pantalla.
 */

public class Circle{

    public static final double PI=3.1416;
    
    private int diameter;
    private int xPosition;
    private int yPosition;
    private String color;
    private boolean isVisible;
    

    public Circle(){
        diameter = 30;
        xPosition = 20;
        yPosition = 15;
        color = "blue";
        isVisible = false;
    }


       
    public void makeVisible(){
        isVisible = true;
        draw();
    }
    

    public void makeInvisible(){
        erase();
        isVisible = false;
    }

    private void draw(){
        if(isVisible) {
            Canvas canvas = Canvas.getCanvas();
            canvas.draw(this, color, 
                new Ellipse2D.Double(xPosition, yPosition, 
                diameter, diameter));
            canvas.wait(10);
        }
    }

    private void erase(){
        if(isVisible) {
            Canvas canvas = Canvas.getCanvas();
            canvas.erase(this);
        }
    }
    
    /**
     * Mueve el circulo hacia la derecha.
     */
    public void moveRight(){
        moveHorizontal(20);
    }

    /**
     * Mueve el circulo hacia la izquierda.
     */
    public void moveLeft(){
        moveHorizontal(-20);
    }

    /**
     * Mueve el circulo hacia arriba.
     */
    public void moveUp(){
        moveVertical(-20);
    }

    /**
     * Mueve el circulo hacia abajo.
     */
    public void moveDown(){
        moveVertical(20);
    }

    /**
     * Mueve el circulo horizontalmente.
     * @param distance the desired distance in pixels
     */
    public void moveHorizontal(int distance){
        erase();
        xPosition += distance;
        draw();
    }

    /**
     * Mueve el circulo verticalmente.
     * @param distance the desired distance in pixels
     */
    public void moveVertical(int distance){
        erase();
        yPosition += distance;
        draw();
    }

    /**
     * Mueve el circulo poco a poco horizontalmente.
     * @param distance the desired distance in pixels
     */
    public void slowMoveHorizontal(int distance){
        int delta;

        if(distance < 0) {
            delta = -1;
            distance = -distance;
        } else {
            delta = 1;
        }

        for(int i = 0; i < distance; i++){
            xPosition += delta;
            draw();
        }
    }

    /**
     * Mueve el circulo poco a poco verticalmente.
     * @param distance the desired distance in pixels
     */
    public void slowMoveVertical(int distance){
        int delta;

        if(distance < 0) {
            delta = -1;
            distance = -distance;
        }else {
            delta = 1;
        }

        for(int i = 0; i < distance; i++){
            yPosition += delta;
            draw();
        }
    }

    /**
     * Cambia el tamaño del circulo.
     * @param newDiameter the new size (in pixels). Size must be >=0.
     */
    public void changeSize(int newDiameter){
        erase();
        diameter = newDiameter;
        draw();
    }

    /**
     * Cambia el color.
     * @param color the new color. Valid colors are "red", "yellow", "blue", "green",
     * "magenta" and "black".
     */
    public void changeColor(String newColor){
        color = newColor;
        draw();
    }



}
