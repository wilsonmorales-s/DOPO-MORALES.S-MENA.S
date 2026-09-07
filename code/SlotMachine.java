import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Random;
import java.util.Set;
import javax.swing.JOptionPane;

/**
 * Esta clase maneja la maquina tragamonedas y sus ruedas.
 * La paleta guarda los colores disponibles y cada rueda guarda la
 * posicion del color que esta usando.
 *
 * @author Samuel Mena Serrato
 */
public class SlotMachine{

    private static final int WHEEL_SPACING = 70;
    private static final int LAYOUT_X = 60;
    private static final int LAYOUT_Y = 100;
    private static final int STEP_DELAY_MS = 300;

    private List<Wheel> wheels;
    private List<String> palette;
    private Rectangle background;
    private Random random;
    private boolean visible;
    private boolean lastOk;

    /** Crea una maquina vacia (sin ruedas ni simbolos) y oculta. */
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

    /**
     * Agrega una rueda en la posicion indicada. Si ya hay colores,
     * empieza usando el primero.
     *
     * @param pos posicion 1-based donde insertar la rueda; se ajusta
     *            al rango valido si viene fuera de limites
     */
    public void addWheel(int pos){
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

    /**
     * Elimina la rueda que esta en la posicion indicada.
     *
     * @param pos posicion 1-based de la rueda a eliminar
     */
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

    /**
     * Agrega un color a la lista de simbolos disponibles.
     *
     * @param pos   posicion 1-based donde insertar el simbolo
     * @param color nombre del color en el estandar CSS
     */
    public void addSymbol(int pos, String color){
        if(!CssColors.isValid(color)){
            fail("Color no reconocido en el estándar CSS: " + color);
            return;
        }
        int insertAt = clamp(pos, 1, palette.size() + 1);
        palette.add(insertAt - 1, color);
        fixWheelPositionsAfterInsertion(insertAt);
        assignDefaultSymbolToEmptyWheels();
        refreshJackpotLook();
        succeed();
    }

    /**
     * Quita un simbolo de la paleta. Si alguna rueda lo tenia, se
     * acomoda al primer simbolo disponible.
     *
     * @param symbol color a eliminar de la paleta
     */
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

    /**
     * Pone un simbolo especifico en una rueda.
     *
     * @param wheel  posicion 1-based de la rueda
     * @param symbol simbolo (debe existir ya en la paleta)
     */
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

    /**
     * Gira una rueda usando un simbolo aleatorio. No funciona si esta bloqueada.
     *
     * @param wheel posicion 1-based de la rueda a girar
     */
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
        Wheel target = wheels.get(wheelAt - 1);
        if(target.isLocked()){
            fail("La rueda está fija (locked) y no puede girar.");
            return;
        }
        spinOneWheelRandom(target);
        refreshJackpotLook();
        succeed();
    }

    /**
     * Gira todas las ruedas que no estan bloqueadas. Las bloqueadas se quedan igual.
     */
    public void spin(){
        if(wheels.isEmpty()){
            fail("No hay ruedas en la máquina.");
            return;
        }
        if(palette.isEmpty()){
            fail("No hay símbolos para girar.");
            return;
        }
        for(Wheel wheel : wheels){
            if(!wheel.isLocked()){
                spinOneWheelRandom(wheel);
            }
        }
        refreshJackpotLook();
        succeed();
    }

    /**
     * Gira una rueda la cantidad exacta de pasos indicada. La paleta se
     * recorre de forma circular y una rueda bloqueada no se mueve.
     *
     * @param wheel posicion 1-based de la rueda a rotar
     * @param steps numero de pasos a avanzar (debe ser mayor o igual a 0)
     */
    public void spin(int wheel, int steps){
        if(wheels.isEmpty()){
            fail("No hay ruedas en la máquina.");
            return;
        }
        if(palette.isEmpty()){
            fail("No hay símbolos para girar.");
            return;
        }
        if(steps < 0){
            fail("El número de pasos no puede ser negativo.");
            return;
        }
        int wheelAt = clamp(wheel, 1, wheels.size());
        Wheel target = wheels.get(wheelAt - 1);
        if(target.isLocked()){
            fail("La rueda está fija (locked) y no puede girar.");
            return;
        }
        for(int i = 0; i < steps; i++){
            advanceOneStep(target);
            if(visible){
                refreshJackpotLook();
                pause();
            }
        }
        refreshJackpotLook();
        succeed();
    }

    /**
     * Deja las ruedas con la configuracion que se recibe. Las ruedas
     * bloqueadas no cambian y si hay un simbolo que no existe, falla.
     *
     * @param setSymbols un simbolo por cada rueda, en el mismo orden
     */
    public void spin(String[] setSymbols){
        if(setSymbols == null || setSymbols.length != wheels.size()){
            fail("La configuración debe traer un símbolo por cada rueda.");
            return;
        }
        for(String symbol : setSymbols){
            if(palette.indexOf(symbol) == -1){
                fail("El símbolo no existe en la máquina: " + symbol);
                return;
            }
        }
        for(int i = 0; i < wheels.size(); i++){
            Wheel wheel = wheels.get(i);
            if(wheel.isLocked()){
                continue;
            }
            String symbol = setSymbols[i];
            wheel.setPosition(palette.indexOf(symbol) + 1, symbol);
        }
        refreshJackpotLook();
        succeed();
    }

    /**
     * Intercambia los simbolos de dos ruedas. No se puede si alguna esta bloqueada.
     *
     * @param wheel1 posicion 1-based de la primera rueda
     * @param wheel2 posicion 1-based de la segunda rueda
     */
    public void swap(int wheel1, int wheel2){
        if(wheels.isEmpty()){
            fail("No hay ruedas en la máquina.");
            return;
        }
        int at1 = clamp(wheel1, 1, wheels.size());
        int at2 = clamp(wheel2, 1, wheels.size());
        Wheel w1 = wheels.get(at1 - 1);
        Wheel w2 = wheels.get(at2 - 1);
        if(w1.isLocked() || w2.isLocked()){
            fail("No se puede intercambiar una rueda fija.");
            return;
        }
        int pos1 = w1.getPosition();
        int pos2 = w2.getPosition();
        String color1 = (pos1 == 0) ? null : palette.get(pos1 - 1);
        String color2 = (pos2 == 0) ? null : palette.get(pos2 - 1);
        w1.setPosition(pos2, color2);
        w2.setPosition(pos1, color1);
        refreshJackpotLook();
        succeed();
    }

    /**
     * Bloquea una rueda para que no pueda girar hasta desbloquearla.
     *
     * @param wheel posicion 1-based de la rueda a fijar
     */
    public void lock(int wheel){
        if(wheels.isEmpty()){
            fail("No hay ruedas en la máquina.");
            return;
        }
        int at = clamp(wheel, 1, wheels.size());
        wheels.get(at - 1).lock();
        succeed();
    }

    /**
     * Desbloquea una rueda que estaba bloqueada.
     *
     * @param wheel posicion 1-based de la rueda a liberar
     */
    public void unlock(int wheel){
        if(wheels.isEmpty()){
            fail("No hay ruedas en la máquina.");
            return;
        }
        int at = clamp(wheel, 1, wheels.size());
        wheels.get(at - 1).unlock();
        succeed();
    }

    /**
     * Devuelve los simbolos que hay en la paleta, en el mismo orden.
     *
     * @return arreglo con los simbolos de la paleta
     */
    public String[] symbols(){
        succeed();
        return palette.toArray(new String[0]);
    }

    /**
     * Cuenta cuantos simbolos diferentes hay en la paleta.
     *
     * @return numero de simbolos diferentes
     */
    public int distinctSymbols(){
        Set<String> distinct = new HashSet<String>(palette);
        succeed();
        return distinct.size();
    }

    /**
     * Devuelve el simbolo que tiene cada rueda.
     *
     * @return arreglo con un simbolo por rueda (o {@code null} en la
     *         posicion de una rueda vacia), en el mismo orden que las ruedas
     */
    public String[] configuration(){
        String[] result = new String[wheels.size()];
        for(int i = 0; i < wheels.size(); i++){
            int pos = wheels.get(i).getPosition();
            result[i] = (pos == 0) ? null : palette.get(pos - 1);
        }
        succeed();
        return result;
    }

    /**
     * Revisa si todas las ruedas tienen el mismo simbolo.
     *
     * @return {@code true} si hay jackpot, {@code false} en caso contrario
     */
    public boolean isJackpot(){
        succeed();
        return computeJackpot();
    }

    /** Muestra la maquina y sus ruedas en el Canvas. */
    public void makeVisible(){
        visible = true;
        background.makeVisible();
        refreshJackpotLook();

        for(Wheel wheel : wheels){
            wheel.show();
        }

        succeed();
    }

    /** Oculta la maquina y sus ruedas del Canvas. */
    public void makeInvisible(){
        background.makeInvisible();
        for(Wheel wheel : wheels){
            wheel.hide();
        }
        visible = false;
        succeed();
    }

    /** Cierra la maquina ocultandola. */
    public void exit(){
        makeInvisible();
        succeed();
    }

    /**
     * Indica si la ultima operacion se hizo correctamente.
     *
     * @return {@code true} si la ultima operacion tuvo exito
     */
    public boolean ok(){
        return lastOk;
    }

    // ---------------------------------------------------------------
    // Metodos privados que ayudan a la clase
    // ---------------------------------------------------------------

    // Mantiene una posicion dentro del rango permitido.
    private int clamp(int pos, int min, int max){
        if(pos < min){
            return min;
        }
        if(pos > max){
            return max;
        }
        return pos;
    }

    // Reacomoda las ruedas para que queden separadas correctamente.
    private void layoutWheels(){
        for(int i = 0; i < wheels.size(); i++){
            wheels.get(i).relayout(LAYOUT_X + i * WHEEL_SPACING, LAYOUT_Y);
        }
        int width = Math.max(WHEEL_SPACING, wheels.size() * WHEEL_SPACING + 20);
        background.changeSize(120, width);
    }

    // Pone el primer simbolo en las ruedas que aun estan vacias.
    private void assignDefaultSymbolToEmptyWheels(){
        for(Wheel wheel : wheels){
            if(wheel.getPosition() == 0){
                wheel.setPosition(1, palette.get(0));
            }
        }
    }

    // // Si metemos un simbolo en la mitad, ajustamos las posiciones de las
    // ruedas para que sigan mostrando el mismo color.
    private void fixWheelPositionsAfterInsertion(int insertedIndex){
        for(Wheel wheel : wheels){
            int pos = wheel.getPosition();
            if(pos == 0){
                continue;
            }
            if(pos - 1 >= insertedIndex - 1){
                int newPos = pos + 1;
                wheel.setPosition(newPos, palette.get(newPos - 1));
            }
        }
    }

    // Ajusta las posiciones de las ruedas cuando se quita un simbolo.
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

    // Hace un giro aleatorio en una sola rueda.
    private void spinOneWheelRandom(Wheel wheel){
        int newPosition = random.nextInt(palette.size()) + 1;
        wheel.setPosition(newPosition, palette.get(newPosition - 1));
    }

    // // Avanza la rueda un simbolo y vuelve al inicio cuando llega al final.
    private void advanceOneStep(Wheel wheel){
        int current = wheel.getPosition();
        int currentIndex = (current == 0) ? -1 : current - 1;
        int nextIndex = (currentIndex + 1) % palette.size();
        wheel.setPosition(nextIndex + 1, palette.get(nextIndex));
    }

    // Pausa un momento para que se pueda ver el giro.
    private void pause(){
        try{
            Thread.sleep(STEP_DELAY_MS);
        } catch(InterruptedException e){
            Thread.currentThread().interrupt();
        }
    }

    // Comprueba internamente si todas las ruedas tienen el mismo simbolo.
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

    // Actualiza el fondo dependiendo de si hay jackpot.
    private void refreshJackpotLook(){
        if(!visible){
            return;
        }
        background.changeColor(computeJackpot() ? "gold" : "white");
    }

    // Marca la ultima operacion como correcta.
    private void succeed(){
        lastOk = true;
    }

    // Marca la operacion como fallida y muestra el mensaje si la maquina esta visible.
    private void fail(String message){
        lastOk = false;
        if(visible){
            JOptionPane.showMessageDialog(null, message,
                "Slot Machine", JOptionPane.WARNING_MESSAGE);
        }
    }
}
