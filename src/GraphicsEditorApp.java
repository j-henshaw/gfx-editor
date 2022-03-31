import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;

import javax.swing.BorderFactory;
import javax.swing.ButtonGroup;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.WindowConstants;

/**
 * This is a GraphicsEditorApp that allows the user to place and manipulate
 * Shapes on a DrawingBoard. Shapes can be added individually or in a stream.
 * Shapes can be moved, resized, cloned, and have their RGBa color changed. When
 * cloning, the user can choose between making one copy or a stream of copies.
 * Individual shapes, along with the entire board, can be eliminated via keypress.
 * There is a TextView that keeps track of the current state of the DrawingBoard.
 * 
 * The user can also output their creation to the Project folder in either
 * .jpeg or .png formats. Transparency is supported for .png.
 * 
 * Current Shapes supported are Circle, Diamond, and Arrow.
 * 
 * @author Jon Henshaw
 * @version 10/20/19
 */
public class GraphicsEditorApp extends JPanel{
    private static final long serialVersionUID = 8427560634858254366L;
    private TextView txtView;
    private GraphicsView gfxView;
    private DrawingBoard shapeDeck;
    private AppController appController;

    /**
     * Constructs a GraphiscEditorApp
     * @param b DrawingBoard that this App will represent
     * @throws IllegalArgumentException when fed a null DrawingBoard to model
     */
    public GraphicsEditorApp(DrawingBoard b){
        //////////////////////
        /// INITIALIZATION ///
        //////////////////////

        //Validation
        if(b == null){
            throw new IllegalArgumentException("Null model...unacceptable");
        }
        shapeDeck = b;
        //Set border and dimensions of window
        this.setLayout(new BorderLayout());
        this.setPreferredSize(new Dimension(1050,600));
        //Construct Graphics and Text Views
        txtView = new TextView(shapeDeck);
        gfxView = new GraphicsView(shapeDeck);
        appController = new AppController(this);

        
        ////////////////////////////
        /// CONSTRUCT MECHANISMS ///
        ////////////////////////////

        //Create r/g/b/a sliders
        ColorSlider redSlider = new ColorSlider(shapeDeck,'r');
        ColorSlider greenSlider = new ColorSlider(shapeDeck,'g');
        ColorSlider blueSlider = new ColorSlider(shapeDeck,'b');
        ColorSlider alphaSlider = new ColorSlider(shapeDeck,'a');

        //Create click behavior radio buttons (add/move/resize/clone)
        //use b3.setEnabled(false); to have these change the visibility
        //of the circle/diamond/arrow buttons in event handler
        JRadioButton radioAdd = new JRadioButton("Add",true);
        JRadioButton radioMove = new JRadioButton("Move");
        JRadioButton radioResize = new JRadioButton("Resize");
        JRadioButton radioClone = new JRadioButton("Clone");
        JRadioButton radioAddP = new JRadioButton("Add+");
        JRadioButton radioCloneP = new JRadioButton("Clone+");
        //Group them
        ButtonGroup clickBehaviors = new ButtonGroup();
        clickBehaviors.add(radioAdd);
        clickBehaviors.add(radioMove);
        clickBehaviors.add(radioResize);
        clickBehaviors.add(radioClone);
        clickBehaviors.add(radioAddP);
        clickBehaviors.add(radioCloneP);

        //Create shape select buttons (circle/diamond/arrow)
        JButton circleButton = new JButton("Circle");
        circleButton.setSelected(true);
        JButton diamondButton = new JButton("Diamond");
        JButton arrowButton = new JButton("Arrow");
        //Put them on the controller—this seems like a bad idea
        //but I'm doing it anyway
        appController.addButton(circleButton);
        appController.addButton(diamondButton);
        appController.addButton(arrowButton);

        //Create 'Save' button
        JButton saveButtonJ = new JButton(".jpeg");
        JButton saveButtonP = new JButton(".png");


        ///////////////////////////
        /// CONSTRUCT LEFT SIDE ///
        ///////////////////////////

        //This is the left side of the screen, a JPanel with BorderLayout,
        //containing the gfxView (CENTER) and the textView (SOUTH)
        JPanel leftSide = new JPanel();
        leftSide.setBorder(BorderFactory.createLineBorder(Color.black));
        leftSide.setLayout(new BorderLayout());
        leftSide.setPreferredSize(new Dimension(700,600));
        
        //Add gfxView to the leftSide
        leftSide.add(gfxView,BorderLayout.CENTER);

        //Add the text view to the bottom of the leftSide
        leftSide.add(txtView,BorderLayout.SOUTH);
        
        //Add leftSide to the app window
        this.add(leftSide,BorderLayout.CENTER);


        ////////////////////////////
        /// CONSTRUCT RIGHT SIDE ///
        ////////////////////////////

        //This is the right side of the screen, containing the
        //colorSliders(r/g/b/a), clickBehavior(add/move/resize/clone),
        //and shapeSelectors (circle/diamond/arrow)
        JPanel rightSide = new JPanel();
        rightSide.setBorder(BorderFactory.createLineBorder(Color.black));
        rightSide.setLayout(new BorderLayout());
        rightSide.setPreferredSize(new Dimension(350,600));
        

        ///COLOR AREA///
        //This section holds the 'Color controls' label, four colorSliders
        //for controlling the RGBa color space, and another unrelated label
        //describing keyboard input controls
        JPanel colorArea = new JPanel();
        colorArea.setLayout(new BorderLayout());
        colorArea.setBorder(BorderFactory.createLineBorder(Color.black));
        colorArea.setPreferredSize(new Dimension(350,270));

        //Color label goes in NORTH, but is held in its own JPanel
        //to achieve centeredness
        JPanel colorLabelArea = new JPanel();
        colorLabelArea.setPreferredSize(new Dimension(400,30));
        JLabel colorLabel = new JLabel("COLOR CONTROLS");
        colorLabelArea.add(colorLabel);
        colorArea.add(colorLabelArea,BorderLayout.NORTH);
        
        //Sliders go in CENTER, but are held in their own JPanel
        //to achieve centeredness
        JPanel sliderArea = new JPanel();
        sliderArea.add(redSlider);
        sliderArea.add(greenSlider);
        sliderArea.add(blueSlider);
        sliderArea.add(alphaSlider);
        colorArea.add(sliderArea,BorderLayout.CENTER);

        //Keyboard controls printed in SOUTH of the colorArea, but
        //are held in their own JPanel to achieve centeredness
        JPanel keyboardControlArea = new JPanel();
        keyboardControlArea.setPreferredSize(new Dimension(400,25));
        keyboardControlArea.setBorder(BorderFactory.createLineBorder(Color.black));
        JLabel keyControlLabel = new JLabel("Remove selected item: 'r' | " + 
                                            "Remove every item: 'e'");
        keyboardControlArea.add(keyControlLabel);
        colorArea.add(keyboardControlArea,BorderLayout.SOUTH);

        //Add colorArea to the NORTH of the rightSide
        rightSide.add(colorArea,BorderLayout.NORTH);

        ///CONTROL AREA///
        ///click behaviors///
        //controlArea area, border layout
        JPanel controlArea = new JPanel();
        controlArea.setBorder(BorderFactory.createLineBorder(Color.black));
        controlArea.setPreferredSize(new Dimension(350,200));
        //Add clickBehaviors label
        JLabel behaviorLabel = new JLabel("CLICK BEHAVIORS");
        controlArea.add(behaviorLabel);
        //Make new panel for and add radio buttons for click behavior
        JPanel radioButtons = new JPanel();
        radioButtons.add(radioAdd);
        radioButtons.add(radioMove);
        radioButtons.add(radioResize);
        radioButtons.add(radioClone);
        //'Plus' behaviors get their own row
        JPanel plusButtons = new JPanel();
        plusButtons.setPreferredSize(new Dimension(300,50));
        plusButtons.add(radioAddP);
        plusButtons.add(radioCloneP);
        //Add radio buttons to controlArea
        controlArea.add(radioButtons);
        controlArea.add(plusButtons);
        ///shape select///
        //add shapeSelect label
        JLabel shapeLabel = new JLabel("SHAPE SELECT");
        controlArea.add(shapeLabel);
        //Make new panel for and add buttons for shapeSelect
        JPanel shapeButtons = new JPanel();
        shapeButtons.add(circleButton);
        shapeButtons.add(diamondButton);
        shapeButtons.add(arrowButton);
        //Add buttons to controlArea
        controlArea.add(shapeButtons);
        //add controlArea to the rightSide
        rightSide.add(controlArea,BorderLayout.CENTER);
        
        ///SAVE AREA///
        //Holds a blank panel for spacing, the saveLabel and 2 saveButtons
        JPanel saveArea = new JPanel();
        saveArea.setBorder(BorderFactory.createLineBorder(Color.black));
        saveArea.setPreferredSize(new Dimension(300,130));
        //Make/add spacer
        JPanel saveSpacer = new JPanel();
        saveSpacer.setPreferredSize(new Dimension(300,7));
        saveArea.add(saveSpacer);
        //Make/add label and second spacer
        JLabel saveLabel = new JLabel("                              " +
                                      "Export image to Project folder:" +
                                      "                              ");
        saveArea.add(saveLabel);
        JPanel saveSpacer2 = new JPanel();
        saveSpacer2.setPreferredSize(new Dimension(300,1));
        saveArea.add(saveSpacer2);
        //Add saveButton to saveArea
        saveArea.add(saveButtonJ);
        saveArea.add(saveButtonP);
        //add saveArea to the rightSide
        rightSide.add(saveArea,BorderLayout.SOUTH);

        //Add rightSide to EAST of screen
        this.add(rightSide,BorderLayout.EAST);
        
        
        /////////////////////////////////////
        /// EVENT HANDLING INFRASTRUCTURE ///
        /////////////////////////////////////
        //Buttons
        circleButton.addActionListener(appController);
        diamondButton.addActionListener(appController);
        arrowButton.addActionListener(appController);
        radioAdd.addActionListener(appController);
        radioMove.addActionListener(appController);
        radioResize.addActionListener(appController);
        radioClone.addActionListener(appController);
        radioCloneP.addActionListener(appController);
        radioAddP.addActionListener(appController);
        saveButtonJ.addActionListener(appController);
        saveButtonP.addActionListener(appController);
        //Mouse
        this.addMouseListener(appController);
        this.addMouseMotionListener(appController);
        //Keyboard——DEAR LORD IS THERE A BETTER WAY TO DO THIS??
        leftSide.addKeyListener(appController);
        rightSide.addKeyListener(appController);
        shapeButtons.addKeyListener(appController);
        plusButtons.addKeyListener(appController);
        radioButtons.addKeyListener(appController);
        controlArea.addKeyListener(appController);
        keyboardControlArea.addKeyListener(appController);
        sliderArea.addKeyListener(appController);
        colorLabelArea.addKeyListener(appController);
        colorArea.addKeyListener(appController);
        radioAdd.addKeyListener(appController);
        radioMove.addKeyListener(appController);
        radioResize.addKeyListener(appController);
        radioClone.addKeyListener(appController);
        radioAddP.addKeyListener(appController);
        radioCloneP.addKeyListener(appController);
        circleButton.addKeyListener(appController);
        diamondButton.addKeyListener(appController);
        arrowButton.addKeyListener(appController);
        saveButtonJ.addKeyListener(appController);
        saveButtonP.addKeyListener(appController);
        redSlider.addKeyListener(appController);
        greenSlider.addKeyListener(appController);
        blueSlider.addKeyListener(appController);
        alphaSlider.addKeyListener(appController);
        
        //App is built!
    }

    /**
     * Getter for this App's GraphicsView component.
     * @return GraphicsView that this App utilizes
     */
    public GraphicsView getView(){
        return gfxView;
    }

    /**
     * Getter for this App's AppController component.
     * @return AppController that this App utilizes
     */
    public AppController getController(){
        return appController;
    }

    /**
     * Static method for instantiating a new GraphicsEditorApp
     * @param args Command line String[] args
     */
    public static void main(String[] args){
        //Create our model and controller/viewer
        DrawingBoard board = new DrawingBoard();
        GraphicsEditorApp playpen = new GraphicsEditorApp(board);

        // Create the window
        JFrame window = new JFrame("A Graphics Editor - Jon Henshaw");
        window.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        
        //Add the playpen to the window
        window.getContentPane().add(playpen);

        //pack and set visible
        window.pack();
        window.setVisible(true);

        //Enable KeyListener for full window
        AppController appController = playpen.getController();
        window.addKeyListener(appController);
        playpen.addKeyListener(appController);
        window.requestFocus();
    }
}