package projet.mi.animation;

import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;

public class Circle {
    private static double r;
    private Vector pos;
    private Vector vel;

    public Circle(Vector pos, Vector vel){
        this.pos = pos;
        this.vel = vel;
    }

    public Circle(double x, double y, double vx, double vy){
        this.pos = new Vector(x, y);
        this.vel = new Vector(vx, vy);
    }

    public static void setRadius(double r){
        Circle.r = r;
    }

    public static double getRadius(){
        return Circle.r;
    }

    public Vector getPos(){
        return this.pos;
    }

    public void setPos(double x, double y){
        this.pos = new Vector(x, y);
    }

    public Vector getVel(){
        return this.vel;
    }

    public boolean collision(Circle c){
        return this.pos.sub(c.pos).length() < Circle.r*2;
    }

    public void collisionResponse(Circle c){
        Vector diff = c.pos.sub(this.pos);
        Vector mid = this.pos.add(diff.multiply(1./2.));
        Vector move = diff.normalize().multiply(Circle.r);

        //separate the circles
        this.pos = mid.sub(move);
        c.pos = mid.add(move);

        //update the velocities
        Vector tmp = this.vel;
        this.vel = c.vel;
        c.vel = tmp;
    }

    public int update(Circle[] circles, double dt){
        //this.pos = this.pos.add(this.vel.multiply(dt));
        this.pos = this.pos.add(this.vel);
        int colIndex = -1;
        for(int i = 0; i < circles.length; i++){
            if(circles[i] != this){
                if(this.collision(circles[i])){
                    this.collisionResponse(circles[i]);
                    colIndex = i;
                }
            }
        }
        return colIndex;
    }

    public void draw(GraphicsContext ctx){
        double d = 2*r;
        ctx.setFill(new Color(0,0,0,1));
        ctx.fillOval(this.pos.getX()-r, this.pos.getY()-r, d, d);
    }
}
