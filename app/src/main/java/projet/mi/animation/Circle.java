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

    public int update(Circle[] circles, double dt){
        this.pos = this.pos.add(this.vel.multiply(dt));
        //TODO collisions, retourne l'index de celui avec lequel il a collisionné
        return -1;
    }

    public void draw(GraphicsContext ctx){
        double d = 2*r;
        ctx.setFill(new Color(0,0,0,1));
        ctx.fillOval(this.pos.getX()-r, this.pos.getY()-r, d, d);
    }
}
