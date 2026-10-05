import java.util.ArrayList;
import java.util.HashSet;
import java.util.Random;
import javax.swing.JOptionPane;

/**
 * Maquina tragamonedas. Tiene una lista de simbolos y una lista de
 * ruedas; cada rueda tiene su propia copia de los simbolos.
 * Tipos de rueda: normal, lefty, rebel y lazy.
 * Tipos de simbolo: normal, ephemeral y shy.<br>
 * <b>(wheels, symbols, visible, lastOk)</b><br>
 * <b>Inv:</b> los simbolos tienen colores diferentes y cada rueda tiene
 * un simbolo por cada simbolo de la maquina, en el mismo orden
 *
 * @author Samuel Mena Serrato
 * @author Wilson Morales Sanchez
 */
public class SlotMachine{

    private static final int SPACE = 70;
    private static final int START_X = 60;
    private static final int START_Y = 100;
    private static final int DELAY = 300;

    // Colores para SlotMachine(n); todos se ven distintos entre si
    private static final String[] COLORS = {
        "red", "blue", "green", "yellow", "orange", "purple", "black",
        "brown", "aqua", "fuchsia", "lime", "navy", "teal", "olive",
        "maroon", "gray", "coral", "crimson", "darkblue", "darkgreen",
        "darkred", "darkviolet", "deeppink", "dodgerblue", "firebrick",
        "forestgreen", "goldenrod", "hotpink", "indigo", "lightgreen",
        "mediumpurple", "midnightblue", "orangered", "orchid", "peru",
        "royalblue", "salmon", "seagreen", "skyblue", "slateblue",
        "springgreen", "steelblue", "tomato", "turquoise", "violet",
        "yellowgreen", "chartreuse", "chocolate", "darkgray", "dimgray"
    };

    private ArrayList<Wheel> wheels;
    private ArrayList<Symbol> symbols;
    private Rectangle background;
    private Random random;
    private boolean visible;
    private boolean lastOk;

    /** Crea una maquina vacia e invisible. */
    public SlotMachine(){
        wheels = new ArrayList<Wheel>();
        symbols = new ArrayList<Symbol>();
        background = new Rectangle();
        background.changeColor("white");
        background.changeSize(120, SPACE);
        background.moveHorizontal(START_X - 10 - 70);
        background.moveVertical(START_Y - 10 - 15);
        random = new Random();
        visible = false;
        lastOk = true;
    }

    /**
     * Crea una maquina con n ruedas normales y n simbolos normales de
     * colores distintos, con las ruedas en posiciones al azar.
     *
     * @param n numero de ruedas y de simbolos (entre 1 y 50)
     */
    public SlotMachine(int n){
        this();
        for(int i = 1; i <= n; i++){
            addSymbol(i, COLORS[i - 1]);
        }
        for(int i = 1; i <= n; i++){
            addWheel(i);
        }
        for(int i = 1; i <= n; i++){
            spin(i);
        }
    }

    /**
     * Agrega una rueda normal.
     *
     * @param pos posicion de la rueda (desde 1)
     */
    public void addWheel(int pos){
        addWheel("normal", pos);
    }

    /**
     * Agrega una rueda del tipo indicado. La rueda recibe su propio
     * simbolo de cada tipo y color, y empieza en el primero.
     *
     * @param type "normal", "lefty", "rebel" o "lazy"
     * @param pos  posicion de la rueda (desde 1)
     */
    public void addWheel(String type, int pos){
        try{
            Wheel wheel = createWheel(type);
            for(int i = 0; i < symbols.size(); i++){
                Symbol s = symbols.get(i);
                wheel.addSymbol(i + 1, createSymbol(s.getType(), s.getColor()));
            }
            wheels.add(inRange(pos, wheels.size() + 1) - 1, wheel);
            placeWheels();
            succeed();
        }catch(SlotMachineException e){
            fail(e.getMessage());
        }
    }

    /**
     * Elimina una rueda. La rueda rebelde no se deja eliminar.
     *
     * @param pos posicion de la rueda (desde 1)
     */
    public void delWheel(int pos){
        try{
            Wheel wheel = wheelAt(pos);
            wheel.checkDeletable();
            wheel.makeInvisible();
            wheels.remove(wheel);
            placeWheels();
            succeed();
        }catch(SlotMachineException e){
            fail(e.getMessage());
        }
    }

    /**
     * Agrega un simbolo normal.
     *
     * @param pos   posicion del simbolo (desde 1)
     * @param color color del simbolo
     */
    public void addSymbol(int pos, String color){
        addSymbol("normal", pos, color);
    }

    /**
     * Agrega un simbolo del tipo indicado a la maquina y a cada rueda.
     * El color debe existir en CSS y no puede estar repetido.
     *
     * @param type  "normal", "ephemeral" o "shy"
     * @param pos   posicion del simbolo (desde 1)
     * @param color color del simbolo
     */
    public void addSymbol(String type, int pos, String color){
        try{
            if(!CssColors.isValid(color)) throw new SlotMachineException(SlotMachineException.INVALID_COLOR);
            if(hasSymbol(color)) throw new SlotMachineException(SlotMachineException.REPEATED_COLOR);
            Symbol symbol = createSymbol(type, color);
            pos = inRange(pos, symbols.size() + 1);
            symbols.add(pos - 1, symbol);
            for(Wheel wheel : wheels){
                wheel.addSymbol(pos, createSymbol(type, color));
            }
            updateBackground();
            succeed();
        }catch(SlotMachineException e){
            fail(e.getMessage());
        }
    }

    /**
     * Elimina un simbolo de la maquina y de cada rueda. Las ruedas que
     * lo mostraban pasan al primer simbolo.
     *
     * @param symbol color del simbolo
     */
    public void delSymbol(String symbol){
        try{
            int pos = findSymbol(symbol);
            symbols.remove(pos - 1);
            for(Wheel wheel : wheels){
                wheel.delSymbol(pos);
            }
            updateBackground();
            succeed();
        }catch(SlotMachineException e){
            fail(e.getMessage());
        }
    }

    /**
     * Pone un simbolo en una rueda.
     *
     * @param wheel  posicion de la rueda (desde 1)
     * @param symbol color del simbolo
     */
    public void placeSymbol(int wheel, String symbol){
        try{
            int pos = findSymbol(symbol);
            wheelAt(wheel).placeSymbol(pos);
            updateBackground();
            succeed();
        }catch(SlotMachineException e){
            fail(e.getMessage());
        }
    }

    /**
     * Gira una rueda al azar. Una rueda bloqueada no gira.
     *
     * @param wheel posicion de la rueda (desde 1)
     */
    public void spin(int wheel){
        try{
            wheelToSpin(wheel).spin(random.nextInt(symbols.size()));
            updateBackground();
            succeed();
        }catch(SlotMachineException e){
            fail(e.getMessage());
        }
    }

    /** Gira al azar, de izquierda a derecha, las ruedas no bloqueadas. */
    public void spin(){
        try{
            checkCanSpin();
            for(Wheel w : wheels){
                if(!w.isLocked()){
                    w.spin(random.nextInt(symbols.size()));
                }
            }
            updateBackground();
            succeed();
        }catch(SlotMachineException e){
            fail(e.getMessage());
        }
    }

    /**
     * Gira una rueda un numero de pasos, de uno en uno. Si la maquina
     * esta visible, se ve cada paso.
     *
     * @param wheel posicion de la rueda (desde 1)
     * @param steps numero de pasos (no negativo)
     */
    public void spin(int wheel, int steps){
        try{
            if(steps < 0) throw new SlotMachineException(SlotMachineException.NEGATIVE_STEPS);
            Wheel w = wheelToSpin(wheel);
            for(int i = 0; i < steps; i++){
                w.spin(1);
                if(visible){
                    updateBackground();
                    pause();
                }
            }
            updateBackground();
            succeed();
        }catch(SlotMachineException e){
            fail(e.getMessage());
        }
    }

    /**
     * Pone cada rueda en el simbolo indicado. Las bloqueadas no cambian.
     *
     * @param setSymbols un color por cada rueda
     */
    public void spin(String[] setSymbols){
        try{
            if(setSymbols == null || setSymbols.length != wheels.size()){
                throw new SlotMachineException(SlotMachineException.WRONG_CONFIGURATION);
            }
            int[] positions = new int[setSymbols.length];
            for(int i = 0; i < setSymbols.length; i++){
                positions[i] = findSymbol(setSymbols[i]);
            }
            for(int i = 0; i < wheels.size(); i++){
                if(!wheels.get(i).isLocked()){
                    wheels.get(i).placeSymbol(positions[i]);
                }
            }
            updateBackground();
            succeed();
        }catch(SlotMachineException e){
            fail(e.getMessage());
        }
    }

    /**
     * Intercambia los simbolos de dos ruedas. No se puede si alguna
     * esta bloqueada o es rebelde.
     *
     * @param wheel1 posicion de la primera rueda (desde 1)
     * @param wheel2 posicion de la segunda rueda (desde 1)
     */
    public void swap(int wheel1, int wheel2){
        try{
            Wheel a = wheelAt(wheel1);
            Wheel b = wheelAt(wheel2);
            a.checkSwappable();
            b.checkSwappable();
            int posA = a.getPosition();
            a.setPosition(b.getPosition());
            b.setPosition(posA);
            updateBackground();
            succeed();
        }catch(SlotMachineException e){
            fail(e.getMessage());
        }
    }

    /**
     * Bloquea una rueda. La rueda rebelde no se deja bloquear.
     *
     * @param wheel posicion de la rueda (desde 1)
     */
    public void lock(int wheel){
        try{
            wheelAt(wheel).lock();
            succeed();
        }catch(SlotMachineException e){
            fail(e.getMessage());
        }
    }

    /**
     * Desbloquea una rueda.
     *
     * @param wheel posicion de la rueda (desde 1)
     */
    public void unlock(int wheel){
        try{
            wheelAt(wheel).unlock();
            succeed();
        }catch(SlotMachineException e){
            fail(e.getMessage());
        }
    }

    /**
     * Consulta los simbolos de la maquina.
     *
     * @return colores de los simbolos, en orden
     */
    public String[] symbols(){
        String[] result = new String[symbols.size()];
        for(int i = 0; i < symbols.size(); i++){
            result[i] = symbols.get(i).getColor();
        }
        succeed();
        return result;
    }

    /**
     * Cuenta los simbolos diferentes que muestran las ruedas.
     *
     * @return cantidad de simbolos diferentes
     */
    public int distinctSymbols(){
        HashSet<String> different = new HashSet<String>();
        for(Wheel w : wheels){
            if(w.getColor() != null){
                different.add(w.getColor());
            }
        }
        succeed();
        return different.size();
    }

    /**
     * Consulta el simbolo que muestra cada rueda.
     *
     * @return colores de izquierda a derecha (null si una rueda esta vacia)
     */
    public String[] configuration(){
        String[] result = new String[wheels.size()];
        for(int i = 0; i < wheels.size(); i++){
            result[i] = wheels.get(i).getColor();
        }
        succeed();
        return result;
    }

    /**
     * Revisa si todas las ruedas muestran el mismo simbolo.
     *
     * @return true si hay jackpot
     */
    public boolean isJackpot(){
        succeed();
        return allEqual();
    }

    /** Muestra la maquina; el fondo es dorado si hay jackpot. */
    public void makeVisible(){
        visible = true;
        background.makeVisible();
        placeWheels();
        succeed();
    }

    /** Oculta la maquina. */
    public void makeInvisible(){
        background.makeInvisible();
        for(Wheel w : wheels){
            w.makeInvisible();
        }
        visible = false;
        succeed();
    }

    /** Termina el simulador. */
    public void exit(){
        makeInvisible();
        succeed();
    }

    /**
     * Indica si la ultima accion se pudo hacer.
     *
     * @return true si la ultima accion tuvo exito
     */
    public boolean ok(){
        return lastOk;
    }

    // Crea una rueda segun su tipo.
    private Wheel createWheel(String type) throws SlotMachineException{
        if(type.equals("normal")) return new NormalWheel();
        if(type.equals("lefty")) return new LeftyWheel();
        if(type.equals("rebel")) return new RebelWheel();
        if(type.equals("lazy")) return new LazyWheel();
        throw new SlotMachineException(SlotMachineException.UNKNOWN_WHEEL_TYPE);
    }

    // Crea un simbolo segun su tipo.
    private Symbol createSymbol(String type, String color) throws SlotMachineException{
        if(type.equals("normal")) return new NormalSymbol(color);
        if(type.equals("ephemeral")) return new EphemeralSymbol(color);
        if(type.equals("shy")) return new ShySymbol(color);
        throw new SlotMachineException(SlotMachineException.UNKNOWN_SYMBOL_TYPE);
    }

    // Devuelve la rueda de esa posicion (ajustada al rango valido).
    private Wheel wheelAt(int pos) throws SlotMachineException{
        if(wheels.isEmpty()) throw new SlotMachineException(SlotMachineException.NO_WHEELS);
        return wheels.get(inRange(pos, wheels.size()) - 1);
    }

    // Revisa que haya ruedas y simbolos para poder girar.
    private void checkCanSpin() throws SlotMachineException{
        if(wheels.isEmpty()) throw new SlotMachineException(SlotMachineException.NO_WHEELS);
        if(symbols.isEmpty()) throw new SlotMachineException(SlotMachineException.NO_SYMBOLS);
    }

    // Devuelve la rueda que se va a girar, si se puede girar.
    private Wheel wheelToSpin(int pos) throws SlotMachineException{
        checkCanSpin();
        Wheel wheel = wheelAt(pos);
        if(wheel.isLocked()) throw new SlotMachineException(SlotMachineException.LOCKED_WHEEL);
        return wheel;
    }

    // Si la posicion es menor que 1 usa 1; si es mayor que max usa max.
    private int inRange(int pos, int max){
        if(pos < 1){
            return 1;
        }
        if(pos > max){
            return max;
        }
        return pos;
    }

    // Indica si ya hay un simbolo con ese color.
    private boolean hasSymbol(String color){
        for(Symbol s : symbols){
            if(s.getColor().equals(color)){
                return true;
            }
        }
        return false;
    }

    // Devuelve la posicion (desde 1) del simbolo con ese color.
    private int findSymbol(String color) throws SlotMachineException{
        for(int i = 0; i < symbols.size(); i++){
            if(symbols.get(i).getColor().equals(color)){
                return i + 1;
            }
        }
        throw new SlotMachineException(SlotMachineException.SYMBOL_NOT_FOUND);
    }

    // Ubica las ruedas una al lado de la otra, le dice a cada una cual
    // es su vecina de la izquierda y actualiza el fondo.
    private void placeWheels(){
        background.changeSize(120, wheels.size() * SPACE + 20);
        for(int i = 0; i < wheels.size(); i++){
            Wheel w = wheels.get(i);
            w.setLocation(START_X + i * SPACE, START_Y);
            if(i == 0){
                w.setLeftNeighbor(null);
            }else{
                w.setLeftNeighbor(wheels.get(i - 1));
            }
        }
        updateBackground();
    }

    // Revisa si todas las ruedas tienen el mismo color.
    private boolean allEqual(){
        if(wheels.isEmpty() || wheels.get(0).getColor() == null){
            return false;
        }
        String first = wheels.get(0).getColor();
        for(Wheel w : wheels){
            if(!first.equals(w.getColor())){
                return false;
            }
        }
        return true;
    }

    // Pinta el fondo de dorado si hay jackpot y de blanco si no. Como el
    // Canvas dibuja encima lo ultimo que cambia, despues se vuelven a
    // mostrar las ruedas para que el fondo no las tape.
    private void updateBackground(){
        if(!visible){
            return;
        }
        if(allEqual()){
            background.changeColor("gold");
        }else{
            background.changeColor("white");
        }
        for(Wheel w : wheels){
            w.makeVisible();
        }
    }

    // Espera un momento para que se vea el giro.
    private void pause(){
        try{
            Thread.sleep(DELAY);
        }catch(InterruptedException e){
            // Si se interrumpe la espera, simplemente sigue
        }
    }

    // Marca que la accion se pudo hacer.
    private void succeed(){
        lastOk = true;
    }

    // Marca que la accion fallo y muestra el mensaje si esta visible.
    private void fail(String message){
        lastOk = false;
        if(visible){
            JOptionPane.showMessageDialog(null, message, "Slot Machine",
                JOptionPane.WARNING_MESSAGE);
        }
    }
}
