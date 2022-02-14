package projet.mi.model;

public class Agent {

    private State state;
    private double x;
    private double y;

    public Agent(State state){
        this.state = new State(state.getState());
        x = 0;
        y = 0;
    }

    public State getState() {
        return this.state;
    }

    public void interact(Agent a, Rule[] rules){
        for (Rule r : rules) {
            State[] rule = r.getRule();
            if (rule[0].equals(this.state) && rule[1].equals(a.state)) {
                this.state.setState(rule[2].getState());
                a.state.setState(rule[3].getState());
                break;
            }
            if (rule[1].equals(this.state) && rule[0].equals(a.state)) {
                this.state.setState(rule[3].getState());
                a.state.setState(rule[2].getState());
                break;
            }
        }
    }

    @Override
    public String toString() {
        return this.state.toString();
    }
}
