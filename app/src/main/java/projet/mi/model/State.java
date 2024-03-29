package projet.mi.model;

import java.util.Iterator;
import java.util.Objects;

public class State {
    private String state;
    public State(String state) {
        this.state = state;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public boolean equals(State s) {
        return this.state.equals(s.state);
    }

    public boolean isIn(StateSet s) {
        Iterator<State> ite = s.iterator();
        while(ite.hasNext()) {
            if(this.equals(ite.next())) return true;
        }
        return false;
    }

    @Override
    public String toString() {
        return state;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        State state1 = (State) o;
        return state1.equals(this);
    }

    @Override
    public int hashCode() {
        return Objects.hash(state);
    }
}
