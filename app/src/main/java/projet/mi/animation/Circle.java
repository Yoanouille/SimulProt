package projet.mi.animation;

import javafx.geometry.VPos;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.TextAlignment;

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

        //randomize velocities a bit
        double l = this.vel.length();
        this.vel = this.vel.add(new Vector(Math.random()*30-15, Math.random()*30-15)).normalize().multiply(l);
        c.vel = c.vel.add(new Vector(Math.random()*30-15, Math.random()*30-15)).normalize().multiply(l);
    }

    public void move(double dt){
        this.pos = this.pos.add(this.vel.multiply(dt));
        //this.pos = this.pos.add(this.vel);
    }

    public int collisions(Circle[] circles){
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

    public void speed() {
        this.vel = this.vel.multiply(2.0);
    }

    public void slow() {
        this.vel = this.vel.multiply(0.5);
    }

    public void draw(GraphicsContext ctx, String name){
        double d = 2*r;
        ctx.fillOval(this.pos.getX()-r, this.pos.getY()-r, d, d);
        ctx.setTextAlign(TextAlignment.CENTER);
        ctx.setTextBaseline(VPos.CENTER);
        ctx.setFont(new Font(ctx.getFont().getName(), r));
        ctx.setFill(Color.BLACK);
        ctx.fillText(name, this.pos.getX(), this.pos.getY());
    }
}
