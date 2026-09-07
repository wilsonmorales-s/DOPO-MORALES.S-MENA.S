import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

/**

 * Mes = Mena Serrato, Mos = Morales Sanchez.
 *
 * @author Samuel Mena Serrato (Mes)
 * @author Wilson Morales Sanchez (Mos)
 */
public class SlotMachineCC2Test{

    private SlotMachine machine;

    @BeforeEach
    public void setUp(){
        machine = new SlotMachine();
    }

    /**
     * Caso 1, aportado por Mes: al agregar una rueda con la paleta
     * vacia, la rueda debe quedar vacia (sin simbolo), no fallar.
     */
    @Test
    public void accordingMesShouldLeaveANewWheelEmptyWhenThePaletteIsEmpty(){
        machine.addWheel(1);
        assertTrue(machine.ok());
        assertNull(machine.configuration()[0]);
    }

    /**
     * Caso 2: si damos una vuelta completa, la rueda vuelve al color inicial.
     */
    @Test
    public void accordingMosShouldReturnToTheSameSymbolAfterAFullRotation(){
        machine.addSymbol(1, "red");
        machine.addSymbol(2, "blue");
        machine.addSymbol(3, "green");
        machine.addWheel(1); // empieza en "red"
        machine.spin(1, 3); // una vuelta completa
        assertTrue(machine.ok());
        assertEquals("red", machine.configuration()[0]);
    }

    /**
     * Caso 3: una maquina sin ruedas no puede tener jackpot.
     */
    @Test
    public void accordingMesMosShouldNeverReportJackpotWithoutWheels(){
        machine.addSymbol(1, "red");
        assertFalse(machine.isJackpot());
    }
}