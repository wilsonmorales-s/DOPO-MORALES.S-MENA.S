/**
 * Simbolo efimero. Cada vez que su rueda gira se hace mas pequeno,
 * hasta quedar como un punto. Se dibuja como un cuadrado.<br>
 * <b>Inv:</b> MIN_SIZE &lt;= size &lt;= FULL_SIZE
 *
 * @author Samuel Mena Serrato
 * @author Wilson Morales Sanchez
 */
public final class EphemeralSymbol extends Symbol{

    /** Tamano minimo: el simbolo queda como un punto. */
    public static final int MIN_SIZE = 4;

    /** Pixeles que pierde en cada giro. */
    public static final int SHRINK = 8;

    private Rectangle square;

    /**
     * Crea un simbolo efimero con su tamano completo.
     *
     * @param color color del simbolo
     */
    public EphemeralSymbol(String color){
        super(color);
        square = new Rectangle();
    }

    @Override
    public String getType(){
        return "ephemeral";
    }

    @Override
    public void draw(int x, int y){
        erase();
        square = new Rectangle();
        square.changeSize(size, size);
        square.changeColor(color);
        // Un Rectangle nuevo empieza en (70, 15)
        square.moveHorizontal(x + (Wheel.SIZE - size) / 2 - 70);
        square.moveVertical(y + (Wheel.SIZE - size) / 2 - 15);
        square.makeVisible();
    }

    @Override
    public void erase(){
        square.makeInvisible();
    }

    /** Se hace mas pequeno, sin bajar del tamano minimo. */
    @Override
    public void onSpin(){
        size = size - SHRINK;
        if(size < MIN_SIZE){
            size = MIN_SIZE;
        }
    }
}
