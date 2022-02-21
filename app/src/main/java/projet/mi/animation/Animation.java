package projet.mi.animation;

import javafx.animation.AnimationTimer;
import javafx.geometry.VPos;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import projet.mi.model.Population;
import projet.mi.model.State;

import java.util.HashMap;
import java.util.Map;

public class Animation {
    private Circle[] circles;
    private Population pop;
    private double height;
    private double width;
    private AnimationTime anim;
    private GraphicsContext ctx;
    private HashMap<State, Color> colorMap;
    private boolean colorMode;
    private int simulationSpeed;

    public Animation(Population pop, double width, double height, GraphicsContext ctx){
        this.pop = pop;
        this.circles = new Circle[pop.size()];
        this.ctx = ctx;
        Circle.setRadius(Math.sqrt(width*height/(10*3.14*this.circles.length))); // 10 is the percentage of the canvas filled with circles
        double d = 2*Circle.getRadius();
        for(int i = 0; i < this.circles.length; i++){
            this.circles[i] = new Circle(Math.random()*(width-d)+Circle.getRadius(), Math.random()*(height-d)+Circle.getRadius(), 150, 150);
            boolean collision;
            do{
                collision = false;
                for(int j = 0; j < i; j++){
                    if(this.circles[i].collision(this.circles[j])){
                        this.circles[i].setPos(Math.random()*(width-d)+Circle.getRadius(), Math.random()*(height-d)+Circle.getRadius());
                        collision = true;
                    }
                }
            }while(collision);
        }
        this.width = width;
        this.height = height;
        anim = new AnimationTime();

        colorMode = false;
        colorMap = new HashMap<>();
        State[] states = new State[pop.getProtocol().getStates().size()];
        pop.getProtocol().getStates().toArray(states);
        for(int i = 0; i < states.length; i++) {
            colorMap.put(states[i], Color.hsb(i * 360.0 / states.length, 1.0, 1.0));
        }
        this.simulationSpeed = 1;
    }

    public void boundCollisions() {
        for(Circle c : this.circles){
            if(c.getPos().getX() - Circle.getRadius() < 0){
                c.getPos().setX(Circle.getRadius());
                c.getVel().setX(-c.getVel().getX());
            }
            if(c.getPos().getX() + Circle.getRadius() > this.width){
                c.getPos().setX(this.width - Circle.getRadius());
                c.getVel().setX(-c.getVel().getX());
            }
            if(c.getPos().getY() - Circle.getRadius() < 0){
                c.getPos().setY(Circle.getRadius());
                c.getVel().setY(-c.getVel().getY());
            }
            if(c.getPos().getY() + Circle.getRadius() > this.height){
                c.getPos().setY(this.height - Circle.getRadius());
                c.getVel().setY(-c.getVel().getY());
            }
        }
    }

    public void change() {
        this.colorMode = !this.colorMode;
    }

    public void start() {
        this.anim.lastUpdateTime = System.nanoTime();
        this.anim.start();
    }

    public void stop() {
        this.anim.stop();
    }

    public void speed() {
        this.simulationSpeed++;
    }

    public void slow() {
        if(this.simulationSpeed > 1) this.simulationSpeed--;
    }

    public void update(double dt){
        for (Circle circle : this.circles) {
            circle.move(dt);
        }
        for(int i = 0; i < this.circles.length; i++) {
            int j = this.circles[i].collisions(this.circles);
            if (j != -1) {
                this.pop.interact(i, j);
                System.out.println(this.pop);
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

    public void drawLegend(GraphicsContext ctx){
        ctx.clearRect(0, 0, ctx.getCanvas().getWidth(), ctx.getCanvas().getHeight());
        double size = this.height/pop.getProtocol().getStates().size();
        double x = 40;
        double y = size/2;
        ctx.setFont(new Font(ctx.getFont().getName(), 30));
        ctx.setTextBaseline(VPos.CENTER);
        for(Map.Entry<State, Color> e : this.colorMap.entrySet()){
            State s = e.getKey();
            Color c = e.getValue();

            if(!colorMode) ctx.setFill(c);
            else ctx.setFill(getColor(s));

            ctx.fillOval(x-15, y-15, 30, 30);

            ctx.setFill(Color.BLACK);
            ctx.fillText(s.getState(), x+35, y);

            y += size;
        }
    }

    private Color getColor(State s) {
        if(s.isIn(pop.getProtocol().getYes())) return Color.GREEN;
        else if(s.isIn(pop.getProtocol().getNo())) return Color.RED;
        else return Color.BLACK;
    }


    public void draw(GraphicsContext ctx){
        ctx.setFill(new Color(1,1,1,1));
        ctx.fillRect(0,0,this.width, this.height);

        for(int i = 0; i < circles.length; i++){
            State s = pop.getAgents()[i].getState();
            if(!colorMode) ctx.setFill(colorMap.get(s));
            else ctx.setFill(getColor(s));
            circles[i].draw(ctx);
        }

        drawBorder(ctx);
    }

    public class AnimationTime extends AnimationTimer {
        /**
         * The time since the game was last updated
         */
        private long lastUpdateTime = System.nanoTime();


        /**
         * Updates and draws the game
         * @param now the current time
         */
        public void handle(long now){
            double dt = ((double)(now-lastUpdateTime))/1000000000.; //delta time in seconds (now is in nanoseconds)
            for(int i = 0; i < simulationSpeed; i++){
                update(dt);
            }
            draw(ctx);
            lastUpdateTime = now;
        }
    }

}
