import java.awt.*;

/**
 * Clase que representa un triangulo que se puede mover y modificar.
 */

public class Triangle{
    
    public static int VERTICES=3;
    
    private int height;
    private int width;
    private int xPosition;
    private int yPosition;
    private String color;
    private boolean isVisible;

    /**
     * Crea el triangulo con sus valores iniciales.
     */
    public Triangle(){
        height = 30;
        width = 40;
        xPosition = 140;
        yPosition = 15;
        color = "green";
        isVisible = false;
    }

    /**
     * Muestra el triangulo en pantalla.
     */
    public void makeVisible(){
        isVisible = true;
        draw();
    }
    
    /**
     * Oculta el triangulo.
     */
    public void makeInvisible(){
        erase();
        isVisible = false;
    }
    
    /**
     * Mueve el triangulo hacia la derecha.
     */
    public void moveRight(){
        moveHorizontal(20);
    }

    /**
     * Mueve el triangulo hacia la izquierda.
     */
    public void moveLeft(){
        moveHorizontal(-20);
    }

    /**
     * Mueve el triangulo hacia arriba.
     */
    public void moveUp(){
        moveVertical(-20);
    }

    /**
     * Mueve el triangulo hacia abajo.
     */
    public void moveDown(){
        moveVertical(20);
    }

    /**
     * Mueve el triangulo horizontalmente.
     * @param distance the desired distance in pixels
     */
    public void moveHorizontal(int distance){
        erase();
        xPosition += distance;
        draw();
    }

    /**
     * Mueve el triangulo verticalmente.
     * @param distance the desired distance in pixels
     */
    public void moveVertical(int distance){
        erase();
        yPosition += distance;
        draw();
    }

    /**
     * Mueve el triangulo poco a poco horizontalmente.
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
     * Mueve el triangulo poco a poco verticalmente.
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
     * @param newWidht the new width in pixels. newWidht must be >=0.
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
     * Dibuja el triangulo con sus valores actuales.
     */
    private void draw(){
        if(isVisible) {
            Canvas canvas = Canvas.getCanvas();
            int[] xpoints = { xPosition, xPosition + (width/2), xPosition - (width/2) };
            int[] ypoints = { yPosition, yPosition + height, yPosition + height };
            canvas.draw(this, color, new Polygon(xpoints, ypoints, 3));
            canvas.wait(10);
        }
    }

    /*
     * Quita el triangulo de la pantalla.
     */
    private void erase(){
        if(isVisible) {
            Canvas canvas = Canvas.getCanvas();
            canvas.erase(this);
        }
    }
}
