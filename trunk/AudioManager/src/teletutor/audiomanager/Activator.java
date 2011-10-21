/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package teletutor.audiomanager;

import java.util.Hashtable;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.osgi.framework.BundleActivator;
import org.osgi.framework.BundleContext;
import org.osgi.framework.ServiceReference;
import org.osgi.util.tracker.ServiceTracker;
import org.osgi.util.tracker.ServiceTrackerCustomizer;
import teletutor.audiomanager.impl.LANAudioManager;
import teletutor.audiomanager.services.AudioManager;
import teletutor.core.services.TeleChannel;

/**
 *
 * @author Sabin Timalsena
 */
public class Activator implements BundleActivator {

    public static BundleContext bc = null;
    private AudioManager audioManager;
    ServiceTracker channelTracker = null;

    @Override
    public void start(BundleContext bcon) throws Exception {
        Activator.bc = bcon;

        // tracker for the TeleChannel service
        // tracker for the AudioManager service not necessary
        ServiceTrackerCustomizer customizer = new ServiceTrackerCustomizer() {

            public Object addingService(ServiceReference reference) {
                Logger.getLogger(Activator.class.getName()).log(Level.INFO, "Found TeleChannel");

                TeleChannel channel = (TeleChannel) bc.getService(reference);
                startAudio(channel);
                return channel;
            }

            public void modifiedService(ServiceReference reference, Object serviceObject) {
                TeleChannel channel = (TeleChannel) bc.getService(reference);
                stopAudio();
                startAudio(channel);
            }

            public void removedService(ServiceReference reference, Object serviceObject) {
                stopAudio();
            }
        };

        channelTracker = new ServiceTracker(bc, TeleChannel.class.getName(), customizer);
        channelTracker.open();
    }

    @Override
    public void stop(BundleContext bc) throws Exception {
        Activator.bc = null;
        stopAudio();
    }

    public void startAudio(TeleChannel chan) {
        try {
            audioManager = new LANAudioManager("AudioManager", chan, "224.144.251.104", 22200, chan.getChannelName());
            audioManager.initialize();
            
            if (chan.getTutorName().equals(chan.getChannelName())) {
                // this is the tutor, so start the audio
                audioManager.resume();
            }
            
            bc.registerService(AudioManager.class.getName(), audioManager, new Hashtable());
        } catch (Exception ex) {
            Logger.getLogger(Activator.class.getName()).log(Level.SEVERE, "Could not initialize audio: ", ex);
        }
    }

    public void stopAudio() {
        if (audioManager == null) {
            return;
        }
        audioManager.stop();
    }
}


//        fix the classloader
//        ClassLoader bundleLoader = Activator.class.getClassLoader();
//        ClassLoader oldLoader = Thread.currentThread().getContextClassLoader();
//        Thread.currentThread().setContextClassLoader(bundleLoader);        
//        reset the classloader
//        Thread.currentThread().setContextClassLoader(oldLoader);