import static org.junit.jupiter.api.Assertions.*;

import java.util.Arrays;
import java.util.HashSet;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

/**
 * Pruebas de unidad del ciclo 4: tipos de ruedas y de simbolos.
 * Cada tipo se prueba con lo que deberia hacer y lo que no deberia hacer.
 * Todas las pruebas se hacen con la maquina invisible.
 *
 * @author Samuel Mena Serrato
 * @author Wilson Morales Sanchez
 */
public class SlotMachineC4Test{

    private SlotMachine machine;

    /** Maquina con tres simbolos: red, blue y green. */
    @BeforeEach
    public void setUp(){
        machine = new SlotMachine();
        machine.addSymbol(1, "red");
        machine.addSymbol(2, "blue");
        machine.addSymbol(3, "green");
    }

    // ---------- Crear ruedas ----------

    @Test
    public void shouldAddWheelsOfEveryType(){
        String[] types = {"normal", "lefty", "rebel", "lazy"};
        for(int i = 0; i < types.length; i++){
            machine.addWheel(types[i], i + 1);
            assertTrue(machine.ok());
        }
        assertEquals(4, machine.configuration().length);
    }

    @Test
    public void shouldStartNewWheelsInTheFirstSymbol(){
        machine.addWheel("rebel", 1);
        machine.addWheel("lazy", 2);
        assertArrayEquals(new String[]{"red", "red"}, machine.configuration());
    }

    @Test
    public void shouldNotAddAWheelOfAnUnknownType(){
        machine.addWheel("flying", 1);
        assertFalse(machine.ok());
        assertEquals(0, machine.configuration().length);
    }

    // ---------- Crear simbolos ----------

    @Test
    public void shouldAddSymbolsOfEveryType(){
        machine.addSymbol("ephemeral", 4, "orange");
        assertTrue(machine.ok());
        machine.addSymbol("shy", 5, "purple");
        assertTrue(machine.ok());
        assertArrayEquals(new String[]{"red", "blue", "green", "orange", "purple"},
            machine.symbols());
    }

    @Test
    public void shouldGiveNewSymbolsToWheelsThatAlreadyExist(){
        machine.addWheel(1);
        machine.addSymbol("shy", 4, "purple");
        machine.placeSymbol(1, "purple");
        assertTrue(machine.ok());
        assertEquals("purple", machine.configuration()[0]);
    }

    @Test
    public void shouldNotAddASymbolOfAnUnknownType(){
        machine.addSymbol("ghost", 1, "orange");
        assertFalse(machine.ok());
        assertEquals(3, machine.symbols().length);
    }

    @Test
    public void shouldNotAddASymbolWithARepeatedColor(){
        machine.addSymbol("shy", 1, "red");
        assertFalse(machine.ok());
        assertEquals(3, machine.symbols().length);
    }

    // ---------- Rueda lefty ----------

    @Test
    public void shouldCopyTheLeftWheelWhenLeftySpins(){
        machine.addWheel("normal", 1);
        machine.addWheel("lefty", 2);
        machine.placeSymbol(1, "green");
        machine.spin(2);
        assertEquals("green", machine.configuration()[1]);
    }

    @Test
    public void shouldCopyTheLeftWheelWhenLeftySpinsWithSteps(){
        machine.addWheel("normal", 1);
        machine.addWheel("lefty", 2);
        machine.placeSymbol(1, "blue");
        machine.spin(2, 2);
        assertEquals("blue", machine.configuration()[1]);
    }

    @Test
    public void shouldMatchTheLeftWheelWhenAllWheelsSpin(){
        machine.addWheel("normal", 1);
        machine.addWheel("lefty", 2);
        for(int i = 0; i < 10; i++){
            machine.spin();
            assertEquals(machine.configuration()[0], machine.configuration()[1]);
        }
    }

    @Test
    public void shouldUseTheNewLeftWheelAfterADeletion(){
        machine.addWheel("normal", 1);
        machine.addWheel("normal", 2);
        machine.addWheel("lefty", 3);
        machine.placeSymbol(1, "green");
        machine.placeSymbol(2, "blue");
        machine.delWheel(2);
        machine.spin(2);
        assertEquals("green", machine.configuration()[1]);
    }

    @Test
    public void shouldSpinNormallyWhenLeftyIsTheFirstWheel(){
        machine.addWheel("lefty", 1);
        machine.spin(1, 1);
        assertEquals("blue", machine.configuration()[0]);
    }

    @Test
    public void shouldNotCopyTheWheelOnTheRight(){
        machine.addWheel("lefty", 1);
        machine.addWheel("normal", 2);
        machine.placeSymbol(2, "green");
        machine.spin(1, 1);
        assertEquals("blue", machine.configuration()[0]);
    }

    @Test
    public void shouldNotMoveALockedLefty(){
        machine.addWheel("normal", 1);
        machine.addWheel("lefty", 2);
        machine.placeSymbol(1, "green");
        machine.lock(2);
        machine.spin(2);
        assertFalse(machine.ok());
        assertEquals("red", machine.configuration()[1]);
    }

    // ---------- Rueda rebel ----------

    @Test
    public void shouldSpinARebelWheelNormally(){
        machine.addWheel("rebel", 1);
        machine.spin(1, 1);
        assertTrue(machine.ok());
        assertEquals("blue", machine.configuration()[0]);
    }

    @Test
    public void shouldNotLockARebelWheel(){
        machine.addWheel("rebel", 1);
        machine.lock(1);
        assertFalse(machine.ok());
        machine.spin(1, 1);
        assertEquals("blue", machine.configuration()[0]);
    }

    @Test
    public void shouldNotSwapARebelWheel(){
        machine.addWheel("rebel", 1);
        machine.addWheel("normal", 2);
        machine.placeSymbol(2, "green");
        machine.swap(1, 2);
        assertFalse(machine.ok());
        assertArrayEquals(new String[]{"red", "green"}, machine.configuration());
    }

    @Test
    public void shouldNotDeleteARebelWheel(){
        machine.addWheel("rebel", 1);
        machine.delWheel(1);
        assertFalse(machine.ok());
        assertEquals(1, machine.configuration().length);
    }

    @Test
    public void shouldStillLockSwapAndDeleteNormalWheels(){
        machine.addWheel("normal", 1);
        machine.addWheel("normal", 2);
        machine.placeSymbol(2, "blue");
        machine.swap(1, 2);
        assertTrue(machine.ok());
        machine.lock(1);
        assertTrue(machine.ok());
        machine.delWheel(2);
        assertTrue(machine.ok());
        assertArrayEquals(new String[]{"blue"}, machine.configuration());
    }

    // ---------- Rueda lazy (propuesta) ----------

    @Test
    public void shouldRestTheFirstTimeAndSpinTheSecondTime(){
        machine.addWheel("lazy", 1);
        machine.spin(1, 1);
        assertEquals("red", machine.configuration()[0]);
        machine.spin(1, 1);
        assertEquals("blue", machine.configuration()[0]);
    }

    @Test
    public void shouldMoveOnlyHalfOfTheSteps(){
        machine.addWheel("lazy", 1);
        machine.spin(1, 4);
        assertEquals("green", machine.configuration()[0]);
    }

    @Test
    public void shouldNotMoveALazyWheelInItsFirstSpin(){
        machine.addWheel("lazy", 1);
        machine.placeSymbol(1, "green");
        machine.spin(1);
        assertEquals("green", machine.configuration()[0]);
    }

    // ---------- Simbolo ephemeral ----------

    @Test
    public void shouldShrinkAnEphemeralSymbolOnEverySpin(){
        Symbol symbol = new EphemeralSymbol("red");
        symbol.onSpin();
        assertEquals(Symbol.FULL_SIZE - EphemeralSymbol.SHRINK, symbol.getSize());
        symbol.onSpin();
        assertEquals(Symbol.FULL_SIZE - 2 * EphemeralSymbol.SHRINK, symbol.getSize());
    }

    @Test
    public void shouldNotShrinkAnEphemeralSymbolBelowAPoint(){
        Symbol symbol = new EphemeralSymbol("red");
        for(int i = 0; i < 20; i++){
            symbol.onSpin();
        }
        assertEquals(EphemeralSymbol.MIN_SIZE, symbol.getSize());
    }

    @Test
    public void shouldKeepAnEphemeralSymbolInTheMachineAfterManySpins(){
        machine.addSymbol("ephemeral", 4, "orange");
        machine.addWheel(1);
        for(int i = 0; i < 10; i++){
            machine.spin();
        }
        machine.placeSymbol(1, "orange");
        assertTrue(machine.ok());
        assertEquals("orange", machine.configuration()[0]);
    }

    // ---------- Simbolo shy ----------

    @Test
    public void shouldHideAndShowAShySymbolEachTimeItIsSelected(){
        ShySymbol symbol = new ShySymbol("red");
        assertFalse(symbol.isHidden());
        symbol.onSelect();
        assertTrue(symbol.isHidden());
        symbol.onSelect();
        assertFalse(symbol.isHidden());
    }

    @Test
    public void shouldNotHideAShySymbolWhenItOnlySpins(){
        ShySymbol symbol = new ShySymbol("red");
        symbol.onSpin();
        assertFalse(symbol.isHidden());
    }

    @Test
    public void shouldCountAHiddenShySymbolForTheJackpot(){
        machine.addSymbol("shy", 4, "purple");
        machine.addWheel(1);
        machine.addWheel(2);
        machine.placeSymbol(1, "purple");
        machine.placeSymbol(2, "purple");
        assertTrue(machine.isJackpot());
        assertEquals(1, machine.distinctSymbols());
    }

    // ---------- Simbolo normal ----------

    @Test
    public void shouldNotChangeANormalSymbol(){
        NormalSymbol symbol = new NormalSymbol("red");
        symbol.onSpin();
        symbol.onSelect();
        assertEquals(Symbol.FULL_SIZE, symbol.getSize());
        assertEquals("red", symbol.getColor());
    }

    // ---------- Excepciones de las ruedas ----------

    @Test
    public void shouldThrowAnExceptionWhenARebelWheelIsLocked(){
        try{
            new RebelWheel().lock();
            fail("La rueda rebelde no se debio bloquear");
        }catch(SlotMachineException e){
            assertEquals(SlotMachineException.REBEL_NOT_LOCKABLE, e.getMessage());
        }
    }

    @Test
    public void shouldThrowAnExceptionWhenARebelWheelIsSwapped(){
        try{
            new RebelWheel().checkSwappable();
            fail("La rueda rebelde no se debio dejar intercambiar");
        }catch(SlotMachineException e){
            assertEquals(SlotMachineException.REBEL_NOT_SWAPPABLE, e.getMessage());
        }
    }

    @Test
    public void shouldThrowAnExceptionWhenARebelWheelIsDeleted(){
        try{
            new RebelWheel().checkDeletable();
            fail("La rueda rebelde no se debio dejar eliminar");
        }catch(SlotMachineException e){
            assertEquals(SlotMachineException.REBEL_NOT_DELETABLE, e.getMessage());
        }
    }

    @Test
    public void shouldThrowAnExceptionWhenALockedWheelIsSwapped() throws SlotMachineException{
        Wheel wheel = new NormalWheel();
        wheel.lock();
        try{
            wheel.checkSwappable();
            fail("Una rueda bloqueada no se debio dejar intercambiar");
        }catch(SlotMachineException e){
            assertEquals(SlotMachineException.LOCKED_WHEEL, e.getMessage());
        }
    }

    @Test
    public void shouldNotThrowAnExceptionWhenANormalWheelIsLocked() throws SlotMachineException{
        Wheel wheel = new NormalWheel();
        wheel.lock();
        assertTrue(wheel.isLocked());
    }

    // ---------- Usabilidad: los tipos se distinguen ----------

    @Test
    public void shouldGiveEachWheelTypeADifferentFrameColor(){
        HashSet<String> colors = new HashSet<String>(Arrays.asList(
            new NormalWheel().getFrameColor(), new LeftyWheel().getFrameColor(),
            new RebelWheel().getFrameColor(), new LazyWheel().getFrameColor()));
        assertEquals(4, colors.size());
    }

    @Test
    public void shouldKnowTheTypeOfEachWheelAndSymbol(){
        assertEquals("normal", new NormalWheel().getType());
        assertEquals("lefty", new LeftyWheel().getType());
        assertEquals("rebel", new RebelWheel().getType());
        assertEquals("lazy", new LazyWheel().getType());
        assertEquals("normal", new NormalSymbol("red").getType());
        assertEquals("ephemeral", new EphemeralSymbol("red").getType());
        assertEquals("shy", new ShySymbol("red").getType());
    }

    // ---------- Lo de ciclos anteriores sigue funcionando ----------

    @Test
    public void shouldCreateAMachineOfSizeNWithDifferentColors(){
        SlotMachine big = new SlotMachine(50);
        assertEquals(50, big.configuration().length);
        assertEquals(50, new HashSet<String>(Arrays.asList(big.symbols())).size());
    }

    @Test
    public void shouldStillSolveTheMachine(){
        SlotMachine m = new SlotMachine(6);
        SlotMachineContest.solve(m, 6);
        assertTrue(m.isJackpot());
    }
}
