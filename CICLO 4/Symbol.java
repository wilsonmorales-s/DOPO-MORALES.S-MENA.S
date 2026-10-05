/**
 * Simbolo de una rueda. Cada simbolo tiene un color y sabe dibujarse
 * dentro de la rueda. Cada tipo de simbolo es una subclase.<br>
 * <b>(color, size)</b><br>
 * <b>Inv:</b> color es un color CSS valido y size &gt; 0
 *
 * @author Samuel Mena Serrato
 * @author Wilson Morales Sanchez
 */
public abstract class Symbol{

    /** Tamano normal del simbolo, en pixeles. */
    public static final int FULL_SIZE = 40;

    protected String color;
    protected int size;

    /**
     * Crea un simbolo con su tamano completo.
     *
     * @param color color del simbolo
     */
    public Symbol(String color){
        this.color = color;
        size = FULL_SIZE;
    }

    /**
     * Consulta el color del simbolo.
     *
     * @return color del simbolo
     */
    public String getColor(){
        return color;
    }

    /**
     * Consulta el tamano actual del simbolo.
     *
     * @return tamano en pixeles
     */
    public int getSize(){
        return size;
    }

    /**
     * Consulta el tipo del simbolo.
     *
     * @return nombre del tipo
     */
    public abstract String getType();

    /**
     * Dibuja el simbolo en el centro de una rueda.
     *
     * @param x esquina izquierda de la rueda
     * @param y esquina superior de la rueda
     */
    public abstract void draw(int x, int y);

    /** Borra el simbolo de la pantalla. */
    public abstract void erase();

    /** Se llama cada vez que la rueda gira. Por defecto no hace nada. */
    public void onSpin(){
    }

    /** Se llama cuando la rueda queda en este simbolo. Por defecto no hace nada. */
    public void onSelect(){
    }
}
