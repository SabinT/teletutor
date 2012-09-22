package teletutor.blackboard.services;

import java.awt.Cursor;
import java.awt.Point;
import java.awt.Toolkit;
import java.awt.event.MouseAdapter;
import teletutor.blackboard.impl.BlackboardImpl;
import java.awt.event.MouseListener;
import javax.swing.ImageIcon;
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
    protected Blackboard board = null;
    protected Cursor cursor = null;
    private Cursor defaultCursor = null;
    
    protected ImageIcon icon = new ImageIcon(getClass().getResource("/move.png"));

    public BoardTool(String string, TeleChannel tc) throws Exception {
        super(string, tc);

        Toolkit kit = Toolkit.getDefaultToolkit();
        defaultCursor = kit.createCustomCursor(icon.getImage(), new Point(12, 4), "move");
    }

    /**
     * Initialization of mListener and toolPanel must be handled by init()
     */
    public void activate() {

        deselectOther();
        board.getBoardPanel().setCursor(cursor);
        board.getBoardPanel().addMouseListener(mListener);
        board.getBoardPanel().addMouseMotionListener(mListener);
    }

    public void deactivate() {

        board.getBoardPanel().setCursor(defaultCursor);
        board.getBoardPanel().removeMouseListener(mListener);
        board.getBoardPanel().removeMouseMotionListener(mListener);
    }

    public abstract void actionComplete(BoardObject obj);

    public abstract void actionAborted(BoardObject obj);

    public final MouseListener getMouseListener() {
        return mListener;
    }

    public JPanel getToolPanel() {
        return toolPanel;
    }

    public final void setBlackBoard(BlackboardImpl bBoard) {
        board = bBoard;
    }

    public JToggleButton getToolButton() {
        return toolButton;
    }

    /**
     * Deselects the current tool and sets itself as the current tool.
     */
    public final void deselectOther() {
        BoardTool tool = board.getCurrentTool();
        if (tool == null) {
            board.setCurrentTool(this);
        } else {
            board.getCurrentTool().toolButton.doClick();
            board.setCurrentTool(this);
        }

    }

    public abstract void createCursor();
}
