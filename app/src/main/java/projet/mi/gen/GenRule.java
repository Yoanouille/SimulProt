package projet.mi.gen;

import java.util.LinkedList;

public class GenRule {
    private GenState[] gs;
    public GenRule(GenState gs1, GenState gs2, int s, int c) {
        this.gs = new GenState[4];
        this.gs[0] = gs1;
        this.gs[1] = gs2;

        GenState[] gen = gs1.getRuleState(gs2, s, c);
        if(gen != null) {
            gs[2] = gen[0];
            gs[3] = gen[1];
        }
    }

    @Override
    public String toString() {
        if(gs[2] == null) return "";
        return "\t" + gs[0] + " " + gs[1] + " -> " + gs[2] + " " + gs[3];
    }
}
