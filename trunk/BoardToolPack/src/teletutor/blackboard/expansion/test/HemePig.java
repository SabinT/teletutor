/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package teletutor.blackboard.expansion.test;

import java.util.logging.Level;
import java.util.logging.Logger;
import teletutor.blackboard.expansion.picture.PictureTool;
import teletutor.blackboard.expansion.toolpack.BrushTool;
import teletutor.blackboard.expansion.toolpack.ClearTool;
import teletutor.blackboard.expansion.toolpack.EraserTool;
import teletutor.blackboard.impl.BlackboardImpl;
import teletutor.blackboard.impl.BoardFrame;

import teletutor.core.impl.TeleChannelImpl;
import teletutor.core.services.TeleChannel;
import teletutor.core.utilities.LectureBean;

/**
 *
 * @author Sabin Timalsena
 */
public class HemePig {

    static TeleChannel chan;

    public static void main(String[] args) {

        try {

            LectureBean lecture = new LectureBean(123, "Plutonium tatto-making", "Heme");
            TeleChannel chan = new TeleChannelImpl("../../settings/UDP.xml", lecture, "Heme");

            BlackboardImpl board = new BlackboardImpl("BlackBoard", chan);
            board.init(800, 600);
       
            BrushTool brush = new BrushTool(BrushTool.class.getName(), chan);      
            brush.init(board);
            EraserTool eraser = new EraserTool(EraserTool.class.getName(), chan);
            eraser.init(board);
            PictureTool pTool = new PictureTool(PictureTool.class.getName(), chan);
            ClearTool clear =  new ClearTool(ClearTool.class.getName(), chan);
            clear.init(board);
            
            board.addTool(brush);
            board.addTool(eraser);
            board.addTool(pTool);
            board.addTool(clear);
            // add tools

            BoardFrame frame = new BoardFrame(board);
            frame.setVisible(true);

        } catch (Exception ex) {
            Logger.getLogger(HemePig.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
}
