/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package teletutor.classroommanager.test;

import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.jgroups.JChannel;
import org.jgroups.ReceiverAdapter;
import org.jgroups.View;
import org.jgroups.conf.ClassConfigurator;
import teletutor.classroommanager.impl.ClassroomManager;
import teletutor.core.impl.SubChannelHeader;
import teletutor.core.impl.TeleChannelImpl;
import teletutor.core.services.TeleChannel;
import teletutor.core.services.ViewObserver;
import teletutor.core.test.NewMessage;
import teletutor.core.test.NewObject;

/**
 *
 * @author Sabin Timalsena
 */
public class TankePig {
    static TeleChannel chan;
    public static void main(String[] args) {
   
        try {
            
            JChannel channel = new JChannel("../../settings/UDP.xml");
            channel.setName("Tanke");

            
            channel.setReceiver(new ReceiverAdapter() {
                @Override
                public void viewAccepted(View view) {
                    System.out.println(view);
                }
            });
                        
            channel.connect("TestGroup");
            Thread.currentThread().sleep(60000);

            System.exit(0);
        } catch (Exception ex) {
            Logger.getLogger(TankePig.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
}