package projet.mi.animation;

import javafx.animation.AnimationTimer;
import javafx.geometry.VPos;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.TextAlignment;
import projet.mi.graph.Configuration;
import projet.mi.graph.Graph;
import projet.mi.model.Population;
import projet.mi.model.Rule;
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
    private boolean drawNames;
    private Graph graph;

    public Animation(Population pop, double width, double height, GraphicsContext ctx){
        this.pop = pop;
        this.circles = new Circle[pop.size()];
        this.ctx = ctx;
        Circle.setRadius(Math.sqrt(width*height/(10*3.14*this.circles.length))); // 10 is the percentage of the canvas filled with circles
        double d = 2*Circle.getRadius();
        double spd = Circle.getRadius()*5;
        for(int i = 0; i < this.circles.length; i++){
            double a = Math.random()*Math.PI*2;
            this.circles[i] = new Circle(Math.random()*(width-d)+Circle.getRadius(), Math.random()*(height-d)+Circle.getRadius(), spd*Math.cos(a), spd*Math.sin(a));
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
        drawNames = false;
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

    public void setDrawNames(boolean b){
        this.drawNames = b;
    }

    public void start() {
        this.anim.lastUpdateTime = System.nanoTime();
        this.anim.start();
    }

    public void stop() {
        this.anim.stop();
    }

    public void speed() {
        this.simulationSpeed *= 2;
    }

    public void slow() {
        if(this.simulationSpeed > 1) this.simulationSpeed /= 2;
    }

    public int getSimulationSpeed(){
        return this.simulationSpeed;
    }

    public boolean update(double dt){
        for (Circle circle : this.circles) {
            circle.move(dt);
        }
        boolean collisionOccurred = false;
        for(int i = 0; i < this.circles.length; i++) {
            int j = this.circles[i].collisions(this.circles);
            if (j != -1) {
                this.pop.interact(i, j);
                //System.out.println(this.pop);
                collisionOccurred = true;
            }
        }
        boundCollisions();
        return collisionOccurred;
    }

    public void drawBorder(GraphicsContext ctx, Color c) {
        ctx.setLineWidth(5);
        ctx.beginPath();
        ctx.setStroke(c);
        ctx.moveTo(0,0);
        ctx.lineTo(this.width, 0);
        ctx.lineTo(this.width, this.height);
        ctx.lineTo(0, this.height);
        ctx.closePath();
        ctx.stroke();
    }

    public void drawLegend(GraphicsContext ctx){
        ctx.clearRect(0, 0, ctx.getCanvas().getWidth(), ctx.getCanvas().getHeight());
        final double ecart = 50;
        //double size = this.height/pop.getProtocol().getStates().size();
        double x = 40;
        //double y = size/2;
        double y = ecart / 2;
        ctx.setFont(new Font(ctx.getFont().getName(), 30));
        ctx.setTextAlign(TextAlignment.LEFT);
        ctx.setTextBaseline(VPos.CENTER);
        ctx.getCanvas().setHeight(ecart * pop.getProtocol().getStates().size() + ecart);
        for(Map.Entry<State, Color> e : this.colorMap.entrySet()){
            State s = e.getKey();
            Color c = e.getValue();

            if(!colorMode) ctx.setFill(c);
            else ctx.setFill(getColor(s));

            ctx.fillOval(x-15, y-15, 30, 30);

            ctx.setFill(Color.BLACK);
            ctx.fillText(s.getState(), x+35, y);

            //y += size;
            y += ecart;
        }
    }

    private void updateFill(Color c, State s, GraphicsContext ctx) {
        if(!colorMode) ctx.setFill(c);
        else ctx.setFill(getColor(s));
    }

    public void drawRuleColors(GraphicsContext ctx) {
        ctx.clearRect(0, 0, ctx.getCanvas().getWidth(), ctx.getCanvas().getHeight());
        final double ecart = 50;
        double y = ecart / 2;
        ctx.setFont(new Font(ctx.getFont().getName(), 15));
        ctx.setTextBaseline(VPos.CENTER);
        Rule[] rules = pop.getProtocol().getRules();
        ctx.getCanvas().setHeight(ecart * rules.length + ecart);
        ctx.setTextAlign(TextAlignment.CENTER);
        ctx.setTextBaseline(VPos.CENTER);
        ctx.setFont(new Font(ctx.getFont().getName(), 16));
        for(int i = 0; i < rules.length; i++) {
            State[] rule = rules[i].getRule();
            double x = 30;

            updateFill(colorMap.get(rule[0]), rule[0], ctx);
            ctx.fillOval(x-15, y-15, 30, 30);
            ctx.setFill(Color.BLACK);
            ctx.fillText(rule[0].getState(), x, y);
            x += 35;

            updateFill(colorMap.get(rule[1]), rule[1], ctx);
            ctx.fillOval(x-15, y-15, 30, 30);
            ctx.setFill(Color.BLACK);
            ctx.fillText(rule[1].getState(), x, y);
            x += 35;

            ctx.setFill(Color.BLACK);
            ctx.fillText("=>", x, y);
            x += 35;

            updateFill(colorMap.get(rule[2]), rule[2], ctx);
            ctx.fillOval(x-15, y-15, 30, 30);
            ctx.setFill(Color.BLACK);
            ctx.fillText(rule[2].getState(), x, y);
            x += 35;

            updateFill(colorMap.get(rule[3]), rule[3], ctx);
            ctx.fillOval(x-15, y-15, 30, 30);
            ctx.setFill(Color.BLACK);
            ctx.fillText(rule[3].getState(), x, y);

            y += ecart;
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
            circles[i].draw(ctx, this.drawNames ? s.getState() : "");
        }

        if(pop.allYes()) drawBorder(ctx, Color.GREEN);
        else if(pop.allNo()) drawBorder(ctx, Color.RED);
        else drawBorder(ctx, Color.BLACK);
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
            //boolean collisionOccurred = false;
            for(int i = 0; i < simulationSpeed; i++){
                update(dt);
            }
            /*if(collisionOccurred && (pop.allNo() || pop.allYes())){
                Configuration conf = pop.getConfiguration();
                //if(graph == null){
                    graph = new Graph(pop.getProtocol(), conf);
                //}
                if(graph.isFinal(conf)){
                    this.stop();
                    System.out.println("STOP!");
                }
                System.out.println(graph);
            }*/
            draw(ctx);
            lastUpdateTime = now;
        }
    }

}
