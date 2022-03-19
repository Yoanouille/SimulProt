package projet.mi.model;

import org.checkerframework.checker.units.qual.C;
import projet.mi.graph.Configuration;
import projet.mi.graph.Graph;

import java.util.HashMap;
import java.util.Map;

public class Population {
    private Protocol protocol;
    private Agent[] agents;
    public static int defaultSize = 8;

    public Population(Protocol protocol, HashMap<State, Integer> configuration){
        this.protocol = protocol;
        this.popFromMap(configuration);
    }

    public Population(Protocol protocol, Agent[] agents){
        this.protocol = protocol;
        this.agents = agents;
    }

    public Population(Protocol protocol) {
        this.protocol = protocol;
        this.randomPop(defaultSize);
    }

    public Population(Protocol protocol, int size) {
        this.protocol = protocol;
        this.randomPop(size);
    }

    public int size(){
        return this.agents.length;
    }

    public void randomPop(int sizePop) {
        this.agents = new Agent[sizePop];
        int len = protocol.getInit().size();
        State[] possibleStates = new State[len];
        protocol.getInit().toArray(possibleStates);
        for(int i = 0; i < sizePop; i++) {
            agents[i] = new Agent(possibleStates[(int)(Math.random() * len)]);
        }
    }

    public void popFromMap(HashMap<State, Integer> configuration){
        int n = 0;
        for(int v : configuration.values()){
            n += v;
        }
        this.agents = new Agent[n];
        int ind = 0;
        for(Map.Entry<State, Integer> e : configuration.entrySet()){
            for(int i = 0; i < e.getValue(); i++){
                this.agents[ind] = new Agent(e.getKey());
                ind++;
            }
        }
    }

    public Population(Protocol protocol, String[] states){
        this.protocol = protocol;
        this.agents = new Agent[states.length];
        for(int i = 0; i < this.agents.length; i++){
            this.agents[i] = new Agent(new State(states[i]));
        }
    }

    public void interact(int i, int j){
        this.agents[i].interact(this.agents[j], this.protocol.getRules());
    }

    public void randomInteraction(){
        int i = (int)(Math.random()*this.agents.length);
        int j;
        do{
            j = (int)(Math.random()*this.agents.length);
        }while(j == i);
        this.interact(i, j);
    }

    public void specialInteractions() {
        int i = (int)(Math.random()*this.agents.length);
        for(int j = 0; j < this.agents.length; j++) {
            if(i != j) {
                this.interact(i,j);
            }
        }
    }

    public boolean allYes(){
        for (Agent agent : this.agents) {
            if (!agent.getState().isIn(this.protocol.getYes())) {
                return false;
            }
        }
        return true;
    }

    public boolean allNo(){
        for (Agent agent : this.agents) {
            if (!agent.getState().isIn(this.protocol.getNo())) {
                return false;
            }
        }
        return true;
    }

    //Fonction temporaire pour tester sans interface graphique
    /*public void simulate(int n){
        System.out.println(this);
        for(int i = 0; i < n || n == -1; i++){
            this.randomInteraction();
            System.out.println(this);
            if(n == -1 && (this.allYes() || this.allNo())){
                break;
            }
        }
    }*/

    public int simulate(int maxIterations){
        int i;
        for(i = 0; i < maxIterations; i++){
            this.randomInteraction();
            if(this.allYes() || this.allNo()){
                Configuration conf = getConfiguration();
                Graph g = new Graph(protocol, conf);
                if(g.isFinal(conf)){
                    break;
                }
            }
        }
        return i;
    }

    public Protocol getProtocol() {
        return protocol;
    }

    public Agent[] getAgents() {
        return agents;
    }

    public static void setDefaultSize(int s){
        defaultSize = s;
    }

    @Override
    public String toString() {
        String s = "";
        for(Agent a : this.agents){
            s += a+" ";
        }
        return s;
    }

    public Configuration getConfiguration(){
        HashMap<State, Integer> c = new HashMap<>();
        for(State s : protocol.getStates()){
            c.put(s, 0);
        }
        for(Agent a : agents){
            c.put(a.getState(), c.get(a.getState())+1);
        }
        return new Configuration(c);
    }
}
