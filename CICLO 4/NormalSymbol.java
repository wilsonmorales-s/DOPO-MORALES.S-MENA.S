/**
 * Simbolo normal. Se dibuja como un circulo y no cambia nunca.
 *
 * @author Samuel Mena Serrato
 * @author Wilson Morales Sanchez
 */
public final class NormalSymbol extends Symbol{

    private Circle circle;

    /**
     * Crea un simbolo normal.
     *
     * @param color color del simbolo
     */
    public NormalSymbol(String color){
        super(color);
        circle = new Circle();
    }

    @Override
    public String getType(){
        return "normal";
    }

    @Override
    public void draw(int x, int y){
        erase();
        circle = new Circle();
        circle.changeSize(size);
        circle.changeColor(color);
        // Un Circle nuevo empieza en (20, 15)
        circle.moveHorizontal(x + (Wheel.SIZE - size) / 2 - 20);
        circle.moveVertical(y + (Wheel.SIZE - size) / 2 - 15);
        circle.makeVisible();
    }

    @Override
    public void erase(){
        circle.makeInvisible();
    }
}
