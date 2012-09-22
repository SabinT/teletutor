/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package teletutor.blackboard.expansion.picture;

import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.ImageIcon;
import javax.swing.JToggleButton;
import javax.swing.SwingUtilities;
import teletutor.blackboard.services.BoardObject;
import teletutor.blackboard.services.BoardTool;
import teletutor.blackboard.services.ConstructionInfo;
import teletutor.blackboard.services.ToolButton;
import teletutor.core.services.TeleChannel;
import teletutor.core.services.UpdateInfo;

/**
 *
 * @author Sabin Timalsena
 */
public class PictureTool extends BoardTool {
    PictureFrame frame;

    public PictureTool(String string, TeleChannel tc) throws Exception {
        super(string, tc);
        toolButton =  new ToolButton(this, null, null);       
        toolButton.setIcon(new ImageIcon(getClass().getResource("/picture.png")));
        
        frame = new PictureFrame(this);
    }

    @Override
    public void activate() {
        super.activate();
        SwingUtilities.invokeLater(new Runnable() {

            @Override
            public void run() {
                frame.setVisible(true);
            }
        });
    }

    @Override
    public void deactivate() {
        super.deactivate();
        SwingUtilities.invokeLater(new Runnable() {

            @Override
            public void run() {
                frame.setVisible(false);
            }
        });
    }
    

    @Override
    public void actionComplete(BoardObject bo) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public void actionAborted(BoardObject bo) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public void received(Object obj) {
        if (obj instanceof ConstructionInfo) {
            ConstructionInfo cinfo = (ConstructionInfo) obj;
            String name =  cinfo.getName();
            UpdateInfo params =  cinfo.getParams();
            try {
                PictureObject pObject =  new PictureObject(name, channel, board);
                pObject.init(params);
                board.addObject(pObject);
                
                board.selectDefaultTool();
                board.selectObject(pObject);
            } catch (Exception ex) {
                Logger.getLogger(PictureTool.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }

    @Override
    protected void objectUpdated(UpdateInfo ui) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public void createCursor() {
        return;
    }
    
}
