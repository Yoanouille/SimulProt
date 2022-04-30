package projet.mi.gen;

import java.util.LinkedList;

public class GenState {
    private int leader;
    private int output;
    private int value;

    private LinkedList<String> alias;
    public GenState(int leader, int output, int value) {
        this.leader = leader;
        this.output = output;
        this.value = value;
    }

    @Override
    public String toString() {
        //if(alias != null) return alias.get(0);
        return "(" + leader + ',' + output + "," + value + ")";
    }

    public GenState[] getRuleState(GenState gs2, int s, int c, int mod) {
        if(this.leader == 0 && gs2.leader == 0) return null;
        if(mod == 0) {
            GenState[] gen = new GenState[2];
            int q = Math.max(-s, Math.min(s, this.value + gs2.value));
            int r = this.value + gs2.value - q;
            int b = (q < c ? 1 : 0);
            gen[0] = new GenState(1, b, q);
            gen[1] = new GenState(0, b, r);
            return gen;
        } else {
            GenState[] gen = new GenState[2];
            int q = (this.value + gs2.value)%mod;
            int b = ( (q + mod)%mod == (c + mod)%mod ? 1 : 0);
            gen[0] = new GenState(1, b, q);
            gen[1] = new GenState(0, b, 0);
            return gen;
        }
    }

    public void addAlias(String alia) {
        if(alias == null) alias = new LinkedList<>();
        alias.add(alia);
    }

    public int getLeader() {
        return leader;
    }

    public boolean equals(GenState genState) {
        return (this.leader == genState.leader && this.output == genState.output && this.value == genState.value);
    }

    public int getOutput() {
        return output;
    }
}
