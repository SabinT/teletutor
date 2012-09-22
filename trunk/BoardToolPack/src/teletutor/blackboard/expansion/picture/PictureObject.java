/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package teletutor.blackboard.expansion.picture;

import java.awt.Dimension;
import java.awt.Image;
import javax.swing.ImageIcon;
import javax.swing.JPanel;
import teletutor.blackboard.services.Blackboard;
import teletutor.blackboard.services.BoardObject;
import teletutor.blackboard.utilities.ImageUtils;
import teletutor.core.services.TeleChannel;
import teletutor.core.services.UpdateInfo;

/**
 *
 * @author Sabin Timalsena
 */
public class PictureObject extends BoardObject {
    
    private static final float MAX_IMAGE_RATIO = 0.5f;

    private String path;
    private Image image;

    public PictureObject(String string, TeleChannel tc, Blackboard board) throws Exception {
        super(string, tc, board);
    }

    @Override
    public void received(Object o) {
        return;
    }

    @Override
    public void init() {
        ImageIcon icon = new ImageIcon(path);
        image = icon.getImage();
        setWidth(image.getWidth(null));
        setHeight(image.getHeight(null));
        if (width > board.getWidth()* MAX_IMAGE_RATIO || height > board.getHeight() * MAX_IMAGE_RATIO) {
            // scale dwon the image
            Dimension d = ImageUtils.getProportionalDimension(image, (int) (board.getWidth() * MAX_IMAGE_RATIO),
                    (int) (board.getHeight() * MAX_IMAGE_RATIO));
            setWidth((Integer) d.getWidth());
            setHeight((Integer) d.getHeight());
            
            // keep the image as large as might be necessary, to retain the quality.
            image = ImageUtils.getProportionalImage(image, board.getWidth(), board.getHeight());
        }
        
        // center this image on the board, set appropriate width and height
        tx = x = (Integer) (board.getWidth() / 2.0 - width / 2.0);
        ty = y = (Integer) (board.getHeight() / 2.0 - height / 2.0);
    }

    @Override
    public UpdateInfo getConstructionInfo() {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public Image getImage() {
        return image;
    }

    @Override
    public JPanel getPropertiesPanel() {
        return null;
    }

    @Override
    public void showProperties() {
        
    }

    @Override
    public void hideProperties() {
        
    }

    @Override
    public void redraw() {
        board.redraw();
    }

    @Override
    public void validate() {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public void invalidate() {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public void dispose() {
        image.getGraphics().dispose();
    }

    @Override
    public boolean isResizable() {
        return true;
    }

}
