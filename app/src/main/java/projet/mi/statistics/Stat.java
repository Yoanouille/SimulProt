package projet.mi.statistics;

public class Stat {
    private int size;
    private int min;
    private int max;
    private double avg;
    private int median;

    public Stat(int size, int min, int max, double avg, int median){
        this.size = size;
        this.min = min;
        this.max = max;
        this.avg = avg;
        this.median = median;
    }

    @Override
    public String toString() {
        return "size:" + size + ", min:" + min + ", max:" + max + ", average:" + avg + ", median:" + median;
    }

    public double getAvg() {
        return avg;
    }

    public int getMax() {
        return max;
    }

    public int getMin() {
        return min;
    }

    public int getMedian() {
        return median;
    }

    public int getSize() {
        return size;
    }
}
