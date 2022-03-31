import java.awt.Color;
import java.awt.Dimension;

import javax.swing.BorderFactory;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;

/**
 * This is a text-based view onto a DrawingBoard model, with text
 * representations of every shape and an updated shape total printed
 * to a TextArea every time there is a change to the model.
 * @author Jon Henshaw
 * @version 10/20/2019
 */
public class TextView extends JScrollPane implements BoardListener{
    private static final long serialVersionUID = 8783335819754494876L;
    private DrawingBoard model;
    private JTextArea display;

    /**
     * Constructs a Text View for a DrawingBoard model
     * @param board The DrawingBoard model which this view is to
     * use for updating its display
     * @throws IllegalArgumentException if fed a null DrawingBoard
    */
    TextView(DrawingBoard board){
        if(board == null){
            throw new IllegalArgumentException("Null model...unacceptable");
        }
        model = board;
        display = new JTextArea();
        display.setEditable(false);

        this.setViewportView(display);
        this.setPreferredSize(new Dimension(600, 150));
        this.setHorizontalScrollBarPolicy(HORIZONTAL_SCROLLBAR_NEVER);
        this.setVerticalScrollBarPolicy(VERTICAL_SCROLLBAR_ALWAYS);
        this.setViewportBorder(BorderFactory.createLineBorder(Color.black));

        model.addListener(this);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void drawingBoardChange(){
        int totalShapes = 0;
        String shapeRoster = "";
        boolean anythingSelected = false;
        
        for(Shape shape : model.getShapes()){
            ++totalShapes;
            shapeRoster = shapeRoster + "  —" + shape + "\n";
            if(shape.isSelected()){
                anythingSelected = true;
            }
        }
        
        String result = "Total number of shapes: " + totalShapes;
        if(!anythingSelected){
            result += " (No item currently selected)\n";
        } else {
            result += "\n";
        }
        result += "Shape Roster (bottom-to-top): \n" + shapeRoster;
        
        display.setText(result);
    }
    
}