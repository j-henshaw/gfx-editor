import java.awt.AWTException;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.Robot;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.image.BufferedImage;
import java.io.File;
import java.util.ArrayList;
import java.util.Random;

import javax.imageio.ImageIO;
import javax.swing.JButton;

/**
 * This is a general-purpose controller for a GraphicsEditorApp
 * @author Jon Henshaw
 * @version 10/20/2019
 */
public class AppController extends MouseAdapter implements KeyListener, ActionListener{
    private DrawingBoard model;
    private Random rand;
    private String shapeType;
    private String actionType;
    private ArrayList<JButton> buttons;
    private Shape shapeHeld;
    private int lastX,lastY;
    private Boolean cloneMade;
    private GraphicsEditorApp app;
    private int saves; //int to be appended to savefiles

    /**
     * Constructs a new AppController for the given GraphicsEditorApp
     * @param app GraphicsEditorApp that this AppController will control
     */
    public AppController(GraphicsEditorApp app){
        super();
        if(app == null) throw new IllegalArgumentException("Null App...unacceptable");
        this.app = app;
        model = app.getView().getBoard();
        
        rand = new Random();
        shapeType = "Circle";
        actionType = "Add";
        buttons = new ArrayList<JButton>();
        shapeHeld = null;
        lastX = lastY = 0;
        cloneMade = false;
        saves = 1;
    }


    ////////////////////
    /// MOUSE EVENTS ///
    ////////////////////

    /**
     * {@inheritDoc}
     */
    @Override
    public void mouseClicked(MouseEvent e){
        Point spot = e.getPoint();

        ///'ADD' BEHAVIOR///
        if(actionType.equals("Add")){
            //Adds new Circle at mousehead
            if(shapeType.equals("Circle"))
            model.addShape(new Circle(spot,rand.nextInt(100)+1));

            //Adds new Diamond at mousehead
            if(shapeType.equals("Diamond"))
            model.addShape(new Diamond(spot,(rand.nextInt(100)+5)*2,(rand.nextInt(100)+5)*2));

            //Adds new Arrow at mousehead
            if(shapeType.equals("Arrow")){
            int thicc = (rand.nextInt(75)+1);
            model.addShape(new Arrow(spot,thicc*4 + rand.nextInt(75),thicc));
            }
        }

        ///SELECT BEHAVIOR///
        if(!actionType.equals("Add")){
            //Selects at mousehead
            model.selectAt(e.getX(),e.getY());
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void mouseDragged(MouseEvent e){
        
        ///'MOVE' BEHAVIOR///
        if(actionType.equals("Move") && shapeHeld != null){
            int deltaX = e.getX() - lastX;
            int deltaY = e.getY() - lastY;
            shapeHeld.shiftUpperLeftBy(deltaX, deltaY);
            lastX = e.getX();
            lastY = e.getY();
            model.notifyListeners();
        }

        ///'RESIZE' BEHAVIOR///
        if(actionType.equals("Resize") && shapeHeld != null){
            int deltaX = e.getX() - lastX;
            int deltaY = e.getY() - lastY;
            shapeHeld.resize(deltaX, deltaY);
            lastX = e.getX();
            lastY = e.getY();
            model.notifyListeners();
        }

        ///'CLONE' BEHAVIOR///
        if(actionType.equals("Clone") && shapeHeld != null && !cloneMade){
            cloneMade = true;
            shapeHeld = shapeHeld.copyOf();
            model.addShape(shapeHeld);
            int deltaX = e.getX() - lastX;
            int deltaY = e.getY() - lastY;
            shapeHeld.shiftUpperLeftBy(deltaX, deltaY);
            lastX = e.getX();
            lastY = e.getY();
            model.notifyListeners();
        }
        if(actionType.equals("Clone") && shapeHeld != null && cloneMade){
            int deltaX = e.getX() - lastX;
            int deltaY = e.getY() - lastY;
            shapeHeld.shiftUpperLeftBy(deltaX, deltaY);
            lastX = e.getX();
            lastY = e.getY();
            model.notifyListeners();
        }

        ///'ADD+' BEHAVIOR///
        if(actionType.equals("Add+")){
            Point spot = new Point(e.getX(),e.getY());

            //Adds new Circle at mousehead
            if(shapeType.equals("Circle"))
            model.addShape(new Circle(spot,rand.nextInt(100)+1));

            //Adds new Diamond at mousehead
            if(shapeType.equals("Diamond"))
            model.addShape(new Diamond(spot,(rand.nextInt(100)+5)*2,(rand.nextInt(100)+5)*2));

            //Adds new Arrow at mousehead
            if(shapeType.equals("Arrow")){
            int thicc = (rand.nextInt(75)+1);
            model.addShape(new Arrow(spot,thicc*4 + rand.nextInt(75),thicc));
            }
        }

        ///'CLONE+' BEHAVIOR///
        if(actionType.equals("Clone+") && shapeHeld != null){
            shapeHeld = shapeHeld.copyOf();
            model.addShape(shapeHeld);
            int deltaX = e.getX() - lastX;
            int deltaY = e.getY() - lastY;
            shapeHeld.shiftUpperLeftBy(deltaX, deltaY);
            lastX = e.getX();
            lastY = e.getY();
            model.notifyListeners();
        }
    }
    
    /**
     * {@inheritDoc}
     */
    @Override
    public void mousePressed(MouseEvent e){
        lastX = e.getX();
        lastY = e.getY();
        for(Shape shape : model.getShapes()){
            if (shape.isOn(lastX,lastY)){
                shapeHeld = shape;
            }
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void mouseReleased(MouseEvent e){
        shapeHeld = null;
        cloneMade = false;
    }


    /////////////////////
    /// BUTTON EVENTS ///
    /////////////////////
    
    /**
     * Don't use this method. The first three buttons passed in will be
     * disabled whenever the App isn't set to add shapes. There is no
     * way to remove the button after it is added.
     * @param button Jbutton to be disabled when not adding shapes to the App
     */
    public void addButton(JButton button){
        buttons.add(button);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void actionPerformed(ActionEvent buttonPress){
        if(buttonPress.getActionCommand().equals("Circle") ||
           buttonPress.getActionCommand().equals("Diamond") ||
           buttonPress.getActionCommand().equals("Arrow")){
            shapeType = buttonPress.getActionCommand();
            for(JButton button : buttons){
                    button.setSelected(false);
                }
            if(shapeType.equals("Circle")){
                buttons.get(0).setSelected(true);
            }else if(shapeType.equals("Diamond")){
                buttons.get(1).setSelected(true);
            }else if(shapeType.equals("Arrow")){
                buttons.get(2).setSelected(true);
                }
        }else{
            if(buttonPress.getActionCommand().equals("Add") ||
               buttonPress.getActionCommand().equals("Move") ||
               buttonPress.getActionCommand().equals("Resize") ||
               buttonPress.getActionCommand().equals("Clone") ||
               buttonPress.getActionCommand().equals("Add+") ||
               buttonPress.getActionCommand().equals("Clone+")){
                actionType = buttonPress.getActionCommand();
            }
            if(buttonPress.getActionCommand().equals("Add") ||
               buttonPress.getActionCommand().equals("Add+")){
                for(JButton button : buttons){
                    button.setEnabled(true);
                }
                if(shapeType.equals("Circle")){
                    buttons.get(0).setSelected(true);
                }else if(shapeType.equals("Diamond")){
                    buttons.get(1).setSelected(true);
                }else if(shapeType.equals("Arrow")){
                    buttons.get(2).setSelected(true);
                }
            }else if(buttonPress.getActionCommand().equals("Move") ||
                     buttonPress.getActionCommand().equals("Resize") ||
                     buttonPress.getActionCommand().equals("Clone") ||
                     buttonPress.getActionCommand().equals("Clone+")){
                for(JButton button : buttons){
                    button.setEnabled(false);
                }
            }
            ///'SAVE' BEHAVIOR//
            //.jpeg
            else if(buttonPress.getActionCommand().equals(".jpeg")){
                BufferedImage savedPic = null;
                try{
                    savedPic = new Robot().createScreenCapture(app.getView().getBounds());
                }catch (AWTException e1) {
                    e1.printStackTrace();
                }
                Graphics2D graphics2D = savedPic.createGraphics();
                app.getView().paint(graphics2D);
                try {
                    ImageIO.write(savedPic,"jpeg", new File("image" + saves + ".jpeg"));
                    ++saves;
                }catch (Exception e) {
                System.out.println("Save failed");
                }
            }
            //.png
            else if(buttonPress.getActionCommand().equals(".png")){
                    BufferedImage savedPic = new BufferedImage(app.getView().getWidth(),
                                                app.getView().getHeight(), BufferedImage.TYPE_INT_ARGB);
            Graphics2D graphics2D = savedPic.createGraphics();
            app.getView().paint2(graphics2D);
            try {
                ImageIO.write(savedPic,"png", new File("image" + saves + ".png"));
                ++saves;
            }catch (Exception e) {
                System.out.println("Save failed");
                }
            }
        }
    }


    ///////////////////////
    /// KEYBOARD EVENTS ///
    ///////////////////////
    /**
     * Removes the selected shape when 'r' is pressed, or all shapes when 'e' is pressed.
     * No other action is supported.
     * @param e KeyEvent detailing a keyTyped action
     */
    @Override
    public void keyTyped(KeyEvent e){
        if(e.getKeyChar() == 'r' && model.getShapes().size() > 0 &&
                                    model.currentlySelected() != null){
            model.deleteSelected();
        }else if(e.getKeyChar() == 'e'){
            model.deleteAll();
        }
    }
    /**
     * {@inheritDoc}
     */
    @Override
    public void keyPressed(KeyEvent e){
    }
    /**
     * {@inheritDoc}
     */
    @Override
    public void keyReleased(KeyEvent e){
    }

}