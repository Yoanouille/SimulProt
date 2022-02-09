
public class Prototype{
    public static String[] interact(String a, String b, String[][] rules){
        String[] rep = new String[2];
        for(int i = 0; i < rules.length; i++){
            if(rules[i][0].equals(a) && rules[i][1].equals(b)){
                rep[0] = rules[i][2];
                rep[1] = rules[i][3];
                return rep;
            }
            if(rules[i][0].equals(b) && rules[i][1].equals(a)){
                rep[1] = rules[i][2];
                rep[0] = rules[i][3];
                return rep;
            }
        }
        rep[0] = a;
        rep[1] = b;
        return rep;
    }

    public static void printPopulation(String[] p){
        String s = "";
        for(int i = 0; i < p.length; i++){
            s += p[i] + " ";
        }
        System.out.println(s);
    }

    public static boolean allInSet(String[] pop, String[] set){
        for(int i = 0; i < pop.length; i++){
            int j;
            for(j = 0; j < set.length; j++){
                if(pop[i].equals(set[j])) break;
            }
            if(j == set.length) return false;
        }
        return true;
    }

    public static int run(int n, String[] pop, String[][] rules, String[] yes, String[] no, boolean print){
        if(print) printPopulation(pop);
        for(int i = 0; i < n || n == -1; i++){
            int ind1 = (int)(Math.random()*pop.length);
            int ind2;
            do{
                ind2 = (int)(Math.random()*pop.length);
            }while(ind2 == ind1);
            String[] newStates = interact(pop[ind1], pop[ind2], rules);
            pop[ind1] = newStates[0];
            pop[ind2] = newStates[1];
            if(print) printPopulation(pop);
            if(n == -1){
                if(allInSet(pop, yes)) return 1;
                if(allInSet(pop, no)) return 0;
            }
        }
        if(allInSet(pop, yes)) return 1;
        if(allInSet(pop, no)) return 0;
        printPopulation(pop);
        return -1;
    }

    public static void test(int n, String[][] rules, String[] yes, String[] no){
        for(int i = 0; i < n; i++){
            System.out.print("test "+i+": ");
            int t = (int)(Math.random()*18)+2;
            String[] pop = new String[t];
            int numberOfA = 0;
            int numberOfB = 0;
            for(int j = 0; j < t; j++){
                if(Math.random() > .5){
                    pop[j] = "Y";
                    numberOfA++;
                } else {
                    pop[j] = "N";
                    numberOfB++;
                }
            }
            double r = ((double)numberOfA)/((double)(numberOfA+numberOfB));
            int result = run(100000, pop, rules, yes, no, false);
            System.out.println("% of Y = "+r+", result: "+result);
        }
    }

    public static void main(String[] args){
        
        // converge vers que des y si Y >= M au départ
        /*String[] pop = {"Y", "Y", "N", "N", "N"};
        String[] yes = {"Y", "y"};
        String[] no = {"N", "n"};
        String[][] rules = {
            {"Y", "N", "y", "n"},
            {"Y", "n", "Y", "n"},
            {"N", "y", "N", "n"},
            {"y", "n", "y", "y"},
        };*/
        /*String[] pop = {"Y", "Y", "Y", "N", "N"};
        String[] yes = {"Y", "Y1", "Y2", "y"};
        String[] no = {"N", "N1", "N2", "N3", "n"};
        String[][] rules = {
            {"Y", "N", "Y1", "N1"},
            {"Y", "N1", "Y1", "N2"},
            {"Y", "N2", "Y1", "N3"},

            {"Y1", "N", "Y2", "N1"},
            {"Y1", "N1", "Y2", "N2"},
            {"Y1", "N2", "Y2", "N3"},

            {"N3", "N3", "y", "y"},
            {"Y", "N3", "Y", "y"},
            {"N1", "Y2", "N1", "n"},
            {"N2", "Y2", "N2", "n"},
            {"Y1", "N3", "Y1", "y"},
            {"y", "n", "y", "y"}

        };*/
        
        //que des yes si il y a 2/3 de A ou plus au départ
        //String[] pop = {"A", "A", "A", "B", "B"};
        /*String[] yes = {"A", "1"};
        String[] no = {"B", "AB", "0"};
        String[][] rules = {
            {"A", "B", "AB", "0"},
            {"AB", "A", "1", "0"},
            {"AB", "1", "AB", "0"},
            {"A", "0", "A", "1"},
            {"B", "1", "B", "0"},
            {"0", "1", "1", "1"}
        };*/
        //que des yes si il y a 3/5 de A ou plus au départ
        //String[] pop = {"A", "A", "A", "B", "B", "B"};
        /*String[] yes = {"A", "AAB", "AAAB", "1"};
        String[] no = {"B", "AB", "ABB", "AABB","0"};
        String[][] rules = {
            {"A", "B", "AB", "0"},
            {"AB", "A", "AAB", "1"},
            {"AB", "B", "ABB", "0"},
            {"ABB", "A", "AABB", "0"},
            {"AAB", "A", "AAAB", "1"},
            {"AAB", "AABB", "AB", "1"},
            {"AAB", "B", "AABB", "0"},
            {"AAB", "AB", "1", "0"},
            {"AAB", "ABB", "AB", "AABB"},
            {"AAAB", "B", "1", "0"},
            {"AABB", "A", "1", "0"},

            {"A", "0", "A", "1"},
            {"B", "1", "B", "0"},
            {"AB", "1", "AB", "0"},
            {"ABB", "1", "ABB", "0"},
            {"AAB", "0", "AAB", "1"},
            {"AAAB", "0", "AAAB", "1"},
            {"AABB", "1", "AABB", "0"},

            {"0", "1", "1", "1"}
        };*/
        //run(-1, pop, rules, yes, no);
        //test(10, rules, yes, no);

        
        String filename = "example.pp";
        if(args.length >= 1){
            filename = args[0];
        }
        String[] pop = {"Y", "Y", "Y", "N", "N", "N"};
        if(args.length >= 2){
            pop = args[1].split(",");
        }

        Protocol p = new Protocol(filename);
        System.out.println(p);
        run(-1, pop, p.getRules(), p.getYes(), p.getNo(), true);
        //test(10, p.getRules(), p.getYes(), p.getNo());
    }
}
