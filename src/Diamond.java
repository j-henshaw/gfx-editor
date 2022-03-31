
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Point;

/** Class defining a Diamond shape
 * @author Jon Henshaw
 * @version 10/20/2019
 */
public class Diamond extends AbstractShape{
    private int h; //longest height
    private int w; //longest width
    
    /**
     * Constructs a new Diamond.
     * @param apex Point representing the topmost tip of the diamond
     * @param height int representing the height of the diamond.
     * Must be >= 10 and even.
     * @param width int representing the width of the diamond.
     * Must be >= 10 and even.
     * @throws IllegalArgumentException if fed a null Pointer, or if height or width are odd and/or
     * less than 10.
     */
    Diamond(Point apex, int height, int width){
        if(height < 10 || width < 10 || height % 2 != 0 || width % 2 != 0){
            throw new IllegalArgumentException("Height and width must both " + 
                                               "be >= 10, and even");
        }
        if(apex == null){
            throw new IllegalArgumentException("Diamond requires an extant apex");
        }
        shapeType = "Diamond";
        h = height;
        w = width;
        moveUpperLeftTo(apex.x - w/2, apex.y);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean isOn(int x, int y){
        if(lineUL(x) <= y &&
           lineUR(x) <= y &&
           lineBL(x) >= y &&
           lineBR(x) >= y){
            return true;
        }
        return false;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Shape copyOf(){
        Shape copy = new Diamond(new Point(aX(),aY()),h,w);
        copy.setSelected(this.isSelected());
        copy.setColor(this.getColor());
        return copy;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void drawSelf(Graphics g){
        g.setColor(this.getColor());
        int[] Xpoints = {aX(), aX()+w/2, aX(), aX()-w/2};
        int[] Ypoints = {aY(), aY()+h/2, aY()+h, aY()+h/2};
        g.fillPolygon(Xpoints,Ypoints,4);

        if(this.isSelected()){
            g.setColor(new Color(255,255,255,255));
            Xpoints[1] += 1;
            Xpoints[3] -= 1;
            Ypoints[0] -= 1;
            Ypoints[2] += 1;
            g.drawPolygon(Xpoints,Ypoints,4);
            
            g.setColor(new Color(0,0,0,255));
            Xpoints[1] += 1;
            Xpoints[3] -= 1;
            Ypoints[0] -= 1;
            Ypoints[2] += 1;
            g.drawPolygon(Xpoints,Ypoints,4); 
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void resize(int changeX, int changeY){
        int evenX = 2 * (changeX/2);
        int evenY = 2 * (changeY/2);

        if(w + evenX >= 10){
        w += evenX;
        //Diamonds are anchored from the apex
        corner.x -= evenX/2;
        }

        if(h + evenY >= 10){
        h += evenY;
        }
    }

    //Private methods define functions for the lines that comprise
    //the sides of the diamond
    private double lineUL(int x){
        return -slope() * (x - aX()) + aY();
    }
    private double lineUR(int x){
        return slope() * (x - aX()) + aY();
    }
    private double lineBL(int x){
        return slope() * (x - aX()) + aY() + h;
    }
    private double lineBR(int x){
        return -slope() * (x - aX()) + aY() + h;
    }

    //private functions to define apex coordinates
    private int aX(){
        return corner.x + w/2;
    }
    private int aY(){
        return corner.y;
    }

    //private function returning the |slope| based on h/w
    private double slope(){
        return 1.0 * h / w;
    }

}