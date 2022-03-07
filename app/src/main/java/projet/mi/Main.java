package projet.mi;

import projet.mi.exception.IllegalSyntax;
import projet.mi.graph.Configuration;
import projet.mi.graph.Graph;
import projet.mi.gui.Menu;
import projet.mi.model.Population;
import projet.mi.model.Protocol;

public class Main {
    public static void main(String[] args) throws IllegalSyntax {
        Protocol p = new Protocol("../examples/example.pp");
        System.out.print(p);
        String[] startingStates = {"Y", "Y", "Y", "N", "N"};
        Population pop = new Population(p, startingStates);
        Configuration conf = pop.getConfiguration();

        System.out.println(conf);
        Graph g = new Graph(p, conf);
        //g.createGraph();
        //System.out.println(g);
        pop.simulate(-1);
        conf = pop.getConfiguration();
        System.out.println(g.isFinal(conf));
        System.out.println(g);


        Menu.launch(Menu.class, args);
    }
}
