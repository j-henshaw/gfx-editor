
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Point;

/** Class defining an Arrow shape
 * @author Jon Henshaw
 * @version 10/20/2019
 */
public class Arrow extends AbstractShape{
    private int length; //Length of arrow from tip to tail
    private int shaftThickness; //Width of shaft
    private final double SLOPE = 0.75;
    
    /**
     * Constructs a new Arrow object
     * @param tip Point object determining the location of this arrow's tip
     * @param length int to set the length of the Arrow from tip to tail
     * @param shaftThickness int to set the thickness of the Arrow's shaft
     * @throws IllegalArgumentException if fed a null Point, negative/zero dimensions, or if the
     * arrow length is not at least 4x the shaftThickness
     */
    Arrow(Point tip, int length, int shaftThickness){
        if(tip == null){
            throw new IllegalArgumentException("Arrow requires an extant tip");
        }
        if(length <= 0 || shaftThickness <= 0){
            throw new IllegalArgumentException("Both length an shaftThickness" +
                                               " must be > 0");
        }
        if(length < 4 * shaftThickness){
            throw new IllegalArgumentException("Arrow length must be at least 4x " + 
                                               "as long as the shaftThickness");
        }
        shapeType = "Arrow";
        this.length = length;
        this.shaftThickness = shaftThickness;
        moveUpperLeftTo(tip.x - length, tip.y - arrowHeight()/2);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean isOn(int x, int y){
        //If within bounds of shaft
        if(x < tX() - arrowWidth() &&
           x >= tX() - length &&
           y <= tY() + shaftThickness/2 &&
           y >= tY() - shaftThickness/2){
               return true;
           }
        //If within bounds of arrow head
        if(x >= tX() - arrowWidth() &&
           arrowUB(x) <= y &&
           arrowLB(x) >= y){
            return true;
        }
       return false;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Shape copyOf(){
        Shape copy = new Arrow(new Point(tX(),tY()),length,shaftThickness);
        copy.setSelected(this.isSelected());
        copy.setColor(this.getColor());
        return copy;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void resize(int changeX, int changeY){
        if(length + changeX >= 4*shaftThickness){
            length += changeX;
        }
        if(shaftThickness + changeY > 0 &&
           4*(shaftThickness + changeY) <= length){
            shaftThickness += changeY;
            corner.y -= changeY;
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void drawSelf(Graphics g){
        g.setColor(this.getColor());
        //draw shaft
        g.fillRect(corner.x, corner.y + shaftThickness, length - 2*shaftThickness, shaftThickness);
        //draw head
        int[] headXs = new int[] {tX(), tX() - 2*shaftThickness, tX() - 2*shaftThickness};
        int[] headYs = new int[] {tY(), tY() - (int)(1.5*shaftThickness), tY() + (int)(1.5*shaftThickness)};
        g.fillPolygon(headXs,headYs, 3);

        if(this.isSelected()){
            g.setColor(new Color(255,255,255,255));
            
            //UL point
            int ULx = corner.x;
            int ULy = corner.y + shaftThickness;
            //CUM point
            int CUMx = tX() - 2*shaftThickness;
            int CUMy = tY() - (int)(0.5 * shaftThickness);
            //UM point
            int UMx = tX() - 2*shaftThickness;
            int UMy = tY() - (int)(1.5 * shaftThickness);
            //TIP point
                //just use tY() tX()
            //LM point
            int LMx = tX() - 2*shaftThickness;
            int LMy = tY() + (int)(1.5 * shaftThickness);
            //CLM point
            int CLMx = tX() - 2*shaftThickness;
            int CLMy = tY() + (int)(0.5 * shaftThickness);
            //LL point
            int LLx = corner.x;
            int LLy = tY() + (int)(0.5 * shaftThickness);

            //X-Points array for 1st outline
            int[] xPoints = new int[] {ULx, CUMx, UMx, tX(), LMx, CLMx, LLx, ULx};
            //Y-Points array for 1st outline
            int[] yPoints = new int[] {ULy, CUMy, UMy, tY(), LMy, CLMy, LLy, ULy};

            g.drawPolyline(xPoints, yPoints, 8);
            
            //do it again for 2nd outline
            g.setColor(new Color(0,0,0,255));

            //New vals for X-Points array for 2nd outline
            xPoints = new int[] {ULx-1, CUMx-1, UMx-1, tX()+1, LMx-1, CLMx-1, LLx-1, ULx-1};
            //New vals for Y-Points array for 2nd outline
            yPoints = new int[] {ULy-1, CUMy-1, UMy-1, tY(), LMy+1, CLMy+1, LLy+1, ULy-1};
            
            g.drawPolyline(xPoints, yPoints, 8);
        }
    }
    
    //Private methods returning the x/y coordinates of the arrow's tip
    private int tX(){
        return corner.x + length;
    }
    private int tY(){
        return corner.y + arrowHeight()/2;
    }

    //private methods defining equations to determine the bounds
    //of the arrow's two diagonal sides.
    private double arrowUB(int x){
        return SLOPE * (x - tX()) + tY();
    }
    private double arrowLB(int x){
        return -SLOPE * (x - tX()) + tY();
    }

    //Private methods returning the height and width of the arrow head
    private int arrowHeight(){
        return 3 * shaftThickness;
    }
    private int arrowWidth(){
        return 2 * shaftThickness;
    }
}