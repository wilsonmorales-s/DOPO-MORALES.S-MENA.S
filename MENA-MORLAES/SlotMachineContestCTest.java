import org.junit.Test;
import static org.junit.Assert.*;

/**
 * Pruebas compartidas del ciclo 3 para SlotMachineContest.
 */
public class SlotMachineContestCTest{

    /**
     * Comprueba que solve resuelva una maquina de 3 ruedas.
     */
    @Test
    public void shouldSolveThreeWheels(){
        SlotMachine machine = new SlotMachine(3);

        SlotMachineContest.solve(machine,3);

        assertEquals(1,machine.distinctSymbols());
    }

    /**
     * Comprueba que solve respete el limite de acciones
     * para una maquina de 20 ruedas.
     */
    @Test
    public void shouldRespectActionLimit(){
        int[][] actions = SlotMachineContest.solve(20);

        assertTrue(actions.length <= 10000);
    }
}