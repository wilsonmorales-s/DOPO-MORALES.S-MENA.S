import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Random;
import java.util.Set;
import javax.swing.JOptionPane;

public class SlotMachine{

    private static final int WHEEL_SPACING = 70;
    private static final int LAYOUT_X = 60;
    private static final int LAYOUT_Y = 100;

    private List<Wheel> wheels;
    private List<String> palette;
    private Rectangle background;
    private Random random;
    private boolean visible;
    private boolean lastOk;

    /** Crea la maquina sin mostrarla. */
    public SlotMachine(){
        wheels = new ArrayList<Wheel>();
        palette = new ArrayList<String>();
        background = new Rectangle();
        background.changeColor("white");
        background.changeSize(120, WHEEL_SPACING);
        background.moveHorizontal(LAYOUT_X - 10 - 70);
        background.moveVertical(LAYOUT_Y - 10 - 15);
        random = new Random();
        visible = false;
        lastOk = true;
    }

    /** Agrega una rueda en la posicion indicada. */
    public void addWheel(int pos){
        // Si la posicion se pasa, la dejamos en un lugar valido.
        int insertAt = clamp(pos, 1, wheels.size() + 1);
        Wheel wheel = new Wheel(0, 0);
        if(!palette.isEmpty()){
            wheel.setPosition(1, palette.get(0));
        }
        wheels.add(insertAt - 1, wheel);
        layoutWheels();
        if(visible){
            wheel.show();
        }
        succeed();
    }

    /** Elimina una rueda de la maquina. */
    public void delWheel(int pos){
        if(wheels.isEmpty()){
            fail("No hay ruedas para eliminar.");
            return;
        }
        int removeAt = clamp(pos, 1, wheels.size());
        Wheel removed = wheels.remove(removeAt - 1);
        removed.hide();
        layoutWheels();
        succeed();
    }

    /** Agrega un color a la lista de simbolos. */
    public void addSymbol(int pos, String color){
        if(!CssColors.isValid(color)){
            fail("Color no reconocido en el estándar CSS: " + color);
            return;
        }
        int insertAt = clamp(pos, 1, palette.size() + 1);
        palette.add(insertAt - 1, color);
        assignDefaultSymbolToEmptyWheels();
        refreshJackpotLook();
        succeed();
    }

    /** Quita un simbolo de la maquina. */
    public void delSymbol(String symbol){
        int idx = palette.indexOf(symbol);
        if(idx == -1){
            fail("El símbolo no existe en la máquina: " + symbol);
            return;
        }
        palette.remove(idx);
        fixWheelPositionsAfterRemoval(idx);
        refreshJackpotLook();
        succeed();
    }

    /** Pone un simbolo en una rueda. */
    public void placeSymbol(int wheel, String symbol){
        if(wheels.isEmpty()){
            fail("No hay ruedas en la máquina.");
            return;
        }
        int symbolIndex = palette.indexOf(symbol);
        if(symbolIndex == -1){
            fail("El símbolo no existe en la máquina: " + symbol);
            return;
        }
        int wheelAt = clamp(wheel, 1, wheels.size());
        wheels.get(wheelAt - 1).setPosition(symbolIndex + 1, symbol);
        refreshJackpotLook();
        succeed();
    }

    /** Gira solamente la rueda indicada. */
    public void spin(int wheel){
        if(wheels.isEmpty()){
            fail("No hay ruedas en la máquina.");
            return;
        }
        if(palette.isEmpty()){
            fail("No hay símbolos para girar.");
            return;
        }
        int wheelAt = clamp(wheel, 1, wheels.size());
        spinOneWheel(wheels.get(wheelAt - 1));
        refreshJackpotLook();
        succeed();
    }

    /** Gira todas las ruedas. */
    public void spin(){
        if(wheels.isEmpty()){
            fail("No hay ruedas en la máquina.");
            return;
        }
        if(palette.isEmpty()){
            fail("No hay símbolos para girar.");
            return;
        }
        // Cada rueda queda con un simbolo aleatorio.
        for(Wheel wheel : wheels){
            spinOneWheel(wheel);
        }
        refreshJackpotLook();
        succeed();
    }

    /** Devuelve los simbolos que tiene la maquina. */
    public String[] symbols(){
        succeed();
        return palette.toArray(new String[0]);
    }

    /** Cuenta los simbolos diferentes. */
    public int distinctSymbols(){
        Set<String> distinct = new HashSet<String>(palette);
        succeed();
        return distinct.size();
    }

    /** Devuelve el simbolo que tiene cada rueda. */
    public String[] configuration(){
        String[] result = new String[wheels.size()];
        for(int i = 0; i < wheels.size(); i++){
            int pos = wheels.get(i).getPosition();
            result[i] = (pos == 0) ? null : palette.get(pos - 1);
        }
        succeed();
        return result;
    }

    /** Revisa si todas las ruedas tienen el mismo simbolo. */
    public boolean isJackpot(){
        succeed();
        return computeJackpot();
    }

    /** Muestra la maquina. */
    public void makeVisible(){
        visible = true;
        background.makeVisible();
        for(Wheel wheel : wheels){
            wheel.show();
        }
        refreshJackpotLook();
        succeed();
    }

    /** Oculta la maquina. */
    public void makeInvisible(){
        background.makeInvisible();
        for(Wheel wheel : wheels){
            wheel.hide();
        }
        visible = false;
        succeed();
    }

    /** Termina el simulador ocultando la maquina. */
    public void exit(){
        makeInvisible();
        succeed();
    }

    /** Indica si la ultima operacion salio bien. */
    public boolean ok(){
        return lastOk;
    }

    
    
    

    private int clamp(int pos, int min, int max){
        if(pos < min){
            return min;
        }
        if(pos > max){
            return max;
        }
        return pos;
    }

    private void layoutWheels(){
        for(int i = 0; i < wheels.size(); i++){
            wheels.get(i).relayout(LAYOUT_X + i * WHEEL_SPACING, LAYOUT_Y);
        }
        int width = Math.max(WHEEL_SPACING, wheels.size() * WHEEL_SPACING + 20);
        background.changeSize(120, width);
    }

    private void assignDefaultSymbolToEmptyWheels(){
        for(Wheel wheel : wheels){
            if(wheel.getPosition() == 0){
                wheel.setPosition(1, palette.get(0));
            }
        }
    }

    private void fixWheelPositionsAfterRemoval(int removedIndex){
        for(Wheel wheel : wheels){
            int pos = wheel.getPosition();
            if(pos == 0){
                continue;
            }
            if(palette.isEmpty()){
                wheel.setPosition(0, null);
            } else if(pos - 1 == removedIndex || pos > palette.size()){
                wheel.setPosition(1, palette.get(0));
            } else if(pos - 1 > removedIndex){
                wheel.setPosition(pos - 1, palette.get(pos - 2));
            }
        }
    }

    private void spinOneWheel(Wheel wheel){
        int newPosition = random.nextInt(palette.size()) + 1;
        wheel.setPosition(newPosition, palette.get(newPosition - 1));
    }

    private boolean computeJackpot(){
        if(wheels.isEmpty()){
            return false;
        }
        String first = null;
        for(Wheel wheel : wheels){
            int pos = wheel.getPosition();
            String color = (pos == 0) ? null : palette.get(pos - 1);
            if(color == null){
                return false;
            }
            if(first == null){
                first = color;
            } else if(!first.equals(color)){
                return false;
            }
        }
        return true;
    }

    private void refreshJackpotLook(){
        if(!visible){
            return;
        }
        background.changeColor(computeJackpot() ? "gold" : "white");
    }

    private void succeed(){
        lastOk = true;
    }

    private void fail(String message){
        lastOk = false;
        if(visible){
            JOptionPane.showMessageDialog(null, message,
                "Slot Machine", JOptionPane.WARNING_MESSAGE);
        }
    }
}
