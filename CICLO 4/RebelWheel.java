/**
 * Rueda rebelde. No se deja bloquear, ni intercambiar, ni eliminar.
 * Gira como una rueda normal. Su marco es rojo.
 *
 * @author Samuel Mena Serrato
 * @author Wilson Morales Sanchez
 */
public final class RebelWheel extends Wheel{

    @Override
    public String getType(){
        return "rebel";
    }

    @Override
    public String getFrameColor(){
        return "crimson";
    }

    /**
     * La rueda rebelde no se deja bloquear.
     *
     * @throws SlotMachineException siempre
     */
    @Override
    public void lock() throws SlotMachineException{
        throw new SlotMachineException(SlotMachineException.REBEL_NOT_LOCKABLE);
    }

    /**
     * La rueda rebelde no se deja intercambiar.
     *
     * @throws SlotMachineException siempre
     */
    @Override
    public void checkSwappable() throws SlotMachineException{
        throw new SlotMachineException(SlotMachineException.REBEL_NOT_SWAPPABLE);
    }

    /**
     * La rueda rebelde no se deja eliminar.
     *
     * @throws SlotMachineException siempre
     */
    @Override
    public void checkDeletable() throws SlotMachineException{
        throw new SlotMachineException(SlotMachineException.REBEL_NOT_DELETABLE);
    }
}
