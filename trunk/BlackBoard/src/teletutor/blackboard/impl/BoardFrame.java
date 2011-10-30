/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package teletutor.blackboard.impl;

/**
 *
 * @author Samyam
 */
/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

/*
 * MoveTest.java
 *
 * Created on Oct 19, 2011, 12:07:37 PM
 */
import java.awt.BorderLayout;
import java.awt.Color;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JToolBar;
import teletutor.blackboard.services.BlackBoard;

/**
 *
 * @author msi
 */
public class BoardFrame extends javax.swing.JFrame {

    BlackBoard board = null;
    Rectango rec1 = null;
    Rectango rec2 = null;
    Rectango rec3 = null;
    JPanel panel = null;
    MoveTool mover = null;
    JToolBar tools = null;
    TextTool texter = null;

    /** Creates new form MoveTest */
    public BoardFrame(BlackBoard _board) {
        try {
//            board = new BlackBoardImpl("BlackBoard", null);
//            board.init(500, 500);

            this.board = _board;

            rec1 = new Rectango("Rec1", null);
            rec1.init(20, 45, 1, 100, 100, Color.red);
            rec2 = new Rectango("Rec2", null);
            rec2.init(300, 100, 2, 108, 40, Color.cyan);
            rec3 = new Rectango("Rec3", null);
            rec3.init(200, 90, 3, 60, 300, Color.ORANGE);

            mover = new MoveTool("MoveTool", null);
            mover.init(board);

            texter = new TextTool("TextTool", null);
            texter.init(board);

            board.addObject(rec1);
            board.addObject(rec2);
            board.addObject(rec3);

            board.addTool(mover);
            board.addTool(texter);


            java.awt.EventQueue.invokeLater(new Runnable() {

                @Override
                public void run() {
                    panel = board.getBoardPanel();
                    tools = board.getToolBar();

                    setLayout(new BorderLayout());
                    getContentPane().add(panel, BorderLayout.CENTER);
                    getContentPane().add(tools, BorderLayout.NORTH);
                    pack();
                }
            });
        } catch (Exception ex) {
            Logger.getLogger(BoardFrame.class.getName()).log(Level.SEVERE, null, ex);
        }

    }

    /**
     * @param args the command line arguments
     */
    public static void main(String args[]) {
        java.awt.EventQueue.invokeLater(new Runnable() {

            public void run() {
                try {
                    BlackBoardImpl board = new BlackBoardImpl("BlackBoard", null);
                    board.init(500, 500);
                    new BoardFrame(board).setVisible(true);
                } catch (Exception ex) {
                    Logger.getLogger(BoardFrame.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        });
    }
    // Variables declaration - do not modify
    private javax.swing.JPanel jPanel1;
    private javax.swing.JScrollPane jScrollPane1;
    // End of variables declaration
}
