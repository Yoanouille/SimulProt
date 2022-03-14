package projet.mi.statistics;

import javafx.animation.AnimationTimer;
import javafx.application.Platform;
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

    private boolean avg = false;
    private boolean min = false;
    private boolean max = false;
    private boolean median = false;

    public Chart(Canvas c, Protocol p) {
        this.canvas = c;
        this.stats = new Stats(p);

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

    public void drawHorizGradLine(GraphicsContext ctx, double x, double y, double size, double label) {
        ctx.beginPath();
        ctx.moveTo(x - size / 2, y);
        ctx.lineTo(x + size / 2, y);
        ctx.stroke();
        ctx.setFill(Color.GREEN);
        ctx.fillText(String.valueOf((int)label), x - size, y);
    }

    public void drawVertGradLine(GraphicsContext ctx, double x, double y, double size, double label) {
        System.out.println(x + " " + y);
        ctx.beginPath();
        ctx.moveTo(x, y - size / 2);
        ctx.lineTo(x, y + size / 2);
        ctx.stroke();
        ctx.setFill(Color.GREEN);
        ctx.fillText(String.valueOf((int)label), x, y + size/2 + 3);
    }

    public void drawGraduation(GraphicsContext ctx, double x, double y, double width, double height, double nbGradX, double nbGradY, double maxX, double maxY) {
        //Horiz Grad
        for(int i = 0; i < nbGradX; i++) {
            drawVertGradLine(ctx, x + (i * width / nbGradX), y + height, 30, i * maxX / nbGradX);
        }
        //Vert Grad
        for(int i = 0; i < nbGradY; i++) {
            drawHorizGradLine(ctx,  x, y + height - (i * height / nbGradY),30, i * maxY / nbGradY);
        }
    }

    public void drawAxes(GraphicsContext ctx, double x, double y, double width, double height, double maxX, double maxY) {
        ctx.setStroke(Color.BLACK);
        ctx.setLineWidth(7);

        ctx.beginPath();
        ctx.moveTo(x,y);
        ctx.lineTo(x, y + height);
        ctx.lineTo(x + width, y + height);
        ctx.stroke();

        ctx.setStroke(Color.BLACK);
        ctx.setLineWidth(3);
        drawGraduation(ctx, x, y, width, height, maxX, 10, maxX, maxY);
    }

    public void initialize() {
        int minPopSize = 2;
        int maxPopSize = 10;
        int step = 1;

        t = new Thread(() -> {
            stats.statsForDifferentSizes(2, 100,1,10000, 100, (s) -> {
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
        double x0 = (0 / maxX) * width + x;
        double y0 = (1.0 - st.get(0).getAvg() / maxY) * height + y;
        ctx.moveTo(x0, y0);
        for (int i = 1; i < st.size(); i++) {
            double xp = (i / maxX) * width + x;
            double yp = (1.0 - getValue(i, type, st) / maxY) * height + y;
            ctx.lineTo(xp, yp);
        }
        ctx.stroke();
    }

    public void drawGraph(GraphicsContext ctx, double x, double y, double width, double height, LinkedList<Stat> st) {

        if(st != null) {
            //double maxY = stats.getMaxAvg();
            double maxY = stats.getMaxValues(avg, min, max, median);
            double maxX = st.size() - 1;

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
        }
    }

    public void draw(GraphicsContext ctx, LinkedList<Stat> st) {
        this.st = st;

        ctx.setFont(new Font(ctx.getFont().getName(), 15));
        ctx.setTextAlign(TextAlignment.CENTER);
        ctx.setTextBaseline(VPos.CENTER);

        ctx.setFill(Color.WHITE);
        ctx.clearRect(0,0,canvas.getWidth(),canvas.getHeight());

        drawGraph(ctx, 75, 75, canvas.getWidth() - 150, canvas.getHeight() - 150, st);
    }
}
