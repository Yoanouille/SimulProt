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

    public void setAll(HashSet<Configuration> visited, String finalSon, int isfinal) {
        for(Configuration conf : visited) {
            conf.setIsFinal(isfinal);
            conf.setFinalSon(finalSon);
        }
    }
    public void setAll2(HashSet<Configuration> visited, String finalSon) {
        for(Configuration conf : visited) {
            conf.setFinalSon(finalSon);
        }
    }

    public boolean isFinal(Configuration conf){
        if(conf.getIsFinal() == 1) return true;
        if(conf.getIsFinal() == 0) return false;
        StateSet set;
        String finalSon = "";
        if(conf.allInSet(protocol.getYes())){
            set = protocol.getYes();
            finalSon = "yes";
        } else if(conf.allInSet(protocol.getNo())){
            set = protocol.getNo();
            finalSon = "no";
        } else {
            return false;
        }

        HashSet<Configuration> visited = new HashSet<>();
        //visited.add(conf);

        LinkedList<Configuration> queue = new LinkedList<>();
        queue.add(conf);
        while(!queue.isEmpty()){
            if(!queue.getFirst().allInSet(set)){
                conf.setIsFinal(0);
                return false;
            }
            step2(visited, queue);
        }
        setAll(visited, finalSon, 1);
        conf.setIsFinal(1);
        conf.setFinalSon(finalSon);
        return true;
    }

    public String hasFinalSon(Configuration conf){
        if(conf.getFinalSon().equals("yes")) return "yes";
        if(conf.getFinalSon().equals("no")) return "no";
        if(conf.getFinalSon().equals("noFinalSon")) return "noFinalSon";
        HashSet<Configuration> visited = new HashSet<>();
        LinkedList<Configuration> queue = new LinkedList<>();
        queue.add(conf);
        while(!queue.isEmpty()){
            Configuration c = queue.getFirst();
            if(isFinal(c)){
//                if(c.allInSet(protocol.getYes())){
//                    return "yes";
//                } else {
//                    return "no";
//                }
                setAll2(visited, c.getFinalSon());
                return c.getFinalSon();
            }
            step2(visited, queue);
        }
        conf.setFinalSon("noFinalSon");
        return "noFinalSon";
    }

    public boolean isWellDefined(Configuration conf){
        HashSet<Configuration> visited = new HashSet<>();
        LinkedList<Configuration> queue = new LinkedList<>();
        String endingConf = "";
        queue.add(conf);
        while(!queue.isEmpty()){
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
            s += e.getKey()+" -> "+e.getValue()+" | finalSon = " + e.getKey().getFinalSon() + " | isFinal = " + e.getKey().getIsFinal() + "\n";
        }
        return s;
    }
}
