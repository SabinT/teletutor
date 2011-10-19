/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package teletutor.audiomanager;

import java.util.Hashtable;
import org.osgi.framework.BundleActivator;
import org.osgi.framework.BundleContext;
import teletutor.audiomanager.impl.LANAudioManager;
import teletutor.audiomanager.services.AudioManager;

/**
 *
 * @author Sabin Timalsena
 */
public class Activator implements BundleActivator {
    public static BundleContext bc = null;
    
    private AudioManager audioManager;

    @Override
    public void start(BundleContext bc) throws Exception {
        Activator.bc = bc;
        
        // fix the classloader
//        ClassLoader bundleLoader = getClass().getClassLoader();
//        ClassLoader oldLoader = Thread.currentThread().getContextClassLoader();
//        Thread.currentThread().setContextClassLoader(bundleLoader);
        
        // create and register the AudioManager service so it can be used by 
        // other bundles
        audioManager = new LANAudioManager("AudioManager", null, "224.144.251.104", 22200);
        audioManager.initialize();
        
        // Initially, no voice should be sent from the audiomanager;
        // should wait till instructed by the ClassroomManager
        //audioManager.resume();
        
        // reset the classloader
        //Thread.currentThread().setContextClassLoader(oldLoader);
        
        bc.registerService(AudioManager.class.getName(), audioManager, new Hashtable());
    }

    @Override
    public void stop(BundleContext bc) throws Exception {
        Activator.bc = null;
        
        // fix the classloader
//        ClassLoader bundleLoader = Activator.class.getClassLoader();
//        ClassLoader oldLoader = Thread.currentThread().getContextClassLoader();
//        Thread.currentThread().setContextClassLoader(bundleLoader);
//        
        audioManager.stop();
        // manual unregistration of services not necessary

        // reset the classloader
//        Thread.currentThread().setContextClassLoader(oldLoader);
    }
    
}
