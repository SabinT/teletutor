/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package teletutor.classroommanager.test;

import java.util.logging.Level;
import java.util.logging.Logger;
import teletutor.classroommanager.UI.ClassroomFrame;
import teletutor.classroommanager.impl.ClassroomManager;
import teletutor.core.UI.CoreFrame;
import teletutor.core.impl.TeleChannelImpl;
import teletutor.core.services.TeleChannel;
import teletutor.core.utilities.LectureBean;

/**
 *
 * @author Sabin Timalsena
 */
public class GandhiPig {

    public static void main(String[] args) {
   
        try {
            
            LectureBean lecture =  new LectureBean(123, "Plutonium tatto-making", "Heme");
            TeleChannel chan = new TeleChannelImpl("../../settings/UDP.xml", lecture, "Gandhi");
            
            ClassroomManager crman = new ClassroomManager("ClassroomManager", chan);
            ClassroomFrame frame =  new ClassroomFrame(crman);
            frame.setVisible(true);

//            ClassroomManager crman = new ClassroomManager("ClassroomManager", coreFrame.getChannel());
//            ClassroomFrame frame =  new ClassroomFrame(crman);
//            frame.setVisible(true);
                        
//            Thread.currentThread().sleep(60000);
//
//            chan.closeChannel();
//            System.exit(0);
        } catch (Exception ex) {
            Logger.getLogger(GandhiPig.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
}