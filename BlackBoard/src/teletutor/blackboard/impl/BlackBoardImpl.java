package teletutor.blackboard.impl;

import java.awt.AlphaComposite;
import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Composite;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.Stroke;
import java.awt.image.BufferedImage;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.ImageIcon;
import javax.swing.JPanel;
import javax.swing.JToolBar;
import teletutor.blackboard.services.BoardObject;
import teletutor.blackboard.services.BoardTool;
import teletutor.core.services.TeleChannel;
import teletutor.core.services.TeleObject;
import teletutor.blackboard.services.Blackboard;
import teletutor.blackboard.utilities.ImageUtils;
import teletutor.blackboard.utilities.composite.BlendComposite;
import teletutor.core.services.UpdateInfo;

/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
/**
 * 
 * @author Samyam
 */
public class BlackboardImpl extends Blackboard {

    protected ArrayList<BoardObject> bObjectList = new ArrayList<BoardObject>();
    protected ArrayList<BoardTool> bToolList = new ArrayList<BoardTool>();
    private JPanel boardPanel = null;
    private JToolBar toolBar = null;
    private Image[] canvas = new Image[2];
    private int current = 0;
    private int width, height;
    private float[] dash = {10.0f};
    private Stroke stroke = new BasicStroke(2.0F, BasicStroke.CAP_BUTT,
            BasicStroke.JOIN_MITER,
            10.0F, dash, 0.F);
    private BoardTool currentTool = null;
    private Image frameImage = null;
    private Image lightingOverlay = null;
    // default tool
    MoveTool mover;
    // *************************************************************************
    private Image background = null;

    public BlackboardImpl(String name, TeleChannel chan) throws Exception {
        super(name, chan);
    }

    /**
     * Set the width and height of the double buffered canvas for the blackboard 
     * Wire boardPanel and toolBar to the blackBoard
     * @param width
     * @param height 
     */
    public void init(int width, int height) {
        this.width = width;
        this.height = height;

        canvas[0] = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
        canvas[1] = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);

        frameImage = ImageUtils.getScaledAlphaImage(
                new ImageIcon(getClass().getResource("/board-frame.png")).getImage(),
                width, height);

        lightingOverlay = ImageUtils.getScaledImage(
                new ImageIcon(getClass().getResource("/lighting-overlay.jpg")).getImage(),
                width, height);


        //*********************************************************************
        background = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
        Graphics2D g2 = (Graphics2D) background.getGraphics();
        g2.setColor(Blackboard.BGCOLOR);
        g2.fillRect(0, 0, width, height);

        // TODO shadow layer

        final Blackboard bb = this;
        java.awt.EventQueue.invokeLater(new Runnable() {

            @Override
            public void run() {
                boardPanel = new BoardPanel(bb);
                toolBar = new ToolBar(bb);

            }
        });

        try {
            mover = new MoveTool(MoveTool.class.getName(), channel);
            mover.init(this);
            addTool(mover);
        } catch (Exception ex) {
            System.out.println("Move Tool initialization failed");
        }
    }

    @Override
    public Image getImage() {
        return canvas[current];
    }

    @Override
    public synchronized final void addObject(BoardObject bObject) {
        bObjectList.add(bObject);
        redraw();
    }

    public synchronized final void pickObject(BoardObject bObject) {
        bObjectList.remove(bObject);
        bObjectList.add(bObject);
    }

    @Override
    public synchronized final void removeObject(BoardObject bObject) {
        bObjectList.remove(bObject);
        redraw();
    }

    public void deleteObject(BoardObject bObject) {
        bObjectList.remove(bObject);
        redraw();

        RemoveCommand rc = new RemoveCommand(bObject.getName());

        try {
            sendObject(rc);
        } catch (Exception ex) {
            Logger.getLogger(BlackboardImpl.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    @Override
    public final void addTool(final BoardTool bTool) {
        bToolList.add(bTool);

        bTool.setBlackBoard(this);

        java.awt.EventQueue.invokeLater(new Runnable() {

            @Override
            public void run() {
                toolBar.add(bTool.getToolButton());
                toolBar.setOrientation(JToolBar.VERTICAL);
            }
        });
    }

    @Override
    public final void removeTool(BoardTool bTool) {
        bToolList.remove(bTool);
    }

    @Override
    public synchronized void redraw() {

        Graphics2D g2 = (Graphics2D) canvas[1 - current].getGraphics();
        g2.setColor(Color.cyan);

        //**********************************************************************************************88
        g2.drawImage(background, 0, 0, null);

        for (BoardObject b : bObjectList) {
            // TODO set alpha
            g2.drawImage(b.getImage(), b.getX(), b.getY(), b.getWidth(), b.getHeight(), null);
            if (b.getShowBorder()) {
                g2.setStroke(stroke);
                g2.drawRect(b.getX(), b.getY(), b.getWidth(), b.getHeight());
            }
        }

//        g2.setComposite(BlendComposite.Overlay);
//        g2.drawImage(lightingOverlay, 0, 0, null);
        g2.setComposite(AlphaComposite.SrcOver);
        g2.drawImage(frameImage, 0, 0, null);

        current = 1 - current;
        if (boardPanel != null) {
            boardPanel.repaint();
        }
    }

    @Override
    public void objectUpdated(UpdateInfo ui) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public void received(Object o) {
        if (o instanceof RemoveCommand) {
            RemoveCommand rc = (RemoveCommand) o;
            BoardObject obj = (BoardObject) channel.getTeleObject(rc.getName());
            removeObject(obj);
        } else if (o instanceof ClearMessage) {
            Graphics2D g2 = (Graphics2D) background.getGraphics();
            g2.setColor(Blackboard.BGCOLOR);
            g2.fillRect(0, 0, width, height);
            redraw();
        }
    }

    @Override
    public JPanel getBoardPanel() {
        return boardPanel;
    }

    @Override
    public JToolBar getToolBar() {
        return toolBar;
    }

    @Override
    public synchronized BoardObject getObjectAt(int x, int y) {
        int i = 0;
        BoardObject obj = null;
        for (i = 0; i < bObjectList.size(); i++) {
            if (bObjectList.get(i).hitTest(x, y)) {
                obj = bObjectList.get(i);
            }
        }
        return obj;
    }

    @Override
    public int getHeight() {
        return height;
    }

    @Override
    public int getWidth() {
        return width;
    }

    public BoardTool getCurrentTool() {
        return currentTool;
    }

    public void setCurrentTool(BoardTool currentTool) {
        this.currentTool = currentTool;
    }

    //*******************************************************************
    @Override
    public synchronized void mergeDown(BoardObject bObj) {
        background.getGraphics().drawImage(bObj.getImage(), bObj.getX(), bObj.getY(), null);
        removeObject(bObj);
        redraw();
    }
    /***********************************************************************
     * Object Selection mechanism
     ***********************************************************************/
    BoardObject selectedObject = null;

    /**
     * To deselect the current object, pass null
     * @param obj 
     */
    @Override
    public void selectObject(BoardObject obj) {
        if (obj == selectedObject) {
            return;
        }

        if (selectedObject != null) {
            selectedObject.setShowBorder(false);
            selectedObject = null;
        }

        if (obj != null) {
            selectedObject = obj;
            selectedObject.setShowBorder(true);
            pickObject(obj);
        }

        redraw();
    }

    public BoardObject getSelectedObject() {
        return selectedObject;
    }

    @Override
    public void selectDefaultTool() {
        mover.getToolButton().doClick();
    }
    // ************************************************************************

    @Override
    public void clear() {
        ArrayList<BoardObject> cloneList = (ArrayList<BoardObject>) bObjectList.clone();
        for (BoardObject b : cloneList) {
            deleteObject(b);
        }
        Graphics2D g2 = (Graphics2D) background.getGraphics();
        g2.setColor(Blackboard.BGCOLOR);
        g2.fillRect(0, 0, width, height);
        redraw();

        try {
            sendObject(new ClearMessage());
        } catch (Exception ex) {
            Logger.getLogger(BlackboardImpl.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
}

class ClearMessage implements Serializable {
}