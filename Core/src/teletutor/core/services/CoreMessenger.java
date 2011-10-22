/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package teletutor.core.services;

import java.util.logging.Level;
import java.util.logging.Logger;
import teletutor.core.UI.CoreFrame;
import teletutor.core.services.SimpleMessage;
import teletutor.core.services.TeleChannel;
import teletutor.core.services.TeleObject;
import teletutor.core.services.UpdateInfo;

/**
 * This is the class that controls the channel. It extends the TeleObject, and 
 * hence can receive messages from other objects like the ClassroomManager
 * @author Sabin Timalsena
 */
public class CoreMessenger extends TeleObject {
    // sent by the e.g. ClassroomManager to Core

    public static final String LEAVE_LECTURE = "Leave Lecture";
    public static final String EXIT = "Exit";

    CoreFrame coreFrame;

    public CoreMessenger(String name, TeleChannel chan, CoreFrame coreFrame) throws Exception {
        super(name, chan);
        this.coreFrame = coreFrame;
    }

    @Override
    public void received(Object obj) {
        if (obj instanceof SimpleMessage) {
            SimpleMessage msg = (SimpleMessage) obj;
            if (msg.getMessage().equals(LEAVE_LECTURE)) {
                /* The Leave Lecture arrives from the ClassroomManager. Stop the
                 * channel and go back to the login page.
                 */
                unregisterSubChannel();
                coreFrame.stopChannel();
            } else if (msg.getMessage().equals(EXIT)) {
                /* Exit is received from the ClassroomManager. It this is the 
                 * tutor, send LEAVE_LECTURE to all other participants.
                 */
                if (getChannel().isTutorChannel()) {
                    try {
                        sendObject(new SimpleMessage(CoreMessenger.LEAVE_LECTURE));
                    } catch (Exception ex) {
                        Logger.getLogger(CoreMessenger.class.getName()).log(Level.SEVERE, null, ex);
                    }
                }
                unregisterSubChannel();
                coreFrame.exit();
            }
        }
    }

    @Override
    public void objectUpdated(UpdateInfo changes) {
        // does not maintain state yet
        return;
    }
}
