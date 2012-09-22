/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package teletutor.blackboard.impl;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.geom.Rectangle2D;
import java.awt.image.BufferedImage;
import javax.swing.JPanel;
import teletutor.blackboard.services.Blackboard;
import teletutor.blackboard.services.BoardObject;
import teletutor.blackboard.services.BoardTool;
import teletutor.core.services.TeleChannel;
import teletutor.core.services.UpdateInfo;

/**
 *
 * @author Samyam
 */
public class Rectango extends BoardObject{
    private Rectangle2D.Double rect = null;
    private Image image = null;

    public Rectango(String string, TeleChannel tc, Blackboard board, int x, int y, int z, int width, int height, Color c) throws Exception {
        super(string, tc, board);
        init(x,y,z,width,height,c);
    }
    
    @Override
    public void init() {
        
    }
    
    void init(int x, int y, int z, int width, int height, Color c){
        setX(x);
        setY(y);

        this.width = width;
        this.height = height;
        image = new BufferedImage(width, height, BufferedImage.TYPE_3BYTE_BGR);
        rect = new Rectangle2D.Double(0, 0, width, height);
        
        Graphics2D g2 = (Graphics2D)image.getGraphics();
        g2.setColor(c);        
        g2.fill(rect);        
    }
    
    @Override
    public Image getImage(){
        return image;
    }

    @Override
    public JPanel getPropertiesPanel() {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public void received(Object o) {
        throw new UnsupportedOperationException("Not supported yet.");
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
    public UpdateInfo getConstructionInfo() {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public void dispose() {

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
    public boolean isResizable() {
        return false;
    }
}
