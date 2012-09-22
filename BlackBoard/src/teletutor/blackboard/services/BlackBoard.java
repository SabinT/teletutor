/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package teletutor.blackboard.services;

import java.awt.Color;
import java.awt.Image;
import javax.swing.JPanel;
import javax.swing.JToolBar;
import teletutor.core.services.TeleChannel;
import teletutor.core.services.TeleObject;

/**
 *
 * @author Samyam
 */
public abstract class Blackboard extends TeleObject {

    public final static Color BGCOLOR = new Color(102, 134, 121);

    public Blackboard(String string, TeleChannel tc) throws Exception {
        super(string, tc);
    }

    public abstract Image getImage();

    public abstract void addObject(BoardObject bObject);

    public abstract void removeObject(BoardObject bObject);

    public abstract void addTool(BoardTool bTool);

    public abstract void removeTool(BoardTool bTool);

    public abstract void redraw();

    public abstract JPanel getBoardPanel();

    public abstract JToolBar getToolBar();

    public abstract BoardObject getObjectAt(int x, int y);

    public abstract int getWidth();

    public abstract int getHeight();

    public abstract BoardTool getCurrentTool();

    public abstract void setCurrentTool(BoardTool currentTool);

    // ************************************************************************
    public abstract void selectObject(BoardObject obj);

    public abstract BoardObject getSelectedObject();

    public abstract void selectDefaultTool();

    //**************************************************************************
    public abstract void mergeDown(BoardObject bObject);

    public abstract void clear();

    public abstract void deleteObject(BoardObject obj);
}
