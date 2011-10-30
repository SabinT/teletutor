package teletutor.blackboard.services;

import java.awt.Image;
import java.io.Serializable;
import java.util.Set;
import java.util.TimerTask;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JPanel;
import teletutor.blackboard.utilities.Interpolator;
import teletutor.core.services.TeleChannel;
import teletutor.core.services.TeleObject;
import teletutor.core.services.UpdateInfo;

/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
/**
 *
 * @author Samyam
 */
public abstract class BoardObject extends TeleObject implements Serializable {

    /**
     * These x and y values are the TARGET values, the actual x and y values used
     * for drawing are tx and ty, the interpolated values. tx and ty gradually 
     * reach x and y. Whenever a new value arrives from network, new TimerTasks
     * are scheduled for interpolation. Similar case for alpha and talpha.
     */
    protected Integer x, y;
    protected float tx, ty;
    protected Float alpha = new Float(1.0);
    protected float talpha = 1.0f;
    protected Integer width, height;
    // z = -1;
    private boolean showBorder = false;

    public BoardObject(String string, TeleChannel tc) throws Exception {
        super(string, tc);
    }

    public abstract Image getImage();

    public abstract JPanel getPropertiesPanel();

    public float getAlpha() {
        return talpha;
    }

    public void setAlpha(float alpha) {
        this.alpha = alpha;
        this.talpha = alpha;
        registerFieldChange("alpha", alpha);
    }

    public int getX() {
        return (int) tx;
    }

    public void setX(int x) {
        this.x = x;
        this.tx = x;
        registerFieldChange("x", this.x);
    }

    public int getY() {
        return (int) ty;
    }

    public void setY(int y) {
        this.y = y;
        this.ty = y;
        registerFieldChange("y", this.y);
    }

//    public int getZ() {
//        return z;
//    }
//
//    public void setZ(int z) {
//        this.z = z;
//    }
    public int getHeight() {
        return height;
    }

    public int getWidth() {
        return width;
    }

    public boolean hitTest(int x, int y) {
        int objX = this.x + this.width;
        int objY = this.y + this.height;

        if (x > this.x && x < objX && y > this.y && y < objY) {
            return true;
        } else {
            return false;
        }
    }

    public boolean getShowBorder() {
        return showBorder;
    }

    public void setShowBorder(boolean showBorder) {
        this.showBorder = showBorder;
    }

    public abstract void redraw();

    public abstract void showProperties();

    public abstract void hideProperties();
    /****************************************************
     * Animation stuff
     ****************************************************/
    AnimateTask animateTask = null;
    boolean animateX, animateY, animateAlpha;
    private final float INTERPOLATION_RATE = 0.3F;
    private final float XY_TOLERANCE = 0.1f;
    private final float ALPHA_TOLERANCE = 0.01f;
    private final long ANIMATE_PERIOD = 50; // target 20 fps

    @Override
    protected void objectUpdated(UpdateInfo params) {
        boolean atLeastOne = false;
        if (params.get("x") != null) {
            // new x has arrived, start interpolating towards it
            animateX = true;
            atLeastOne = true;
        }
        if (params.get("y") != null) {
            // new x has arrived, start interpolating towards it
            animateY = true;
            atLeastOne = true;
        }
        if (params.get("alpha") != null) {
            // new x has arrived, start interpolating towards it
            animateAlpha = true;
            atLeastOne = true;
        }
        if (atLeastOne) {
            if (animateTask == null) {
                animateTask = new AnimateTask();
                timer.schedule(animateTask, 0, ANIMATE_PERIOD);
            }
        }
    }

    class AnimateTask extends TimerTask {

        @Override
        public void run() {
            boolean allDone = true;
            if (animateX) {
                tx = Interpolator.lerp(tx, x, INTERPOLATION_RATE);
                allDone = false;
            }
            if (animateY) {
                ty = Interpolator.lerp(ty, y, INTERPOLATION_RATE);
                allDone = false;
            }
            if (animateAlpha) {
                talpha = Interpolator.lerp(talpha, alpha, INTERPOLATION_RATE);
                allDone = false;
            }

            redraw();

            if (allDone) {
                cancel();
                animateTask = null;
            }

            if (Math.abs(x - tx) < XY_TOLERANCE) {
                animateX = false;
            }
            if (Math.abs(y - ty) < XY_TOLERANCE) {
                animateY = false;
            }
            if (Math.abs(alpha - talpha) < ALPHA_TOLERANCE) {
                animateAlpha = false;
            }
        }
    }
}
