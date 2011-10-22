package teletutor.core;

import java.util.Hashtable;
import org.osgi.framework.BundleActivator;
import org.osgi.framework.BundleContext;
import org.osgi.framework.ServiceRegistration;
import teletutor.core.UI.CoreFrame;
import teletutor.core.impl.TeleChannelImpl;
import teletutor.core.services.TeleChannel;

/**
 * Activator class for TeleTutor Core
 * @author Sabin Timalsena
 */
public class Activator implements BundleActivator {

    public static BundleContext bc = null;
    public static ServiceRegistration serviceReg;
    CoreFrame frame;

    @Override
    public void start(BundleContext bContext) throws Exception {
        Activator.bc = bContext;

        /* The initial GUI for logging in, selecting class etc.
         * This frame handles all detail retrieving and channel registration
         * and such other stuff.
         */
        frame = new CoreFrame();

    }

    @Override
    public void stop(BundleContext bContext) throws Exception {
        Activator.bc = null;
        if (frame != null) {
            frame.exit();
        }
    }
}
