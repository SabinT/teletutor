/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package teletutor.blackboard.impl;

import javax.swing.JToolBar;
import teletutor.blackboard.services.BlackBoard;

/**
 *
 * @author Samyam
 */
class ToolBar extends JToolBar {

    BlackBoard board = null;

    public ToolBar(BlackBoard board) {
        this.board = board;
    }
    
    
}
