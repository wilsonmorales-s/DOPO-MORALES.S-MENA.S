import java.awt.*;

/**
 * Clase que representa un rectangulo que se puede mover y modificar.
 */


 
public class Rectangle{

    public static int EDGES = 4;
    
    private int height;
    private int width;
    private int xPosition;
    private int yPosition;
    private String color;
    private boolean isVisible;

    /**
     * Crea el rectangulo con sus valores iniciales.
     */
    public Rectangle(){
        height = 30;
        width = 40;
        xPosition = 70;
        yPosition = 15;
        color = "magenta";
        isVisible = false;
    }
    

    /**
     * Muestra el rectangulo en pantalla.
     */
    public void makeVisible(){
        isVisible = true;
        draw();
    }
    
    /**
     * Oculta el rectangulo.
     */
    public void makeInvisible(){
        erase();
        isVisible = false;
    }
    
    /**
     * Mueve el rectangulo hacia la derecha.
     */
    public void moveRight(){
        moveHorizontal(20);
    }

    /**
     * Mueve el rectangulo hacia la izquierda.
     */
    public void moveLeft(){
        moveHorizontal(-20);
    }

    /**
     * Mueve el rectangulo hacia arriba.
     */
    public void moveUp(){
        moveVertical(-20);
    }

    /**
     * Mueve el rectangulo hacia abajo.
     */
    public void moveDown(){
        moveVertical(20);
    }

    /**
     * Mueve el rectangulo horizontalmente.
     * @param distance the desired distance in pixels
     */
    public void moveHorizontal(int distance){
        erase();
        xPosition += distance;
        draw();
    }

    /**
     * Mueve el rectangulo verticalmente.
     * @param distance the desired distance in pixels
     */
    public void moveVertical(int distance){
        erase();
        yPosition += distance;
        draw();
    }

    /**
     * Mueve el rectangulo poco a poco horizontalmente.
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
     * Mueve el rectangulo poco a poco verticalmente.
     * @param distance the desired distance in pixels
     */
    public void slowMoveVertical(int distance){
        int delta;

        if(distance < 0) {
            delta = -1;
            distance = -distance;
        } else {
            delta = 1;
        }

        for(int i = 0; i < distance; i++){
            yPosition += delta;
            draw();
        }
    }

    /**
     * Cambia el tamaño del triangulo.
     * @param newHeight the new height in pixels. newHeight must be >=0.
     * @param newWidht the new width in pixels. newWidth must be >=0.
     */
    public void changeSize(int newHeight, int newWidth) {
        erase();
        height = newHeight;
        width = newWidth;
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

    /*
     * Dibuja el rectangulo con sus valores actuales.
     */

    private void draw() {
        if(isVisible) {
            Canvas canvas = Canvas.getCanvas();
            canvas.draw(this, color,
                new java.awt.Rectangle(xPosition, yPosition, 
                                       width, height));
            canvas.wait(10);
        }
    }

    /*
     * Quita el rectangulo de la pantalla.
     */
    private void erase(){
        if(isVisible) {
            Canvas canvas = Canvas.getCanvas();
            canvas.erase(this);
        }
    }
}

