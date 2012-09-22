/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package teletutor.blackboard.expansion.toolpack;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Image;
import java.awt.Point;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.AbstractButton;
import javax.swing.ImageIcon;
import teletutor.blackboard.services.Blackboard;
import teletutor.blackboard.services.BoardObject;
import teletutor.blackboard.services.BoardTool;
import teletutor.blackboard.services.ConstructionInfo;
import teletutor.blackboard.services.ToolButton;
import teletutor.core.services.TeleChannel;
import teletutor.core.services.UpdateInfo;

/**
 *
 * @author Samyam
 */
public class BrushTool extends BoardTool {

    protected int brushSize;
    protected Color color;
    private BrushFrame bFrame = null;
    Toolkit kit = Toolkit.getDefaultToolkit();

    public BrushTool(String string, TeleChannel tc) throws Exception {
        super(string, tc);
        icon = new ImageIcon(getClass().getResource("/brush.png"));
        createCursor();
    }

    public void init(Blackboard board) {
        mListener = new BrushMouseListener(board);
        toolButton = new ToolButton();
        toolButton.setSize(new Dimension(32, 32));
        toolButton.setIcon(new ImageIcon(getClass().getResource("/brush.png")));
        brushSize = 2;
        color = Color.WHITE;
        final BrushTool bTool = this;

        toolButton.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {
                AbstractButton eSource = (AbstractButton) e.getSource();
                boolean selected = eSource.getModel().isSelected();
                if (selected) {

                    java.awt.EventQueue.invokeLater(new Runnable() {

                        @Override
                        public void run() {
                            bFrame = new BrushFrame(bTool);
                            bFrame.setVisible(true);
                        }
                    });
                    activate();
                } else {
                    deactivate();
                    java.awt.EventQueue.invokeLater(new Runnable() {

                        @Override
                        public void run() {
                            bFrame.setVisible(false);
                        }
                    });
                }
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
            try {
                ScribbleObject sObj = new ScribbleObject(cinfo.getName(), channel, board);
                sObj.init(cinfo.getParams());
                board.addObject(sObj);

                if (cinfo.getCreator().equals(channel.getChannelName())) {
                    sObj.update();
                    board.mergeDown(sObj);
                } else {
                    sObj.animateScribble();
                }
            } catch (Exception ex) {
                Logger.getLogger(BrushTool.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }

    @Override
    protected void objectUpdated(UpdateInfo ui) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    public int getBrushWidth() {
        return brushSize;
    }

    public void setBrushWidth(int brushWidth) {
        this.brushSize = brushWidth;
    }

    public Color getColor() {
        return color;
    }

    public void setColor(Color color) {
        this.color = color;
    }

    @Override
    public void createCursor() {
        Toolkit kit = Toolkit.getDefaultToolkit();
        cursor = kit.createCustomCursor(icon.getImage(), new Point(0, 29), "brush");

    }

    public class BrushMouseListener extends MouseAdapter {

        private Blackboard board = null;
        private ScribbleObject obj = null;

        public BrushMouseListener(Blackboard board) {
            this.board = board;
        }

        @Override
        public void mouseDragged(MouseEvent e) {
            obj.addPoint(e.getX(), e.getY());
        }

        @Override
        public void mousePressed(MouseEvent e) {
            try {
                obj = new ScribbleObject("Scribble", null, board);
                obj.init(brushSize, color);
                board.addObject(obj);
                obj.addPoint(e.getX(), e.getY());

            } catch (Exception ex) {
                Logger.getLogger(BrushTool.class.getName()).log(Level.SEVERE, null, ex);
            }
        }

        @Override
        public void mouseReleased(MouseEvent e) {
            obj.addPoint(e.getX() + 1, e.getY() + 1);
            obj.validate();
        }
    }
}
