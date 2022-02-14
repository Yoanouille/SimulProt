package projet.mi.animation;

/**
 * This class represents a 2 dimensional vector
 */
public class Vector {
    /**
     * The x coordinate of the vector
     */
    private double x;
    /**
     * The y coordinate of the vector
     */
    private double y;

    /**
     * The class constructor
     * @param x the x coordinate of the vector
     * @param y the y coordinate of the vector
     */
    public Vector(double x, double y){
        this.x = x;
        this.y = y;
    }

    /**
     * Returns the x coordinate of the vector
     * @return the x coordinate of the vector
     */
    public double getX(){
        return x;
    }

    /**
     * Returns the y coordinate of the vector
     * @return the y coordinate of the vector
     */
    public double getY(){
        return y;
    }

    /**
     * Sets the x coordinate of the vector
     * @param x the new x coordinate of the vector
     */
    public void setX(double x){
        this.x = x;
    }

    /**
     * Sets the y coordinate of the vector
     * @param y the new y coordinate of the vector
     */
    public void setY(double y){
        this.y = y;
    }

    /**
     * Returns a copy of the vector
     * @return a copy of the vector
     */
    public Vector copy() {
        return new Vector(this.x, this.y);
    }

    /**
     * Returns the vector's information as a String
     * @return the vector's information as a String
     */
    public String toString(){
        return "x: "+this.x+", y: "+this.y;
    }

    /**
     * Returns the sum of the vectors this and v ("this" is not modified)
     * @param v the vector to add to this
     * @return the sum af the vectors this and v
     */
    public Vector add(Vector v){
        return new Vector(x+v.x, y+v.y);
    }

    /**
     * Returns the difference between the vectors this and v ("this" is not modified)
     * @param v the vector to substract to this
     * @return the difference between the vectors this and v
     */
    public Vector sub(Vector v){
        return new Vector(x-v.x, y-v.y);
    }

    /**
     * Returns the multiplication of the vector this by a scalar m
     * @param m the scalar
     * @return the multiplication of the vector this by the scalar m
     */
    public Vector multiply(double m){
        return new Vector(x*m, y*m);
    }

    /**
     * Returns the dot product of the vectors this and v
     * @param v a vector
     * @return the dot product of the vectors this and v
     */
    public double dotProduct(Vector v){
        return x*v.x+y*v.y;
    }

    /**
     * Returns the length of the vector
     * @return the length of the vector
     */
    public double length(){
        return Math.sqrt(dotProduct(this));
    }

    /**
     * Returns a vector of length 1 with the same direction as itself
     * @return a vector of length 1 with the same direction as itself
     */
    public Vector normalize(){
        double l = length();
        if(l == 0) return this;
        return multiply(1./l);
    }

    /**
     * Returns the reflection of the vector this according to the vector n
     * @param n a vector of length 1
     * @return the reflection of the vector this according to the vector n
     */
    public Vector reflection(Vector n){
        //n multiplied by the dot product of this and n is the projection of this on the vector n (since ||n|| = 1)
        //so subtracting 2 times this vector to this means reversing the coordinate of this along the axis that goes in the direction of n, which is effectively a reflection
        return add(n.multiply(-2*dotProduct(n)));
    }

    /**
     * Returns a vector orthogonal to this
     * @return a vector orthogonal to this
     */
    public Vector getOrthogonal(){
        return new Vector(-y, x);
    }
}