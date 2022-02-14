package projet.mi.animation;

import javafx.animation.AnimationTimer;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;
import projet.mi.model.Population;

public class Animation {
    private Circle[] circles;
    private Population pop;
    private double height;
    private double width;
    private AnimationTime anim;
    private GraphicsContext ctx;

    public Animation(Population pop, double width, double height, GraphicsContext ctx){
        this.pop = pop;
        this.circles = new Circle[pop.size()];
        this.ctx = ctx;
        Circle.setRadius(10); //TODO calculer le radius en fonction du nombre de cercles et de width et height
        for(int i = 0; i < this.circles.length; i++){
            double d = 2*Circle.getRadius();
            this.circles[i] = new Circle(Math.random()*(width-d)+Circle.getRadius(), Math.random()*(height-d)+Circle.getRadius(), 5, 5);
            //TODO générer les cerles de manière à ce qu'ils ne se superposent pas
        }
        this.width = width;
        this.height = height;
        anim = new AnimationTime();
    }

    public void boundCollisions() {
        //TODO
    }

    public void start() {
        this.anim.lastUpdateTime = System.nanoTime();
        this.anim.start();
    }

    public void stop() {
        this.anim.stop();
    }

    public void update(double dt){
        for(int i = 0; i < this.circles.length; i++){
            int j = this.circles[i].update(this.circles, dt);
            if(j != -1){
                this.pop.interact(i, j);
            }
        }
        boundCollisions();
    }


    public void drawBorder(GraphicsContext ctx) {
        ctx.beginPath();
        ctx.setStroke(Color.BLACK);
        ctx.moveTo(0,0);
        ctx.lineTo(this.width, 0);
        ctx.lineTo(this.width, this.height);
        ctx.lineTo(0, this.height);
        ctx.closePath();
        ctx.stroke();
    }


    public void draw(GraphicsContext ctx){
        ctx.setFill(new Color(1,1,1,1));
        ctx.fillRect(0,0,this.width, this.height);

        for(Circle c : this.circles){
            c.draw(ctx);
        }

        drawBorder(ctx);
    }

    public class AnimationTime extends AnimationTimer {
        /**
         * The time since the game was last updated
         */
        private long lastUpdateTime = System.nanoTime();
        /**
         * The current frame count
         */
        private int frame = 0;


        /**
         * Updates and draws the game
         * @param now the current time
         */
        public void handle(long now){
            frame++;
            if(frame % 2 == 0) {
                long dt = now-lastUpdateTime;
                update(dt);
                draw(ctx);
                lastUpdateTime = now;
                frame = 0;
            }
        }
    }

}
