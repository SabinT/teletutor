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
 * @author Rae
 */
public class Activator implements BundleActivator {
    TeleChannelImpl chan = null;
    static BundleContext bc = null;
    
    // TODO remove this
    String testString;
    
    @Override
    public void start(BundleContext bContext) throws Exception {
        Activator.bc = bContext;
        testString = "Aye Captian.";
        
        //Thread.currentThread().setContextClassLoader(ClassConfigurator.class.getClassLoader());
        // register the SubChannelHeader
        //Class headerClass = Class.forName("teletutor.core.impl.SubChannelHeader", true, SubChannelHeader.class.getClassLoader());
        try {
            ClassConfigurator.add((short)2000, SubChannelHeader.class);
        } catch (Exception ex) {
            System.out.println(ex.getMessage());
        }
        //ClassConfigurator.add()
        
        // create the channel
        chan = new TeleChannelImpl();
        chan.createChannel("settings/UDP.xml", "TestGroup", "Heme Poudel");
        
        bc.registerService(TeleChannel.class.getName(), (TeleChannel) chan, new Hashtable());
    }

    @Override
    public void stop(BundleContext bContext) throws Exception {
        Activator.bc = null;
        chan.closeChannel();
        
        // manual unregistration not required

    }
    
}
