/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package teletutor.blackboard.impl;

import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.logging.Level;
import java.util.logging.Logger;
import teletutor.blackboard.services.BlackBoard;
import teletutor.blackboard.services.BoardObject;

/**
 *
 * @author Samyam
 */
public class TextMouseListener extends MouseAdapter {

    private TextObject obj = null;
    private BlackBoard board = null;
    
    private int x1, y1, x2, y2;
    
    
    public TextMouseListener(BlackBoard board) {
        this.board = board;
    }

    @Override
    public void mouseClicked(MouseEvent e) {
        try {
            obj = new TextObject("Obj1", null);
            obj.init(board);
            obj.setX(e.getX());
            obj.setY(e.getY());
            obj.showProperties();
           
        } catch (Exception ex) {
            Logger.getLogger(TextMouseListener.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    @Override
    public void mouseDragged(MouseEvent e) {
        
    }

    @Override
    public void mousePressed(MouseEvent e) {
        super.mousePressed(e);
    }

    @Override
    public void mouseReleased(MouseEvent e) {
        super.mouseReleased(e);
    }
}
