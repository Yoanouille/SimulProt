package projet.mi.model;
import projet.mi.exception.IllegalSyntax;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Scanner;

public class Protocol {
    private StateSet states;
    private StateSet init;
    private StateSet yes;
    private StateSet no;
    private Rule[] rules;
    private LinkedList<HashMap<State, Integer>> configurations;

    public Protocol(String fileName) throws IllegalSyntax {
        this.configurations = new LinkedList<>();

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
            throw new IllegalSyntax("The file: \""+fileName+"\" was not found");
        }
        if(states == null) {
            throw new IllegalSyntax("STATES missing!");
        }
        if(init == null) {
           throw new IllegalSyntax ("INITIAL missing!");
        }
        if(yes == null) {
            throw new IllegalSyntax("YES missing!");
        }
        if(no == null) {
            throw new IllegalSyntax("NO missing!");
        }
        if(rules == null) {
            throw new IllegalSyntax("RULES missing!");
        }

        if(!init.isIn(states)){
            throw new IllegalSyntax("INITIAL should be a subset of STATES!");
        }

        if(!yes.isIn(states)){
            throw new IllegalSyntax("YES should be a subset of STATES!");
        }

        if(!no.isIn(states)){
            throw new IllegalSyntax("NO should be a subset of STATES!");
        }

        if(!yes.disjoint(no)){
            throw new IllegalSyntax("YES and NO should be disjoint!");
        }
        for(int i = 0; i < rules.length; i++) {
            rules[i].isIn(states);
        }
    }

    private boolean readLine(String[] words) throws IllegalSyntax {
        switch(words[0]){
            case "STATES":
                if(words.length <= 1) {
                    throw new IllegalSyntax("Error syntax on line STATES !");
                }
                states = new StateSet(words[1].split(" "));
                break;

            case "INITIAL":
                if(words.length <= 1) {
                    throw new IllegalSyntax("Error syntax on line INITIAL !");
                }
                init = new StateSet(words[1].split(" "));
                break;

            case "YES":
                if(words.length <= 1) {
                    throw new IllegalSyntax("Error syntax on line YES !");
                }
                yes = new StateSet(words[1].split(" "));
                break;
            
            case "NO":
                if(words.length <= 1) {
                    throw new IllegalSyntax("Error syntax on line NO !");
                }
                no = new StateSet(words[1].split(" "));
                break;
            case "CONF":
                if(words.length <= 1) {
                    throw new IllegalSyntax("Error syntax on line CONF !");
                }
                this.configurations.add(this.readConfiguration(words[1]));
                break;

            default:
                return false;
        }
        return true;
    }

    private String readRules(Scanner sc) throws IllegalSyntax{
        String lastLine = "";
        LinkedList<Rule> rulesList = new LinkedList<>();
        boolean stillInRules = true;
        while(sc.hasNext() && stillInRules){
            String line = sc.next();
            stillInRules = line.length() == 0 || Character.isWhitespace(line.charAt(0));
            if(stillInRules){
                int n = 0;
                while(n < line.length() && Character.isWhitespace(line.charAt(n))) n++;
                if(n >= line.length()) continue;
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

    private HashMap<State, Integer> readConfiguration(String line) throws IllegalSyntax{
        HashMap<State, Integer> map = new HashMap<>();
        String[] words = line.split(" ");
        for(String w : words){
            String[] stateNumber = w.split("=");
            if(stateNumber.length != 2){
                throw new IllegalSyntax("Error syntax on line CONF !");
            }
            int n;
            try {
                n = Integer.parseInt(stateNumber[1]);
            } catch(RuntimeException e){
                throw new IllegalSyntax("Error syntax on line CONF : an integer is needed after the '='");
            }
            State s = new State(stateNumber[0]);
            if(!s.isIn(this.init)){
                throw new IllegalSyntax("Error syntax on line CONF : the states of the configuration must be in INIT");
            }
            map.put(s, n);
        }
        return map;
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

    public LinkedList<HashMap<State, Integer>> getConfigurations() {
        return configurations;
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
