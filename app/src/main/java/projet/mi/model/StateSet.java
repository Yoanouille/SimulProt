package projet.mi.model;

import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedList;

public class StateSet extends HashSet<State> {
    public StateSet() {
        super();
    }

    public StateSet(String[] states) {
        super();
        for(int i = 0; i < states.length; i++) {
            this.add(new State(states[i]));
        }
    }

    public StateSet(State[] states) {
        for(int i = 0; i < states.length; i++) {
            this.add(states[i]);
        }
    }

    public boolean isIn(StateSet s) {
        Iterator<State> ite = this.iterator();
        while(ite.hasNext()) {
            State stateT = ite.next();
            if(!stateT.isIn(s)) return false;
        }
        return true;
    }

    public boolean equals(StateSet s) {
        return s.isIn(this) && this.isIn(s);
    }

    public boolean disjoint(StateSet s){
        Iterator<State> ite = this.iterator();
        while(ite.hasNext()) {
            State stateT = ite.next();
            if(stateT.isIn(s)) return false;
        }
        return true;
    }

    @Override
    public String toString() {
        String rep = "";
        Iterator<State> ite = this.iterator();
        while(ite.hasNext()) {
            rep += ite.next().toString() + " ";
        }
        return rep;
    }

    public LinkedList<State> toList() {
        LinkedList<State> res = new LinkedList<>();
        Iterator<State> ite = this.iterator();
        while(ite.hasNext()) {
            res.add(ite.next());
        }
        return res;
    }
}
