package projet.mi.statistics;

import javafx.animation.AnimationTimer;
import javafx.application.Platform;
import javafx.geometry.Pos;
import javafx.geometry.VPos;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.TextAlignment;
import projet.mi.model.Protocol;

import java.util.LinkedList;

public class Chart {
    private Canvas canvas;
    private Stats stats;
    private Thread t;
    private LinkedList<Stat> st;

    private boolean avg;
    private boolean min;
    private boolean max;
    private boolean median;

    private int maxIteration;
    private int maxPopSize;
    private int minPopSize;
    private int nbSimu;
    private int step;

    public Chart(Canvas c, Protocol p, boolean avg, boolean min, boolean max, boolean median, int maxIteration, int maxPopSize, int minPopSize, int nbSimu, int step) {
        this.canvas = c;
        this.stats = new Stats(p);
        this.avg = avg;
        this.min = min;
        this.max = max;
        this.median = median;
        this.maxIteration = maxIteration;
        this.maxPopSize = maxPopSize;
        this.minPopSize = minPopSize;
        this.nbSimu = nbSimu;
        this.step = step;
        initialize();
    }

    public void setAvg(boolean avg) {
        this.avg = avg;
        draw(canvas.getGraphicsContext2D(), st);
    }

    public void setMax(boolean max) {
        this.max = max;
        draw(canvas.getGraphicsContext2D(), st);
    }

    public void setMin(boolean min) {
        this.min = min;
        draw(canvas.getGraphicsContext2D(), st);
    }

    public void setMedian(boolean median) {
        this.median = median;
        draw(canvas.getGraphicsContext2D(), st);
    }

    public void drawHorizGradLine(GraphicsContext ctx, double x, double y, double size, double label, boolean visible) {
        ctx.beginPath();
        ctx.moveTo(x - size, y);
        ctx.lineTo(x, y);
        ctx.stroke();
        ctx.setTextAlign(TextAlignment.RIGHT);
        if(visible) {
            ctx.setFill(Color.BLACK);
            ctx.fillText(String.valueOf((int) label), x - size - 8, y);
        }
    }

    public void drawVertGradLine(GraphicsContext ctx, double x, double y, double size, double label, boolean visible) {
        ctx.beginPath();
        ctx.moveTo(x, y);
        ctx.lineTo(x, y + size);
        ctx.stroke();
        ctx.setTextAlign(TextAlignment.CENTER);
        if(visible) {
            ctx.setFill(Color.BLACK);
            ctx.fillText(String.valueOf((int) label), x, y + size + 10);
        }
    }

    public void drawGraduation(GraphicsContext ctx, double x, double y, double width, double height, double nbGradX, double nbGradY, double maxX, double maxY) {
        //Horiz Grad
        double l =0.5*Math.pow(10, Math.floor(Math.log10(Math.max(1, maxX-10))));
        double step = (Math.ceil(Math.ceil(maxX/nbGradX)/l)*l);
        for(double i = 0; i < maxX; i += step) {
            drawVertGradLine(ctx, x + i*width/maxX, y + height, 15, i, true);
        }
        //Vert Grad
        l =0.5*Math.pow(10, Math.floor(Math.log10(Math.max(1, maxY-10))));
        step = (Math.ceil(Math.ceil(maxY/nbGradY)/l)*l);
        for(double i = 0; i < maxY; i += step) {
            drawHorizGradLine(ctx,  x, y + height - i*height/maxY,15, i, true);
        }
    }

    public void drawAxes(GraphicsContext ctx, double x, double y, double width, double height, double maxX, double maxY) {
        ctx.setStroke(Color.BLACK);
        ctx.setLineWidth(1);

        ctx.beginPath();
        ctx.moveTo(x,y);
        ctx.lineTo(x, y + height);
        ctx.lineTo(x + width, y + height);
        ctx.stroke();

        ctx.setStroke(Color.BLACK);
        ctx.setLineWidth(1);
        drawGraduation(ctx, x, y, width, height, 10,10, maxX, maxY);
    }

    public void initialize() {

        t = new Thread(() -> {
            stats.statsForDifferentSizes(minPopSize, maxPopSize,step,maxIteration, nbSimu, (s) -> {
                Platform.runLater(() -> draw(canvas.getGraphicsContext2D(), s));
            });
        });
        t.start();
    }

    public void stop() {
        t.interrupt();
    }

    public double getValue(int i, String type, LinkedList<Stat> st) {
        switch (type) {
            case "avg": return st.get(i).getAvg();
            case "min": return st.get(i).getMin();
            case "max": return st.get(i).getMax();
            case "median": return st.get(i).getMedian();
            default: return 0;
        }
    }

    public void drawOneGraph(GraphicsContext ctx, double x, double y, double width, double height, LinkedList<Stat> st, double maxX, double maxY, String type) {
        ctx.beginPath();
        ctx.moveTo(x, y+height);
        for (int i = 0; i < st.size(); i++) {
            double xp = (st.get(i).getSize() / maxX) * width + x;
            double yp = (1.0 - getValue(i, type, st) / maxY) * height + y;
            ctx.lineTo(xp, yp);
        }
        ctx.stroke();
    }

    public void drawGraph(GraphicsContext ctx, double x, double y, double width, double height, LinkedList<Stat> st) {

        if(st != null) {
            //double maxY = stats.getMaxAvg();
            double maxY = stats.getMaxValues(avg, min, max, median);
            double maxX = st.getLast().getSize();

            drawAxes(ctx, x, y, width, height, maxX, maxY);

            ctx.setLineWidth(3);

            if(avg) {
                ctx.setStroke(Color.BLUE);
                drawOneGraph(ctx, x, y, width, height, st, maxX, maxY, "avg");
            }
            if(min) {
                ctx.setStroke(Color.GREEN);
                drawOneGraph(ctx, x, y, width, height, st, maxX, maxY, "min");
            }
            if(max) {
                ctx.setStroke(Color.RED);
                drawOneGraph(ctx, x, y, width, height, st, maxX, maxY, "max");
            }
            if(median) {
                ctx.setStroke(Color.PURPLE);
                drawOneGraph(ctx, x, y, width, height, st, maxX, maxY, "median");
            }


            ctx.setFont(new Font(ctx.getFont().getName(), 20));
            ctx.setFill(Color.BLACK);
            ctx.setTextBaseline(VPos.CENTER);
            ctx.setTextAlign(TextAlignment.CENTER);
            ctx.fillText("Population size", x+width/2, y+height+45);

            ctx.save();
            ctx.translate(x-80, y+height/2);
            ctx.rotate(-90);
            ctx.fillText("Iterations", 0, 0);
            ctx.restore();
        }
    }

    public void draw(GraphicsContext ctx, LinkedList<Stat> st) {
        this.st = st;

        ctx.setFont(new Font(ctx.getFont().getName(), 15));
        ctx.setTextBaseline(VPos.CENTER);

        ctx.clearRect(0,0,canvas.getWidth(),canvas.getHeight());

        drawGraph(ctx, 100, 15, canvas.getWidth() - 150, canvas.getHeight() - 80, st);
    }
}
