public class Wheel{

    private static final int DIAMETER = 50;

    private Circle shape;
    private int position;   
    private int x;
    private int y;

    /** Crea una rueda en la posicion dada. */
    public Wheel(int x, int y){
        shape = new Circle();
        shape.changeSize(DIAMETER);
        this.x = x;
        this.y = y;
        shape.moveHorizontal(x - 20);
        shape.moveVertical(y - 15);
        position = 0;
    }

    /** Cambia la posicion y el color de la rueda. */
    public void setPosition(int newPosition, String color){
        position = newPosition;
        if(color != null){
            shape.changeColor(color);
        }
    }

    /** Devuelve la posicion actual. */
    public int getPosition(){
        return position;
    }

    /** Mueve la rueda cuando cambia la distribucion. */
    public void relayout(int newX, int newY){
        shape.moveHorizontal(newX - x);
        shape.moveVertical(newY - y);
        x = newX;
        y = newY;
    }

    /** Hace visible la rueda. */
    public void show(){
        shape.makeVisible();
    }

    /** Oculta la rueda. */
    public void hide(){
        shape.makeInvisible();
    }
}
