/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package teletutor.blackboard.impl;

import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.logging.Level;
import java.util.logging.Logger;
import teletutor.blackboard.services.Blackboard;
/**
 *
 * @author Samyam
 */
public class TextMouseListener extends MouseAdapter {

    private TextObject obj = null;
    private Blackboard board = null;
    private int x1, y1, x2, y2;

    public TextMouseListener(Blackboard board) {
        this.board = board;
    }

    @Override
    public void mouseDragged(MouseEvent e) {
        if (e.getX() < x1) {
            obj.setX(e.getX());
        } else {
            obj.setX(x1);
        }
        if (e.getY() < y1) {
            obj.setY(e.getY());
        } else {
            obj.setY(y1);
        }
        obj.setWidth(Math.abs(x1 - e.getX()));
        obj.setHeight(Math.abs(y1 - e.getY()));       
        board.redraw();
    }

    @Override
    public void mousePressed(MouseEvent e) {
        try {
            obj = new TextObject("NewTextObject", null, board);
            obj.init();
            obj.setWrap(Boolean.TRUE);
            obj.setShowBorder(true);
            board.addObject(obj);
            x1 = e.getX();
            y1 = e.getY();
            obj.setX(x1);
            obj.setY(y1);
        } catch (Exception ex) {
            Logger.getLogger(TextMouseListener.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    @Override
    public void mouseReleased(MouseEvent e) {
        if(obj.getHeight() < 70){
            obj.setWrap(Boolean.FALSE);
        }
        if(obj.getWidth() < 70){
            obj.setWidth(70);
        }
        obj.redraw();
        obj.showProperties();
    }
}
