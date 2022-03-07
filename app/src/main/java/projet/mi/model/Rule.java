package projet.mi.model;

import projet.mi.exception.IllegalSyntax;
import projet.mi.graph.Configuration;

import java.util.Arrays;

public class Rule {
    private State[] rule;

    public Rule(String[] rule) throws IllegalSyntax {
        if(rule.length != 4) {
            throw new IllegalSyntax("Error syntax rules, not the right number of states !");
        }
        this.rule = new State[4];
        for(int i = 0; i < 4; i++) {
            this.rule[i] = new State(rule[i]);
        }
    }

    public State[] getRule(){
        return this.rule;
    }

    @Override
    public String toString() {
        return rule[0].toString() + " " + rule[1].toString() + " -> " + rule[2].toString() + " " + rule[3].toString();
    }

    public void isIn(StateSet possibleStates) throws IllegalSyntax {
        for(int i = 0; i < 4; i++) {
            if(!rule[i].isIn(possibleStates)) {
                throw new IllegalSyntax("Error syntax Rule, some states are not defined in STATES !");
            }
        }
    }
}
