package projet.mi.animation;

import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;
import projet.mi.model.Population;

public class Animation {
    private Circle[] circles;
    private Population pop;
    private double height;
    private double width;

    public Animation(Population pop, double width, double height){
        this.pop = pop;
        this.circles = new Circle[pop.size()];
        Circle.setRadius(10); //TODO calculer le radius en fonction du nombre de cercles et de width et height
        for(int i = 0; i < this.circles.length; i++){
            double d = 2*Circle.getRadius();
            this.circles[i] = new Circle(Math.random()*(width-d)+Circle.getRadius(), Math.random()*(height-d)+Circle.getRadius(), 0, 0);
            //TODO générer les cerles de manière à ce qu'ils ne se superposent pas
        }
        this.width = width;
        this.height = height;
    }

    public void boundCollisions() {
        //TODO
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

    public void draw(GraphicsContext ctx){
        for(Circle c : this.circles){
            c.draw(ctx);
        }
    }
}
