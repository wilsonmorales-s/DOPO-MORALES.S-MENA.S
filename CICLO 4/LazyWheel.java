/**
 * Rueda perezosa (tipo nuevo propuesto por el equipo). Solo gira una de
 * cada dos veces: la primera descansa, la segunda gira, y asi.
 * Su marco es dorado.<br>
 * <b>(resting)</b>
 *
 * @author Samuel Mena Serrato
 * @author Wilson Morales Sanchez
 */
public final class LazyWheel extends Wheel{

    private boolean resting;

    /** Crea una rueda perezosa, que descansa en su primer giro. */
    public LazyWheel(){
        super();
        resting = true;
    }

    @Override
    public String getType(){
        return "lazy";
    }

    @Override
    public String getFrameColor(){
        return "goldenrod";
    }

    /**
     * Una vez descansa y la siguiente gira normal.
     *
     * @param steps cuantos simbolos avanza cuando le toca girar
     */
    @Override
    public void spin(int steps){
        if(resting){
            resting = false;
        }else{
            resting = true;
            super.spin(steps);
        }
    }
}
