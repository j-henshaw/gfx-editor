
import java.awt.Color;
import java.awt.Point;
import java.util.Random;

/** Abstract Class for various gemoetric shapes
 * @author Jon Henshaw
 * @version 10/20/2019
 */
public abstract class AbstractShape implements Shape{
    protected Point corner; //Upper-left corner of this shape's bounding box
    private boolean selected;
    protected String shapeType;
    private Color color;
    
    AbstractShape(int x, int y){
        corner = new Point(x,y);
        setSelected(false);
        Random rand = new Random();
        setColor(new Color(rand.nextInt(256), rand.nextInt(256), rand.nextInt(256), rand.nextInt(256)));
        shapeType = "Shape";
    }
    AbstractShape(){
        this(0,0);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean isSelected(){
        return selected;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void setSelected(boolean b){
        selected = b;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void setColor(Color color){
        if(color == null){
            throw new IllegalArgumentException("Null Color...unacceptable");
        }
        this.color = color;
    }
    
    /**
     * {@inheritDoc}
     */
    @Override
    public Color getColor(){
        return color;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void shiftUpperLeftBy(int deltaX, int deltaY){
        corner.x += deltaX;
        corner.y += deltaY;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void moveUpperLeftTo(int newX, int newY){
        corner.x = newX;
        corner.y = newY;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String toString(){
        String result = "";
        if(this.isSelected()){
            result += "[Selected] ";
        }
        String position = this.corner.toString().substring(14, this.corner.toString().length());
        String myColor = color.toString().substring(14, color.toString().length());
        myColor = myColor.substring(0,myColor.length()-1) + ",a=" + color.getAlpha() + "]";
        result = result + shapeType + ". Upper-left corner:" + position + ", Color:" + myColor;
        return result;
    }
    
    /**
     * {@inheritDoc}}
     */
    @Override
    public Point getUpperLeft(){
        return corner;
    }

    

}