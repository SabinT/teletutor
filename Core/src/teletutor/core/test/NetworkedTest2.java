/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package teletutor.core.test;

import java.util.logging.Level;
import java.util.logging.Logger;
import org.jgroups.conf.ClassConfigurator;
import teletutor.core.impl.SubChannelHeader;
import teletutor.core.impl.TeleChannelImpl;
import teletutor.core.services.TeleChannel;
import teletutor.core.services.TeleObject;

/**
 *
 * @author Sabin Timalsena
 */
public class NetworkedTest2 {
    static TeleChannel chan;
    public static void main(String[] args) {
   
        try {
            ClassConfigurator.add((short)2000, SubChannelHeader.class);
 
            chan = new TeleChannelImpl("../../settings/UDP.xml", "TestGroup", "Mohan Dumba");
            
            NewObject nobj = new NewObject("new1", chan);
            // Change and send the value of this object for 10 times
            for (int i = 0; i < 20; i++) {
                nobj.sendObject("Heme Poudel", new NewMessage("Hahaha" + i));
                Thread.currentThread().sleep(2000);
            }
        } catch (Exception ex) {
            Logger.getLogger(NetworkedTest2.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
}