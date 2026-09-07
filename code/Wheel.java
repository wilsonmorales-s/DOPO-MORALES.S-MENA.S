/**
 * Representa una rueda de la maquina.
 * La rueda usa un Circle para mostrarse y guarda la posicion del
 * simbolo que tiene. Tambien puede quedar bloqueada para que no gire.
 *
 * @author Samuel Mena Serrato
 */
public class Wheel{

    private static final int DIAMETER = 50;

    private Circle shape;
    private int position;
    private boolean locked;
    private int x;
    private int y;

    /**
     * Crea una rueda nueva, vacia (sin simbolo) y sin fijar, ubicada
     * en las coordenadas dadas.
     *
     * @param x coordenada horizontal del centro de la rueda
     * @param y coordenada vertical del centro de la rueda
     */
    public Wheel(int x, int y){
        shape = new Circle();
        shape.changeSize(DIAMETER);
        this.x = x;
        this.y = y;
        shape.moveHorizontal(x - 20);
        shape.moveVertical(y - 15);
        position = 0;
        locked = false;
    }

    /**
     * Cambia el simbolo mostrado por esta rueda.
     *
     * @param newPosition indice 1-based del simbolo dentro de la paleta
     *                     de la maquina (0 = vacia)
     * @param color        color CSS a pintar en la rueda, o {@code null}
     *                     si no se debe repintar (por ejemplo al vaciarla)
     */
    public void setPosition(int newPosition, String color){
        position = newPosition;
        if(color != null){
            shape.changeColor(color);
        }
    }

    /**
     * Consulta la posicion (indice 1-based en la paleta) que tiene
     * actualmente esta rueda.
     *
     * @return la posicion actual, o 0 si la rueda esta vacia
     */
    public int getPosition(){
        return position;
    }

    /** Fija la rueda: mientras este locked, la maquina no debe girarla. */
    public void lock(){
        locked = true;
    }

    /** Suelta la rueda para que vuelva a poder girar. */
    public void unlock(){
        locked = false;
    }

    /**
     * Indica si la rueda esta actualmente fija.
     *
     * @return {@code true} si esta fija (locked), {@code false} si no
     */
    public boolean isLocked(){
        return locked;
    }

    /**
     * Reubica la rueda cuando cambia la distribucion de la maquina
     * (por ejemplo al agregar o quitar otra rueda).
     *
     * @param newX nueva coordenada horizontal del centro
     * @param newY nueva coordenada vertical del centro
     */
    public void relayout(int newX, int newY){
        shape.moveHorizontal(newX - x);
        shape.moveVertical(newY - y);
        x = newX;
        y = newY;
    }

    /** Hace visible la rueda en el canvas. */
    public void show(){
        shape.makeVisible();
    }

    /** Oculta la rueda del canvas. */
    public void hide(){
        shape.makeInvisible();
    }
}
