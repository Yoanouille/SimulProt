package projet.mi;

import projet.mi.exception.IllegalSyntax;
import projet.mi.gui.Menu;
import projet.mi.model.Population;
import projet.mi.model.Protocol;

public class Main {
    public static void main(String[] args) throws IllegalSyntax {
        Protocol p = new Protocol("../examples/example.pp");
        System.out.print(p);
        String[] startingStates = {"Y", "Y", "Y", "N", "N"};
        Population pop = new Population(p, startingStates);
        pop.simulate(-1);

        Menu.launch(Menu.class, args);
    }
}
