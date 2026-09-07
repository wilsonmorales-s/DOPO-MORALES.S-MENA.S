import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

/**
 * Pruebas de los metodos principales de SlotMachine para el ciclo 2.
 *
 * @author Samuel Mena Serrato
 */
public class SlotMachineC2Test{

    private SlotMachine machine;

    @BeforeEach
    public void setUp(){
        machine = new SlotMachine();
    }

    // ------------------------------------------------------------
    // Pruebas de agregar y quitar ruedas
    // ------------------------------------------------------------

    @Test
    public void addWheelShouldCreateAnEmptyWheelWhenPaletteIsEmpty(){
        machine.addWheel(1);
        assertTrue(machine.ok());
        assertArrayEquals(new String[]{null}, machine.configuration());
    }

    @Test
    public void addWheelShouldDefaultToFirstPaletteColorWhenPaletteIsNotEmpty(){
        machine.addSymbol(1, "red");
        machine.addWheel(1);
        assertArrayEquals(new String[]{"red"}, machine.configuration());
    }

    @Test
    public void delWheelShouldFailWhenMachineHasNoWheels(){
        machine.delWheel(1);
        assertFalse(machine.ok());
    }

    @Test
    public void delWheelShouldRemoveTheWheelAtThatPosition(){
        machine.addSymbol(1, "red");
        machine.addWheel(1);
        machine.addWheel(2);
        machine.delWheel(1);
        assertTrue(machine.ok());
        assertEquals(1, machine.configuration().length);
    }

    // ------------------------------------------------------------
    // Pruebas de agregar y quitar simbolos
    // ------------------------------------------------------------

    @Test
    public void addSymbolShouldFailWithAnInvalidCssColorName(){
        machine.addSymbol(1, "not-a-css-color");
        assertFalse(machine.ok());
        assertEquals(0, machine.symbols().length);
    }

    @Test
    public void addSymbolShouldNotChangeAWheelThatAlreadyHasASymbolWhenInsertingBeforeIt(){
        // Al meter un color antes de otro, la rueda debe seguir mostrando
        // el color que ya tenia.
        machine.addSymbol(1, "blue");
        machine.addWheel(1); // empieza con "blue"
        machine.addSymbol(1, "red"); // se mete antes de "blue"
        assertArrayEquals(new String[]{"blue"}, machine.configuration());
        assertArrayEquals(new String[]{"red", "blue"}, machine.symbols());
    }

    @Test
    public void delSymbolShouldFailWhenTheSymbolDoesNotExist(){
        machine.delSymbol("red");
        assertFalse(machine.ok());
    }

    @Test
    public void delSymbolShouldReassignWheelsThatHadThatSymbol(){
        machine.addSymbol(1, "red");
        machine.addSymbol(2, "blue");
        machine.addWheel(1); // red
        machine.placeSymbol(1, "blue");
        machine.delSymbol("blue");
        assertTrue(machine.ok());
        assertArrayEquals(new String[]{"red"}, machine.configuration());
    }

    // ------------------------------------------------------------
    // Pruebas de colocar un simbolo
    // ------------------------------------------------------------

    @Test
    public void placeSymbolShouldFailWhenSymbolIsNotInThePalette(){
        machine.addWheel(1);
        machine.placeSymbol(1, "purple");
        assertFalse(machine.ok());
    }

    @Test
    public void placeSymbolShouldSetTheExactSymbolRequested(){
        machine.addSymbol(1, "red");
        machine.addSymbol(2, "blue");
        machine.addWheel(1);
        machine.placeSymbol(1, "blue");
        assertArrayEquals(new String[]{"blue"}, machine.configuration());
    }

    // ------------------------------------------------------------
    // Pruebas de los giros aleatorios
    // ------------------------------------------------------------

    @Test
    public void spinSingleWheelShouldFailWhenThereAreNoSymbols(){
        machine.addWheel(1);
        machine.spin(1);
        assertFalse(machine.ok());
    }

    @Test
    public void spinSingleWheelShouldLeaveTheWheelWithASymbolFromThePalette(){
        machine.addSymbol(1, "red");
        machine.addSymbol(2, "blue");
        machine.addWheel(1);
        machine.spin(1);
        assertTrue(machine.ok());
        String result = machine.configuration()[0];
        assertTrue(result.equals("red") || result.equals("blue"));
    }

    @Test
    public void spinSingleWheelShouldNotChangeALockedWheel(){
        machine.addSymbol(1, "red");
        machine.addSymbol(2, "blue");
        machine.addWheel(1);
        machine.lock(1);
        machine.spin(1);
        assertFalse(machine.ok());
        assertArrayEquals(new String[]{"red"}, machine.configuration());
    }

    @Test
    public void spinAllShouldSkipLockedWheelsButSpinTheRest(){
        machine.addSymbol(1, "red");
        machine.addWheel(1);
        machine.addWheel(2);
        machine.lock(1);
        machine.spin();
        assertTrue(machine.ok());
        // La rueda 1 esta bloqueada, asi que debe quedarse en "red".
        assertEquals("red", machine.configuration()[0]);
    }

    // ------------------------------------------------------------
    // Pruebas del giro por cantidad de pasos
    // ------------------------------------------------------------

    @Test
    public void spinWithStepsShouldMoveExactlyThatManyPositionsForward(){
        machine.addSymbol(1, "red");
        machine.addSymbol(2, "blue");
        machine.addSymbol(3, "green");
        machine.addWheel(1); // empieza en "red"
        machine.spin(1, 2);
        assertTrue(machine.ok());
        assertEquals("green", machine.configuration()[0]);
    }

    @Test
    public void spinWithStepsShouldWrapAroundThePalette(){
        machine.addSymbol(1, "red");
        machine.addSymbol(2, "blue");
        machine.addWheel(1); // empieza en "red"
        machine.spin(1, 2); // red -> blue -> red
        assertEquals("red", machine.configuration()[0]);
    }

    @Test
    public void spinWithZeroStepsShouldNotChangeTheWheel(){
        machine.addSymbol(1, "red");
        machine.addSymbol(2, "blue");
        machine.addWheel(1);
        machine.spin(1, 0);
        assertTrue(machine.ok());
        assertEquals("red", machine.configuration()[0]);
    }

    @Test
    public void spinWithNegativeStepsShouldFailAndNotChangeTheWheel(){
        machine.addSymbol(1, "red");
        machine.addWheel(1);
        machine.spin(1, -3);
        assertFalse(machine.ok());
        assertEquals("red", machine.configuration()[0]);
    }

    @Test
    public void spinWithStepsShouldFailOnALockedWheel(){
        machine.addSymbol(1, "red");
        machine.addSymbol(2, "blue");
        machine.addWheel(1);
        machine.lock(1);
        machine.spin(1, 1);
        assertFalse(machine.ok());
        assertEquals("red", machine.configuration()[0]);
    }

    // ------------------------------------------------------------
    // Pruebas de configurar todas las ruedas
    // ------------------------------------------------------------

    @Test
    public void spinWithConfigurationShouldSetEverySymbolExactly(){
        machine.addSymbol(1, "red");
        machine.addSymbol(2, "blue");
        machine.addWheel(1);
        machine.addWheel(2);
        machine.spin(new String[]{"blue", "red"});
        assertTrue(machine.ok());
        assertArrayEquals(new String[]{"blue", "red"}, machine.configuration());
    }

    @Test
    public void spinWithConfigurationShouldFailWhenLengthDoesNotMatchWheelCount(){
        machine.addSymbol(1, "red");
        machine.addWheel(1);
        machine.spin(new String[]{"red", "red"});
        assertFalse(machine.ok());
    }

    @Test
    public void spinWithConfigurationShouldFailAndChangeNothingWhenASymbolDoesNotExist(){
        machine.addSymbol(1, "red");
        machine.addSymbol(2, "blue");
        machine.addWheel(1);
        machine.addWheel(2);
        machine.spin(new String[]{"red", "purple"});
        assertFalse(machine.ok());
        assertArrayEquals(new String[]{"red", "blue"}, machine.configuration());
    }

    @Test
    public void spinWithConfigurationShouldSkipLockedWheels(){
        machine.addSymbol(1, "red");
        machine.addSymbol(2, "blue");
        machine.addWheel(1);
        machine.addWheel(2);
        machine.lock(1);
        machine.spin(new String[]{"blue", "blue"});
        assertTrue(machine.ok());
        assertArrayEquals(new String[]{"red", "blue"}, machine.configuration());
    }

    // ------------------------------------------------------------
    // Pruebas de intercambio
    // ------------------------------------------------------------

    @Test
    public void swapShouldExchangeTheSymbolsOfTwoWheels(){
        machine.addSymbol(1, "red");
        machine.addSymbol(2, "blue");
        machine.addWheel(1);
        machine.addWheel(2);
        machine.placeSymbol(2, "blue");
        machine.swap(1, 2);
        assertTrue(machine.ok());
        assertArrayEquals(new String[]{"blue", "red"}, machine.configuration());
    }

    @Test
    public void swapShouldFailWhenEitherWheelIsLocked(){
        machine.addSymbol(1, "red");
        machine.addSymbol(2, "blue");
        machine.addWheel(1);
        machine.addWheel(2);
        machine.placeSymbol(2, "blue");
        machine.lock(2);
        machine.swap(1, 2);
        assertFalse(machine.ok());
        assertArrayEquals(new String[]{"red", "blue"}, machine.configuration());
    }

    // ------------------------------------------------------------
    // Pruebas de bloquear y desbloquear
    // ------------------------------------------------------------

    @Test
    public void unlockShouldAllowAPreviouslyLockedWheelToSpinAgain(){
        machine.addSymbol(1, "red");
        machine.addSymbol(2, "blue");
        machine.addWheel(1);
        machine.lock(1);
        machine.unlock(1);
        machine.spin(1, 1);
        assertTrue(machine.ok());
        assertEquals("blue", machine.configuration()[0]);
    }

    // ------------------------------------------------------------
    // Pruebas de las consultas de la maquina
    // ------------------------------------------------------------

    @Test
    public void distinctSymbolsShouldNotCountDuplicatesTwice(){
        machine.addSymbol(1, "red");
        machine.addSymbol(2, "red");
        machine.addSymbol(3, "blue");
        assertEquals(2, machine.distinctSymbols());
    }

    @Test
    public void isJackpotShouldBeFalseWhenWheelsHaveDifferentSymbols(){
        machine.addSymbol(1, "red");
        machine.addSymbol(2, "blue");
        machine.addWheel(1);
        machine.addWheel(2);
        machine.placeSymbol(2, "blue");
        assertFalse(machine.isJackpot());
    }

    @Test
    public void isJackpotShouldBeTrueWhenAllWheelsShareTheSameSymbol(){
        machine.addSymbol(1, "red");
        machine.addWheel(1);
        machine.addWheel(2);
        assertTrue(machine.isJackpot());
    }

    @Test
    public void isJackpotShouldBeFalseWhenThereAreNoWheels(){
        assertFalse(machine.isJackpot());
    }
}