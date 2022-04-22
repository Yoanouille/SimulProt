package projet.mi.graph;

import projet.mi.model.Protocol;
import projet.mi.model.Rule;
import projet.mi.model.StateSet;

import java.util.*;

public class Graph {
    private Protocol protocol;
    private HashMap<Configuration, ArrayList<Configuration>> graph;
    //private LinkedList<Configuration> queue;

    public Graph(Protocol p){
        protocol = p;
        graph = new HashMap<>();
        //queue = new LinkedList<>();
        //queue.add(conf);
        //graph.put(conf, new ArrayList<>());

    }

    public void step(LinkedList<Configuration> queue){
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

    public void createGraph(Configuration conf){
        LinkedList<Configuration> queue = new LinkedList<>();
        queue.add(conf);
        while(!queue.isEmpty()){
            step(queue);
        }
    }

    public void step2(HashSet<Configuration> visited, LinkedList<Configuration> queue){
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

        LinkedList<Configuration> queue = new LinkedList<>();
        queue.add(conf);
        while(!queue.isEmpty()){
            if(!queue.getFirst().allInSet(set)){
                return false;
            }
            step2(visited, queue);
        }
        return true;
    }

    public String hasFinalSon(Configuration conf){
        HashSet<Configuration> visited = new HashSet<>();
        LinkedList<Configuration> queue = new LinkedList<>();
        queue.add(conf);
        while(!queue.isEmpty()){
            Configuration c = queue.getFirst();
            if(isFinal(c)){
                if(c.allInSet(protocol.getYes())){
                    return "yes";
                } else {
                    return "no";
                }
            }
            step2(visited, queue);
        }
        return "noFinalSon";
    }

    public boolean isWellDefined(Configuration conf){
        HashSet<Configuration> visited = new HashSet<>();
        LinkedList<Configuration> queue = new LinkedList<>();
        String endingConf = "";
        queue.add(conf);
        while(!queue.isEmpty()){
            if(Thread.currentThread().isInterrupted()) return true;
            String hasFinalSon = hasFinalSon(queue.getFirst());
            if(hasFinalSon.equals("noFinalSon")){
                return false;
            } else if(endingConf.equals("")){
                endingConf = hasFinalSon;
            } else if(!endingConf.equals(hasFinalSon)){
                return false;
            }
            step2(visited, queue);
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
