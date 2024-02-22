package projet.mi.statistics;

import projet.mi.model.Population;
import projet.mi.model.Protocol;

import java.util.Arrays;
import java.util.LinkedList;
import java.util.function.Consumer;
import java.util.function.Supplier;

public class Stats {
    private Protocol protocol;
    private LinkedList<Stat> stats;

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

    public LinkedList<Stat> statsForDifferentSizes(int minPopSize, int maxPopSize, int step, int maxIterations, int n, Consumer<LinkedList<Stat>> function){
        stats = new LinkedList<>();
        int i = 0;
        for(int size = minPopSize; size < maxPopSize; size += step){
            if(Thread.currentThread().isInterrupted()) return stats;
            stats.add(computeStats(Math.min(size, maxPopSize), maxIterations, n));
            System.out.println(stats.get(i));
            i++;
            function.accept(stats);
        }
        stats.add(computeStats(maxPopSize, maxIterations, n));
        System.out.println(stats.get(i));
        function.accept(stats);
        return stats;
    }

    public double getMaxAvg() {
        double max = 0;
        for(int i = 0; i < stats.size(); i++) {
            if(stats.get(i).getAvg() > max) max = stats.get(i).getAvg();
        }
        return max;
    }

    public double getMaxValues(boolean avg, boolean min, boolean max, boolean median) {
        double max_value = 0;
        for(int i = 0; i < stats.size(); i++) {
            if(avg && stats.get(i).getAvg() > max_value) max_value = stats.get(i).getAvg();
            if(min && stats.get(i).getMin() > max_value) max_value = stats.get(i).getMin();
            if(max && stats.get(i).getMax() > max_value) max_value = stats.get(i).getMax();
            if(median && stats.get(i).getMedian() > max_value) max_value = stats.get(i).getMedian();
        }
        return max_value;
    }
}
