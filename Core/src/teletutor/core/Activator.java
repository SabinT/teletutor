/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package teletutor.core;

import java.util.Hashtable;
import org.jgroups.conf.ClassConfigurator;
import org.osgi.framework.BundleActivator;
import org.osgi.framework.BundleContext;
import teletutor.core.impl.SubChannelHeader;
import teletutor.core.impl.TeleChannelImpl;
import teletutor.core.services.TeleChannel;

/**
 *
 * @author Sabin Timalsena
 */
public class Activator implements BundleActivator {
    TeleChannelImpl chan = null;
    static BundleContext bc = null;
    
    @Override
    public void start(BundleContext bContext) throws Exception {
        Activator.bc = bContext;
        
        // this needs to be done in order to be able to add a header to the 
        // JGroups messages
        try {
            ClassConfigurator.add((short)2000, SubChannelHeader.class);
        } catch (Exception ex) {
            System.out.println(ex.getMessage());
        }
        
        // create the channel
        chan = new TeleChannelImpl("settings/UDP.xml", "TestGroup", "Heme Poudel");
        
        bc.registerService(TeleChannel.class.getName(), (TeleChannel) chan, new Hashtable());
    }

    @Override
    public void stop(BundleContext bContext) throws Exception {
        Activator.bc = null;
        chan.closeChannel();
        
        // manual unregistration not required

    }
    
}
