package teletutor.core;

import java.util.Hashtable;
import org.osgi.framework.BundleActivator;
import org.osgi.framework.BundleContext;
import teletutor.core.UI.CoreFrame;
import teletutor.core.impl.TeleChannelImpl;
import teletutor.core.services.TeleChannel;

/**
 * Old Activator class for TeleTutor Core
 * @author Sabin Timalsena
 */

/*
public class Activator_old implements BundleActivator {
    TeleChannelImpl chan = null;
    public static BundleContext bc = null;
    
    CoreFrame frame = new CoreFrame();
    
    @Override
    public void start(BundleContext bContext) throws Exception {
        Activator_old.bc = bContext;
        
        // TODO the initial GUI for logging in, selecting class etc.
        
        // create the channel
        chan = new TeleChannelImpl("settings/UDP.xml", "TestGroup", "Heme");
        
        bc.registerService(TeleChannel.class.getName(), (TeleChannel) chan, new Hashtable());
    }

    @Override
    public void stop(BundleContext bContext) throws Exception {
        Activator_old.bc = null;
        chan.closeChannel();
        
        // manual unregistration not required

    }
    
}
*/