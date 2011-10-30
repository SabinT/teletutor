/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package teletutor.blackboard.impl;

import java.awt.Dimension;
import java.awt.Graphics;
import javax.swing.JPanel;
import teletutor.blackboard.services.BlackBoard;


/**
 *
 * @author Samyam
 */
public class BoardPanel extends JPanel {

    private BlackBoard board = null;

    public BoardPanel(BlackBoard board) {
        super.setPreferredSize(new Dimension(board.getWidth(), board.getHeight()));
        this.board = board;        
    }

    @Override
    public void paintComponent(Graphics g) {
        g.drawImage(board.getImage(), 0, 0, null);
    }
}
