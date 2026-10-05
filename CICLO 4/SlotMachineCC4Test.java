import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

/**
 *
 * @author Samuel Mena Serrato
 * @author Wilson Morales SancheZ
 */
public class SlotMachineCC4Test{

    private SlotMachine machine;

    @BeforeEach
    public void setUp(){
        machine = new SlotMachine();
        machine.addSymbol(1, "red");
        machine.addSymbol(2, "blue");
        machine.addSymbol(3, "green");
    }

    /**
     * Caso 1, aportado por Mes: una rueda rebelde no se deja eliminar,
     * ni bloquear, ni intercambiar.
     */
    @Test
    public void accordingMesShouldNotLetARebelWheelBeDeletedLockedOrSwapped(){
        machine.addWheel("rebel", 1);
        machine.addWheel("normal", 2);
        machine.delWheel(1);
        assertFalse(machine.ok());
        machine.lock(1);
        assertFalse(machine.ok());
        machine.swap(1, 2);
        assertFalse(machine.ok());
        assertEquals(2, machine.configuration().length);
    }

    /**
     * Caso 2, aportado por Mos: una rueda lefty que gira copia el simbolo
     * de la rueda que tiene a su izquierda.
     */
    @Test
    public void accordingMosShouldCopyTheLeftWheelWhenALeftyWheelSpins(){
        machine.addWheel("normal", 1);
        machine.addWheel("lefty", 2);
        machine.placeSymbol(1, "green");
        machine.spin(2);
        assertEquals("green", machine.configuration()[1]);
    }

    /**
     * Caso 3: un simbolo efimero se encoge hasta quedar como un punto,
     * pero sigue siendo un simbolo valido de la maquina.
     */
    @Test
    public void accordingMesMosShouldKeepAnEphemeralSymbolAfterItBecomesAPoint(){
        machine.addSymbol("ephemeral", 4, "orange");
        machine.addWheel(1);
        for(int i = 0; i < 10; i++){
            machine.spin();
        }
        machine.placeSymbol(1, "orange");
        assertEquals("orange", machine.configuration()[0]);
    }
}
