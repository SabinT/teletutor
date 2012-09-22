/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package teletutor.blackboard.expansion.toolpack;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.Point;
import java.awt.RenderingHints;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.TimerTask;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JPanel;
import teletutor.blackboard.services.Blackboard;
import teletutor.blackboard.services.BoardObject;
import teletutor.blackboard.services.ConstructionInfo;
import teletutor.core.services.TeleChannel;
import teletutor.core.services.UpdateInfo;

/**
 *
 * @author Samyam
 */
public class ScribbleObject extends BoardObject {

    private Image image = null;
    private Integer brushSize;
    private Color color;
    private BasicStroke stroke = null;
    private ArrayList<Point> pointList = new ArrayList<Point>();
    private Graphics2D g2 = null;
    // animation
    int currentPoint = 0;
    int targetPoint;

    public ScribbleObject(String string, TeleChannel tc, Blackboard board) throws Exception {
        super(string, tc, board);
        x = 0;
        y = 0;
        tw = width = board.getWidth();
        th = height = board.getHeight();
    }

    public void init(Integer bSize, Color color) {
        this.brushSize = bSize;
        this.color = color;
        stroke = new BasicStroke((float) brushSize, BasicStroke.CAP_ROUND, BasicStroke.JOIN_ROUND);
        image = new BufferedImage(width, height, BufferedImage.TYPE_4BYTE_ABGR);
        g2 = (Graphics2D) image.getGraphics();
        g2.setColor(color);
        g2.setStroke(stroke);
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
    }

    @Override
    public UpdateInfo getConstructionInfo() {
        UpdateInfo params = new UpdateInfo();
        params.put("brushSize", brushSize);
        params.put("color", color);
        params.put("pointList", pointList);

        return params;
    }

    @Override
    public Image getImage() {
        return image;
    }

    @Override
    public JPanel getPropertiesPanel() {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public void showProperties() {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public void hideProperties() {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public void redraw() {
        board.redraw();
    }

    public void update() {
        if (pointList.size() < 2) return;

        g2 = (Graphics2D) image.getGraphics();
        g2.setStroke(stroke);
        g2.setColor(color);
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        
        for (int i = 0; i < pointList.size() - 1; i++) {
            g2.drawLine((int) pointList.get(i).getX(), (int) pointList.get(i).getY(), (int) pointList.get(i + 1).getX(), (int) pointList.get(i + 1).getY());
        }
        
        redraw();
    }

    @Override
    public void validate() {
        if (channel != null) {
            return;
        }

        UpdateInfo params = getConstructionInfo();
        String name = BoardObject.generateName();
        String creator = board.getChannel().getChannelName();
        ConstructionInfo cinfo = new ConstructionInfo(name, params, creator);
        try {
            board.sendObject(null, BrushTool.class.getName(), cinfo);
        } catch (Exception ex) {
            Logger.getLogger(BrushTool.class.getName()).log(Level.SEVERE, null, ex);
        }

        board.removeObject(this);
        dispose();
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
    public void received(Object o) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public void init() {
        stroke = new BasicStroke((float) brushSize, BasicStroke.CAP_ROUND, BasicStroke.JOIN_ROUND);
        image = new BufferedImage(width, height, BufferedImage.TYPE_4BYTE_ABGR);
        g2 = (Graphics2D) image.getGraphics();
        g2.setStroke(stroke);
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
    }

    public void animateScribble() {
        timer.schedule(new AnimatePoints(pointList.size()), 0, 50);
    }

    public void addPoint(int x, int y) {
        pointList.add(new Point(x, y));
        if (pointList.size() < 2) {
            return;
        } else {
            Point last = pointList.get(pointList.size() - 2);
            g2.drawLine((int) last.getX(), (int) last.getY(), x, y);
            board.redraw();
        }
    }

    @Override
    public boolean isResizable() {
        return false;
    }

// animation    
    class AnimatePoints extends TimerTask {

        int num;
        int current = 0;

        public AnimatePoints(int num) {
            this.num = num;
        }

        @Override
        public void run() {
            g2 = (Graphics2D) image.getGraphics();
            g2.setStroke(stroke);
            g2.setColor(color);
            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

            for (int i = 0; i < 10; i++) {

                if (current >= num - 1) {
                    cancel();
                    board.mergeDown(ScribbleObject.this);
                    return;
                }
                g2.drawLine((int) pointList.get(current).getX(), (int) pointList.get(current).getY(), (int) pointList.get(current + 1).getX(), (int) pointList.get(current + 1).getY());

                current++;
            }

            redraw();

        }
    }
}
