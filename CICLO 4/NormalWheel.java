/**
 * Rueda normal. Gira, se bloquea, se intercambia y se elimina sin
 * restricciones. Su marco es gris.
 *
 * @author Samuel Mena Serrato
 * @author Wilson Morales Sanchez
 */
public final class NormalWheel extends Wheel{

    @Override
    public String getType(){
        return "normal";
    }

    @Override
    public String getFrameColor(){
        return "dimgray";
    }
}
