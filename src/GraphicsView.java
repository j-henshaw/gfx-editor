import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;

import javax.swing.JPanel;

/** Class defining a graphical view of a DrawingBoard
 * @author Jon Henshaw
 * @version 10/20/2019
 */
public class GraphicsView extends JPanel implements BoardListener{
  
  private static final long serialVersionUID = -6645924837349202741L;
  private DrawingBoard model;

  /**
   * Constructs a Graphical View for a DrawingBoard model
   * @param b DrawingBoard model which this view is to
   * use for updating its display
   */
  GraphicsView(DrawingBoard b){
    super();
    this.setBackground(new Color(255,255,255,255));
    this.setPreferredSize(new Dimension(600,450));
    model = b;
    model.addListener(this);
  }

  /**
   * GraphicsViews will repaint their components whenever notified
   * of a change in the model.
   */
  public void drawingBoardChange(){
      repaint();
  }

  /**
   * Repaint the DrawingBoard's contents by having each item draw itself
   * @param g Graphics object where painting is to take place
   */
  public void paintComponent(Graphics g) {
    /*Graphics2D g2 = (Graphics2D) g;
    g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
    RenderingHints.VALUE_ANTIALIAS_ON);
    g2.setRenderingHint(RenderingHints.KEY_INTERPOLATION,
    RenderingHints.VALUE_INTERPOLATION_BILINEAR);
    */
    super.paintComponent(g);
    //Draw each shape in the model
    for(Shape shape : model.getShapes()){
      shape.drawSelf(g);
    }
    
  }

  /**
   * This method paints this GraphicsView's model's shapes without
   * painting the GraphicsView itself. Used for .png export.
   * @param g Graphics object to which the shapes shall be painted
   */
  public void paint2(Graphics g){
    /*Graphics2D g2 = (Graphics2D) g;
    g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
    RenderingHints.VALUE_ANTIALIAS_ON);
    g2.setRenderingHint(RenderingHints.KEY_INTERPOLATION,
    RenderingHints.VALUE_INTERPOLATION_BILINEAR);
    */
    for(Shape shape : model.getShapes()){
      shape.drawSelf(g);
    }
    
  }

  /**
   * Returns the DrawingBoard which this GraphicsView monitors
   */
  public DrawingBoard getBoard(){
    return model;
  }

}