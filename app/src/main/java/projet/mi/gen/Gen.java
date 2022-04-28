package projet.mi.gen;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.util.LinkedList;

public class Gen {

    LinkedList<GenState> initial;
    LinkedList<GenState> yes;
    LinkedList<GenState> no;
    public Gen(int[] var, String[]stateNames, int sup, String file) throws FileNotFoundException {
        //Faire une vérification que les var sont tous distincts sinon dire qu'on fait une réunion entre 2 états
        // Ex : si qqun veut faire x + y < 2, lui dire que cela revient à faire x' < 2 (où x' = x + y)
        // Cela permet d'éviter une énorme redondance au niveau des règles et de potentiellement accélérer nos algos !

        // Ou alors ajouter un moyen pour dans les fichiers pour dire que x et y suivent le comportement de un tel
        // Genre faire ALIAS: y->x
        // Et quand on génére la population, on remplace y par x

        File f = new File(file);
        PrintWriter pw = new PrintWriter(f);

        int s = getS(var, sup);
        initial = new LinkedList<>();
        yes = new LinkedList<>();
        no = new LinkedList<>();
        GenState[] states = genStates(s);
        for(int i = 0; i < var.length; i++) {
            GenState state = new GenState(1, (var[i] < sup ? 1 : 0), var[i]);
            for(int j = 0; j < states.length; j++) {
                if(state.equals(states[j])) {
                    states[j].addAlias(stateNames[i]);
                    initial.add(states[j]);
                    break;
                }
            }
        }

        pw.print("STATES: ");
        System.out.print("STATES: ");
        for(int i = 0; i < states.length; i++) {
            System.out.print(states[i] + " ");
            pw.print(states[i] + " ");
        }
        pw.println();
        System.out.println();

        pw.print("INITIAL: ");
        System.out.print("INITIAL: ");
        for(GenState gst : initial) {
            pw.print(gst + " ");
            System.out.print(gst + " ");
        }
        pw.println();
        System.out.println();

        pw.print("YES: ");
        System.out.print("YES: ");
        for(GenState gst : yes) {
            pw.print(gst + " ");
            System.out.print(gst + " ");
        }
        pw.println();
        System.out.println();

        pw.print("NO: ");
        System.out.print("NO: ");
        for(GenState gst : no) {
            pw.print(gst + " ");
            System.out.print(gst + " ");
        }
        pw.println();
        System.out.println();

        pw.println("RULES:");
        LinkedList<GenRule> genRules = genRule(states, s, sup);
        for(int i = 0; i < genRules.size(); i++) {
            pw.println(genRules.get(i));
            System.out.println(genRules.get(i));
        }
        pw.close();
    }

    public int getS(int[] var, int sup) {
        int s = Math.abs(sup) + 1;
        for(int i = 0; i < var.length; i++) {
            int a = Math.abs(var[i]);
            if(a > s) s = a;
        }
        return s;
    }

    public GenState[] genStates(int s) {
        GenState[] states = new GenState[(2 * s + 1) * 2 * 2];
        for(int i = -s;  i <= s; i++) {
            for(int j = 0; j < 4; j++) {
                GenState st = new GenState(j % 2, (j < 2 ? 1 : 0), i);
                states[4 * (i + s) + j] = st;
                if(st.getOutput() == 1) yes.add(st);
                else no.add(st);
            }
        }
        return states;
    }

    public LinkedList<GenRule> genRule(GenState[] genStates, int s, int c) {
        LinkedList<GenRule> genRules = new LinkedList<>();
        for(int i = 0; i < genStates.length; i++) {
            for(int j = i + 1; j < genStates.length; j++) {
                if(genStates[i].getLeader() == 1 || genStates[j].getLeader() == 1) {
                    genRules.add(new GenRule(genStates[i], genStates[j], s, c));
                }
            }
        }
        return genRules;
    }

}
