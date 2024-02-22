package projet.mi.graph;

import projet.mi.model.Protocol;
import projet.mi.model.Rule;
import projet.mi.model.State;
import projet.mi.model.StateSet;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

public class Configuration {
    private HashMap<State, Integer> conf;
    private String finalSon = "";
    private int isFinal = -1;

    public Configuration(HashMap<State, Integer> conf){
        this.conf = conf;
    }

    public Configuration clone(){
        HashMap<State, Integer> c = new HashMap<>();
        for(Map.Entry<State, Integer> e : conf.entrySet()){
            c.put(e.getKey(), e.getValue());
        }
        return new Configuration(c);
    }


    public Configuration applyRule(Rule r){
        Configuration c = clone();
        State[] arr = r.getRule();
        if((arr[0].equals(arr[1]) && c.conf.get(arr[0]) >= 2) || (!arr[0].equals(arr[1]) && c.conf.get(arr[0]) > 0 && c.conf.get(arr[1]) > 0)){
            c.conf.put(arr[0], c.conf.get(arr[0])-1);
            c.conf.put(arr[1], c.conf.get(arr[1])-1);
            c.conf.put(arr[2], c.conf.get(arr[2])+1);
            c.conf.put(arr[3], c.conf.get(arr[3])+1);
        }
        return c;
    }

    public boolean allInSet(StateSet set){
        for(Map.Entry<State, Integer> e : conf.entrySet()){
            if(e.getValue() > 0 && !e.getKey().isIn(set)){
                return false;
            }
        }
        return true;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Configuration that = (Configuration) o;
        return Objects.equals(conf, that.conf);
    }

    @Override
    public int hashCode() {
        return Objects.hash(conf);
    }

    @Override
    public String toString() {
        return conf.toString();
    }

    public int getIsFinal() {
        return isFinal;
    }

    public void setIsFinal(int isFinal) {
        this.isFinal = isFinal;
    }

    public void setFinalSon(String finalSon) {
        this.finalSon = finalSon;
    }

    public String getFinalSon() {
        return finalSon;
    }
}
