package teletutor.blackboard.services;

import java.awt.Image;
import java.io.Serializable;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JPanel;
import teletutor.core.services.TeleChannel;
import teletutor.core.services.TeleObject;

/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
/**
 *
 * @author Samyam
 */
public abstract class BoardObject extends TeleObject implements Serializable {

    protected Integer x, y;
    protected Float alpha = new Float(0.0);
    protected Integer width, height, z = -1;
    private boolean showBorder = false;

    public BoardObject(String string, TeleChannel tc) throws Exception {
        super(string, tc);
    }

    /**
     * This method needs to be synchronized.
     * @param updateInfo 
     */

    public abstract Image getImage();

    /**
     * This method needs to be synchronized.
     */
    public abstract void updateGraphics();

    public abstract JPanel getPropertiesPanel();

    public float getAlpha() {
        return alpha;
    }

    public void setAlpha(float alpha) {
        this.alpha = alpha;
    }

    public int getX() {
        return x;
    }

    public void setX(int x) {
        this.x = x;
        registerFieldChange("x", this.x);
    }

    public int getY() {
        return y;
    }

    public void setY(int y) {
        this.y = y;
        registerFieldChange("y", this.y);
    }

    public int getZ() {
        return z;
    }

    public void setZ(int z) {
        this.z = z;
    }

    public int getHeight() {
        return height;
    }

    public int getWidth() {
        return width;
    }
    
    public boolean hitTest(int x, int y){
        int objX = this.x + this.width;
        int objY = this.y + this.height;
        
        if(x > this.x && x < objX && y > this.y && y < objY) {return true;}
        else return false;
    }

    public boolean getShowBorder() {
        return showBorder;
    }

    public void setShowBorder(boolean showBorder) {
        this.showBorder = showBorder;
    }
    
    
}
