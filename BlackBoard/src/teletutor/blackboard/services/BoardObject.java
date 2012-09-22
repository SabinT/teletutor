package teletutor.blackboard.services;

import java.awt.Image;
import java.io.Serializable;
import java.util.TimerTask;
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

    static int count = 0;
    static String prefix = "";
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
    protected float tw, th;
    protected Integer width, height;
    // z = -1;
    private boolean showBorder = false;
    protected Blackboard board = null;

    public static String generateName() {
        return prefix + BoardObject.class.getName() + count;
    }

    public BoardObject(String name, TeleChannel tc, Blackboard board) throws Exception {
        super(name, tc);
        this.board = board;
        BoardObject.prefix =  name;
        count++;
    }

    public abstract void init();

    public final void init(UpdateInfo params) {
        update(params);
        init();
    }

    public abstract UpdateInfo getConstructionInfo();

    /**
     * This method needs to be synchronized.
     * @param updateInfo 
     */
    public abstract Image getImage();

    /**
     * This method needs to be synchronized.
     */
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

    public int getHeight() {
        return (int) th;
    }

    public int getWidth() {
        return (int) tw;
    }

    public boolean hitTest(int x, int y) {
        int objX = (int) this.tx + this.width;
        int objY = (int) this.ty + this.height;

        if (x > this.tx && x < objX && y > this.ty && y < objY) {
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

    public abstract void showProperties();

    public abstract void hideProperties();

    public abstract void redraw();

    public void setHeight(Integer height) {
        this.th = height;
        this.height = height;
        registerFieldChange("height", height);
    }

    public void setWidth(Integer width) {
        this.tw = width;
        this.width = width;
        registerFieldChange("width", width);
    }
    /****************************************************
     * Animation stuff
     ****************************************************/
    AnimateTask animateTask = null;
    boolean animateX, animateY, animateAlpha;
    boolean animateW, animateH;
    private final float INTERPOLATION_RATE = 0.3F;
    private final float XY_TOLERANCE = 0.1f;
    private final float ALPHA_TOLERANCE = 0.01f;
    private final long ANIMATE_PERIOD = 50; // target 20 fps

    @Override
    protected void objectUpdated(UpdateInfo params) {
        System.out.println("BoardObject properties updated!!");
        boolean atLeastOne = false;
        if (params.get("x") != null) {
            // new x has arrived, start interpolating towards it
            animateX = true;
            atLeastOne = true;
        }
        if (params.get("y") != null) {
            animateY = true;
            atLeastOne = true;
        }
        if (params.get("width") != null) {
            animateW = true;
            atLeastOne = true;
        }
        if (params.get("height") != null) {
            animateH = true;
            atLeastOne = true;
        }
        if (params.get("alpha") != null) {
            animateAlpha = true;
            atLeastOne = true;
        }
        if (atLeastOne) {
            if (animateTask == null) {
                animateTask = new AnimateTask();
                timer.schedule(animateTask, 0, ANIMATE_PERIOD);
            }
        }
        redraw();
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
            if (animateW) {
                tw = Interpolator.lerp(tw, width, INTERPOLATION_RATE);
                allDone = false;
            }
            if (animateH) {
                th = Interpolator.lerp(th, height, INTERPOLATION_RATE);
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
            if (Math.abs(width - tw) < XY_TOLERANCE) {
                animateW = false;
            }
            if (Math.abs(height - th) < XY_TOLERANCE) {
                animateH = false;
            }
            if (Math.abs(alpha - talpha) < ALPHA_TOLERANCE) {
                animateAlpha = false;
            }
        }
    }

    public abstract void validate();

    public abstract void invalidate();

    public abstract void dispose();

    //**************************************************************************
    // Resizing functionality
    //**************************************************************************
    public abstract boolean isResizable();

    public void scale(float factor) {
        if (!isResizable()) {
            return;
        }

        // old values
        int ow = getWidth();
        int oh = getHeight();

        int nw = (int) (factor * ow);
        int nh = (int) (factor * oh);

        int dw = nw - ow;
        int dh = nh - oh;

        setWidth(nw);
        setHeight(nh);

        setX(getX() - dw / 2);
        setY(getY() - dh / 2);

        redraw();
    }
}
