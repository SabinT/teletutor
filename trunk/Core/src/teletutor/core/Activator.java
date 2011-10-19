package teletutor.core;

import java.util.Hashtable;
import org.osgi.framework.BundleActivator;
import org.osgi.framework.BundleContext;
import teletutor.core.impl.TeleChannelImpl;
import teletutor.core.services.TeleChannel;

/**
 * Activator class for TeleTutor Core
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
        // maybe this code is better off inside TeleChannelImpl
//        try {
//            ClassConfigurator.add((short)2000, SubChannelHeader.class);
//        } catch (Exception ex) {
//            System.out.println(ex.getMessage());
//        }
        
        // TODO the initial GUI for logging in, selecting class etc.
        
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
