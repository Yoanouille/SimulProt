package projet.mi;

import projet.mi.gui.Menu;
import projet.mi.model.Population;
import projet.mi.model.Protocol;

public class Main {
    public static void main(String[] args) {
        Protocol p = new Protocol("../example.pp");
        System.out.print(p);
        String[] startingStates = {"Y", "Y", "Y", "N", "N"};
        Population pop = new Population(p, startingStates);
        pop.simulate(-1);

        Menu.launch(Menu.class, args);
    }
}
