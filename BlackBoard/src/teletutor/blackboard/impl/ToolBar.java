/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package teletutor.blackboard.impl;

import javax.swing.JToolBar;
import teletutor.blackboard.services.Blackboard;

/**
 *
 * @author Samyam
 */
class ToolBar extends JToolBar {

    Blackboard board = null;

    public ToolBar(Blackboard board) {
        this.board = board;
    }
    
    
}
