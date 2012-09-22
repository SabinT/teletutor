/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package teletutor.blackboard.impl;

import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import teletutor.blackboard.services.Blackboard;
import teletutor.blackboard.services.BoardObject;

/**
 *
 * @author Samyam
 */
public class MoveMouseListener extends MouseAdapter {

    private Blackboard board = null;
    int dx, dy;

    public MoveMouseListener(Blackboard board) {
        this.board = board;
    }

    @Override
    public void mouseDragged(MouseEvent e) {
        if (board.getSelectedObject() == null) {
            return;
        }
        BoardObject obj = board.getSelectedObject();
        obj.setX(e.getX() - dx);
        obj.setY(e.getY() - dy);
        board.redraw();
    }

    @Override
    public void mouseClicked(MouseEvent e) {
        if (e.getClickCount() == 2) {
            BoardObject obj = board.getObjectAt(e.getX(), e.getY());
            if (obj != null) {
                board.selectObject(obj);
                obj.showProperties();
            }
        }
    }

    @Override
    public void mousePressed(MouseEvent e) {
//        BoardObject obj = board.getSelectedObject();

//        if (obj != null) {
            board.selectObject(null);
//        }

        BoardObject obj = board.getObjectAt(e.getX(), e.getY());
        if (obj != null) {
            board.selectObject(obj);

            dx = e.getX() - obj.getX();
            dy = e.getY() - obj.getY();
        }
    }

    @Override
    public void mouseReleased(MouseEvent e) {
    }

    public void deselectObject() {
        BoardObject obj = board.getSelectedObject();
        if (obj != null) {
            board.selectObject(null);
        }
    }
}
