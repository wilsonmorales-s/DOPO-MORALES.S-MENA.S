/**
 * Excepcion de la maquina tragamonedas. Se lanza cuando se pide algo
 * que la maquina no puede hacer. Los mensajes estan encapsulados como
 * constantes de la clase.
 *
 * @author Samuel Mena Serrato
 * @author Wilson Morales Sanchez
 */
public class SlotMachineException extends Exception{

    public static final String UNKNOWN_WHEEL_TYPE = "No existe ese tipo de rueda.";
    public static final String UNKNOWN_SYMBOL_TYPE = "No existe ese tipo de simbolo.";
    public static final String INVALID_COLOR = "El color no existe en el estandar CSS.";
    public static final String REPEATED_COLOR = "Ya hay un simbolo de ese color.";
    public static final String SYMBOL_NOT_FOUND = "El simbolo no existe en la maquina.";
    public static final String NO_WHEELS = "La maquina no tiene ruedas.";
    public static final String NO_SYMBOLS = "La maquina no tiene simbolos.";
    public static final String LOCKED_WHEEL = "La rueda esta bloqueada.";
    public static final String NEGATIVE_STEPS = "Los pasos no pueden ser negativos.";
    public static final String WRONG_CONFIGURATION = "Se necesita un simbolo por cada rueda.";
    public static final String REBEL_NOT_LOCKABLE = "La rueda rebelde no se deja bloquear.";
    public static final String REBEL_NOT_SWAPPABLE = "La rueda rebelde no se deja intercambiar.";
    public static final String REBEL_NOT_DELETABLE = "La rueda rebelde no se deja eliminar.";

    /**
     * Crea la excepcion con su mensaje.
     *
     * @param message explicacion de lo que paso
     */
    public SlotMachineException(String message){
        super(message);
    }
}
