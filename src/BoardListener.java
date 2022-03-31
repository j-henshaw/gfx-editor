/** Interface allowing an object to be a Listener of a DrawingBoard
 * @author Jon Henshaw
 * @version 10/16/2019
 */
public interface BoardListener{

    /**
     * This method is used to notify this BoardListener that a
     * change has occurred in the model.
     */
    public void drawingBoardChange();
}