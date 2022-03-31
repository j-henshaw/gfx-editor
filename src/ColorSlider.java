import java.awt.Color;
import java.util.Hashtable;

import javax.swing.JLabel;
import javax.swing.JSlider;

/**
 * This class defines a viewer/controller for a DrawingBoard model. It
 * extends JSlider, and is a slider ranging from 0-255, corresponding
 * to one of the RGBa color channels. This slider listens to a DrawingBoard
 * model, and will update to display the RGBa color space of any selected
 * object in the model. It can then be used to change that selected object's
 * color.
 * @author Jon Henshaw
 * @version 10/20/19
 */
public class ColorSlider extends JSlider implements BoardListener{
    private static final long serialVersionUID = -2661360918390893815L;
    private char colorChannel;
    private DrawingBoard model;

    /**
     * Constructs a new ColorSlider for the model and color channel requested
     * @param board DrawingBoard that this slider will interact with
     * @param RGBaChannel char One of r/g/b/a, dictating the color channel
     * this slider will control. Cannot be altered once constructed.
     * @throws IllegalArgumentException if provided with any char other than r/g/b/a
     * @throws IllegalArgumentException if fed a null DrawingBoard
     */
    public ColorSlider(DrawingBoard board,char RGBaChannel){
        super(JSlider.VERTICAL,0,255,0);
        if(board == null){
            throw new IllegalArgumentException("Null DrawingBoard...unacceptable");
        }
        model = board;
        model.addListener(this);
        setColorChannel(RGBaChannel);

        //Labels
        this.setMajorTickSpacing(63);
        this.setMinorTickSpacing(9);
        this.setPaintTicks(true);
        Hashtable<Integer,JLabel> labelTable = new Hashtable<Integer,JLabel>();
        if(RGBaChannel == 'r'){
            labelTable.put(new Integer(0), new JLabel("0 (r)"));
        } else if(RGBaChannel == 'g'){
            labelTable.put(new Integer(0), new JLabel("0 (g)"));
        } else if(RGBaChannel == 'b'){
            labelTable.put(new Integer(0), new JLabel("0 (b)"));
        } else if(RGBaChannel == 'a'){
            setValue(255);
            labelTable.put(new Integer(0), new JLabel("0 (a)"));
        }
        labelTable.put(new Integer(255), new JLabel("255"));
        this.setLabelTable(labelTable);
        this.setPaintLabels(true);
    }

    //Private method to set the color channel w/validation
    private void setColorChannel(char channel){
        if(channel != 'r' && channel != 'g' && channel != 'b' && channel != 'a'){
            throw new IllegalArgumentException("Only r/g/b/a are legal RGBa color spaces");
        }
        colorChannel = channel;
    }

    /**
     * Sets the value of the slider, and the RGBa color space of the selected
     * shape, to the provided value. This value must be [0-255]. If nothing is
     * selected, that's fine. Only the slider will be updated.
     * @param val int value [0-255] to which the slider is to be set.
     * @throws IllegalArgumentException if val is less than 0 or greater than 255
     */
    @Override
    public void setValue(int val){
        if(val < 0 || val > 255){
            throw new IllegalArgumentException("Color space must be [0-255]");
        }
        super.setValue(val);
        if(model.currentlySelected() != null){
            if(colorChannel == 'r' && val != model.currentlySelected().getColor().getRed()){
                Color newColor = new Color(val,
                                           model.currentlySelected().getColor().getGreen(),
                                           model.currentlySelected().getColor().getBlue(),
                                           model.currentlySelected().getColor().getAlpha());
                model.changeColorOfSelected(newColor);
            }
            if(colorChannel == 'g' && val != model.currentlySelected().getColor().getGreen()){
                Color newColor = new Color(model.currentlySelected().getColor().getRed(),
                                           val,
                                           model.currentlySelected().getColor().getBlue(),
                                           model.currentlySelected().getColor().getAlpha());
                model.changeColorOfSelected(newColor);
            }
            if(colorChannel == 'b' && val != model.currentlySelected().getColor().getBlue()){
                Color newColor = new Color(model.currentlySelected().getColor().getRed(),
                                           model.currentlySelected().getColor().getGreen(),
                                           val,
                                           model.currentlySelected().getColor().getAlpha());
                model.changeColorOfSelected(newColor);
            }
            if(colorChannel == 'a' && val != model.currentlySelected().getColor().getAlpha()){
                Color newColor = new Color(model.currentlySelected().getColor().getRed(),
                                           model.currentlySelected().getColor().getGreen(),
                                           model.currentlySelected().getColor().getBlue(),
                                           val);
                model.changeColorOfSelected(newColor);
            }
        }
    }

    /**
     * {@inheritDoc}
     */
    public void drawingBoardChange(){
        if(model.currentlySelected() != null){
            if(colorChannel == 'r') setValue(model.currentlySelected().getColor().getRed());
            if(colorChannel == 'g') setValue(model.currentlySelected().getColor().getGreen());
            if(colorChannel == 'b') setValue(model.currentlySelected().getColor().getBlue());
            if(colorChannel == 'a') setValue(model.currentlySelected().getColor().getAlpha());
        }
    }
}