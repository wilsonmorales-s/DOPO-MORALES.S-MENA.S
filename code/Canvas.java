import javax.swing.*;
import java.awt.*;
import java.util.List;
import java.util.*;

/**
 * Esta clase se encarga de mostrar las figuras del proyecto en una ventana.
 * Es una version adaptada del Canvas usado en los ejemplos de BlueJ.
 */
public class Canvas{
    // Esta parte maneja las figuras y sus colores para poder dibujarlas
    // sin tener que meter esta logica dentro de cada figura.

	private static Canvas canvasSingleton;

	/** Obtiene el Canvas que se usa para dibujar las figuras. */
	public static Canvas getCanvas(){
		if(canvasSingleton == null) {
			canvasSingleton = new Canvas("BlueJ Shapes Demo", 300, 300, 
										 Color.white);
		}
		canvasSingleton.setVisible(true);
		return canvasSingleton;
	}

	//  ----- instance part -----

    private JFrame frame;
    private CanvasPane canvas;
    private Graphics2D graphic;
    private Color backgroundColour;
    private Image canvasImage;
    private List <Object> objects;
    private HashMap <Object,ShapeDescription> shapes;
    
    /**
     * Crea la ventana donde se van a mostrar las figuras.
     */
    private Canvas(String title, int width, int height, Color bgColour){
        frame = new JFrame();
        canvas = new CanvasPane();
        frame.setContentPane(canvas);
        frame.setTitle(title);
        canvas.setPreferredSize(new Dimension(width, height));
        backgroundColour = bgColour;
        frame.pack();
        objects = new ArrayList <Object>();
        shapes = new HashMap <Object,ShapeDescription>();
    }

    /**
     * Set the canvas visibility and brings canvas to the front of screen
     * when made visible. This method can also be used to bring an already
     * visible canvas to the front of other windows.
     * @param visible  boolean value representing the desired visibility of
     * the canvas (true or false) 
     */
    public void setVisible(boolean visible){
        if(graphic == null) {
            // La primera vez se crea la imagen y se pinta el fondo.
            Dimension size = canvas.getSize();
            canvasImage = canvas.createImage(size.width, size.height);
            graphic = (Graphics2D)canvasImage.getGraphics();
            graphic.setColor(backgroundColour);
            graphic.fillRect(0, 0, size.width, size.height);
            graphic.setColor(Color.black);
        }
        frame.setVisible(visible);
    }

    /**
     * Dibuja una figura en el Canvas.
     * @param referenceObject objeto que identifica la figura
     * @param color color de la figura
     * @param shape figura que se va a dibujar
     */
     // Si la figura ya estaba, se reemplaza para mantener una sola copia.
    public void draw(Object referenceObject, String color, Shape shape){
    	objects.remove(referenceObject);   // Por si ya estaba guardada.
    	objects.add(referenceObject);      // La agregamos al final.
    	shapes.put(referenceObject, new ShapeDescription(shape, color));
    	redraw();
    }
 
    /**
     * Quita una figura del Canvas.
     * @param referenceObject figura que se quiere quitar
     */
    public void erase(Object referenceObject){
    	objects.remove(referenceObject);   // Por si ya estaba guardada.
    	shapes.remove(referenceObject);
    	redraw();
    }

    /**
     * Cambia el color que se usa para dibujar.
     * @param colorString nombre del color que se quiere usar
     */
    public void setForegroundColor(String colorString){
        graphic.setColor(CssColors.toAwtColor(colorString));
    }

    /**
     * Hace una pequeña pausa, principalmente para las animaciones.
     * @param milliseconds tiempo de espera
     */
    public void wait(int milliseconds){
        try{
            Thread.sleep(milliseconds);
        } catch (Exception e){
            // ignoring exception at the moment
        }
    }

	/** Vuelve a dibujar las figuras que estan en el Canvas. */
	private void redraw(){
		erase();
		for(Iterator i=objects.iterator(); i.hasNext(); ) {
                       shapes.get(i.next()).draw(graphic);
        }
        canvas.repaint();
    }
       
    /** Limpia todo el Canvas, pero no lo vuelve a pintar. */
    private void erase(){
        Color original = graphic.getColor();
        graphic.setColor(backgroundColour);
        Dimension size = canvas.getSize();
        graphic.fill(new java.awt.Rectangle(0, 0, size.width, size.height));
        graphic.setColor(original);
    }


    /************************************************************************
     * Clase interna que funciona como la parte donde se dibuja la imagen.
     ************************************************************************/
    private class CanvasPane extends JPanel{
        public void paint(Graphics g){
            g.drawImage(canvasImage, 0, 0, null);
        }
    }
    
    /************************************************************************
     * Clase interna que funciona como la parte donde se dibuja la imagen.
     ************************************************************************/
    private class ShapeDescription{
    	private Shape shape;
    	private String colorString;

		public ShapeDescription(Shape shape, String color){
    		this.shape = shape;
    		colorString = color;
    	}

		public void draw(Graphics2D graphic){
			setForegroundColor(colorString);
			graphic.draw(shape);
			graphic.fill(shape);
		}
    }

}
