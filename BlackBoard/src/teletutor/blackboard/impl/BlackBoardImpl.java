package teletutor.blackboard.impl;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.Stroke;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import javax.swing.JPanel;
import javax.swing.JToolBar;
import teletutor.blackboard.services.BoardObject;
import teletutor.blackboard.services.BoardTool;
import teletutor.core.services.TeleChannel;
import teletutor.core.services.TeleObject;
import teletutor.blackboard.services.BlackBoard;
import teletutor.core.services.UpdateInfo;

/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
/**
 *
 * @author Samyam
 */
public class BlackBoardImpl extends TeleObject implements BlackBoard {

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
    

    public BlackBoardImpl(String name, TeleChannel chan) throws Exception {
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

        canvas[0] = new BufferedImage(width, height, BufferedImage.TYPE_3BYTE_BGR);
        canvas[1] = new BufferedImage(width, height, BufferedImage.TYPE_3BYTE_BGR);

        final BlackBoard bb = this;
        java.awt.EventQueue.invokeLater(new Runnable() {

            @Override
            public void run() {
                boardPanel = new BoardPanel(bb);
                toolBar = new ToolBar(bb);
            }
        });

    }

    @Override
    public Image getImage() {
        return canvas[current];
    }

    @Override
    public final void addObject(BoardObject bObject) {
        bObjectList.add(bObject);
        redraw();
    }

    public final void pickObject(BoardObject bObject) {
        bObjectList.remove(bObject);
        bObjectList.add(bObject);
    }
    
    @Override
    public final void removeObject(BoardObject bObject) {
        bObjectList.remove(bObject);
    }

    @Override
    public final void addTool(final BoardTool bTool) {
        bToolList.add(bTool);

        bTool.setBlackBoard(this);

        java.awt.EventQueue.invokeLater(new Runnable() {

            @Override
            public void run() {
                toolBar.add(bTool.getToolButton());
            }
        });
    }

    @Override
    public final void removeTool(BoardTool bTool) {
        bToolList.remove(bTool);
    }

    @Override
    public void redraw() {

        Graphics2D g2 = (Graphics2D) canvas[1 - current].getGraphics();
        g2.setColor(Color.cyan);
        g2.clearRect(0, 0, width, height);
        for (BoardObject b : bObjectList) {
            // TODO set alpha
            g2.drawImage(b.getImage(), b.getX(), b.getY(), null);
            if (b.getShowBorder()) {
                g2.setStroke(stroke);
                g2.drawRect(b.getX(), b.getY(), b.getWidth(), b.getHeight());
            }
        }
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
        throw new UnsupportedOperationException("Not supported yet.");
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
    public BoardObject getObjectAt(int x, int y) {
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
    
    
}
