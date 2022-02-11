package projet.mi.model;

public class Population {
    private Protocol protocol;
    private Agent[] agents;


    public Population(Protocol protocol, Agent[] agents){
        this.protocol = protocol;
        this.agents = agents;
    }

    public Population(Protocol protocol, String[] states){
        this.protocol = protocol;
        this.agents = new Agent[states.length];
        for(int i = 0; i < this.agents.length; i++){
            this.agents[i] = new Agent(new State(states[i]));
        }
    }

    public void randomInteraction(){
        int i = (int)(Math.random()*this.agents.length);
        int j;
        do{
            j = (int)(Math.random()*this.agents.length);
        }while(j == i);
        this.agents[i].interact(this.agents[j], this.protocol.getRules());
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
    public void simulate(int n){
        System.out.println(this);
        for(int i = 0; i < n || n == -1; i++){
            this.randomInteraction();
            System.out.println(this);
            if(n == -1 && (this.allYes() || this.allNo())){
                break;
            }
        }
    }

    @Override
    public String toString() {
        String s = "";
        for(Agent a : this.agents){
            s += a+" ";
        }
        return s;
    }
}
