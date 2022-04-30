package projet.mi.gen;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Map;

public class Gen {

    LinkedList<String> initial;
    LinkedList<GenState> yes;
    LinkedList<GenState> no;
    HashMap<String, String> alias;
    public Gen(int[] var, String[]stateNames, int sup, int mod, String file, String title) throws FileNotFoundException {

        File f = new File(file);
        PrintWriter pw = new PrintWriter(f);

        int s = getS(var, sup, mod);
        initial = new LinkedList<>();
        alias = new HashMap<>();

        yes = new LinkedList<>();
        no = new LinkedList<>();
        GenState[] states = genStates(s);
        for(int i = 0; i < var.length; i++) {
            GenState state = new GenState(1, (var[i] < sup ? 1 : 0), var[i]);
            for(int j = 0; j < states.length; j++) {
                if(state.equals(states[j])) {
                    alias.put(stateNames[i], states[j].toString());
                    initial.add(stateNames[i]);
                    break;
                }
            }
        }

        pw.println("TITLE: " + title);

        pw.print("STATES: ");
        for(int i = 0; i < states.length; i++) {
            pw.print(states[i] + " ");
        }
        for(int i = 0; i < stateNames.length; i++) {
            pw.print(stateNames[i] + " ");
        }
        pw.println();

        pw.print("INITIAL: ");
        for(String gst : initial) {
            pw.print(gst + " ");
        }
        pw.println();

        pw.print("ALIAS: ");
        for(Map.Entry<String, String> entry : alias.entrySet()) {
            pw.print(entry.getKey() + "->" + entry.getValue() + " ");
        }
        pw.println();

        pw.print("YES: ");
        for(GenState gst : yes) {
            pw.print(gst + " ");
        }
        pw.println();

        pw.print("NO: ");
        for(GenState gst : no) {
            pw.print(gst + " ");
        }
        pw.println();

        pw.println("RULES:");
        LinkedList<GenRule> genRules = genRule(states, s, sup, mod);
        for(int i = 0; i < genRules.size(); i++) {
            pw.println(genRules.get(i));
        }
        pw.close();
    }

    public int getS(int[] var, int sup, int mod) {
        int s = Math.abs(sup) + 1;
        for(int i = 0; i < var.length; i++) {
            int a = Math.abs(var[i]);
            if(a > s) s = a;
        }
        s = Math.max(s, mod);
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

    public LinkedList<GenRule> genRule(GenState[] genStates, int s, int c, int mod) {
        LinkedList<GenRule> genRules = new LinkedList<>();
        for(int i = 0; i < genStates.length; i++) {
            for(int j = i; j < genStates.length; j++) {
                if(genStates[i].getLeader() == 1 || genStates[j].getLeader() == 1) {
                    genRules.add(new GenRule(genStates[i], genStates[j], s, c, mod));
                }
            }
        }
        return genRules;
    }

}
