package projet.mi.statistics;

import javafx.animation.AnimationTimer;
import javafx.application.Platform;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;
import projet.mi.model.Protocol;

import java.util.LinkedList;

public class Chart {
    private Canvas canvas;
    private Stats stats;
    private Stat[] st;

    public Chart(Canvas c, Protocol p) {
        this.canvas = c;
        this.stats = new Stats(p);

        initialize();
    }

    public void drawAxes(GraphicsContext ctx, double x, double y, double width, double height) {
        ctx.setStroke(Color.BLACK);
        ctx.setLineWidth(7);

        ctx.beginPath();
        ctx.moveTo(x,y);
        ctx.lineTo(x, y + height);
        ctx.lineTo(x + width, y + height);
        ctx.stroke();
    }

    public void initialize() {
        int minPopSize = 2;
        int maxPopSize = 10;
        int step = 1;

        new Thread(() -> {
            stats.statsForDifferentSizes(2, 10,1,10000, 10000, (s) -> {
                Platform.runLater(() -> draw(canvas.getGraphicsContext2D(), s));
            });
        }).start();
    }

    public void drawGraph(GraphicsContext ctx, double x, double y, double width, double height, LinkedList<Stat> st) {

        //On va draw l'avg pour le moment
        drawAxes(ctx, x, y, width, height);

        if(st != null) {
            double maxY = stats.getMaxAvg();
            double maxX = st.size() - 1;


            ctx.setStroke(Color.BLUE);
            ctx.setLineWidth(3);
            ctx.beginPath();
            double x0 = (0 / maxX) * width + x;
            double y0 = (1.0 - st.get(0).getAvg() / maxY) * height + y;
            ctx.moveTo(x0, y0);
            for (int i = 1; i < st.size(); i++) {
                double xp = (i / maxX) * width + x;
                double yp = (1.0 - st.get(i).getAvg() / maxY) * height + y;
                ctx.lineTo(xp, yp);
            }
            ctx.stroke();
        }
    }

    public void draw(GraphicsContext ctx, LinkedList<Stat> st) {
        ctx.setFill(Color.WHITE);
        ctx.clearRect(0,0,canvas.getWidth(),canvas.getHeight());

        drawGraph(ctx, 50, 50, canvas.getWidth() - 100, canvas.getHeight() - 100, st);
    }
}
