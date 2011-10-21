/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package teletutor.classroommanager.test;

import java.util.logging.Level;
import java.util.logging.Logger;
import teletutor.classroommanager.UI.ClassroomFrame;
import teletutor.classroommanager.impl.ClassroomManager;
import teletutor.core.impl.TeleChannelImpl;

/**
 *
 * @author Sabin Timalsena
 */
public class GandhiPig {

    public static void main(String[] args) {
   
        try {
            
            TeleChannelImpl chan = new TeleChannelImpl("../../settings/UDP.xml", "TestGroup", "Gandhi");

            ClassroomManager crman = new ClassroomManager("ClassroomManager", chan);
            ClassroomFrame frame =  new ClassroomFrame(crman);
            frame.setVisible(true);
                        
//            Thread.currentThread().sleep(60000);
//
//            chan.closeChannel();
//            System.exit(0);
        } catch (Exception ex) {
            Logger.getLogger(GandhiPig.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
}