package projet.mi.model;

import java.util.Arrays;

public class Rule {
    private State[] rule;

    public Rule(String[] rule) {
        if(rule.length != 4) {
            System.out.println("Error syntax rule !");
            System.exit(1);
        }
        this.rule = new State[4];
        for(int i = 0; i < 4; i++) {
            this.rule[i] = new State(rule[i]);
        }
    }

    @Override
    public String toString() {
        return rule[0].toString() + " " + rule[1].toString() + " -> " + rule[2].toString() + " " + rule[3].toString();
    }
}
