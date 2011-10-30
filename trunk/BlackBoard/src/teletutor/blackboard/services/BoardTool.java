package teletutor.blackboard.services;

import java.awt.event.MouseAdapter;
import teletutor.blackboard.impl.BlackBoardImpl;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import javax.swing.JPanel;
import javax.swing.JToggleButton;
import teletutor.core.services.TeleChannel;
import teletutor.core.services.TeleObject;

/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
/**
 *
 * @author Samyam
 */
public abstract class BoardTool extends TeleObject {
    
    protected MouseAdapter mListener = null;
    protected JPanel toolPanel = null;
    protected JToggleButton toolButton;
    protected BlackBoardImpl board = null;
    
    public BoardTool(String string, TeleChannel tc) throws Exception {
        super(string, tc);
    }

    /**
     * Initialization of mListener and toolPanel must be handled by init()
     */
    public abstract void activate();
    
    public abstract void deactivate();
    
    public abstract void actionComplete();
    
    public abstract void actionAborted();
    
    public final MouseListener getMouseListener() {
        return mListener;
    }
    
    public JPanel getToolPanel() {
        return toolPanel;
    }
    
    public final void setBlackBoard(BlackBoardImpl bBoard) {
        board = bBoard;
    }
    
    public JToggleButton getToolButton() {
        return toolButton;
    }
    
    public final void deselectOther() {
        BoardTool tool = board.getCurrentTool();
        if (tool == null) {            
            board.setCurrentTool(this);
        } else {
            board.getCurrentTool().toolButton.setSelected(false);
            board.setCurrentTool(this);
        }
        
    }
}
