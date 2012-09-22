/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package teletutor.blackboard.test;

import java.util.logging.Level;
import java.util.logging.Logger;
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
            board.init(500, 500);
            BoardFrame frame = new BoardFrame(board);
            frame.setVisible(true);

//            chan = new TeleChannelImpl("../../settings/UDP.xml", "TestGroup", "Heme");
//            
//            ClassroomManager crman = new ClassroomManager("ClassroomManager", chan);
//            ClassroomFrame frame =  new ClassroomFrame(crman);
//            frame.setVisible(true);
//            
//            MemberProxy mp = null;
//            int i;
//            for (i = 1; i < 10; i++) {
//                mp = crman.getMemberProxy("Gandhi");
//                Thread.currentThread().sleep(2000);
//                if (mp != null) break;
//            }
//            if (i == 10) {
//                System.out.println("Gandhi not found.");
//                System.exit(0);
//            }
//            System.out.println("Now sending to gandhi...");
//            for (i = 1; i < 10; i++) {
//                mp.sendPM("I love you.");
//                Thread.currentThread().sleep(2000);
//            }
//            
//            ((TeleChannelImpl)chan).closeChannel();
//            System.exit(0);
        } catch (Exception ex) {
            Logger.getLogger(HemePig.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
}
