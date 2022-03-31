
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Point;

/** Class defining a Circle shape
 * @author Jon Henshaw
 * @version 10/20/2019
 */
 public class Circle extends AbstractShape{
    private int r;
    
    /**
    * Constructs a Circle centered upon the Point provided, with radius provided.
    * @param center Point representing the center of the desired Circle
    * @param radius int representing the radius of the desired Circle.
    * @throws IllegalArgumentException when radius is <= 0, or center == null.
    */
    Circle(Point center, int radius){
        if(radius <= 0){
            throw new IllegalArgumentException("Circle requires a positive radius");
        }
        if(center == null){
            throw new IllegalArgumentException("Circle requires an extant center");
        }
        moveUpperLeftTo(center.x - radius, center.y - radius);
        shapeType = "Circle";
        r = radius;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Shape copyOf(){
        Shape copy = new Circle(new Point(cX(),cY()),r);
        copy.setSelected(this.isSelected());
        copy.setColor(this.getColor());
        return copy;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean isOn(int x, int y){
        //pythagorean theorem to find distance from c to provided point
        //if distance from center is <= the radius
        if((x - cX()) * (x - cX()) + (y - cY()) * (y - cY()) <= r * r){
            return true;
        }
        return false;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void drawSelf(Graphics g){
        g.setColor(this.getColor());
        g.fillOval(corner.x, corner.y, r*2, r*2);

        if(this.isSelected()){
            g.setColor(new Color(255,255,255,255));
            g.drawArc(corner.x, corner.y, r*2, r*2,0,360);
            g.setColor(new Color(0,0,0,255));
            g.drawArc(corner.x-1, corner.y-1, r*2+2, r*2+2,0,360);
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void resize(int changeX, int changeY){
        if(r + Math.max(changeX, changeY) > 0){
        r += Math.max(changeX, changeY);
        //circles grow from their center point
        corner.x -= Math.max(changeX, changeY);
        corner.y -= Math.max(changeX, changeY);
        }
    }

    //private methods to calculate center point coordinates from corner coordinates
    private int cX(){
        return corner.x + r;
    }
    private int cY(){
        return corner.y + r;
    }
    

}