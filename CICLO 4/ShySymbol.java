/**
 * Simbolo timido. Cada vez que su rueda queda en el, cambia de visible
 * a escondido o al reves. Se dibuja como un triangulo.<br>
 * <b>(hidden)</b>
 *
 * @author Samuel Mena Serrato
 * @author Wilson Morales Sanchez
 */
public final class ShySymbol extends Symbol{

    private boolean hidden;
    private Triangle triangle;

    /**
     * Crea un simbolo timido, visible al inicio.
     *
     * @param color color del simbolo
     */
    public ShySymbol(String color){
        super(color);
        hidden = false;
        triangle = new Triangle();
    }

    @Override
    public String getType(){
        return "shy";
    }

    /**
     * Indica si el simbolo esta escondido.
     *
     * @return true si esta escondido
     */
    public boolean isHidden(){
        return hidden;
    }

    /** Dibuja un triangulo, solo si el simbolo no esta escondido. */
    @Override
    public void draw(int x, int y){
        erase();
        if(hidden){
            return;
        }
        triangle = new Triangle();
        triangle.changeSize(size, size);
        triangle.changeColor(color);
        // Un Triangle nuevo tiene la punta en (140, 15)
        triangle.moveHorizontal(x + Wheel.SIZE / 2 - 140);
        triangle.moveVertical(y + (Wheel.SIZE - size) / 2 - 15);
        triangle.makeVisible();
    }

    @Override
    public void erase(){
        triangle.makeInvisible();
    }

    /** Cambia entre visible y escondido. */
    @Override
    public void onSelect(){
        hidden = !hidden;
    }
}
