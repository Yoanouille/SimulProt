package projet.mi.model;
import java.io.File;
import java.io.FileNotFoundException;
import java.util.LinkedList;
import java.util.Scanner;

public class Protocol {
    private StateSet states;
    private StateSet init;
    private StateSet yes;
    private StateSet no;
    private Rule[] rules;

    public Protocol(String fileName){
        File f = new File(fileName);
        try{
            Scanner sc = new Scanner(f);
            sc.useDelimiter("\n");
            while(sc.hasNext()){
                String line = sc.next();
                String[] words = line.split(": ");
                if(!readLine(words) && words[0].length() >= 5 && words[0].substring(0, 5).equals("RULES")){
                    String l = readRules(sc);
                    if(l.length() != 0){
                        readLine(l.split(": "));
                    }
                }
            }

        } catch(FileNotFoundException e){
            System.out.println("The file: \""+fileName+"\" was not found");
            System.exit(1);
        }
        if(states == null) {
            System.out.println("STATES missing!");
            System.exit(1);
        }
        if(init == null) {
            System.out.println("INITIAL missing!");
            System.exit(1);
        }
        if(yes == null) {
            System.out.println("YES missing!");
            System.exit(1);
        }
        if(no == null) {
            System.out.println("NO missing!");
            System.exit(1);
        }
        if(rules == null) {
            System.out.println("RULES missing!");
            System.exit(1);
        }

        if(!init.isIn(states)){
            System.out.println("INITIAL should be a subset of STATES!");
            System.exit(1);
        }

        if(!yes.isIn(states)){
            System.out.println("YES should be a subset of STATES!");
            System.exit(1);
        }

        if(!no.isIn(states)){
            System.out.println("NO should be a subset of STATES!");
            System.exit(1);
        }

        if(!yes.disjoint(no)){
            System.out.println("YES and NO should be disjoint!");
            System.exit(1);
        }
    }

    private boolean readLine(String[] words){
        switch(words[0]){
            case "STATES":
                if(words.length <= 1) {
                    System.out.println("Error syntax on line STATES !");
                    System.exit(1);
                }
                states = new StateSet(words[1].split(" "));
                break;

            case "INITIAL":
                if(words.length <= 1) {
                    System.out.println("Error syntax on line INITIAL !");
                    System.exit(1);
                }
                init = new StateSet(words[1].split(" "));
                break;

            case "YES":
                if(words.length <= 1) {
                    System.out.println("Error syntax on line YES !");
                    System.exit(1);
                }
                yes = new StateSet(words[1].split(" "));
                break;
            
            case "NO":
                if(words.length <= 1) {
                    System.out.println("Error syntax on line NO !");
                    System.exit(1);
                }
                no = new StateSet(words[1].split(" "));
                break;

            default:
                return false;
        }
        return true;
    }

    private String readRules(Scanner sc){
        String lastLine = "";
        LinkedList<Rule> rulesList = new LinkedList<>();
        boolean stillInRules = true;
        while(sc.hasNext() && stillInRules){
            String line = sc.next();
            stillInRules = Character.isWhitespace(line.charAt(0));
            if(stillInRules){
                int n = 1;
                while(Character.isWhitespace(line.charAt(n))) n++;
                line = line.substring(n-1);
                Rule rule = new Rule(line.substring(1).split(" -> | "));
                rulesList.add(rule);
            } else {
                lastLine = line;
            }
        }
        rules = new Rule[rulesList.size()];
        rulesList.toArray(rules);
        return lastLine;
    }

    public StateSet getStates(){
        return states;
    }

    public StateSet getInit(){
        return init;
    }

    public StateSet getYes(){
        return yes;
    }

    public StateSet getNo(){
        return no;
    }

    public Rule[] getRules(){
        return rules;
    }

    @Override
    public String toString() {
        String s = "STATES: " + states + "\n";
        s += "INITIAL: " + init + "\n";
        s += "YES: " + yes + "\n";
        s += "NO: " + no + "\n";
        s += "RULE:\n";
        for(int i = 0; i < rules.length; i++) {
            s += "\t" + rules[i] + "\n";
        }
        return s;
    }
}
