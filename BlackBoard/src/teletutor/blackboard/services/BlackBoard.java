/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package teletutor.blackboard.services;

import java.awt.Image;
import javax.swing.JPanel;
import javax.swing.JToolBar;

/**
 *
 * @author Samyam
 */
public interface BlackBoard {
    public Image getImage();
    
    public void addObject(BoardObject bObject);
        
    public void removeObject(BoardObject bObject);
    
    public void addTool(BoardTool bTool);
    
    public void removeTool(BoardTool bTool);
    
    public void redraw();
    
    public JPanel getBoardPanel();
    
    public JToolBar getToolBar();
    
    public BoardObject getObjectAt(int x, int y);
    
    int getWidth();
    
    int getHeight();
} 

