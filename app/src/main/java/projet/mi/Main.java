package projet.mi;

import projet.mi.exception.IllegalSyntax;
import projet.mi.gen.Gen;
import projet.mi.gen.GenParser;
import projet.mi.graph.Configuration;
import projet.mi.graph.Graph;
import projet.mi.gui.MenuStart;
import projet.mi.model.Population;
import projet.mi.model.Protocol;

import java.io.FileNotFoundException;

public class Main {
    public static void main(String[] args) throws IllegalSyntax, FileNotFoundException {
        // x - y < 1
//        int[] var = {1,-1};
//        String[] nameState = {"x", "y"};
//        int c = 1;
//        Gen gen = new Gen(var, nameState, c, "../test.pp");

        //x - y + 2z < 3
//        int[] var = {1,-1,2};
//        String[] nameState = {"x", "y", "z"};
//        int c = 3;
//        Gen gen = new Gen(var, nameState, c, "../test2.pp");

        //x + y < 10
//        int[] var = {1,1};
//        String[] nameState = {"x", "y"};
//        int c = 10;
//        Gen gen = new Gen(var, nameState, c, "../test2.pp");

        GenParser parser = new GenParser("1*x-1*y<10");
        Gen gen = new Gen(parser.getVar(), parser.getNames(), parser.getC(), "../test3.pp");

        Protocol p = new Protocol("../examples/notWellDefined.pp");
    //    System.out.print(p);
        //String[] startingStates = {"Y", "Y", "Y", "N", "N"};
        String[] startingStates = {"0", "0", "1", "1"};
        Population pop = new Population(p, startingStates);
        Configuration conf = pop.getConfiguration();
        Graph g = new Graph(p);
     //   System.out.println(g.isWellDefined(conf));
     //   System.out.println(g);
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
