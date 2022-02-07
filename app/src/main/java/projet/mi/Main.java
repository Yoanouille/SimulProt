package projet.mi;

import projet.mi.model.Protocol;

public class Main {
    public static void main(String[] args) {
        Protocol p = new Protocol("../example.pp");
        System.out.print(p);
    }
}
