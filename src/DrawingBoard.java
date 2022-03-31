import java.awt.Color;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.ListIterator;

/** Class defining a DrawingBoard upon which shapes may be placed and manipulated
 * @author Jon Henshaw
 * @version 10/20/2019
 */
public class DrawingBoard{
    ArrayList<Shape> stack; //index 0 is bottom layer
    ArrayList<BoardListener> viewers;

    /**
     * Constructs a new, empty DrawingBoard
     */
    DrawingBoard(){
        stack = new ArrayList<Shape>();
        viewers = new ArrayList<BoardListener>();
    }

    /**
     * Adds a new Shape to the DrawingBoard, and selects it
     * @param newShape Shape to be added to the DrawingBoard
     * @throws IllegalArgumentException if passed Null Shape
     */
    public void addShape(Shape newShape){
        if(newShape == null){
            throw new IllegalArgumentException("Non-extant shape may not be added");
        }
        deselectAll();
        newShape.setSelected(true);
        stack.add(newShape);
        notifyListeners();
    }

    /**
     * Adds a new view to the DrawingBoard's list of Listeners
     * @param newView BoardListener to be added to the DrawingBoard
     * @throws IllegalArgumentException if passed Null BoardListener
     */
    public void addListener(BoardListener newView){
        if(newView == null){
            throw new IllegalArgumentException("Non-extant listener may not be added");
        }
        viewers.add(newView);
        notifyListeners();
    }
    
    /**
     * Selects the topmost Shape at the target Point
     * @param targetX int x-coordinate at which selection is to occur
     * @param targetY int y-coordinate at which selection is to occur
     */
    public void selectAt(int targetX, int targetY){
        ListIterator<Shape> topDown = stack.listIterator(stack.size());
        boolean anythingHere = false;
        while(topDown.hasPrevious()){
            Shape test = (Shape) topDown.previous();
            if(test.isOn(targetX,targetY)){
                anythingHere = true;
                deselectAll();
                test.setSelected(true);
                Shape temp = test;
                stack.remove(test);
                stack.add(temp);
                notifyListeners();
                break;
            }
        }
        if(!anythingHere){
            deselectAll();
            notifyListeners();
        }
    }

    /**
     * Returns the object that is currently selected. Returns Null if no shape
     * is currently selected.
     * @return Shape that is currently selected, or null if none is.
     */
    public Shape currentlySelected(){
        if(stack.size() < 1) return null;
        if(stack.get(stack.size() - 1).isSelected()){
            return stack.get(stack.size() - 1);
        }
        return null;
    }

    /**
     * Deletes the currently selected Shape from the DrawingBoard.
     * The new topmost Shape will become selected.
     * @throws IllegalStateException if no object is currently selected
     */
    public void deleteSelected(){
        if(stack.size() < 1){
            throw new IllegalStateException("No item selected for deletion");
        }
        if(!stack.get(stack.size() - 1).isSelected()){
            throw new IllegalStateException("No item selected for deletion");
        }
        stack.remove(stack.size() - 1);
        deselectAll(); //just in case??
        if(stack.size() > 0){
            stack.get(stack.size()-1).setSelected(true);
        }
        notifyListeners();
    }
    /**
     * Deletes all shapes from the DrawingBoard, clearing it.
     */
    public void deleteAll(){
        stack = new ArrayList<Shape>();
        notifyListeners();
    }

    /**
     * Changes the color of the currently selected Shape.
     * @param color Color object from java.awt that represents desired new color
     * @throws IllegalStateException if no item is currently selected
     * @throws IllegalArgumentException for null Color
     */
    public void changeColorOfSelected(Color color){
        if(color == null){
            throw new IllegalArgumentException("Null Color...unacceptable");
        }
        if(stack.size() > 0 ){
            if(!stack.get(stack.size() - 1).isSelected()){
                throw new IllegalStateException("No item selected for color change");
            }
            stack.get(stack.size() - 1).setColor(color);
            notifyListeners();
        }
    }

    /**
     * Moves the selected shape by the provided values
     * @param changeInX int The amount of motion in the x-direction
     * @param changeInY int The amount of motion in the y-direction
     * @throws IllegalStateException if no item is currently selected
     */
    public void moveShapeBy(int changeInX, int changeInY){
        if(stack.size() == 0){
            throw new IllegalStateException("No item selected for movement");
        }
        if(!stack.get(stack.size() - 1).isSelected()){
            throw new IllegalStateException("No item selected for movement");
        }
        stack.get(stack.size() - 1).shiftUpperLeftBy(changeInX, changeInY);
        notifyListeners();
    }

    /**
     * Returns a copy of the list of shapes on this DrawingBoard
     * @return ArrayList<Shape> New copy of shapes currently on this DrawingBoard
     */
    public ArrayList<Shape> getShapes(){
        ArrayList<Shape> copy = new ArrayList<Shape>();
        if(stack.size() > 0){
            Iterator<Shape> stackSearch = stack.iterator();
            while(stackSearch.hasNext()){
                Shape temp = stackSearch.next();
                copy.add(temp);
            }
        }
        return copy;
    }

    //Private method to deselect all
    //Only the top item should ever be selected, but this is used as a failsafe
    private void deselectAll(){
        ListIterator<Shape> stackSearch = stack.listIterator();
        while(stackSearch.hasNext()){
            stackSearch.next().setSelected(false);
        }
    }

    /**
     * Notify listeners of this object of a change in the model.
     */
    public void notifyListeners(){
        for(BoardListener x : viewers){
            x.drawingBoardChange();
        }
    }

}