import java.util.ArrayList;

/**
 * Rueda de la maquina tragamonedas. Cada rueda tiene su propia lista de
 * simbolos y una posicion que indica cual simbolo esta mostrando. Se
 * dibuja como un marco de color con un cuadro blanco adentro. Cada
 * tipo de rueda es una subclase.<br>
 * <b>(symbols, position, locked, leftNeighbor)</b><br>
 * <b>Inv:</b> si symbols esta vacia, position = 0;
 * si no, 1 &lt;= position &lt;= #symbols
 *
 * @author Samuel Mena Serrato
 * @author Wilson Morales Sanchez
 */
public abstract class Wheel{

    /** Lado del cuadro blanco donde se dibuja el simbolo. */
    public static final int SIZE = 50;

    protected ArrayList<Symbol> symbols;
    protected int position;
    protected boolean locked;
    protected Wheel leftNeighbor;
    private boolean visible;
    private int x;
    private int y;
    private Rectangle frame;
    private Rectangle box;

    /** Crea una rueda vacia, sin bloquear y sin vecina. */
    public Wheel(){
        symbols = new ArrayList<Symbol>();
        position = 0;
        locked = false;
        leftNeighbor = null;
        visible = false;
        frame = new Rectangle();
        box = new Rectangle();
    }

    /**
     * Consulta el tipo de la rueda.
     *
     * @return nombre del tipo
     */
    public abstract String getType();

    /**
     * Consulta el color del marco. Cada tipo de rueda tiene uno distinto
     * para que se pueda reconocer en pantalla.
     *
     * @return color del marco
     */
    public abstract String getFrameColor();

    /**
     * Bloquea la rueda para que no gire.
     *
     * @throws SlotMachineException si la rueda no se deja bloquear
     */
    public void lock() throws SlotMachineException{
        locked = true;
    }

    /** Desbloquea la rueda. */
    public void unlock(){
        locked = false;
    }

    /**
     * Indica si la rueda esta bloqueada.
     *
     * @return true si esta bloqueada
     */
    public boolean isLocked(){
        return locked;
    }

    /**
     * Revisa que la rueda se pueda intercambiar con otra.
     *
     * @throws SlotMachineException si la rueda esta bloqueada
     */
    public void checkSwappable() throws SlotMachineException{
        if(locked) throw new SlotMachineException(SlotMachineException.LOCKED_WHEEL);
    }

    /**
     * Revisa que la rueda se pueda eliminar. Una rueda normal siempre se puede.
     *
     * @throws SlotMachineException si la rueda no se deja eliminar
     */
    public void checkDeletable() throws SlotMachineException{
    }

    /**
     * Guarda cual es la rueda de la izquierda.
     *
     * @param neighbor rueda de la izquierda, o null si es la primera
     */
    public void setLeftNeighbor(Wheel neighbor){
        leftNeighbor = neighbor;
    }

    /**
     * Agrega un simbolo. La rueda sigue mostrando el que tenia y, si
     * estaba vacia, muestra el primero.
     *
     * @param pos    posicion donde se agrega (desde 1)
     * @param symbol simbolo que se agrega
     */
    public void addSymbol(int pos, Symbol symbol){
        eraseSymbol();
        symbols.add(pos - 1, symbol);
        if(position == 0){
            position = 1;
        }else if(pos <= position){
            position++;
        }
        drawSymbol();
    }

    /**
     * Quita un simbolo. Si era el que mostraba, pasa al primero.
     *
     * @param pos posicion del simbolo que se quita (desde 1)
     */
    public void delSymbol(int pos){
        eraseSymbol();
        symbols.remove(pos - 1);
        if(symbols.isEmpty()){
            position = 0;
        }else if(pos == position){
            position = 1;
        }else if(pos < position){
            position--;
        }
        drawSymbol();
    }

    /**
     * Gira la rueda. Primero avisa a todos sus simbolos que hubo un giro,
     * luego se mueve y al final avisa al simbolo donde quedo.
     *
     * @param steps cuantos simbolos avanza
     */
    public void spin(int steps){
        if(symbols.isEmpty()){
            return;
        }
        eraseSymbol();
        for(Symbol symbol : symbols){
            symbol.onSpin();
        }
        move(steps);
        symbols.get(position - 1).onSelect();
        drawSymbol();
    }

    /**
     * Avanza la rueda; si pasa del ultimo simbolo vuelve al primero.
     *
     * @param steps cuantos simbolos avanza
     */
    protected void move(int steps){
        for(int i = 0; i < steps; i++){
            position++;
            if(position > symbols.size()){
                position = 1;
            }
        }
    }

    /**
     * Pone la rueda en un simbolo y le avisa que fue seleccionado.
     *
     * @param pos posicion del simbolo (desde 1)
     */
    public void placeSymbol(int pos){
        setPosition(pos);
        symbols.get(position - 1).onSelect();
        drawSymbol();
    }

    /**
     * Cambia la posicion sin avisarle al simbolo.
     *
     * @param pos posicion del simbolo (desde 1)
     */
    public void setPosition(int pos){
        eraseSymbol();
        position = pos;
        drawSymbol();
    }

    /**
     * Consulta la posicion del simbolo que muestra la rueda.
     *
     * @return posicion desde 1, o 0 si la rueda esta vacia
     */
    public int getPosition(){
        return position;
    }

    /**
     * Consulta el color del simbolo que muestra la rueda.
     *
     * @return color del simbolo, o null si la rueda esta vacia
     */
    public String getColor(){
        if(position == 0){
            return null;
        }
        return symbols.get(position - 1).getColor();
    }

    /**
     * Pone la rueda en otro lugar de la pantalla.
     *
     * @param newX esquina izquierda
     * @param newY esquina superior
     */
    public void setLocation(int newX, int newY){
        boolean wasVisible = visible;
        makeInvisible();
        x = newX;
        y = newY;
        if(wasVisible){
            makeVisible();
        }
    }

    /** Muestra la rueda con su marco y su simbolo. */
    public void makeVisible(){
        visible = true;
        // Un Rectangle nuevo empieza en (70, 15)
        frame = new Rectangle();
        frame.changeSize(SIZE + 10, SIZE + 10);
        frame.changeColor(getFrameColor());
        frame.moveHorizontal(x - 5 - 70);
        frame.moveVertical(y - 5 - 15);
        frame.makeVisible();
        box = new Rectangle();
        box.changeSize(SIZE, SIZE);
        box.changeColor("white");
        box.moveHorizontal(x - 70);
        box.moveVertical(y - 15);
        box.makeVisible();
        drawSymbol();
    }

    /** Oculta la rueda. */
    public void makeInvisible(){
        eraseSymbol();
        frame.makeInvisible();
        box.makeInvisible();
        visible = false;
    }

    // Dibuja el simbolo actual si la rueda esta visible.
    private void drawSymbol(){
        if(visible && position != 0){
            symbols.get(position - 1).draw(x, y);
        }
    }

    // Borra el simbolo actual.
    private void eraseSymbol(){
        if(position != 0){
            symbols.get(position - 1).erase();
        }
    }
}
