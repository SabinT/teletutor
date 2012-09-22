/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package teletutor.blackboard.expansion;

import java.util.logging.Level;
import java.util.logging.Logger;
import org.osgi.framework.BundleActivator;
import org.osgi.framework.BundleContext;
import org.osgi.framework.ServiceReference;
import org.osgi.framework.ServiceRegistration;
import org.osgi.util.tracker.ServiceTracker;
import org.osgi.util.tracker.ServiceTrackerCustomizer;
import teletutor.blackboard.expansion.picture.PictureTool;
import teletutor.blackboard.expansion.toolpack.BrushTool;
import teletutor.blackboard.expansion.toolpack.ClearTool;
import teletutor.blackboard.expansion.toolpack.EraserTool;
import teletutor.blackboard.services.Blackboard;
import teletutor.blackboard.services.BoardTool;
import teletutor.core.services.TeleChannel;

/**
 *
 * @author Sabin Timalsena
 */
public class Activator implements BundleActivator {

    public static BundleContext bc = null;
    public static ServiceRegistration serviceReg;
    ServiceTracker channelTracker = null;
    ServiceTracker boardTracker = null;
    public static Blackboard board = null;
    // Tool references
    PictureTool pTool = null;
    BrushTool brush;
    EraserTool eraser;
    ClearTool clear;

    @Override
    public void start(BundleContext bcon) throws Exception {
        Activator.bc = bcon;

        ServiceTrackerCustomizer boardTrackerCustomizer = new ServiceTrackerCustomizer() {

            public Object addingService(ServiceReference reference) {
                Logger.getLogger(Activator.class.getName()).log(Level.INFO, "Found TeleChannel");

                Blackboard board = (Blackboard) bc.getService(reference);
                startTools(board);
                addTools(board);

                Activator.board = board;
                return board;
            }

            public void modifiedService(ServiceReference reference, Object serviceObject) {
                Blackboard board = (Blackboard) bc.getService(reference);
                addTools(board);
            }

            public void removedService(ServiceReference reference, Object serviceObject) {
                // no need to remove tools
                stopTools();
                Activator.board = null;
            }
        };

        boardTracker = new ServiceTracker(bc, Blackboard.class.getName(), boardTrackerCustomizer);
        boardTracker.open();
    }

    @Override
    public void stop(BundleContext bc) throws Exception {
        Activator.bc = null;
    }

    public void startTools(Blackboard board) {
        try {
            TeleChannel chan = board.getChannel();

            pTool = new PictureTool(PictureTool.class.getName(), chan);
            brush = new BrushTool(BrushTool.class.getName(), chan);
            brush.init(board);
            eraser = new EraserTool(EraserTool.class.getName(), chan);
            eraser.init(board);
            clear = new ClearTool(ClearTool.class.getName(), chan);
            clear.init(board);
        } catch (Exception ex) {
            Logger.getLogger(Activator.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public void stopTools() {
        pTool = null;
        brush = null;
        eraser = null;
        clear = null;
    }

    public void addTools(Blackboard board) {
        board.addTool(pTool);
        board.addTool(brush);
        board.addTool(eraser);
        board.addTool(clear);
    }
}
