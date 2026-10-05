/**
 * Rueda lefty. Si tiene una rueda a su izquierda, al girar queda en el
 * mismo simbolo que ella. Si no tiene, gira como una rueda normal.
 * Su marco es azul.
 *
 * @author Samuel Mena Serrato
 * @author Wilson Morales Sanchez
 */
public final class LeftyWheel extends Wheel{

    @Override
    public String getType(){
        return "lefty";
    }

    @Override
    public String getFrameColor(){
        return "dodgerblue";
    }

    /**
     * Copia la posicion de la rueda de la izquierda. Si no tiene vecina,
     * avanza como una rueda normal.
     *
     * @param steps cuantos simbolos avanza si no tiene vecina
     */
    @Override
    protected void move(int steps){
        if(leftNeighbor != null && leftNeighbor.getPosition() != 0){
            position = leftNeighbor.getPosition();
        }else{
            super.move(steps);
        }
    }
}
