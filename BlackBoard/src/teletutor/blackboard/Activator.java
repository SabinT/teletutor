/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package teletutor.blackboard;

import java.util.Hashtable;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.SwingUtilities;
import org.osgi.framework.BundleActivator;
import org.osgi.framework.BundleContext;
import org.osgi.framework.ServiceReference;
import org.osgi.framework.ServiceRegistration;
import org.osgi.util.tracker.ServiceTracker;
import org.osgi.util.tracker.ServiceTrackerCustomizer;
import teletutor.blackboard.impl.BlackboardImpl;
import teletutor.blackboard.impl.BoardFrame;
import teletutor.blackboard.services.Blackboard;
import teletutor.core.services.TeleChannel;

/**
 *
 * @author Sabin Timalsena
 */
public class Activator implements BundleActivator {

    public static BundleContext bc = null;
    public static ServiceRegistration serviceReg;
    ServiceTracker channelTracker = null;
    BlackboardImpl board = null;
    BoardFrame frame = null;

    @Override
    public void start(BundleContext bcon) throws Exception {
        Activator.bc = bcon;

        // tracker for the TeleChannel service
        // tracker for the AudioManager service not necessary
        ServiceTrackerCustomizer customizer = new ServiceTrackerCustomizer() {

            public Object addingService(ServiceReference reference) {
                Logger.getLogger(Activator.class.getName()).log(Level.INFO, "Found TeleChannel");

                TeleChannel channel = (TeleChannel) bc.getService(reference);

                startBlackboard(channel);

                return channel;
            }

            public void modifiedService(ServiceReference reference, Object serviceObject) {
                TeleChannel channel = (TeleChannel) bc.getService(reference);
                stopBlackboard();
                startBlackboard(channel);
            }

            public void removedService(ServiceReference reference, Object serviceObject) {
                stopBlackboard();
            }
        };

        channelTracker = new ServiceTracker(bc, TeleChannel.class.getName(), customizer);
        channelTracker.open();
    }

    @Override
    public void stop(BundleContext bc) throws Exception {
        Activator.bc = null;
    }

    public void startBlackboard(TeleChannel channel) {
        try {
            board = new BlackboardImpl("BlackBoard", channel);
            board.init(800, 600);

            // register the board service
            if (Activator.bc != null) {
                Activator.serviceReg =
                        Activator.bc.registerService(Blackboard.class.getName(), (Blackboard) board, new Hashtable());
            }

            frame = new BoardFrame(board);
            frame.setVisible(true);
        } catch (Exception ex) {
            Logger.getLogger(Activator.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public void stopBlackboard() {
        if (serviceReg != null) {
            serviceReg.unregister();
            serviceReg = null;
            
            board.unregisterSubChannel();
            board = null;

            SwingUtilities.invokeLater(new Runnable() {

                @Override
                public void run() {
                    frame.setVisible(false);
                    frame.dispose();
                    frame = null;
                }
            });
        }
    }
}
