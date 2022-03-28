package projet.mi;

import projet.mi.exception.IllegalSyntax;
import projet.mi.graph.Configuration;
import projet.mi.graph.Graph;
import projet.mi.gui.MenuStart;
import projet.mi.model.Population;
import projet.mi.model.Protocol;

public class Main {
    public static void main(String[] args) throws IllegalSyntax {
        Protocol p = new Protocol("../examples/notWellDefined.pp");
        System.out.print(p);
        //String[] startingStates = {"Y", "Y", "Y", "N", "N"};
        String[] startingStates = {"0", "0", "1", "1"};
        Population pop = new Population(p, startingStates);
        Configuration conf = pop.getConfiguration();
        Graph g = new Graph(p);
        System.out.println(g.isWellDefined(conf));
        System.out.println(g);
        /*String[] startingStates = {"Y", "Y", "Y", "N", "N"};
        Population pop = new Population(p, startingStates);
        Configuration conf = pop.getConfiguration();

        System.out.println(conf);
        Graph g = new Graph(p, conf);
        //g.createGraph();
        //System.out.println(g);
        pop.simulate(-1);
        conf = pop.getConfiguration();
        System.out.println(g.isFinal(conf));
        System.out.println(g);*/

        //Stats s = new Stats(p);
        //s.statsForDifferentSizes(2, 10,1,10000, 10000, (st) -> {});

        MenuStart.launch(MenuStart.class, args);
    }
}
