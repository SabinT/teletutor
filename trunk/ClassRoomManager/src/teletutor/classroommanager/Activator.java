/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package teletutor.classroommanager;

import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.SwingUtilities;
import org.osgi.framework.BundleActivator;
import org.osgi.framework.BundleContext;
import org.osgi.framework.ServiceReference;
import org.osgi.util.tracker.ServiceTracker;
import org.osgi.util.tracker.ServiceTrackerCustomizer;
import teletutor.classroommanager.UI.ClassroomFrame;
import teletutor.classroommanager.impl.ClassroomManager;
import teletutor.core.services.TeleChannel;

/**
 *
 * @author Sabin Timalsena
 */
public class Activator implements BundleActivator {

    private static BundleContext bc = null;
    ServiceTracker channelTracker = null;
    ClassroomManager crman = null;
    ClassroomFrame frame = null;

    @Override
    public void start(BundleContext bcon) throws Exception {
        Activator.bc = bcon;

        // tracker for the TeleChannel service
        // tracker for the AudioManager service not necessary
        ServiceTrackerCustomizer customizer = new ServiceTrackerCustomizer() {

            public Object addingService(ServiceReference reference) {
                Logger.getLogger(Activator.class.getName()).log(Level.INFO, "Found TeleChannel");

                TeleChannel channel = (TeleChannel) bc.getService(reference);
                startClassroom(channel);
                return channel;
            }

            public void modifiedService(ServiceReference reference, Object serviceObject) {
                TeleChannel channel = (TeleChannel) bc.getService(reference);
                stopClassroom();
                startClassroom(channel);
            }

            public void removedService(ServiceReference reference, Object serviceObject) {
                stopClassroom();
            }
        };

        channelTracker = new ServiceTracker(bc, TeleChannel.class.getName(), customizer);
        channelTracker.open();
    }

    @Override
    public void stop(BundleContext bc) throws Exception {
        Activator.bc = null;
    }

    void startClassroom(TeleChannel channel) {
        try {
            crman = new ClassroomManager("ClassroomManager", channel);
            frame = new ClassroomFrame(crman);
            SwingUtilities.invokeLater(new Runnable() {

                @Override
                public void run() {
                    frame.setVisible(true);
                }
            });
        } catch (Exception ex) {
            Logger.getLogger(Activator.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    void stopClassroom() {
        SwingUtilities.invokeLater(new Runnable() {

            @Override
            public void run() {
                if (frame != null) {
                    frame.dispose();
                    frame = null;
                }
            }
        });

        crman = null;
    }
}
