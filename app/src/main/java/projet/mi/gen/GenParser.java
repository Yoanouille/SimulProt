package projet.mi.gen;

import projet.mi.exception.IllegalSyntax;

import java.util.LinkedList;

public class GenParser {
    private LinkedList<String> names;
    private LinkedList<Integer> var;

    private int c;

    public GenParser(String line) throws IllegalSyntax {
        String[] parts = line.split("<");
        if(parts.length != 2) {
            throw new IllegalSyntax("Error split on <");
        }
        String left = parts[0];
        String right = parts[1];
        try {
            c = Integer.parseInt(right);
        } catch (RuntimeException e) {
            throw new IllegalSyntax("Error need number right part !");
        }

        names = new LinkedList<>();
        var = new LinkedList<>();
        int begin = 0;
        boolean nextPlus = true;
        for(int i = 0; i < left.length(); i++) {
            if(left.charAt(i) == '+') {
                boolean old = nextPlus;
                nextPlus = true;
                if(i != 0) {
                    String word = left.substring(begin, i);
                    splitWords(word, old);
                }
                begin = i + 1;
            } else if (left.charAt(i) == '-') {
                boolean old = nextPlus;
                nextPlus = false;
                if(i != 0) {
                    String word = left.substring(begin, i);
                    splitWords(word, old);
                }
                begin = i + 1;
            }
        }
        String word = left.substring(begin);
        splitWords(word, nextPlus);

        for(String name : names) {
            System.out.print(name + " ");
        }
        System.out.println();
        for(Integer va : var) {
            System.out.print(va + " ");
        }
        System.out.println();
    }

    public void splitWords(String word, boolean plus) throws IllegalSyntax {
        String[] words = word.split("\\*");
//        System.out.print("WORDS : ");
//        for(int i = 0; i < words.length; i++) {
//            System.out.print(words[i] + " ");
//        }
//        System.out.println();
        if(words.length != 2) {
            throw new IllegalSyntax("Error split on *");
        }
        var.add(Integer.parseInt((plus ? "" : "-") + words[0]));
        names.add(words[1]);
    }

    public int[] getVar() {
        int[] va = new int[var.size()];
        int i = 0;
        for(Integer elt : var) {
            va[i] = elt;
            i++;
        }
        return va;
    }

    public String[] getNames() {
        String[] na = new String[names.size()];
        names.toArray(na);
        return na;
    }

    public int getC() {
        return c;
    }
}