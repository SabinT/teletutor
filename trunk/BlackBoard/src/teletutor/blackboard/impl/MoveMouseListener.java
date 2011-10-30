/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package teletutor.blackboard.impl;

import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import teletutor.blackboard.services.BlackBoard;
import teletutor.blackboard.services.BoardObject;

/**
 *
 * @author Samyam
 */
public class MoveMouseListener extends MouseAdapter {
    
    private BoardObject obj = null;
    private BlackBoard board = null;
    int dx, dy;
    
    public MoveMouseListener(BlackBoard board) {
        this.board = board;
    }
    
    @Override
    public void mouseDragged(MouseEvent e) {
        if (obj == null) {
            return;
        }
        
        obj.setX(e.getX() - dx);
        obj.setY(e.getY() - dy);
        board.redraw();
    }

    @Override
    public void mouseClicked(MouseEvent e) {
        if (obj != null){
            obj.setShowBorder(false);
            board.redraw();
            
        }
        
        obj = board.getObjectAt(e.getX(), e.getY());
        if(obj != null){
        obj.setShowBorder(true);
        board.redraw();
        }
    }
    
    @Override
    public void mousePressed(MouseEvent e) {
        obj = board.getObjectAt(e.getX(), e.getY());
        if (obj != null) {
            obj.setShowBorder(true);
            dx = e.getX() - obj.getX();
            dy = e.getY() - obj.getY();
        }
    }
    
    @Override
    public void mouseReleased(MouseEvent e) {
        if(obj != null){
            obj.setShowBorder(false);
            board.redraw();
        }
        obj = null;               
         
    }
}
