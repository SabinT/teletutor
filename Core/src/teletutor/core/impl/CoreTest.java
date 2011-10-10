/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package teletutor.core.impl;

import java.util.logging.Level;
import java.util.logging.Logger;
import org.jgroups.conf.ClassConfigurator;

/**
 *
 * @author Rae
 */
public class CoreTest {
    public static void main(String[] args) throws Exception {
        
        Thread.currentThread().setContextClassLoader(ClassConfigurator.class.getClassLoader());
        // register the SubChannelHeader
        ClassConfigurator.add((short)2000, SubChannelHeader.class);
        //ClassConfigurator.add()
        
        // create the channel
        TeleChannelImpl chan = new TeleChannelImpl();
        try {
            chan.createChannel("settings/UDP.xml", "TestGroup", "Heme Poudel");
        } catch (Exception ex) {
            Logger.getLogger(CoreTest.class.getName()).log(Level.SEVERE, null, ex);
        }

       
        
        chan.closeChannel();
    }
}
