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

/**
 *
 * @author Sabin Timalsena
 */
public class GandhiPig {

    public static void main(String[] args) {
   
        try {
            
            CoreFrame coreFrame = new CoreFrame();

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