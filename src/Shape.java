
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Point;

/** Interface for various geometric shapes
 * @author Jon Henshaw
 * @version 10/20/2019
 */
public interface Shape{
    
    /**
     * Determines whether the provided point coordinates lie within this shape.
     * @param x x-coordinate of provided point
     * @param y y-coordinate of provided point
     * @return boolean describing whether this shape encompasses provided point
     */
    public boolean isOn(int x, int y);

    /**
     * Determines whether the user has selected this element or not.
     * @return boolean describing whether this object is selected
     */
    public boolean isSelected();

    /**
     * Selects and/or deselects this object.
     * @param b boolean value used to set whether this object is selected or not.
     */
    public void setSelected(boolean b);

    /**
     * Changes the color of this object to the desired color provided.
     * @param color Color object from Java.awt that describes this shape's color
     * @throws IllegalArgumentException if fed a null Color object
     */
    public void setColor(Color color);

    /**
     * Returns a Color object from Java.awt describing this shape's color
     * @return Color object describing this shape's color
     */
    public Color getColor();

    /**
     * Moves the upper-left corner of this object by the provided x and y values
     * @param deltaX int representing desired motion in x-direction
     * @param deltaY int representing desired motion on y-direction
     */
    public void shiftUpperLeftBy(int deltaX, int deltaY);

    /**
     * Moves the upper-left corner of the bounding box for this object to
     * the provided location.
     * @param newX int representing this object's desired new x-coordinate
     * @param newY int representing this object's desired new y-coordinate
     */
    public void moveUpperLeftTo(int newX, int newY);
    
    /**
     * Provides a String representation of this object's current state
     * @return String representing this object's current state
     */
    public String toString();

    /**
     * Returns a new Shape with the exact same type and parameters of this shape
     * @return Shape that is a unique copy of the original Shape
     */
    public Shape copyOf();

    /**
     * Method used in conjunction with paintComponent() to draw this shape
     * to the screen. Must include functionality to outline this shape
     * in the case it is selected.
     * @param Graphics The Graphics component onto which this shape is to be painted
     */
    public void drawSelf(Graphics g);

    /**
     * Resizes this shape in accordance with the change in x and y values.
     * @param changeX int detailing the desired change in size in th x-direction
     * @param changeY int detailing the desired change in size in the y-direction
     */
    public void resize(int changeX, int changeY);

    /**
     * Returns the upper-left corner of this shape's bounding box
     * @return Point representing the upper-left corner of this shape's bounding box
     */
    public Point getUpperLeft();
    
}