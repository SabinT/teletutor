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
public class NetworkedTest {
    static TeleChannel chan;
    public static void main(String[] args) {
        
        try {
            ClassConfigurator.add((short)2000, SubChannelHeader.class);
 
            chan = new TeleChannelImpl("../../settings/UDP.xml", "TestGroup", "Heme Poudel");
            
            NewObject nobj = new NewObject("new1", chan);
            // Print the value of this object for 10 times
            for (int i = 0; i < 20; i++) {
                System.out.println(nobj.getStr());
                Thread.currentThread().sleep(2000);
            }
        } catch (Exception ex) {
            Logger.getLogger(NetworkedTest.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
}

