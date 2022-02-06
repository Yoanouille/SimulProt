import java.io.File;
import java.io.FileNotFoundException;
import java.util.LinkedList;
import java.util.Scanner;

public class Protocol {
    private String[] states;
    private String[] init;
    private String[] yes;
    private String[] no;
    private String[][] rules;

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
        }
    }

    private boolean readLine(String[] words){
        switch(words[0]){
            case "STATES":
                //vérifier qu'il y a bien words[1]
                states = words[1].split(" ");
                break;

            case "INITIAL":
                //vérifier qu'il y a bien words[1]
                init = words[1].split(" ");
                break;

            case "YES":
                //vérifier qu'il y a bien words[1]
                yes = words[1].split(" ");
                break;
            
            case "NO":
                //vérifier qu'il y a bien words[1]
                no = words[1].split(" ");
                break;

            default:
                return false;
        }
        return true;
    }

    private String readRules(Scanner sc){
        String lastLine = "";
        LinkedList<String[]> rulesList = new LinkedList<>();
        boolean stillInRules = true;
        while(sc.hasNext() && stillInRules){
            String line = sc.next();
            stillInRules = Character.isWhitespace(line.charAt(0));
            if(stillInRules){
                int n = 1;
                while(Character.isWhitespace(line.charAt(n))) n++;
                line = line.substring(n-1);
                String[] rule = line.substring(1).split(" -> | ");
                rulesList.add(rule);
            } else {
                lastLine = line;
            }
        }
        rules = new String[rulesList.size()][];
        rulesList.toArray(rules);
        return lastLine;
    }

    public String[] getStates(){
        return states;
    }

    public String[] getInit(){
        return init;
    }

    public String[] getYes(){
        return yes;
    }

    public String[] getNo(){
        return no;
    }

    public String[][] getRules(){
        return rules;
    }

    @Override
    public String toString() {
        String s = "STATES: ";
        for(int i = 0; i < states.length; i++){
            s += states[i]+" ";
        }
        s += "\nINITIAL: ";
        for(int i = 0; i < init.length; i++){
            s += init[i]+" ";
        }
        s += "\nYES: ";
        for(int i = 0; i < yes.length; i++){
            s += yes[i]+" ";
        }
        s += "\nNO: ";
        for(int i = 0; i < no.length; i++){
            s += no[i]+" ";
        }
        s += "\nRULES: \n";
        for(int i = 0; i < rules.length; i++){
            s += "\t"+rules[i][0]+" "+rules[i][1]+" -> "+rules[i][2]+" "+rules[i][3]+"\n";
        }
        return s;
    }
}
