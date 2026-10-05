import org.junit.Test;
import static org.junit.Assert.*;

/**
 * Pruebas de unidad del ciclo 3 para SlotMachineContest.
 */
public class SlotMachineContestTest{

    /**
     * Comprueba que una maquina creada con solve tenga
     * la cantidad correcta de ruedas.
     */
    @Test
    public void shouldCreateMachineWithCorrectSize(){
        SlotMachine m = new SlotMachine(5);

        assertEquals(5,m.configuration().length);
    }

    /**
     * Comprueba que las acciones de solve siempre indiquen
     * una rueda existente.
     */
    @Test
    public void shouldUseExistingWheels(){
        int n = 8;
        int[][] actions = SlotMachineContest.solve(n);

        for(int[] action : actions){
            assertTrue(action[0] >= 1);
            assertTrue(action[0] <= n);
        }
    }

    /**
     * Comprueba que solve no genere pasos mayores
     * que los necesarios para una vuelta completa.
     */
    @Test
    public void shouldUseValidNumberOfSteps(){
        int n = 6;
        int[][] actions = SlotMachineContest.solve(n);

        for(int[] action : actions){
            assertTrue(action[1] >= 0);
            assertTrue(action[1] < n);
        }
    }

    /**
     * Comprueba que cada accion tenga rueda y pasos.
     */
    @Test
    public void shouldCreateCompleteActions(){
        int[][] actions = SlotMachineContest.solve(5);

        for(int[] action : actions){
            assertNotNull(action);
            assertEquals(2,action.length);
        }
    }

    /**
     * Comprueba que solve pueda trabajar directamente
     * con una maquina ya creada.
     */
    @Test
    public void shouldSolveExistingMachine(){
        SlotMachine m = new SlotMachine(6);

        int[][] actions = SlotMachineContest.solve(m,6);

        assertNotNull(actions);
        assertEquals(1,m.distinctSymbols());
    }

    /**
     * Comprueba que solve no produzca acciones para
     * un tamaño que no pertenece al problema.
     */
    @Test
    public void shouldReturnEmptyForInvalidSize(){
        assertEquals(0,SlotMachineContest.solve(1).length);
        assertEquals(0,SlotMachineContest.solve(60).length);
    }

    /**
     * Comprueba que el numero de acciones permanezca
     * dentro del limite establecido por la maraton.
     */
    @Test
    public void shouldStayInsideActionLimit(){
        int[][] actions = SlotMachineContest.solve(20);

        assertTrue(actions.length <= 10000);
    }
}