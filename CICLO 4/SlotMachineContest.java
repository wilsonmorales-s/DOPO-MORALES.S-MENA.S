import java.util.List;
import java.util.ArrayList;
import javax.swing.JOptionPane;

/**
 * Resuelve el problema I (Slot Machine) de la maraton ICPC 2025.
 *
 * La solucion trabaja descubriendo la posicion de cada rueda
 * y despues utiliza esa informacion para formar el jackpot.
 */
public class SlotMachineContest{

    /**
     * Crea una maquina de n ruedas y n simbolos y calcula
     * las acciones necesarias para conseguir el jackpot.
     *
     * @param n numero de ruedas y simbolos
     * @return acciones {rueda, pasos}
     */
    public static int[][] solve(int n){
        if(n<3 || n>50){
            return new int[0][];
        }
        return solve(new SlotMachine(n),n);
    }

    /**
     * Resuelve una maquina sin consultar directamente
     * la configuracion de sus ruedas.
     *
     * @param machine maquina que se va a resolver
     * @param n numero de ruedas
     * @return acciones {rueda, pasos}
     */
    public static int[][] solve(SlotMachine machine,int n){
        machine.makeInvisible();
        List<int[]> actions=new ArrayList<int[]>();
        int different=discover(machine,n,actions);

        if(different>1){
            int[] positions=locate(machine,n,actions);
            if(machine.distinctSymbols()>1){
                finish(machine,n,positions,actions);
            }
        }

        return actions.toArray(new int[0][]);
    }

    /**
     * Simula la solucion mostrando la maquina.
     *
     * @param n numero de ruedas y simbolos
     */
    public static void simulate(int n){
        if(n<3 || n>50){
            JOptionPane.showMessageDialog(null,
                "No es posible simular: n debe estar entre 3 y 50.");
            return;
        }

        SlotMachine machine=new SlotMachine(n);
        int[][] actions=solve(machine,n);

        restore(machine,n,actions);
        machine.makeVisible();

        for(int[] action:actions){
            machine.spin(action[0],action[1]);
        }
    }

    /**
     * Busca una posicion diferente para cada rueda.
     */
    private static int discover(SlotMachine machine,int n,
                                List<int[]> actions){
        int different=machine.distinctSymbols();

        for(int wheel=1;wheel<=n && different<n;wheel++){
            int before=different;
            boolean changed=false;

            for(int step=1;step<n && different>1;step++){
                machine.spin(wheel,1);
                actions.add(new int[]{wheel,1});
                different=machine.distinctSymbols();

                if(different>before){
                    changed=true;
                    break;
                }
            }

            if(!changed && different>1){
                machine.spin(wheel,1);
                actions.add(new int[]{wheel,1});
                different=machine.distinctSymbols();
            }
        }

        return different;
    }

    /**
     * Encuentra la posicion que permite relacionar
     * cada rueda con la rueda de referencia.
     */
    private static int[] locate(SlotMachine machine,int n,
                                List<int[]> actions){
        int[] positions=new int[n+1];

        machine.spin(1,1);
        actions.add(new int[]{1,1});

        for(int wheel=2;wheel<=n;wheel++){
            int found=0;
            int foundAlmost=0;

            for(int step=1;step<n;step++){
                machine.spin(wheel,1);
                actions.add(new int[]{wheel,1});

                int different=machine.distinctSymbols();

                if(different==1){
                    return positions;
                }

                if(different==n && found==0){
                    found=step;
                }

                if(different==n-1 && foundAlmost==0){
                    foundAlmost=step;
                }
            }

            if(found>0){
                positions[wheel]=found;
            }else{
                positions[wheel]=foundAlmost;
            }

            machine.spin(wheel,1);
            actions.add(new int[]{wheel,1});
        }

        positions[1]=n-1;
        return positions;
    }

    /**
     * Usa las posiciones encontradas para formar
     * el jackpot.
     */
    private static void finish(SlotMachine machine,int n,
                               int[] positions,List<int[]> actions){
        for(int wheel=2;wheel<=n;wheel++){
            int steps=(positions[wheel]+1)%n;

            if(steps>0){
                machine.spin(wheel,steps);
                actions.add(new int[]{wheel,steps});
            }

            if(machine.distinctSymbols()==1){
                return;
            }
        }
    }

    /**
     * Devuelve la maquina a la configuracion inicial.
     */
    private static void restore(SlotMachine machine,int n,
                                int[][] actions){
        for(int i=actions.length-1;i>=0;i--){
            int steps=actions[i][1]%n;
            int reverse=(n-steps)%n;

            if(reverse>0){
                machine.spin(actions[i][0],reverse);
            }
        }
    }
}