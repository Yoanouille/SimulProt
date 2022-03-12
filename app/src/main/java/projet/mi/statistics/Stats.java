package projet.mi.statistics;

import projet.mi.model.Population;
import projet.mi.model.Protocol;

import java.util.Arrays;

public class Stats {
    private Protocol protocol;

    public Stats(Protocol p){
        protocol = p;
    }

    public Stat computeStats(int popSize, int maxIterations, int n){
        int sum = 0;
        int[] values = new int[n];
        for(int i = 0; i < n; i++){
            Population pop = new Population(protocol, popSize);
            int iter = pop.simulate(maxIterations);
            values[i] = iter;
            sum += iter;
        }
        Arrays.sort(values);
        return new Stat(popSize, values[0], values[n-1], ((double)sum)/((double)n), values[n/2]);
    }

    public Stat[] statsForDifferentSizes(int minPopSize, int maxPopSize, int step, int maxIterations, int n){
        Stat[] stats = new Stat[(maxPopSize-minPopSize)/step+1];
        int i = 0;
        for(int size = minPopSize; size < maxPopSize; size += step){
            stats[i] = computeStats(Math.min(size, maxPopSize), maxIterations, n);
            System.out.println(stats[i]);
            i++;
        }
        stats[stats.length-1] = computeStats(maxPopSize, maxIterations, n);
        System.out.println(stats[i]);
        return stats;
    }
}
