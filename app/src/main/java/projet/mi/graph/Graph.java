package projet.mi.graph;

import projet.mi.model.Protocol;
import projet.mi.model.Rule;
import projet.mi.model.StateSet;

import java.util.*;

public class Graph {
    private Protocol protocol;
    private HashMap<Configuration, ArrayList<Configuration>> graph;
    private LinkedList<Configuration> queue;

    public Graph(Protocol p, Configuration conf){
        protocol = p;
        graph = new HashMap<>();
        queue = new LinkedList<>();
        queue.add(conf);
        //graph.put(conf, new ArrayList<>());

    }

    public void step(){
        Configuration conf = queue.removeFirst();
        ArrayList<Configuration> nexts = new ArrayList<>();
        for(Rule r : protocol.getRules()){
            Configuration newConf = conf.applyRule(r);
            if(!newConf.equals(conf) && !nexts.contains(newConf)) nexts.add(newConf);
        }
        graph.put(conf, nexts);
        for(Configuration c : nexts){
            if(graph.get(c) == null){
                graph.put(c, new ArrayList<Configuration>());
                queue.add(c);
            }
        }
    }

    public void createGraph(){
        while(!queue.isEmpty()){
            step();
        }
    }

    public void step2(HashSet<Configuration> visited){
        Configuration conf = queue.removeFirst();
        ArrayList<Configuration> nexts = new ArrayList<>();

        if(!graph.containsKey(conf)){
            for(Rule r : protocol.getRules()){
                Configuration newConf = conf.applyRule(r);
                if(!newConf.equals(conf) && !nexts.contains(newConf)) nexts.add(newConf);
            }
            graph.put(conf, nexts);
        } else if(!visited.contains(conf)){
            nexts = graph.get(conf);
        }
        visited.add(conf);
        queue.addAll(nexts);
    }

    public boolean isFinal(Configuration conf){
        StateSet set;
        if(conf.allInSet(protocol.getYes())){
            set = protocol.getYes();
        } else if(conf.allInSet(protocol.getNo())){
            set = protocol.getNo();
        } else {
            return false;
        }

        HashSet<Configuration> visited = new HashSet<>();
        //visited.add(conf);

        queue = new LinkedList<>();
        queue.add(conf);
        while(!queue.isEmpty()){
            if(!queue.getFirst().allInSet(set)){
                return false;
            }
            step2(visited);
        }
        return true;
    }

    @Override
    public String toString() {
        String s = "";
        for(Map.Entry<Configuration, ArrayList<Configuration>> e : graph.entrySet()){
            s += e.getKey()+" -> "+e.getValue()+"\n";
        }
        return s;
    }
}
